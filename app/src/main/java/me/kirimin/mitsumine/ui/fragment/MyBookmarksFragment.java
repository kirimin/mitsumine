package me.kirimin.mitsumine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import me.kirimin.mitsumine.R;
import me.kirimin.mitsumine.db.AccountDAO;
import me.kirimin.mitsumine.model.Account;
import me.kirimin.mitsumine.model.MyBookmark;
import me.kirimin.mitsumine.network.api.MyBookmarksApi;
import me.kirimin.mitsumine.ui.activity.EntryInfoActivity;
import me.kirimin.mitsumine.ui.adapter.MyBookmarksAdapter;
import me.kirimin.mitsumine.util.MyBookmarksFunc;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MyBookmarksFragment extends Fragment implements MyBookmarksAdapter.OnMyBookmarkClickListener {

    public static MyBookmarksFragment newFragment(String keyword) {
        MyBookmarksFragment fragment = new MyBookmarksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);
        return fragment;
    }

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_bookmarks, container, false);
        Account account = AccountDAO.get();
        final ListView listView = (ListView) rootView.findViewById(R.id.MyBookmarksListView);
        final MyBookmarksAdapter adapter = new MyBookmarksAdapter(getActivity(), this);
        subscriptions.add(MyBookmarksApi.request(account)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(MyBookmarksFunc.mapToMyBookmarkList())
                .subscribe(new Observer<MyBookmark>() {
                    @Override
                    public void onCompleted() {
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MyBookmark myBookmark) {
                        adapter.add(myBookmark);
                    }
                }));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        subscriptions.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onMyBookmarkClick(View v, MyBookmark myBookmark) {
        Intent intent = new Intent(getActivity(), EntryInfoActivity.class);
        intent.putExtras(EntryInfoActivity.buildBundle(myBookmark.getLinkUrl()));
        startActivity(intent);
    }
}