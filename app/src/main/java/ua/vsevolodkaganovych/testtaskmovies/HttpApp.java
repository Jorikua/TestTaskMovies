package ua.vsevolodkaganovych.testtaskmovies;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;


public class HttpApp extends Application {

    private static OkHttpClient sOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        sOkHttpClient = new OkHttpClient();
    }

    public static OkHttpClient getOkHttpClient() {
        return sOkHttpClient;
    }
}
