package me.kirimin.mitsumine.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast

import org.json.JSONObject

import java.util.ArrayList

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.db.AccountDAO
import me.kirimin.mitsumine.model.Bookmark
import me.kirimin.mitsumine.network.api.BookmarkApi
import me.kirimin.mitsumine.util.EntryInfoFunc
import rx.android.schedulers.AndroidSchedulers
import rx.android.widget.OnTextChangeEvent
import rx.android.widget.WidgetObservable
import rx.functions.Action1
import rx.functions.Func1
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
        val url = getArguments().getString("url")
        if (url == null) {
            throw IllegalStateException("url is null")
        }
        val rootView = inflater.inflate(R.layout.fragment_register_bookmark, container, false)
        rootView.cardView.setVisibility(View.INVISIBLE)
        subscriptions.add(BookmarkApi.requestBookmarkInfo(url, AccountDAO.get())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(EntryInfoFunc.mapToMyBookmarkInfo())
                .subscribe({ bookmark ->
                    rootView.cardView.setVisibility(View.VISIBLE)
                    val isAlreadyBookmarked = bookmark != null
                    changeBookmarkStatus(isAlreadyBookmarked)
                    if (!isAlreadyBookmarked) {
                        rootView.commentEditText.setText("")
                    } else {
                        tags = ArrayList(bookmark.getTags())
                        rootView.commentEditText.setText(bookmark.getComment())
                        rootView.tagListText.setText(TextUtils.join(", ", tags))
                        rootView.privateCheckBox.setChecked(bookmark.isPrivate())
                    }
                }, { e -> showToastIfExistsActivity(R.string.network_error) }))

        rootView.registerButton.setOnClickListener {
            rootView.registerButton.setEnabled(false)
            rootView.deleteButton.setEnabled(false)
            subscriptions.add(BookmarkApi.requestAddBookmark(url, AccountDAO.get(), rootView.commentEditText.getText().toString(), tags, rootView.privateCheckBox.isChecked())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        showToastIfExistsActivity(if (isAlreadyBookmarked) R.string.register_bookmark_edit_success else R.string.register_bookmark_register_success)
                        changeBookmarkStatus(true)
                        rootView.registerButton.setEnabled(true)
                        rootView.deleteButton.setEnabled(true)
                    }, {
                        rootView.registerButton.setEnabled(true)
                        rootView.deleteButton.setEnabled(true)
                        showToastIfExistsActivity(R.string.network_error)
                    }))
        }
        rootView.deleteButton.setOnClickListener {
            rootView.deleteButton.setEnabled(false)
            subscriptions.add(BookmarkApi.requestDeleteBookmark(url, AccountDAO.get())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        changeBookmarkStatus(false)
                        showToastIfExistsActivity(R.string.register_bookmark_delete_success)
                    }, {
                        rootView.deleteButton.setEnabled(true)
                        showToastIfExistsActivity(R.string.network_error)
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

    private fun showToastIfExistsActivity(messageResourceId: Int) {
        Toast.makeText(getActivity(), messageResourceId, Toast.LENGTH_SHORT).show()
    }
}
