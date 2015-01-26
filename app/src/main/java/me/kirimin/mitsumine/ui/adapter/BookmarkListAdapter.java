package me.kirimin.mitsumine.ui.adapter;

import android.app.Activity;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.ui.event.IfNeededLinkMovementMethod;

public class BookmarkListAdapter extends ArrayAdapter<Bookmark> implements View.OnClickListener {

    public interface EntryInfoAdapterListener {
        void onCommentClick(View v, Bookmark bookmark);
    }

    private final EntryInfoAdapterListener listener;

    public BookmarkListAdapter(Activity activity, EntryInfoAdapterListener listener) {
        // MovementMethod内でブラウザ遷移時にContextがactivityじゃないと例外が発生するためActivityを取得
        super(activity, 0);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_bookmark_list, null);
            holder = new ViewHolder();
            holder.cardView = convertView.findViewById(R.id.card_view);
            holder.userName = (TextView) convertView.findViewById(R.id.BookmarkListUserNameTextView);
            holder.comment = (TextView) convertView.findViewById(R.id.BookmarkListCommentTextView);
            holder.comment.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    LinkMovementMethod linkMovementMethod = new IfNeededLinkMovementMethod();
                    holder.comment.setMovementMethod(linkMovementMethod);
                    boolean onTouchResult = linkMovementMethod.onTouchEvent(holder.comment, (Spannable) holder.comment.getText(), event);
                    holder.comment.setMovementMethod(null);
                    holder.comment.setFocusable(false);
                    return onTouchResult;
                }
            });
            holder.userIcon = (ImageView) convertView.findViewById(R.id.BookmarkListUserIconImageView);
            holder.tag = (TextView) convertView.findViewById(R.id.BookmarkListUserTagTextView);
            holder.timeStamp = (TextView) convertView.findViewById(R.id.BookmarkListTimeStampTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bookmark bookmark = getItem(position);
        holder.cardView.setOnClickListener(this);
        holder.cardView.setTag(bookmark);
        holder.userName.setText(bookmark.getUser());
        holder.comment.setText(bookmark.getComment());
        holder.tag.setText(TextUtils.join(", ", bookmark.getTags()));
        holder.timeStamp.setText(bookmark.getTimeStamp());
        Transformation transformation = new RoundedTransformationBuilder().borderWidthDp(0).cornerRadiusDp(32).oval(false).build();
        Picasso.with(getContext()).load(bookmark.getUserIcon()).fit().transform(transformation).into(holder.userIcon);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }
        listener.onCommentClick(v, (Bookmark) v.getTag());
    }


    static class ViewHolder {
        View cardView;
        TextView userName;
        ImageView userIcon;
        TextView comment;
        TextView tag;
        TextView timeStamp;
    }
}
