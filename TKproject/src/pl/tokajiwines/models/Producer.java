
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Producer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6518995486453290604L;
    @SerializedName("IdProducer")
    public int mIdProducer;
    @SerializedName("Email")
    public String mEmail;
    @SerializedName("Link")
    public String mLink;
    @SerializedName("Name")
    public String mName;
    @SerializedName("Phone")
    public String mPhone;
    @SerializedName("IdDescription_")
    public int mIdDescription_;
    @SerializedName("IdAddress_")
    public int mIdAddress_;
    @SerializedName("IdUser_")
    public int mIdUser_;
    @SerializedName("IdImageCover_")
    public int mIdImageCover_;
    @SerializedName("LastUpdate")
    public String mLastUpdate;

    public Producer(int IdProducer, String Email, String Link, String Name, String Phone,
            int IdDescription_, int IdAddress_, int IdUser_, int IdImageCover_, String LastUpdate) {
        super();
        mIdProducer = IdProducer;
        mEmail = Email;
        mLink = Link;
        mName = Name;
        mPhone = Phone;
        mIdDescription_ = IdDescription_;
        mIdAddress_ = IdAddress_;
        mIdUser_ = IdUser_;
        mIdImageCover_ = IdImageCover_;
        mLastUpdate = LastUpdate;
    }

    public Producer() {
        // TODO Auto-generated constructor stub
    }

}
