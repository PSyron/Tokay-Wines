package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

public class NewsListItem {
    
    @SerializedName("idNews")
    public int mIdNews;
    @SerializedName("header")
    public String mHeader;
    @SerializedName("short")
    public String mDescription;
    @SerializedName("Image")
    public String mImageUrl;
    
    public NewsListItem(int id, String header, String description)
    {
        mIdNews = id;
        mHeader = header;
        mDescription = description;
    }

}
