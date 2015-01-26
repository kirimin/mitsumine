package me.kirimin.mitsumine.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import me.kirimin.mitsumine.R;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;
import rx.functions.Action1;

public class TagEditDialogFragmentAdapter extends ArrayAdapter<String> {

    private final LayoutInflater inflater;
    private final Action1<OnClickEvent> onDeleteButtonClick;

    public TagEditDialogFragmentAdapter(Context context, ArrayList<String> tags, Action1<OnClickEvent> onDeleteButtonClick) {
        super(context, 0, tags);
        inflater = LayoutInflater.from(context);
        this.onDeleteButtonClick = onDeleteButtonClick;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_tag_edit_dialog_fragment, null);
            ViewHolder holder = new ViewHolder();
            holder.tagName = (TextView) convertView.findViewById(R.id.RegisterBookmarkTagEditDialogTagName);
            holder.deleteButton = (Button) convertView.findViewById(R.id.RegisterBookmarkTagEditDialogDelete);
            ViewObservable.clicks(holder.deleteButton).subscribe(onDeleteButtonClick);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tagName.setText(getItem(position));
        holder.deleteButton.setTag(holder.tagName.getText().toString());
        return convertView;
    }

    public ArrayList<String> getAllItem() {
        ArrayList<String> tags = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            tags.add(getItem(i));
        }
        return tags;
    }

    static class ViewHolder {
        TextView tagName;
        Button deleteButton;
    }
}
