package pl.tokajiwines.jsonresponses;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class NewsDetails implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 3971127494907748706L;
    @SerializedName("idNews")
    public int mIdNews;
    @SerializedName("header")
    public String mHeader;
    @SerializedName("vast")
    public String mVast;
    @SerializedName("image")
    public String mImage;
    @SerializedName("entryDate")
    public String mEntryDate;
    @SerializedName("startDate")
    public String mStartDate;
    @SerializedName("endDate")
    public String mEndDate;

}
