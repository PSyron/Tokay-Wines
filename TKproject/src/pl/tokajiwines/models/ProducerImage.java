
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProducerImage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7295741394863949903L;
    @SerializedName("IdProducerImage")
    public int mIdProducerImage;
    @SerializedName("IdProducer_")
    public int mIdProducer_;
    @SerializedName("IdImage_")
    public int mIdImage_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public ProducerImage(int IdProducerImage, int IdProducer_, int IdImage_, String LastUpdate) {
        super();
        mIdProducerImage = IdProducerImage;
        mIdProducer_ = IdProducer_;
        mIdImage_ = IdImage_;
        mLastUpdate = LastUpdate;
    }

    public ProducerImage() {
        // TODO Auto-generated constructor stub
    }
}
