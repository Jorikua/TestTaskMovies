package ua.vsevolodkaganovych.testtaskmovies;

import android.text.Editable;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;


public class ApiClient {


    private static MovieApiInterface sMovieApiService;

    public static MovieApiInterface getMovieApiInterface() {
        if (sMovieApiService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.themoviedb.org/3")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            sMovieApiService = restAdapter.create(MovieApiInterface.class);
        }
        return sMovieApiService;
    }
    public interface MovieApiInterface {
        @GET("/movie/top_rated?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
        void getTop(@Query("page") int page, Callback<Movie> callback);

        @GET("/movie/popular?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
        void getPopular(@Query("page") int page, Callback<Movie> callback);

        @GET("/movie/upcoming?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
        void getUpcoming(@Query("page") int page, Callback<Movie> callback);

        @GET("/search/movie?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
        void getSearch(@Query("page") int page, @Query("query") Editable query, @Query("year") Editable year, Callback<Movie> callback);
    }
}
