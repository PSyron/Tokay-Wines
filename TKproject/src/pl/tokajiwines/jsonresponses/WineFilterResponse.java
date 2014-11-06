package pl.tokajiwines.jsonresponses;

import pl.tokajiwines.models.Flavour;
import pl.tokajiwines.models.Grade;
import pl.tokajiwines.models.Producer;
import pl.tokajiwines.models.Strain;

public class WineFilterResponse {
    
    public int success;
    public String message;
    public String[] years;
    public Flavour[] flavours;
    public Strain[] strains;
    public Grade[] grades;
    public ProducerListItem[] producers;

}
