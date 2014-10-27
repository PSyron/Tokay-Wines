
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Lang implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3413154513125285867L;
    @SerializedName("IdLang")
    public int mIdLang;
    @SerializedName("Name")
    public String mName;
    @SerializedName("ShortName")
    public String mShortName;

    public Lang(int IdLang, String Name, String ShortName) {
        super();
        mIdLang = IdLang;
        mName = Name;
        mShortName = ShortName;
    }
}
