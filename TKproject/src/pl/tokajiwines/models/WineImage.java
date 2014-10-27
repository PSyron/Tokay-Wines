
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WineImage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1995336021910532387L;
    @SerializedName("IdWineImage")
    public int mIdWineImage;
    @SerializedName("IdWine_")
    public int mIdWine_;
    @SerializedName("IdImage_")
    public int mIdImage_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public WineImage(int IdWineImage, int IdWine_, int IdImage_, String LastUpdate) {
        super();
        mIdWineImage = IdWineImage;
        mIdWine_ = IdWine_;
        mIdImage_ = IdImage_;
        mLastUpdate = LastUpdate;
    }
}
