
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Curiosity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2835563831165747338L;
    @SerializedName("IdCuriosity")
    public int mIdCuriosity;
    @SerializedName("Name")
    public String mName;
    @SerializedName("IdCuriosityType_")
    public int mIdCuriosityType_;
    @SerializedName("IdDescription_")
    public int mIdDescription_;
    @SerializedName("IdAddress")
    public int mIdAddress_;
    @SerializedName("IdImageCover_")
    public int mIdImageCover_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public Curiosity(int IdCuriosity, String Name, int IdCuriosityType_, int IdDescription_,
            int IdAddress_, int IdImageCover_, String LastUpdate) {
        super();
        mIdCuriosity = IdCuriosity;
        mName = Name;
        mIdCuriosityType_ = IdCuriosityType_;
        mIdDescription_ = IdDescription_;
        mIdAddress_ = IdAddress_;
        mIdImageCover_ = IdImageCover_;
        mLastUpdate = LastUpdate;
    }

    public Curiosity() {
        // TODO Auto-generated constructor stub
    }

}
