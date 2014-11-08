
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Strain implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2076907745855401720L;

    @SerializedName("IdStrain")
    public int mIdStrain;
    @SerializedName("Name")
    public String mName;

    public Strain(int IdStrain, String Name) {
        super();
        mIdStrain = IdStrain;
        mName = Name;
    }

    public Strain() {
        // TODO Auto-generated constructor stub
    }

}
