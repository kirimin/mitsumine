package me.kirimin.mitsumine.registerbookmark

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_register_bookmark.*

import java.util.ArrayList

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.domain.model.Bookmark

class RegisterBookmarkFragment : RegisterBookmarkView, Fragment(), TagEditDialogFragment.OnOkClickListener {

    private val presenter = RegisterBookmarkPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_register_bookmark, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val url = arguments.getString("url") ?: throw IllegalStateException("url is null")
        presenter.onCreate(this, RegisterBookmarkUseCase(), url)
    }

    override fun initView() {
        cardView.visibility = View.INVISIBLE
        registerButton.setOnClickListener { presenter.onRegisterButtonClick() }
        deleteButton.setOnClickListener { presenter.onDeleteButtonClick() }
        tagEditButton.setOnClickListener { presenter.onTagEditButtonClick() }
        commentEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                presenter.onCommentEditTextChanged(s.toString())
            }
        })
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onOkClick(tags: ArrayList<String>) {
        tagListText.text = TextUtils.join(", ", tags)
    }

    override fun showViewWithoutBookmarkInfo() {
        cardView.visibility = View.VISIBLE
        deleteButton.isEnabled = false
        registerButton.isEnabled = true
        registerButton.text = getString(R.string.register_bookmark_resister)
    }

    override fun showViewWithBookmarkInfo(bookmark: Bookmark) {
        cardView.visibility = View.VISIBLE
        deleteButton.isEnabled = true
        registerButton.isEnabled = true
        registerButton.text = getString(R.string.register_bookmark_edit)
        commentEditText.setText(bookmark.comment)
        tagListText.text = TextUtils.join(", ", bookmark.tags)
        privateCheckBox.isChecked = bookmark.isPrivate
    }

    override fun showErrorToast() {
        Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show()
    }

    override fun showDeletedToast() {
        Toast.makeText(activity, R.string.register_bookmark_delete_success, Toast.LENGTH_SHORT).show()
    }

    override fun showRegisterToast() {
        Toast.makeText(activity, R.string.register_bookmark_register_success, Toast.LENGTH_SHORT).show()
    }

    override fun showTagEditDialog(tags: ArrayList<String>) {
        TagEditDialogFragment.newInstance(tags, this).show(fragmentManager, null)
    }

    override fun disableButtons() {
        registerButton.isEnabled = false
        deleteButton.isEnabled = false
    }

    override fun updateCommentCount(length: Int) {
        commentCountTextView.text = getString(R.string.register_bookmark_limit, length)
    }

    override fun getViewStatus() = Triple(commentEditText.text.toString(), privateCheckBox.isChecked, postTwitterCheckBox.isChecked)

    override fun getTagsText() = tagListText.text.toString()

    companion object {

        fun newFragment(url: String): RegisterBookmarkFragment {
            val fragment = RegisterBookmarkFragment()
            val bundle = Bundle()
            bundle.putString("url", url)
            fragment.arguments = bundle
            return fragment
        }
    }
}
