package ua.vsevolodkaganovych.testtaskmovies.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import ua.vsevolodkaganovych.testtaskmovies.UI.DetailedActivity;
import ua.vsevolodkaganovych.testtaskmovies.UI.EndlessScrollListener;


public class SearchActivity extends ListActivity {

    private ArrayList<Movie> mItems;
    private CustomListAdapter mAdapter;
    private int mPageNumber;
    private EditText mSearch;
    private EditText mYear;
    private Button mButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearch = (EditText) findViewById(R.id.search_text);
        mYear = (EditText) findViewById(R.id.search_year);
        mButton = (Button) findViewById(R.id.search_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mItems = new ArrayList<>();
        mAdapter = new CustomListAdapter(this, mItems);
        setListAdapter(mAdapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.getMovieApiInterface().getSearch(mPageNumber = 1, mSearch.getText(), mYear.getText(), new Callback<Result>() {
                    @Override
                    public void success(Result result, Response response) {
                        if (mItems != null) {
                            mItems.clear();
                            mItems.addAll(result.movies);
                        }
                        mAdapter.notifyDataSetChanged();
                        getListView().setOnScrollListener(new EndlessScrollListener() {
                            @Override
                            public void onLoadMore(int page, int totalItemsCount) {
                                mProgressBar.setVisibility(View.VISIBLE);
                                ApiClient.getMovieApiInterface().getSearch(mPageNumber += 1, mSearch.getText(), mYear.getText(), new Callback<Result>() {
                                    @Override
                                    public void success(Result result1, Response response) {
                                        mItems.addAll(result1.movies);
                                        mAdapter.notifyDataSetChanged();
                                        mProgressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        error.printStackTrace();
                                    }
                                });
                            }
                        });
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                    }
                });
            }
        });
    }


    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        Movie item = mAdapter.getItem(position);
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra("id", item.id);
        Log.d("LOG", String.valueOf(item.id));
        startActivity(intent);
    }
}
