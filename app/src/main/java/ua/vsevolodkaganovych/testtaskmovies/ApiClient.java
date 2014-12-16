package ua.vsevolodkaganovych.testtaskmovies;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;


public class ApiClient extends Application {

    private static MovieApiInterface sMovieApiInterface;


    static class MyDeserializer implements JsonDeserializer<Item> {

        @Override
        public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            JsonElement resultEl = object.getAsJsonObject().get("result");
            JsonArray resultArray = resultEl.getAsJsonArray();

            return new Gson().fromJson(resultArray, Item.class);
        }
    }

    public static MovieApiInterface getMovieApiClient() {



        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new MyDeserializer())
                .create();

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("api_key", "47e00b224e4f05989fa8b2a8d4ed743");
            }
        };

        if(sMovieApiInterface == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.themoviedb.org/3")
//                    .setConverter(new GsonConverter(gson))
                    .build();

            sMovieApiInterface = restAdapter.create(MovieApiInterface.class);
        }

        return sMovieApiInterface;
    }

    public interface MovieApiInterface {
        @GET("/movie/top_rated?api_key=47e00b224e4f05989fa8b2a8d4ed7437")
        void getTopMovies(@Query("page") int page, Callback<ArrayList<Item>> callback);
    }

}
