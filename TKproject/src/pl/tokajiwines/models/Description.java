
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Description implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4258322462227586985L;
    @SerializedName("IdDescription")
    public int mIdDescription;
    @SerializedName("Version")
    public int mVersion;
    @SerializedName("Short")
    public String mShort;
    @SerializedName("Vast")
    public String mVast;
    @SerializedName("IdUser_")
    public int mIdUser_;
    @SerializedName("IdLang_")
    public int mIdLang_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public Description(int IdDescription, int Version, String Short, String Vast, int IdLang_,
            int IdUser_, String LastUpdate) {
        super();
        mIdDescription = IdDescription;
        mVersion = Version;
        mShort = Short;
        mVast = Vast;
        mIdLang_ = IdLang_;
        mIdUser_ = IdUser_;
        mLastUpdate = LastUpdate;
    }

    public Description() {
        // TODO Auto-generated constructor stub
    }
}
