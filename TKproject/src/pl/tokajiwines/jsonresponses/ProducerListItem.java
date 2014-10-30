
package pl.tokajiwines.jsonresponses;

import com.google.gson.annotations.SerializedName;

import pl.tokajiwines.models.Producer;

import java.io.Serializable;

public class ProducerListItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2285843843638473736L;
    @SerializedName("idProducer")
    public int mIdProducer;
    @SerializedName("name")
    public String mName;
    @SerializedName("short")
    public String mDescription;
    @SerializedName("image")
    public String mImageUrl;

    public ProducerListItem(int id, String name, String description) {
        mIdProducer = id;
        mName = name;
        mDescription = description;
    }

    public ProducerListItem(Producer p) {
        mIdProducer = p.mIdProducer;
        mName = p.mName;
        mDescription = Integer.toString(p.mIdDescription_);
    }

}
