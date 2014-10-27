
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4434207464135516838L;
    @SerializedName("IdImage")
    public int mIdImage;
    @SerializedName("Version")
    public int mVersion;
    @SerializedName("Height")
    public int mHeight;
    @SerializedName("Width")
    public int mWidth;
    @SerializedName("Weight")
    public int mWeight;
    @SerializedName("Author")
    public String mAuthor;
    @SerializedName("IdUser_")
    public int mIdUser_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public Image(int IdImage, int Version, int Height, int Width, int Weight, String Author,
            int IdUser_, String LastUpdate) {
        super();
        mIdImage = IdImage;
        mVersion = Version;
        mHeight = Height;
        mWidth = Width;
        mWeight = Weight;
        mAuthor = Author;
        mIdUser_ = IdUser_;
        mLastUpdate = LastUpdate;
    }
}
