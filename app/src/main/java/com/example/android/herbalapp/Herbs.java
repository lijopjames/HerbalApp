package com.example.android.herbalapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LENOVO on 03-01-2018.
 */

public class Herbs {
    @SerializedName("plantid")
    public int plantid;
    @SerializedName("pname")
    public String pname;
    @SerializedName("sname")
    public String sname;
    @SerializedName("imageurl")
    public String imageurl;
}
