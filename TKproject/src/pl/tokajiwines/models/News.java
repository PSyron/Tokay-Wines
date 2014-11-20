
package pl.tokajiwines.models;

import java.io.Serializable;

public class News implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2308726436283474494L;

    public String image; // W CELACH TESTOWYCH NIECH NA RAZIE ZOSTANIE
    public String vast; // W CELACH TESTOWYCH NIECH NA RAZIE ZOSTANIE
    public int mIdNews;
    public String mHeaderPl;
    public String mHeaderEng;
    public String mEntryDate;
    public String mStartDate;
    public String mEndDate;
    public int mIdDescription_;
    public int mIdAddress_;
    public int mIdImageCover_;
    public String mLastUpdate;

    public News(int IdNews, String HeaderPl, String HeaderEng, String EntryDate, String StartDate,
            String EndDate, int IdAddress_, int IdImageCover_, String LastUpdate) {
        super();
        mIdNews = IdNews;
        mHeaderPl = HeaderPl;
        mHeaderEng = HeaderEng;
        mEntryDate = EntryDate;
        mStartDate = StartDate;
        mEndDate = EndDate;
        mIdAddress_ = IdAddress_;
        mIdImageCover_ = IdImageCover_;
        mLastUpdate = LastUpdate;
    }

    public News(int IdNews, String Header, String HeaderEng, String EntryDate, int IdAddress_,
            int IdImageCover_, String LastUpdate) {
        super();
        mIdNews = IdNews;
        mHeaderPl = Header;
        mHeaderEng = HeaderEng;
        mEntryDate = EntryDate;
        mIdAddress_ = IdAddress_;
        mIdImageCover_ = IdImageCover_;
        mLastUpdate = LastUpdate;
    }

    public News() {
        // TODO Auto-generated constructor stub
    }

}
