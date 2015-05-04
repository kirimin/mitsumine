package me.kirimin.mitsumine.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.model.MyBookmark

public class MyBookmarksAdapter(context: Context, private val listener: MyBookmarksAdapter.OnMyBookmarkClickListener) : ArrayAdapter<MyBookmark>(context, 0), View.OnClickListener, View.OnLongClickListener {

    public trait OnMyBookmarkClickListener {
        public fun onMyBookmarkClick(v: View, myBookmark: MyBookmark)

        public fun onMyBookmarkLongClick(v: View, myBookmark: MyBookmark)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_my_bookmarks, null)
            holder = ViewHolder()
            holder.cardView = convertView!!.findViewById(R.id.card_view)
            holder.title = convertView!!.findViewById(R.id.MyBookmarksTitleTextView) as TextView
            holder.userCount = convertView!!.findViewById(R.id.MyBookmarksUsersTextView) as TextView
            holder.url = convertView!!.findViewById(R.id.MyBookmarksUrlTextView) as TextView
            convertView!!.setTag(holder)
        } else {
            holder = convertView!!.getTag() as ViewHolder
        }
        val bookmark = getItem(position)
        holder.cardView.setTag(bookmark)
        holder.cardView.setOnClickListener(this)
        holder.cardView.setOnLongClickListener(this)
        holder.title.setText(bookmark.getTitle())
        holder.userCount.setText(bookmark.getBookmarkCount() + getContext().getString(R.string.users_lower_case))
        holder.url.setText(bookmark.getLinkUrl())
        return convertView
    }

    override fun onClick(v: View) {
        listener.onMyBookmarkClick(v, v.getTag() as MyBookmark)
    }

    override fun onLongClick(v: View): Boolean {
        listener.onMyBookmarkLongClick(v, v.getTag() as MyBookmark)
        return false
    }

    class ViewHolder {
        var cardView: View
        var title: TextView
        var userCount: TextView
        var url: TextView
    }
}
