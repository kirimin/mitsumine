package me.kirimin.mitsumine.bookmarklist

import android.app.Activity
import android.text.Spannable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.makeramen.RoundedTransformationBuilder
import com.squareup.picasso.Picasso

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.common.domain.model.Bookmark
import me.kirimin.mitsumine.bookmarklist.BookmarkListPresenter
import me.kirimin.mitsumine.common.widget.IfNeededLinkMovementMethod

class BookmarkListAdapter(activity: Activity, val presenter: BookmarkListPresenter) : ArrayAdapter<Bookmark>(activity, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.row_bookmark_list, null)
            holder = ViewHolder(view)
            view.tag = holder
            holder.comment.setOnTouchListener { v, event ->
                val linkMovementMethod = IfNeededLinkMovementMethod()
                holder.comment.movementMethod = linkMovementMethod
                val onTouchResult = linkMovementMethod.onTouchEvent(holder.comment, holder.comment.text as Spannable, event);
                holder.comment.movementMethod = null
                holder.comment.isFocusable = false
                onTouchResult
            }
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        val bookmark = getItem(position)
        holder.cardView.setOnClickListener { v -> presenter.onItemClick(v.tag as Bookmark) }
        holder.cardView.tag = bookmark
        holder.userName.text = bookmark.user
        holder.comment.text = bookmark.comment
        holder.tag.text = TextUtils.join(", ", bookmark.tags)
        holder.timeStamp.text = bookmark.timeStamp
        val transformation = RoundedTransformationBuilder().borderWidthDp(0f).cornerRadiusDp(32f).oval(false).build()
        Picasso.with(context).load(bookmark.userIcon).fit().transform(transformation).into(holder.userIcon)
        return view
    }

    class ViewHolder(view: View) {
        val cardView: View = view.findViewById(R.id.card_view)
        val userName: TextView = view.findViewById(R.id.BookmarkListUserNameTextView) as TextView
        val userIcon: ImageView = view.findViewById(R.id.BookmarkListUserIconImageView) as ImageView
        val comment: TextView = view.findViewById(R.id.BookmarkListCommentTextView) as TextView
        val tag: TextView = view.findViewById(R.id.BookmarkListUserTagTextView) as TextView
        val timeStamp: TextView = view.findViewById(R.id.BookmarkListTimeStampTextView) as TextView
    }
}
