package me.kirimin.mitsumine.registerbookmark

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

import java.util.ArrayList

import me.kirimin.mitsumine.R
import rx.android.view.OnClickEvent
import rx.android.view.ViewObservable
import rx.functions.Action1

public class TagEditDialogFragmentAdapter(context: Context, tags: ArrayList<String>, private val onDeleteButtonClick: Action1<OnClickEvent>) : ArrayAdapter<String>(context, 0, tags) {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = inflater.inflate(R.layout.row_tag_edit_dialog_fragment, null)
            val holder = ViewHolder(
                    view.findViewById(R.id.RegisterBookmarkTagEditDialogTagName) as TextView,
                    view.findViewById(R.id.RegisterBookmarkTagEditDialogDelete) as Button)
            ViewObservable.clicks(holder.deleteButton).subscribe(onDeleteButtonClick)
            view.tag = holder
        } else {
            view = convertView
        }
        val holder = view.tag as ViewHolder
        holder.tagName.text = getItem(position)
        holder.deleteButton.tag = holder.tagName.text.toString()
        return view
    }

    public fun getAllItem(): ArrayList<String> {
        val tags = ArrayList<String>()
        for (i in 0..count - 1) {
            tags.add(getItem(i))
        }
        return tags
    }

    class ViewHolder(
            val tagName: TextView,
            val deleteButton: Button) {
    }
}
