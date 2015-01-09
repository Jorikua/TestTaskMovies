package ua.vsevolodkaganovych.testtaskmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailed {

    private boolean adult;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("belongs_to_collection")
    public Object belongsToCollection;
    public int budget;
    public ArrayList<Genre> genres = new ArrayList<>();
    public String homepage;
    public int id;
    @SerializedName("imdb_id")
    public String imdbId;
    @SerializedName("original_language")
    public String originalLanguage;
    @SerializedName("original_title")
    public String originalTitle;
    public String overview;
    public double popularity;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("production_companies")
    public List<ProductionCompany> productionCompanies = new ArrayList<>();
    @SerializedName("production_countries")
    public List<ProductionCountry> productionCountries = new ArrayList<>();
    @SerializedName("release_date")
    public String releaseDate;
    public int revenue;
    public int runtime;
    @SerializedName("spoken_languages")
    public List<SpokenLanguage> spokenLanguages = new ArrayList<>();
    public String status;
    public String tagline;
    public String title;
    public boolean video;
    @SerializedName("vote_average")
    public double voteAverage;
    @SerializedName("vote_count")
    public int voteCount;
}
