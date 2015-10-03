package me.kirimin.mitsumine.ui.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.ui.adapter.TagEditDialogFragmentAdapter
import rx.functions.Action1

import kotlinx.android.synthetic.dialog_fragment_tag_edit.view.*

public class TagEditDialogFragment : DialogFragment() {

    companion object {

        fun newInstance(tags: ArrayList<String>, targetFragment: Fragment): TagEditDialogFragment {
            val fragment = TagEditDialogFragment()
            val bundle = Bundle()
            bundle.putStringArrayList("tags", tags)
            fragment.arguments = bundle
            fragment.setTargetFragment(targetFragment, 0)
            return fragment
        }
    }

    public interface OnOkClickListener {
        public fun onOkClick(tags: ArrayList<String>)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val tags = arguments.getStringArrayList("tags")
        val rootView = inflater.inflate(R.layout.dialog_fragment_tag_edit, container, false)
        rootView.listView.adapter = TagEditDialogFragmentAdapter(activity, tags, Action1 { onClickEvent ->
            val adapter = rootView.listView.adapter as TagEditDialogFragmentAdapter
            adapter.remove(onClickEvent.view().tag as String)
        })
        rootView.okButton.setOnClickListener {
            val adapter = rootView.listView.adapter as TagEditDialogFragmentAdapter
            (targetFragment as OnOkClickListener).onOkClick(adapter.getAllItem())
            dismiss()
        }
        rootView.addButton.setOnClickListener {
            if (!rootView.tagNameEditText.text.toString().isEmpty()) {
                val adapter = rootView.listView.adapter as TagEditDialogFragmentAdapter
                adapter.remove(rootView.tagNameEditText.text.toString())
                adapter.add(rootView.tagNameEditText.text.toString())
                rootView.tagNameEditText.setText("")
            }
        }

        getDialog().setTitle(R.string.register_bookmark_tag_edit)
        return rootView
    }
}
