
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

import pl.tokajiwines.models.News;

import java.io.Serializable;

public class NewsDetails implements Serializable {

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

    public NewsDetails(News n) {
        mIdNews = n.mIdNews;
        mHeader = n.mHeaderEng;
        mVast = n.description.mVast;
        mImage = n.imageCover.mImage;
        mEndDate = n.mEndDate;
        mStartDate = n.mStartDate;
        mEntryDate = n.mEntryDate;

    }
}
