
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

import pl.tokajiwines.models.News;

public class NewsListItem {

    @SerializedName("idNews")
    public int mIdNews;
    @SerializedName("header")
    public String mHeader;
    @SerializedName("short")
    public String mDescription;
    @SerializedName("image")
    public String mImageUrl;

    public NewsListItem(int id, String header, String description) {
        mIdNews = id;
        mHeader = header;
        mDescription = description;
    }

    public NewsListItem(News n) {
        mIdNews = n.mIdNews;
        mHeader = n.mHeaderEng;
        mDescription = n.description.mShort;
        mImageUrl = n.imageCover.mImage;

    }

}
