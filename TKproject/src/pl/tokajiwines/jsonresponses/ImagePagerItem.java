
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

import pl.tokajiwines.models.Image;

import java.io.Serializable;

public class ImagePagerItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3878772131018850059L;
    @SerializedName("url")
    public String ImageUrl;

    public ImagePagerItem(Image image) {
        ImageUrl = image.mImage;
    }
}
