package me.kirimin.mitsumine.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
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
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MyBookmarksFragment extends Fragment implements MyBookmarksAdapter.OnMyBookmarkClickListener, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {

    public static MyBookmarksFragment newFragment(String keyword) {
        MyBookmarksFragment fragment = new MyBookmarksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        fragment.setArguments(bundle);
        return fragment;
    }

    @InjectView(R.id.MyBookmarksListView)
    ListView listView;
    @InjectView(R.id.MyBookmarksSwipeLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private int total = 0;
    private MyBookmarksAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_bookmarks, container, false);
        ButterKnife.inject(this, rootView);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.orange);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        adapter = new MyBookmarksAdapter(getActivity(), this);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(MyBookmarksFragment.this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestApi();
    }

    @Override
    public void onDestroyView() {
        subscriptions.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onMyBookmarkClick(View v, MyBookmark myBookmark) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(myBookmark.getLinkUrl())));
    }

    @Override
    public void onMyBookmarkLongClick(View v, MyBookmark myBookmark) {
        Intent intent = new Intent(getActivity(), EntryInfoActivity.class);
        intent.putExtras(EntryInfoActivity.Companion.buildBundle(myBookmark.getLinkUrl()));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        requestApi();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && adapter.getCount() < total && !swipeRefreshLayout.isRefreshing()) {
            requestApi(adapter.getCount());
        }
    }

    private void requestApi() {
        requestApi(0);
    }

    private void requestApi(int offset) {
        if (getView() == null) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        Account account = AccountDAO.get();
        swipeRefreshLayout.setRefreshing(true);
        subscriptions.add(MyBookmarksApi.request(account, getArguments().getString("keyword"), offset)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<JSONObject, JSONObject>() {
                    @Override
                    public JSONObject call(JSONObject jsonObject) {
                        try {
                            total = jsonObject.getJSONObject("meta").getInt("total");
                        } catch (JSONException e) {
                        }
                        return jsonObject;
                    }
                })
                .flatMap(MyBookmarksFunc.mapToMyBookmarkList())
                .subscribe(new Observer<MyBookmark>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(MyBookmark myBookmark) {
                        adapter.add(myBookmark);
                    }
                }));
    }
}