package me.kirimin.mitsumine.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import java.util.ArrayList

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.data.database.AccountDAO
import me.kirimin.mitsumine.data.network.api.BookmarkApi
import rx.android.schedulers.AndroidSchedulers
import rx.android.widget.WidgetObservable
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

import kotlinx.android.synthetic.main.fragment_register_bookmark.view.*

class RegisterBookmarkFragment : Fragment(), TagEditDialogFragment.OnOkClickListener {

    companion object {

        public fun newFragment(url: String): RegisterBookmarkFragment {
            val fragment = RegisterBookmarkFragment()
            val bundle = Bundle()
            bundle.putString("url", url)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var isAlreadyBookmarked: Boolean = false
    private var tags = ArrayList<String>()
    private val subscriptions = CompositeSubscription()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val url = arguments.getString("url") ?: throw IllegalStateException("url is null")
        val rootView = inflater.inflate(R.layout.fragment_register_bookmark, container, false)
        rootView.cardView.visibility = View.INVISIBLE
        subscriptions.add(BookmarkApi.requestBookmarkInfo(url, AccountDAO.get()!!)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bookmark ->
                    rootView.cardView.visibility = View.VISIBLE
                    changeBookmarkStatus(bookmark != null)
                    if (bookmark == null) {
                        rootView.commentEditText.setText("")
                    } else {
                        tags = ArrayList(bookmark.tags)
                        rootView.commentEditText.setText(bookmark.comment)
                        rootView.tagListText.text = TextUtils.join(", ", tags)
                        rootView.privateCheckBox.isChecked = bookmark.isPrivate()
                    }
                }, { e -> Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show() }))

        rootView.registerButton.setOnClickListener {
            rootView.registerButton.isEnabled = false
            rootView.deleteButton.isEnabled = false
            val comment = rootView.commentEditText.text.toString()
            val isPrivate = rootView.privateCheckBox.isChecked
            val isTwitter = rootView.postTwitterCheckBox.isChecked
            subscriptions.add(BookmarkApi.requestAddBookmark(url, AccountDAO.get()!!, comment, tags, isPrivate, isTwitter)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ bookmark ->
                        Toast.makeText(activity,
                                if (isAlreadyBookmarked) R.string.register_bookmark_edit_success else R.string.register_bookmark_register_success,
                                Toast.LENGTH_SHORT).show()
                        changeBookmarkStatus(true)
                        rootView.registerButton.isEnabled = true
                        rootView.deleteButton.isEnabled = true
                    }, { e ->
                        rootView.registerButton.isEnabled = true
                        rootView.deleteButton.isEnabled = true
                        Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show()
                    }))
        }
        rootView.deleteButton.setOnClickListener {
            rootView.deleteButton.isEnabled = false
            subscriptions.add(BookmarkApi.requestDeleteBookmark(url, AccountDAO.get()!!)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        changeBookmarkStatus(false)
                        Toast.makeText(activity, R.string.register_bookmark_delete_success, Toast.LENGTH_SHORT).show()
                    }, { e ->
                        rootView.deleteButton.setEnabled(true)
                        Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show()
                    }))
        }
        WidgetObservable.text(rootView.commentEditText)
                .map { event -> event.text().length }
                .subscribe { i -> rootView.commentCountTextView.text = getString(R.string.register_bookmark_limit, i) }
        rootView.tagEditButton.setOnClickListener {
            TagEditDialogFragment.newInstance(tags, this).show(fragmentManager, null)
        }
        return rootView
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }

    override fun onOkClick(tags: ArrayList<String>) {
        val view = view ?: return
        this.tags = tags
        view.tagListText.text = TextUtils.join(", ", tags)
    }

    private fun changeBookmarkStatus(isAlreadyBookmarked: Boolean) {
        val view = view ?: return
        this.isAlreadyBookmarked = isAlreadyBookmarked
        view.deleteButton.isEnabled = isAlreadyBookmarked
        view.registerButton.text = getString(if (isAlreadyBookmarked) R.string.register_bookmark_edit else R.string.register_bookmark_resister)
    }
}
