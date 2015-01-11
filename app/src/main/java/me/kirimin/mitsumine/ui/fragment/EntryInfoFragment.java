package me.kirimin.mitsumine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.ui.activity.search.UserSearchActivity;
import me.kirimin.mitsumine.ui.adapter.EntryInfoAdapter;

public class EntryInfoFragment extends Fragment implements EntryInfoAdapter.EntryInfoAdapterListener {

    public static EntryInfoFragment newFragment(List<Bookmark> bookmarkList) {
        EntryInfoFragment fragment = new EntryInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArray("bookmarkList", bookmarkList.toArray(new Bookmark[bookmarkList.size()]));
        fragment.setArguments(bundle);
        return fragment;
    }

    private ListView mListView;
    private EntryInfoAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bookmark[] bookmarks = (Bookmark[]) getArguments().getParcelableArray("bookmarkList");
        View rootView = inflater.inflate(R.layout.fragment_entry_info, container, false);
        mAdapter = new EntryInfoAdapter(getActivity().getApplicationContext(), this);
        mListView = (ListView) rootView.findViewById(R.id.EntryInfoListView);
        mListView.setAdapter(mAdapter);
        mAdapter.addAll(bookmarks);
        return rootView;
    }

    @Override
    public void onCommentClick(View v, Bookmark bookmark) {
        Intent intent = new Intent(getActivity(), UserSearchActivity.class);
        intent.putExtras(UserSearchActivity.buildBundle(bookmark.getUser()));
        startActivity(intent);
    }
}
