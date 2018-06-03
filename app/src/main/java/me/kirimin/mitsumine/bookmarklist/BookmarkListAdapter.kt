package me.kirimin.mitsumine.bookmarklist

import android.content.Context
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.makeramen.RoundedTransformationBuilder
import com.squareup.picasso.Picasso

import me.kirimin.mitsumine.R
import me.kirimin.mitsumine._common.domain.model.Bookmark
import me.kirimin.mitsumine._common.network.repository.StarRepository
import rx.Subscription

class BookmarkListAdapter(context: Context, val presenter: BookmarkListPresenter, val entryId: String) : ArrayAdapter<Bookmark>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.row_bookmark_list, null)
            val popupWindow = BookmarkPopupWindowBuilder.build(context)
            holder = ViewHolder(view, popupWindow)
            view.tag = holder
            holder.comment.movementMethod = LinkMovementMethod.getInstance()
            holder.more.setOnClickListener { v ->
                popupWindow.showAsDropDown(v, v.width, -v.height);
            }
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        val bookmark = getItem(position)
        holder.cardView.tag = bookmark
        holder.userName.text = bookmark.user
        holder.comment.text = bookmark.comment
        holder.tag.text = TextUtils.join(", ", bookmark.tags)
        holder.timeStamp.text = bookmark.timestamp
        holder.stars.visibility = View.GONE
        if (holder.stars.tag is Subscription) {
            (holder.stars.tag as Subscription).unsubscribe()
        }

        holder.stars.visibility = View.GONE
        bookmark.stars?.let {
            if (it.allStarsCount > 0) {
                holder.stars.visibility = View.VISIBLE
                holder.stars.text = it.allStarsCount.toString()
            }
        }

        val transformation = RoundedTransformationBuilder().borderWidthDp(0f).cornerRadiusDp(32f).oval(false).build()
        Picasso.with(context).load(bookmark.userIcon).fit().transform(transformation).into(holder.userIcon)
        holder.popupList.setOnItemClickListener { adapterView, view, i, l ->
            presenter.onMoreIconClick(bookmark, entryId, i)
            holder.popupWindow.dismiss()
        }

        return view
    }

    class ViewHolder(view: View, val popupWindow: PopupWindow) {
        val cardView: View = view.findViewById(R.id.card_view)
        val userName: TextView = view.findViewById(R.id.BookmarkListUserNameTextView) as TextView
        val userIcon: ImageView = view.findViewById(R.id.BookmarkListUserIconImageView) as ImageView
        val comment: TextView = view.findViewById(R.id.BookmarkListCommentTextView) as TextView
        val tag: TextView = view.findViewById(R.id.BookmarkListUserTagTextView) as TextView
        val timeStamp: TextView = view.findViewById(R.id.BookmarkListTimeStampTextView) as TextView
        val stars: TextView = view.findViewById(R.id.BookmarkListStarsTextView) as TextView
        val more: ImageView = view.findViewById(R.id.BookmarkListMoreImageView) as ImageView
        val popupList: ListView = popupWindow.contentView.findViewById(R.id.popupWindowListView) as ListView
    }
}
