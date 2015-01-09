package ua.vsevolodkaganovych.testtaskmovies.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ua.vsevolodkaganovych.testtaskmovies.Adapters.CustomListAdapter;
import ua.vsevolodkaganovych.testtaskmovies.Api.ApiClient;
import ua.vsevolodkaganovych.testtaskmovies.Models.Movie;
import ua.vsevolodkaganovych.testtaskmovies.Models.Result;
import ua.vsevolodkaganovych.testtaskmovies.R;


public class FragmentTop extends ListFragment {

    private ProgressBar mProgressBar;
    private CustomListAdapter mAdapter;
    private ArrayList<Movie> mItems;
    private int mPageNumber = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movies_view, container, false);

        setHasOptionsMenu(true);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        mItems = new ArrayList<>();
        mAdapter = new CustomListAdapter(getActivity(), mItems);
        setListAdapter(mAdapter);
        mProgressBar.setVisibility(View.INVISIBLE);

        ApiClient.getMovieApiInterface().getTop(mPageNumber, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                mItems.addAll(result.movies);
                mAdapter.notifyDataSetChanged();
                getListView().setOnScrollListener(new EndlessScrollListener() {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        ApiClient.getMovieApiInterface().getTop(mPageNumber += 1, new Callback<Result>() {
                            @Override
                            public void success(Result result1, Response response) {
                                mItems.addAll(result1.movies);
                                mAdapter.notifyDataSetChanged();
                                mProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                error.getCause().printStackTrace();
                            }
                        });
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                error.getCause().printStackTrace();
            }
        });

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        Movie item = mAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), DetailedActivity.class);
        intent.putExtra("id", item.id);
        Log.d("LOG", String.valueOf(item.id));
        startActivity(intent);
    }
}
