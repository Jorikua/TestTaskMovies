package ua.vsevolodkaganovych.testtaskmovies.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SpokenLanguage {

    @SerializedName("iso_639_1")
    @Expose
    public String iso6391;
    @Expose
    public String name;
}
