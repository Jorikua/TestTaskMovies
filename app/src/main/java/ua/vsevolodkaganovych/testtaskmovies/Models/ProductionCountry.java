package ua.vsevolodkaganovych.testtaskmovies.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductionCountry {

    @SerializedName("iso_3166_1")
    @Expose
    public String iso31661;
    @Expose
    public String name;
}
