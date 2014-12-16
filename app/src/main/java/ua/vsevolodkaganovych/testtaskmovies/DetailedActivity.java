package ua.vsevolodkaganovych.testtaskmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class DetailedActivity extends Activity {

    private TextView mTitle;
    private TextView mGenres;
    private TextView mCountry;
    private ImageView mPoster;
    private TextView mTagline;
    private TextView mDate;
    private TextView mOverView;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        mTitle = (TextView) findViewById(R.id.title);
        mGenres = (TextView) findViewById(R.id.genres);
        mCountry = (TextView) findViewById(R.id.country);
        mPoster = (ImageView) findViewById(R.id.poster);
        mTagline = (TextView) findViewById(R.id.tagline);
        mDate = (TextView) findViewById(R.id.date);
        mOverView = (TextView) findViewById(R.id.overview);

        Intent intent = getIntent();
        mId = intent.getStringExtra("id");

        new Task().execute();

    }

    private class Task extends AsyncTask<Void, Void, JSONObject> {

        JSONObject jsonObject = null;

        @Override
        protected JSONObject doInBackground(Void... params) {
            Request request = new Request.Builder()
                    .url("http://api.themoviedb.org/3/movie/" + mId + "?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
                    .build();
            try {
                Response response = HttpApp.getOkHttpClient().newCall(request).execute();
                jsonObject = new JSONObject(response.body().string());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            String title = result.optString("title");
            mTitle.setText(title);
            String poster = result.optString("poster_path");
            if (poster.equals("null")) {
                Picasso.with(getApplicationContext()).load("https://d3a8mw37cqal2z.cloudfront.net/assets/f996aa2014d2ffddfda8463c479898a3/images/no-poster-w185.jpg").resize(500, 800).into(mPoster);
            }else {
                Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w500" + poster).into(mPoster);
            }
            JSONArray array = result.optJSONArray("genres");
            if (result.has("genres") && result.optJSONArray("genres") != null && result.optJSONArray("genres").length() != 0) {
                JSONObject object = array.optJSONObject(0);
                String genre = object.optString("name");
                mGenres.setText(genre);
            }
            JSONArray array1 = result.optJSONArray("production_countries");
            if (result.has("production_countries") && result.optJSONArray("production_countries") != null && result.optJSONArray("production_countries").length() != 0) {
                JSONObject object1 = array1.optJSONObject(0);
                String country = object1.optString("name");
                mCountry.setText(country);
            }
            String tagline = result.optString("tagline");
            if (tagline.equals("null")) {
                mTagline.setText("");
            } else {
                mTagline.setText(tagline);
            }
            String date = result.optString("release_date");
            mDate.setText(date);
            String overview = result.optString("overview");
            if (overview.equals("null")) {
                mOverView.setText("");
            } else {
                mOverView.setText(overview);
            }
        }
    }

}
