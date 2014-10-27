
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

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

    public ProducerListItem(int id, String name, String description) {
        mIdProducer = id;
        mName = name;
        mDescription = description;
    }

}
