package ua.vsevolodkaganovych.testtaskmovies;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FragmentTop extends ListFragment {

    private ProgressBar mProgressBar;
    private CustomListAdapter mAdapter;
    private ArrayList<Result> mItems;
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

        ApiClient.getMovieApiInterface().getTop(mPageNumber, new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {
                mItems.addAll(movie.results);
                mAdapter.notifyDataSetChanged();
                getListView().setOnScrollListener(new EndlessScrollListener() {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        ApiClient.getMovieApiInterface().getTop(mPageNumber += 1, new Callback<Movie>() {
                            @Override
                            public void success(Movie movie, Response response) {
                                mItems.addAll(movie.results);
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
        ApiClient.getMovieApiInterface().getTop(mPageNumber, new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
