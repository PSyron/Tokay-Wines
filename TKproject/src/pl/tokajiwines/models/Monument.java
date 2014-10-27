
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Monument implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5336768746291707454L;
    @SerializedName("IdMonument")
    public int mIdMonument;
    @SerializedName("Name")
    public String mName;
    @SerializedName("IdDescription_")
    public int mIdDescription_;
    @SerializedName("IdAddress_")
    public int mIdAddress_;
    @SerializedName("IdImageCover_")
    public int mIdImageCover_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public Monument(int IdMonument, String Name, int IdAddress_, int IdUser_, int IdImageCover_,
            String LastUpdate) {
        super();
        mIdMonument = IdMonument;
        mName = Name;
        mIdAddress_ = IdAddress_;
        mIdImageCover_ = IdImageCover_;
        mLastUpdate = LastUpdate;
    }
}
