package ua.vsevolodkaganovych.testtaskmovies.Api;

import android.text.Editable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import ua.vsevolodkaganovych.testtaskmovies.Models.Genre;
import ua.vsevolodkaganovych.testtaskmovies.Models.MovieDetailed;
import ua.vsevolodkaganovych.testtaskmovies.Models.Result;


public class ApiClient {

    private static MovieApiInterface sMovieApiService;


    public static MovieApiInterface getMovieApiInterface() {


        class GenreDeserializer implements JsonDeserializer<MovieDetailed> {

            @Override
            public MovieDetailed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                MovieDetailed movieDetailed = new MovieDetailed();
                movieDetailed.title = jsonObject.get("title").getAsString();
                movieDetailed.posterPath = jsonObject.get("poster_path").getAsString();
                JsonArray genresArray = jsonObject.getAsJsonArray("genres");
//                movieDetailed.genres = new Gson().fromJson(genresArray, new TypeToken<ArrayList<Genre>>(){}.getType());
                for (int i = 0; i < genresArray.size(); i++) {
                    Genre genre = new Genre();
                    JsonElement element = genresArray.get(i);
                    genre.name = element.getAsJsonObject().get("name").getAsString();
                    movieDetailed.genres.add(genre);
                }
                return movieDetailed;
            }
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MovieDetailed.class, new GenreDeserializer())
                .create();

        if (sMovieApiService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.themoviedb.org/3")
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            sMovieApiService = restAdapter.create(MovieApiInterface.class);
        }
        return sMovieApiService;
    }

    public interface MovieApiInterface {
        @GET("/movie/top_rated?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
        void getTop(@Query("page") int page, Callback<Result> callback);

        @GET("/movie/popular?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
        void getPopular(@Query("page") int page, Callback<Result> callback);

        @GET("/movie/upcoming?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
        void getUpcoming(@Query("page") int page, Callback<Result> callback);

        @GET("/search/movie?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
        void getSearch(@Query("page") int page, @Query("query") Editable query, @Query("year") Editable year, Callback<Result> callback);

        @GET("/movie/{id}?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
        void getDetailed(@Path("id") int id, Callback<MovieDetailed> callback);
    }
}
