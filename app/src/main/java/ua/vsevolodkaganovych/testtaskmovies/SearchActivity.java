package ua.vsevolodkaganovych.testtaskmovies;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SearchActivity extends ListActivity {

    private ArrayList<Result> mItems;
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
                ApiClient.getMovieApiInterface().getSearch(mPageNumber = 1, mSearch.getText(), mYear.getText(), new Callback<Movie>() {
                    @Override
                    public void success(Movie movie, Response response) {
                        if (mItems != null) {
                            mItems.clear();
                            mItems.addAll(movie.results);
                        }
                        mAdapter.notifyDataSetChanged();
                        getListView().setOnScrollListener(new EndlessScrollListener() {
                            @Override
                            public void onLoadMore(int page, int totalItemsCount) {
                                mProgressBar.setVisibility(View.VISIBLE);
                                ApiClient.getMovieApiInterface().getSearch(mPageNumber += 1, mSearch.getText(), mYear.getText(), new Callback<Movie>() {
                                    @Override
                                    public void success(Movie movie, Response response) {
                                        mItems.addAll(movie.results);
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
}
