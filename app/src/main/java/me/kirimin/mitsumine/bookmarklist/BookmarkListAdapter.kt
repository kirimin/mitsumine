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
import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.network.StarApi
import me.kirimin.mitsumine.bookmarklist.BookmarkListPresenter
import me.kirimin.mitsumine._common.widget.IfNeededLinkMovementMethod
import rx.Subscription

class BookmarkListAdapter(activity: Activity, val presenter: BookmarkListPresenter, val entryId: String) : ArrayAdapter<Bookmark>(activity, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.row_bookmark_list, null)
            holder = ViewHolder(view)
            view.tag = holder
            holder.comment.setOnTouchListener { v, event ->
                // コメント欄のリンク部分をタップ時はリンクを開きそれ以外の場所のタップは透過する為の処理
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
        holder.stars.visibility = View.GONE
        if (holder.stars.tag is Subscription) {
            (holder.stars.tag as Subscription).unsubscribe()
        }
        holder.stars.tag = StarApi.requestCommentStar(context, bookmark.user, bookmark.timeStamp, entryId).subscribe ({
            if (it.isEmpty()) {
                holder.stars.visibility = View.GONE
            } else {
                holder.stars.visibility = View.VISIBLE
                holder.stars.text = it.count().toString()
            }
        }, { holder.stars.visibility = View.GONE })
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
        val stars: TextView = view.findViewById(R.id.BookmarkListStarsTextView) as TextView
    }
}
