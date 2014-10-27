
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OddityType implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7153072856653782324L;
    @SerializedName("IdOddityType")
    public int mIdOddityType;
    @SerializedName("Name")
    public String mName;

    public OddityType(int IdOddityType, String Name) {
        super();
        mIdOddityType = IdOddityType;
        mName = Name;
    }
}
