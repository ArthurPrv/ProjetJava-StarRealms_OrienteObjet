package Model;

public interface Card {
	
	boolean isShip();
    
    boolean isTrashShip();
    
    boolean isBase();
    
    boolean isHeroe();
    
    boolean isTrash();
    
    int getPrice();
    
    boolean isPriority();
    
    String toSave();
}
