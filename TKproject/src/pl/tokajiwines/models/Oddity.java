
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Oddity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2835563831165747338L;
    @SerializedName("IdOddity")
    public int mIdOddity;
    @SerializedName("Name")
    public String mName;
    @SerializedName("Header")
    public String mHeader;
    @SerializedName("IdOddityType_")
    public int mIdOddityType_;
    @SerializedName("IdDescription_")
    public int mIdDescription_;
    @SerializedName("IdAddress")
    public int mIdAddress_;
    @SerializedName("IdImageCover_")
    public int mIdImageCover_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public Oddity(int IdOddity, String Name, String Header, int IdOddityType_, int IdDescription_,
            int IdAddress_, int IdImageCover_, String LastUpdate) {
        super();
        mIdOddity = IdOddity;
        mName = Name;
        mHeader = Header;
        mIdOddityType_ = IdOddityType_;
        mIdDescription_ = IdDescription_;
        mIdAddress_ = IdAddress_;
        mIdImageCover_ = IdImageCover_;
        mLastUpdate = LastUpdate;
    }

    public Oddity() {
        // TODO Auto-generated constructor stub
    }

}
