package me.kirimin.mitsumine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.ui.adapter.TagEditDialogFragmentAdapter;
import rx.android.events.OnClickEvent;
import rx.functions.Action1;

public class TagEditDialogFragment extends DialogFragment {

    public interface OnOkClickListener {
        public void onOkClick(ArrayList<String> tags);
    }

    static TagEditDialogFragment newInstance(ArrayList tags, Fragment targetFragment) {
        TagEditDialogFragment fragment = new TagEditDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("tags", tags);
        fragment.setArguments(bundle);
        fragment.setTargetFragment(targetFragment, 0);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ArrayList<String> tags = getArguments().getStringArrayList("tags");
        final View rootView = inflater.inflate(R.layout.dialog_fragment_tag_edit, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.RegisterBookmarkTagEditDialogListView);
        listView.setAdapter(new TagEditDialogFragmentAdapter(getActivity(), tags, new Action1<OnClickEvent>() {
            @Override
            public void call(OnClickEvent onClickEvent) {
                TagEditDialogFragmentAdapter adapter = (TagEditDialogFragmentAdapter) listView.getAdapter();
                adapter.remove((String) onClickEvent.view.getTag());
            }
        }));
        rootView.findViewById(R.id.RegisterBookmarkTagEditDialogOkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagEditDialogFragmentAdapter adapter = (TagEditDialogFragmentAdapter) listView.getAdapter();
                ((OnOkClickListener) getTargetFragment()).onOkClick(adapter.getAllItem());
                dismiss();
            }
        });
        rootView.findViewById(R.id.RegisterBookmarkTagEditDialogAddButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tagNameEdit = (EditText) rootView.findViewById(R.id.RegisterBookmarkTagEditDialogAddEditText);
                if (!tagNameEdit.getText().toString().isEmpty()) {
                    TagEditDialogFragmentAdapter adapter = (TagEditDialogFragmentAdapter) listView.getAdapter();
                    adapter.remove(tagNameEdit.getText().toString());
                    adapter.add(tagNameEdit.getText().toString());
                    tagNameEdit.setText("");
                }
            }
        });

        getDialog().setTitle("タグ編集");
        return rootView;
    }
}
