package me.kirimin.mitsumine.registerbookmark

import me.kirimin.mitsumine._common.domain.model.Bookmark
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

class RegisterBookmarkPresenter @Inject constructor(val useCase: RegisterBookmarkUseCase) {

    private val subscriptions = CompositeSubscription()
    private var view: RegisterBookmarkView? = null
    private lateinit var url: String

    fun onCreate(registerBookmarkView: RegisterBookmarkView, url: String) {
        this.view = registerBookmarkView
        this.url = url
        registerBookmarkView.initView()
        subscriptions.add(useCase.requestBookmarkInfo(url)
                .subscribe({ bookmark ->
                    if (bookmark is Bookmark.EmptyBookmark) {
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
        subscriptions.add(useCase.requestAddBookmark(url, comment, getTags(), isPrivate, isTwitter)
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
        subscriptions.add(useCase.requestDeleteBookmark(url)
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