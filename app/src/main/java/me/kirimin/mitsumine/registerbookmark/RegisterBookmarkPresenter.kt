package me.kirimin.mitsumine.registerbookmark

import me.kirimin.mitsumine._common.domain.model.Bookmark
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.util.*

class RegisterBookmarkPresenter {

    private val subscriptions = CompositeSubscription()
    private var view: RegisterBookmarkView? = null
    private lateinit var repository: RegisterBookmarkRepository
    private lateinit var url: String

    fun onCreate(view: RegisterBookmarkView, repository: RegisterBookmarkRepository, url: String) {
        this.view = view
        this.repository = repository
        this.url = url
        view.initView()
        subscriptions.add(repository.requestBookmarkInfo(url).subscribe(bookmarkInfoSubscriber))
    }

    fun onDestroy() {
        subscriptions.unsubscribe()
    }

    fun onRegisterButtonClick() {
        val view = view ?: return

        view.disableButtons();
        val (comment, isPrivate, isTwitter) = view.getViewStatus()
        subscriptions.add(repository.requestRegisterBookmark(url, comment, getTags(), isPrivate, isTwitter)
                .subscribe(registerBookmarkSubscriber))
    }

    fun onDeleteButtonClick() {
        val view = view ?: return
        view.disableButtons()
        subscriptions.add(repository.requestDeleteBookmark(url)
                .subscribe(deleteBookmarkSubscriber))
    }

    fun onTagEditButtonClick() {
        view?.showTagEditDialog(getTags())
    }

    fun onCommentEditTextChanged(text: String) {
        view?.updateCommentCount(text.length)
    }

    val bookmarkInfoSubscriber = object : Subscriber<Bookmark?>() {
        override fun onNext(bookmark: Bookmark?) {
            val view = view ?: return
            if (bookmark == null) {
                view.showViewWithoutBookmarkInfo()
            } else {
                view.showViewWithBookmarkInfo(bookmark)
            }
        }

        override fun onError(e: Throwable?) {
            view?.showErrorToast()
        }

        override fun onCompleted() {
        }
    }

    val registerBookmarkSubscriber = object : Subscriber<Bookmark>() {
        override fun onNext(bookmark: Bookmark) {
            view?.showViewWithBookmarkInfo(bookmark)
            view?.showRegisterToast()
        }

        override fun onError(e: Throwable?) {
            view?.showViewWithoutBookmarkInfo()
            view?.showErrorToast()
        }

        override fun onCompleted() {
        }
    }

    val deleteBookmarkSubscriber = object : Subscriber<Boolean>() {
        override fun onNext(result: Boolean) {
            view?.showViewWithoutBookmarkInfo()
            view?.showDeletedToast()
        }

        override fun onError(e: Throwable?) {
            view?.showViewWithoutBookmarkInfo()
            view?.showErrorToast()
        }

        override fun onCompleted() {
        }
    }

    private fun getTags() = ArrayList(view!!.getTagsText().split(", ").filter { it.isNotEmpty() })
}