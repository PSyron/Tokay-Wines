
package pl.tokajiwines.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CuriosityType implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7153072856653782324L;
    @SerializedName("IdCuriosityType")
    public int mIdCuriosityType;
    @SerializedName("Name")
    public String mName;

    public CuriosityType(int IdCuriosityType, String Name) {
        super();
        mIdCuriosityType = IdCuriosityType;
        mName = Name;
    }

    public CuriosityType() {
        // TODO Auto-generated constructor stub
    }
}
