package me.kirimin.mitsumine.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import java.util.ArrayList

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.AccountDAO
import me.kirimin.mitsumine.network.api.BookmarkApi
import me.kirimin.mitsumine.network.api.parser.EntryInfoApiParser
import rx.android.schedulers.AndroidSchedulers
import rx.android.widget.WidgetObservable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

import kotlinx.android.synthetic.fragment_register_bookmark.view.*

public class RegisterBookmarkFragment : Fragment(), TagEditDialogFragment.OnOkClickListener {

    companion object {

        public fun newFragment(url: String): RegisterBookmarkFragment {
            val fragment = RegisterBookmarkFragment()
            val bundle = Bundle()
            bundle.putString("url", url)
            fragment.setArguments(bundle)
            return fragment
        }
    }

    private var isAlreadyBookmarked: Boolean = false
    private var tags = ArrayList<String>()
    private val subscriptions = CompositeSubscription()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val url = getArguments().getString("url") ?: throw IllegalStateException("url is null")
        val rootView = inflater.inflate(R.layout.fragment_register_bookmark, container, false)
        rootView.cardView.setVisibility(View.INVISIBLE)
        subscriptions.add(BookmarkApi.requestBookmarkInfo(url, AccountDAO.get()!!)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bookmark ->
                    rootView.cardView.setVisibility(View.VISIBLE)
                    changeBookmarkStatus(bookmark != null)
                    if (bookmark == null) {
                        rootView.commentEditText.setText("")
                    } else {
                        tags = ArrayList(bookmark.tags)
                        rootView.commentEditText.setText(bookmark.comment)
                        rootView.tagListText.setText(TextUtils.join(", ", tags))
                        rootView.privateCheckBox.setChecked(bookmark.isPrivate())
                    }
                }, { e -> Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show() }))

        rootView.registerButton.setOnClickListener {
            rootView.registerButton.setEnabled(false)
            rootView.deleteButton.setEnabled(false)
            val comment = rootView.commentEditText.getText().toString()
            val isPrivate = rootView.privateCheckBox.isChecked()
            val isTwitter = rootView.postTwitterCheckBox.isChecked()
            subscriptions.add(BookmarkApi.requestAddBookmark(url, AccountDAO.get()!!, comment, tags, isPrivate, isTwitter)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ bookmark ->
                        Toast.makeText(getActivity(),
                                if (isAlreadyBookmarked) R.string.register_bookmark_edit_success else R.string.register_bookmark_register_success,
                                Toast.LENGTH_SHORT).show()
                        changeBookmarkStatus(true)
                        rootView.registerButton.setEnabled(true)
                        rootView.deleteButton.setEnabled(true)
                    }, { e ->
                        rootView.registerButton.setEnabled(true)
                        rootView.deleteButton.setEnabled(true)
                        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show()
                    }))
        }
        rootView.deleteButton.setOnClickListener {
            rootView.deleteButton.setEnabled(false)
            subscriptions.add(BookmarkApi.requestDeleteBookmark(url, AccountDAO.get()!!)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        changeBookmarkStatus(false)
                        Toast.makeText(getActivity(), R.string.register_bookmark_delete_success, Toast.LENGTH_SHORT).show()
                    }, {
                        rootView.deleteButton.setEnabled(true)
                        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show()
                    }))
        }
        WidgetObservable.text(rootView.commentEditText)
                .map { event -> event.text().length() }
                .subscribe { i -> rootView.commentCountTextView.setText(getString(R.string.register_bookmark_limit, i)) }
        rootView.tagEditButton.setOnClickListener {
            TagEditDialogFragment.newInstance(tags, this).show(getFragmentManager(), null)
        }
        return rootView
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super<Fragment>.onDestroy()
    }

    override fun onOkClick(tags: ArrayList<String>) {
        if (getView() == null) return
        this.tags = tags
        getView().tagListText.setText(TextUtils.join(", ", tags))
    }

    private fun changeBookmarkStatus(isAlreadyBookmarked: Boolean) {
        if (getView() == null) return
        this.isAlreadyBookmarked = isAlreadyBookmarked
        getView().deleteButton.setEnabled(isAlreadyBookmarked)
        getView().registerButton.setText(getString(if (isAlreadyBookmarked) R.string.register_bookmark_edit else R.string.register_bookmark_resister))
    }
}
