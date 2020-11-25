package Model;

import java.util.Objects;

public class Couple {
    private final String faction;
    private final int value;


    public Couple(String faction, int value) {
		this.faction = Objects.requireNonNull(faction);
        this.value = value;
    }
    public Couple(){
    	this.faction = "";
		this.value = 0;
    }

    
    public String getFaction() {
        return faction;
    }

    public int getValue() {
        return value;
    }
    
    public String toString(){
        if(faction.equals("")) {
        	return "";
        }
        else {
        	return faction+", "+value;
        }
    }
    
    /**
     *Retourne le format du couple permettant la sauvegarde au format texte 
     *
     */
    public String toSave(){
        return faction+"@"+value;
    }
}
