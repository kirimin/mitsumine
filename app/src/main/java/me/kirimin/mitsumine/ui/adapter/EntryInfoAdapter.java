package me.kirimin.mitsumine.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.Bookmark;

public class EntryInfoAdapter extends ArrayAdapter<Bookmark> {


    public EntryInfoAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_entry_info, null);
            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.EntryInfoUserNameTextView);
            holder.comment = (TextView) convertView.findViewById(R.id.EntryInfoCommentTextView);
            holder.userIcon = (ImageView) convertView.findViewById(R.id.EntryInfoUserIconImageView);
            holder.tag = (TextView) convertView.findViewById(R.id.EntryInfoUserTagTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bookmark bookmark = getItem(position);
        holder.userName.setText(bookmark.getUser());
        holder.comment.setText(bookmark.getComment());
        holder.tag.setText(TextUtils.join(", ", bookmark.getTags()));
        Picasso.with(getContext()).load(bookmark.getUserIcon()).fit().into(holder.userIcon);
        return convertView;
    }

    static class ViewHolder {
        TextView userName;
        ImageView userIcon;
        TextView comment;
        TextView tag;
    }
}
