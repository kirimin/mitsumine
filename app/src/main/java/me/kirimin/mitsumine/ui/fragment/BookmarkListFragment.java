package me.kirimin.mitsumine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.model.Bookmark;
import me.kirimin.mitsumine.ui.activity.search.UserSearchActivity;
import me.kirimin.mitsumine.ui.adapter.BookmarkListAdapter;

public class BookmarkListFragment extends Fragment implements BookmarkListAdapter.EntryInfoAdapterListener {

    public static BookmarkListFragment newFragment(List<Bookmark> bookmarkList) {
        BookmarkListFragment fragment = new BookmarkListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArray("bookmarkList", bookmarkList.toArray(new Bookmark[bookmarkList.size()]));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Object[] objects = getArguments().getParcelableArray("bookmarkList");
        Bookmark[] bookmarks = Arrays.asList(objects).toArray(new Bookmark[objects.length]);
        View rootView = inflater.inflate(R.layout.fragment_bookmark_list, container, false);
        BookmarkListAdapter adapter = new BookmarkListAdapter(getActivity(), this);
        ListView listView = (ListView) rootView.findViewById(R.id.BookmarkListListView);
        listView.setAdapter(adapter);
        adapter.addAll(bookmarks);
        return rootView;
    }

    @Override
    public void onCommentClick(View v, Bookmark bookmark) {
        Intent intent = new Intent(getActivity(), UserSearchActivity.class);
        intent.putExtras(UserSearchActivity.buildBundle(bookmark.getUser()));
        startActivity(intent);
    }
}
