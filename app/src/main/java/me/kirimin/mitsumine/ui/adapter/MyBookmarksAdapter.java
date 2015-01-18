package me.kirimin.mitsumine.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.MyBookmark;

public class MyBookmarksAdapter extends ArrayAdapter<MyBookmark> implements View.OnClickListener {

    public interface OnMyBookmarkClickListener {
        public void onMyBookmarkClick(View v, MyBookmark myBookmark);
    }

    private final OnMyBookmarkClickListener listener;

    public MyBookmarksAdapter(Context context, OnMyBookmarkClickListener listener) {
        super(context, 0);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_my_bookmarks, null);
            holder = new ViewHolder();
            holder.cardView = convertView.findViewById(R.id.card_view);
            holder.title = (TextView) convertView.findViewById(R.id.MyBookmarksTitleTextView);
            holder.userCount = (TextView) convertView.findViewById(R.id.MyBookmarksUsersTextView);
            holder.url = (TextView) convertView.findViewById(R.id.MyBookmarksUrlTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyBookmark bookmark = getItem(position);
        holder.cardView.setTag(bookmark);
        holder.cardView.setOnClickListener(this);
        holder.title.setText(bookmark.getTitle());
        holder.userCount.setText(bookmark.getBookmarkCount() + "users");
        holder.url.setText(bookmark.getLinkUrl());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        listener.onMyBookmarkClick(v, (MyBookmark) v.getTag());
    }

    static class ViewHolder {
        View cardView;
        TextView title;
        TextView userCount;
        TextView url;
    }
}
