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
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.row_my_bookmarks, null)
            holder = ViewHolder(view.findViewById(R.id.card_view),
                    view.findViewById(R.id.MyBookmarksTitleTextView) as TextView,
                    view.findViewById(R.id.MyBookmarksUsersTextView) as TextView,
                    view.findViewById(R.id.MyBookmarksUrlTextView) as TextView)
            view.setTag(holder)
        } else {
            view = convertView
            holder = view.getTag() as ViewHolder
        }
        val bookmark = getItem(position)
        holder.cardView.setTag(bookmark)
        holder.cardView.setOnClickListener(this)
        holder.cardView.setOnLongClickListener(this)
        holder.title.setText(bookmark.getTitle())
        holder.userCount.setText(bookmark.getBookmarkCount().toString() + getContext().getString(R.string.users_lower_case))
        holder.url.setText(bookmark.getLinkUrl())
        return view
    }

    override fun onClick(v: View) {
        listener.onMyBookmarkClick(v, v.getTag() as MyBookmark)
    }

    override fun onLongClick(v: View): Boolean {
        listener.onMyBookmarkLongClick(v, v.getTag() as MyBookmark)
        return false
    }

    class ViewHolder(
            val cardView: View,
            val title: TextView,
            val userCount: TextView,
            val url: TextView) {
    }
}
