
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

import pl.tokajiwines.models.Wine;

import java.io.Serializable;

public class WineDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6246309935269188015L;

    @SerializedName("success")
    public String mSuccess;
    @SerializedName("message")
    public String mMessage;
    @SerializedName("image")
    public String mImageUrl;
    @SerializedName("description")
    public String mDescription;
    @SerializedName("basics")
    public WineListItem mBasics;

    public WineDetails(Wine w) {
        if (w.big != null) mImageUrl = w.big.mImage;
        mDescription = w.description.mVast;
    }
}
