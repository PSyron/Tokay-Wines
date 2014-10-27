
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class News implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2308726436283474494L;

    public String image; // W CELACH TESTOWYCH NIECH NA RAZIE ZOSTANIE
    public String vast; // W CELACH TESTOWYCH NIECH NA RAZIE ZOSTANIE
    @SerializedName("IdNews")
    public int mIdNews;
    @SerializedName("Header")
    public String mHeader;
    @SerializedName("EntryDate")
    public String mEntryDate;
    @SerializedName("StartDate")
    public String mStartDate;
    @SerializedName("EndDate")
    public String mEndDate;
    @SerializedName("IdDescription_")
    public int mIdDescription_;
    @SerializedName("IdAddress_")
    public int mIdAddress_;
    @SerializedName("IdImageCover_")
    public int mIdImageCover_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public News(int IdNews, String Header, String EntryDate, String StartDate, String EndDate,
            int IdAddress_, int IdImageCover_, String LastUpdate) {
        super();
        mIdNews = IdNews;
        mHeader = Header;
        mEntryDate = EntryDate;
        mStartDate = StartDate;
        mEndDate = EndDate;
        mIdAddress_ = IdAddress_;
        mIdImageCover_ = IdImageCover_;
        mLastUpdate = LastUpdate;
    }

}
