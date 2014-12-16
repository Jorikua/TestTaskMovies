package ua.vsevolodkaganovych.testtaskmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class FragmentTop extends ListFragment {

    private JSONObject jsonObject;
    private ProgressBar mProgressBar;
    private CustomListAdapter mAdapter;
    private ArrayList<Item> mItems;
    private int mPageNumber = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_movies_view, container, false);

        mProgressBar = (ProgressBar)rootView.findViewById(R.id.progressBar2);
        mProgressBar.setVisibility(View.INVISIBLE);
        mItems = new ArrayList<>();
        mAdapter = new CustomListAdapter(getActivity(), mItems);
        setListAdapter(mAdapter);

        new Task().execute();

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
            Request request = new Request.Builder()
                    .url("http://api.themoviedb.org/3/movie/top_rated?api_key=47e00b224e4f05989fa8b2a8d4ed7437&page=" + mPageNumber)
                    .build();
            try {
                Response response = HttpApp.getOkHttpClient().newCall(request).execute();
                jsonObject = new JSONObject(response.body().string());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            ArrayList<Item> items = new ArrayList<>();
            JSONArray array = jsonObject.optJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                Item item = new Item();
                item.title = object.optString("title");
                item.poster_path = object.optString("poster_path");
                item.release_date = object.optString("release_date");
                item.vote_average = object.optString("vote_average");
                items.add(item);
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

            mItems.addAll(result);
            mAdapter.notifyDataSetChanged();
        }


        private class LoadMoreData extends AsyncTask<Void, Void, ArrayList<Item>> {

            @Override
            protected void onPreExecute() {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected ArrayList<Item> doInBackground(Void... params) {

                Request request = new Request.Builder()
                        .url("http://api.themoviedb.org/3/movie/top_rated?api_key=47e00b224e4f05989fa8b2a8d4ed7437&page="
                                + (mPageNumber += 1))
                        .build();
                try {
                    Response response = HttpApp.getOkHttpClient().newCall(request).execute();
                    jsonObject = new JSONObject(response.body().string());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<Item> items = new ArrayList<>();
                JSONArray array = jsonObject.optJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.optJSONObject(i);
                    Item item = new Item();
                    item.title = object.optString("title");
                    item.poster_path = object.optString("poster_path");
                    item.release_date = object.optString("release_date");
                    item.vote_average = object.optString("vote_average");
                    items.add(item);
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
}
