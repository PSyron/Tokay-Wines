
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewsImage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7844915519846547196L;
    @SerializedName("IdNewsImage")
    public int mIdNewsImage;
    @SerializedName("IdNews_")
    public int mIdNews_;
    @SerializedName("IdImage_")
    public int mIdImage_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public NewsImage(int IdNewsImage, int IdNews_, int IdImage_, String LastUpdate) {
        super();
        mIdNewsImage = IdNewsImage;
        mIdNews_ = IdNews_;
        mIdImage_ = IdImage_;
        mLastUpdate = LastUpdate;
    }
}
