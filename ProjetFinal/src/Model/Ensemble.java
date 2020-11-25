package Model;

import java.util.Objects;

public class Ensemble {
	private final String capacity;
    private final String faction;
    private boolean used;


    public Ensemble(String capacity, String faction) {
    	this.capacity = Objects.requireNonNull(capacity);
		this.faction = Objects.requireNonNull(faction);
        this.used = false;
    }
    public Ensemble(){
    	this.capacity = "";
    	this.faction = "";
		this.used = false;
    }
    
    public Ensemble(String faction,String capacity,Boolean used) {
        this.capacity = Objects.requireNonNull(capacity);
        this.faction = Objects.requireNonNull(faction);
        this.used = Objects.requireNonNull(used);
    }
    
    public String toString(){
    	if(faction.equals("")) {
        	return "";
        }
        else {
        	return "("+faction+": "+capacity+")";
        }
    }

    public String getCapacity() {
    	return capacity;
    }
    
    public String getFaction() {
        return faction;
    }

    public boolean getUsed() {
        return used;
    }
    
    public void setUsed(boolean b) {
        used = b;
    }
    
    /**
     * methode permettant d'avoir le format correspondant a un ensemble permettant l'enregistrement en format texte
     *
     */
    public String toSave(){
        return faction+"@"+capacity+"@"+used;
    }
    
}
