package ua.vsevolodkaganovych.testtaskmovies;


import com.google.gson.annotations.SerializedName;

public class Result {


    @SerializedName("adult")
    public Boolean adult;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("id")
    public Integer id;
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("popularity")
    public Double popularity;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("title")
    public String title;
    @SerializedName("video")
    public Boolean video;
    @SerializedName("vote_average")
    public Double voteAverage;
    @SerializedName("vote_count")
    public Integer voteCount;
}
