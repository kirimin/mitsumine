package me.kirimin.mitsumine.registerbookmark

import rx.subscriptions.CompositeSubscription
import java.util.*

class RegisterBookmarkPresenter {

    private val subscriptions = CompositeSubscription()
    private var view: RegisterBookmarkView? = null
    private lateinit var repository: RegisterBookmarkRepository
    private lateinit var url: String

    fun onCreate(registerBookmarkView: RegisterBookmarkView, repository: RegisterBookmarkRepository, url: String) {
        this.view = registerBookmarkView
        this.repository = repository
        this.url = url
        registerBookmarkView.initView()
        subscriptions.add(repository.requestBookmarkInfo(url)
                .subscribe({ bookmark ->
                    if (bookmark == null) {
                        view?.showViewWithoutBookmarkInfo()
                    } else {
                        view?.showViewWithBookmarkInfo(bookmark)
                    }
                }, {
                    view?.showErrorToast()
                })
        )
    }

    fun onDestroy() {
        subscriptions.unsubscribe()
    }

    fun onRegisterButtonClick() {
        view!!.disableButtons();
        val (comment, isPrivate, isTwitter) = view!!.getViewStatus()
        subscriptions.add(repository.requestRegisterBookmark(url, comment, getTags(), isPrivate, isTwitter)
                .subscribe({
                    view?.showViewWithBookmarkInfo(it)
                    view?.showRegisterToast()
                }, {
                    view?.showViewWithoutBookmarkInfo()
                    view?.showErrorToast()
                })
        )
    }

    fun onDeleteButtonClick() {
        view?.disableButtons()
        subscriptions.add(repository.requestDeleteBookmark(url)
                .subscribe({
                    view?.showViewWithoutBookmarkInfo()
                    view?.showDeletedToast()
                }, {
                    view?.showViewWithoutBookmarkInfo()
                    view?.showErrorToast()
                })
        )
    }

    fun onTagEditButtonClick() {
        view?.showTagEditDialog(getTags())
    }

    fun onCommentEditTextChanged(text: String) {
        view?.updateCommentCount(text.length)
    }

    private fun getTags() = ArrayList(view!!.getTagsText().split(", ").filter { it.isNotEmpty() })
}