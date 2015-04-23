package pl.tokajiwines.jsonresponses;


import com.google.gson.annotations.SerializedName;

public class DatabaseResponse {
    public int success;

    @SerializedName("Version")
    public int Version;
    @SerializedName("Name")
    public String Name;
    @SerializedName("LastUpdate")
    public String LastUpdate;
}