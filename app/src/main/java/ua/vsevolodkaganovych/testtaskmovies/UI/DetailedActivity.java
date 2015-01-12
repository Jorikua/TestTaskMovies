package ua.vsevolodkaganovych.testtaskmovies.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ua.vsevolodkaganovych.testtaskmovies.Api.ApiClient;
import ua.vsevolodkaganovych.testtaskmovies.Models.Genre;
import ua.vsevolodkaganovych.testtaskmovies.Models.MovieDetailed;
import ua.vsevolodkaganovych.testtaskmovies.Models.ProductionCountry;
import ua.vsevolodkaganovych.testtaskmovies.R;


public class DetailedActivity extends Activity {

    private static final String TAG = DetailedActivity.class.getSimpleName();

    private String mDefaultPoster = "https://d3a8mw37cqal2z.cloudfront.net/assets/f996aa2014d2ffddfda8463c479898a3/images/no-poster-w185.jpg";
    private TextView mTitle;
    private TextView mGenres;
    private TextView mCountry;
    private ImageView mPoster;
    private TextView mTagline;
    private TextView mDate;
    private TextView mOverView;
    private int mId;

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
        mId = intent.getIntExtra("id", 0);

        ApiClient.getMovieApiInterface().getDetailed(mId, new Callback<MovieDetailed>() {
            @Override
            public void success(MovieDetailed movieDetailed, Response response) {
                mTitle.setText(movieDetailed.title);
                if (movieDetailed.posterPath == null) {
                    Picasso.with(getApplicationContext()).load(mDefaultPoster).resize(500, 800)
                            .placeholder(R.drawable.posterholderlarge).into(mPoster);
                } else {
                    Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w500" + movieDetailed.posterPath)
                            .placeholder(R.drawable.posterholderlarge).into(mPoster);
                }
                StringBuilder genresBuilder = new StringBuilder();
                for (Genre genre : movieDetailed.genres) {
                    genresBuilder.append(genre.name);
                    genresBuilder.append(", ");
                }
                if (genresBuilder.length() == 0) {
                    mGenres.setText("Genres: No data");
                } else {
                    String genString = genresBuilder.substring(0, genresBuilder.length() - 2);
                    mGenres.setText(genString);
                }

                StringBuilder countriesBuilder = new StringBuilder();
                for (ProductionCountry country : movieDetailed.productionCountries) {
                    countriesBuilder.append(country.name);
                    countriesBuilder.append(", ");
                }
                if (countriesBuilder.length() == 0) {
                    mCountry.setText("Production countries: No data");
                }else {
                    String countryString = countriesBuilder.substring(0, countriesBuilder.length() - 2);
                    mCountry.setText(countryString);
                }
                mTagline.setText(movieDetailed.tagline);
                mDate.setText(movieDetailed.releaseDate);
                mOverView.setText(movieDetailed.overview);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "error " + error);
            }
        });
    }
}
