
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Lookout implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4396491217213411371L;
    @SerializedName("IdLookout")
    public int mIdLookout;
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

    public Lookout(int IdLookout, String Name, int IdAddress_, int IdUser_, int IdImageCover_,
            String LastUpdate) {
        super();
        mIdLookout = IdLookout;
        mName = Name;
        mIdAddress_ = IdAddress_;
        mIdImageCover_ = IdImageCover_;
        mLastUpdate = LastUpdate;
    }

    public Lookout() {
        // TODO Auto-generated constructor stub
    }
}
