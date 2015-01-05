package ua.vsevolodkaganovych.testtaskmovies;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Movie {


    @SerializedName("page")
    public Integer page;
    @SerializedName("results")
    public ArrayList<Result> results = new ArrayList<Result>();
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("total_results")
    public Integer totalResults;
}
