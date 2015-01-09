package ua.vsevolodkaganovych.testtaskmovies.Models;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ua.vsevolodkaganovych.testtaskmovies.Models.Movie;

public class Result {


    @SerializedName("page")
    public Integer page;
    @SerializedName("results")
    public ArrayList<Movie> movies = new ArrayList<Movie>();
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("total_results")
    public Integer totalResults;
}
