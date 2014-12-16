package ua.vsevolodkaganovych.testtaskmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class FragmentSearch extends ListFragment {

    private JSONObject jsonObject;
    private Button mButton;
    private EditText mMovie;
    private EditText mYear;
    private ProgressBar mProgressBar;
    private CustomListAdapter mAdapter;
    private ArrayList<Item> mItems;
    private int mPageNumber = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search_view, container, false);

        mButton = (Button) rootView.findViewById(R.id.button);
        mMovie = (EditText) rootView.findViewById(R.id.movie);
        mYear = (EditText) rootView.findViewById(R.id.year);
        mProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);


        mItems = new ArrayList<>();
        mAdapter = new CustomListAdapter(getActivity(), mItems);
        setListAdapter(mAdapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Task().execute();
            }
        });

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        JSONArray jsonArray = jsonObject.optJSONArray("results");
        JSONObject jsonId = jsonArray.optJSONObject(position);
        String movieId = jsonId.optString("id");

        Intent intent = new Intent(getActivity(), DetailedActivity.class);
        intent.putExtra("id", movieId);
        startActivity(intent);
    }

    private class Task extends AsyncTask<Void, Void, ArrayList<Item>> {

        @Override
        protected ArrayList<Item> doInBackground(Void... params) {
            Request request = null;
            try {
                request = new Request.Builder()
                        .url("http://api.themoviedb.org/3/search/movie?api_key=47e00b224e4f05989fa8b2a8d4ed7437&page=1"
                                + "&query=" + URLEncoder.encode(String.valueOf(mMovie.getText()), "UTF-8")
                                + "&year=" + mYear.getText().toString())
                        .build();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                Response response = HttpApp.getOkHttpClient().newCall(request).execute();
                jsonObject = new JSONObject(response.body().string());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            ArrayList<Item> items = new ArrayList<>();
            JSONArray array = jsonObject.optJSONArray("results");
            if (jsonObject.has("results") && jsonObject.optJSONArray("results") != null
                    && jsonObject.optJSONArray("results").length() != 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.optJSONObject(i);
                    Item item = new Item();
                    item.title = object.optString("title");
                    item.poster_path = object.optString("poster_path");
                    item.release_date = object.optString("release_date");
                    item.vote_average = object.optString("vote_average");
                    items.add(item);
                }
            }

            return items;

        }

        @Override
        protected void onPostExecute(ArrayList<Item> result) {

            getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    int count = getListView().getCount();
                    if (scrollState == SCROLL_STATE_IDLE) {
                        if (getListView().getLastVisiblePosition() >= count - 2) {
                            new LoadMoreData().execute();
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });

            if (mItems != null) {
                mItems.clear();
                mItems.addAll(result);
            }
            mAdapter.notifyDataSetChanged();

        }
    }

    private class LoadMoreData extends AsyncTask<Void, Void, ArrayList<Item>> {


        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Item> doInBackground(Void... params) {
            Request request = new Request.Builder()
                    .url("http://api.themoviedb.org/3/search/movie?api_key=47e00b224e4f05989fa8b2a8d4ed7437&page="
                            + (mPageNumber += 1) + "&query=" + mMovie.getText() + "&year=" + mYear.getText().toString())
                    .build();
            try {
                Response response = HttpApp.getOkHttpClient().newCall(request).execute();
                jsonObject = new JSONObject(response.body().string());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            ArrayList<Item> items = new ArrayList<>();
            JSONArray array = jsonObject.optJSONArray("results");
            if (jsonObject.has("results") && jsonObject.optJSONArray("results") != null
                    && jsonObject.optJSONArray("results").length() != 0) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.optJSONObject(i);
                    Item item = new Item();
                    item.title = object.optString("title");
                    item.poster_path = object.optString("poster_path");
                    item.release_date = object.optString("release_date");
                    item.vote_average = object.optString("vote_average");
                    items.add(item);
                }
            }

            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> result) {
            int position = getListView().getLastVisiblePosition();
            getListView().setSelectionFromTop(position, 0);
            mProgressBar.setVisibility(View.INVISIBLE);
            mItems.addAll(result);
            mAdapter.notifyDataSetChanged();
        }
    }
}



















