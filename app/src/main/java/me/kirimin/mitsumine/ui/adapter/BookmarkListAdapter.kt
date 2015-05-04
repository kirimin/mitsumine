package me.kirimin.mitsumine.ui.adapter

import android.app.Activity
import android.text.Spannable
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.makeramen.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine.model.Bookmark
import me.kirimin.mitsumine.ui.event.IfNeededLinkMovementMethod

public class BookmarkListAdapter(activity: Activity, private val listener: BookmarkListAdapter.EntryInfoAdapterListener?)// MovementMethod内でブラウザ遷移時にContextがactivityじゃないと例外が発生するためActivityを取得
: ArrayAdapter<Bookmark>(activity, 0), View.OnClickListener {

    public trait EntryInfoAdapterListener {
        public fun onCommentClick(v: View, bookmark: Bookmark)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.row_bookmark_list, null)
            holder = ViewHolder(view.findViewById(R.id.card_view),
                    view.findViewById(R.id.BookmarkListUserNameTextView) as TextView,
                    view.findViewById(R.id.BookmarkListUserIconImageView) as ImageView,
                    view.findViewById(R.id.BookmarkListCommentTextView) as TextView,
                    view.findViewById(R.id.BookmarkListUserTagTextView) as TextView,
                    view.findViewById(R.id.BookmarkListTimeStampTextView) as TextView)
            view.setTag(holder)
            holder.comment.setOnTouchListener { v, event ->
                val linkMovementMethod = IfNeededLinkMovementMethod()
                val onTouchResult = linkMovementMethod.onTouchEvent(holder.comment, holder.comment.getText() as Spannable, event);
                holder.comment.setMovementMethod(linkMovementMethod)
                holder.comment.setMovementMethod(null)
                holder.comment.setFocusable(false)
                onTouchResult
            }
        } else {
            view = convertView
            holder = view.getTag() as ViewHolder
        }
        val bookmark = getItem(position)
        holder.cardView.setOnClickListener(this)
        holder.cardView.setTag(bookmark)
        holder.userName.setText(bookmark.getUser())
        holder.comment.setText(bookmark.getComment())
        holder.tag.setText(TextUtils.join(", ", bookmark.getTags()))
        holder.timeStamp.setText(bookmark.getTimeStamp())
        val transformation = RoundedTransformationBuilder().borderWidthDp(0f).cornerRadiusDp(32f).oval(false).build()
        Picasso.with(getContext()).load(bookmark.getUserIcon()).fit().transform(transformation).into(holder.userIcon)
        return view
    }

    override fun onClick(v: View) {
        if (listener == null) {
            return
        }
        listener.onCommentClick(v, v.getTag() as Bookmark)
    }


    class ViewHolder(
            val cardView: View,
            val userName: TextView,
            val userIcon: ImageView,
            val comment: TextView,
            val tag: TextView,
            val timeStamp: TextView
    ) {}
}
