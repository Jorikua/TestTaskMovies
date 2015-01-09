package ua.vsevolodkaganovych.testtaskmovies.Api;

import android.text.Editable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
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
import ua.vsevolodkaganovych.testtaskmovies.Models.ProductionCountry;
import ua.vsevolodkaganovych.testtaskmovies.Models.Result;


public class ApiClient {

    private static MovieApiInterface sMovieApiService;

    public static String getStringSafely(JsonElement element) {
        return element == null || element == JsonNull.INSTANCE ? null : element.getAsString();
    }

    public static MovieApiInterface getMovieApiInterface() {


        class GenreDeserializer implements JsonDeserializer<MovieDetailed> {

            @Override
            public MovieDetailed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                MovieDetailed movieDetailed = new MovieDetailed();
                movieDetailed.title = getStringSafely(jsonObject.get("title"));
                movieDetailed.posterPath = getStringSafely(jsonObject.get("poster_path"));
                JsonArray genresArray = jsonObject.getAsJsonArray("genres");
                for (int i = 0; i < genresArray.size(); i++) {
                    Genre genre = new Genre();
                    JsonElement element = genresArray.get(i);
                    genre.name = getStringSafely(element.getAsJsonObject().get("name"));
                    Log.d("TAG", genre.name);
                    movieDetailed.genres.add(genre);
                }
                JsonArray countriesArray = jsonObject.getAsJsonArray("production_countries");
                for (int i = 0; i < countriesArray.size(); i++) {
                    ProductionCountry country = new ProductionCountry();
                    JsonElement element = countriesArray.get(i);
                    country.name = getStringSafely(element.getAsJsonObject().get("name"));
                    movieDetailed.productionCountries.add(country);
                }
                movieDetailed.tagline = getStringSafely(jsonObject.get("tagline"));
                movieDetailed.releaseDate = getStringSafely(jsonObject.get("release_date"));
                movieDetailed.overview = getStringSafely(jsonObject.get("overview"));
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
