package Model;

import java.util.ArrayList;
import java.util.Objects;

public class Heroes implements Card{
    private final String name;
    private final int price;
    private final String capacity;
    private final String scrapCapacity;


    public Heroes(String nom, int prix, String capacity, String scrapCapacity) {
        this.name = Objects.requireNonNull(nom);
        this.price = prix;
        this.capacity = Objects.requireNonNull(capacity);
        this.scrapCapacity = Objects.requireNonNull(scrapCapacity);
    }
    
    public String toString() {
    	String texte = "|"+name+(" ".repeat(18-name.length()))+"|"+
    			price+(" ".repeat(5-1))+"|"+
                capacity+(" ".repeat(165-capacity.length()))+"|"+
                scrapCapacity+(" ".repeat(122-scrapCapacity.length()))+"|";
    	
    	return texte;   
    }
    
    public int getPrice() {
    	return price;
    }
    
    public boolean isShip() {
    	return false;
    }
    
    public boolean isTrashShip() {
    	return false;
    }
    
    public boolean isBase() {
    	return false;
    }
    
    public boolean isHeroe() {
    	return true;
    }
    
    
    /**
     * Methode permettant de lancer la capacité d'un Héro grace à son nom
     *
     */
    public static boolean stringToCapacity(Heroes h, Boolean scrap, ArrayList<Player> p, ArrayList<CardPlayed> cp, ArrayList<CardPlayer> dp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ListGeneral listG, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ShopGeneral shopGeneral, ShopExplorer shopExplorer, boolean ia){
        if(h.name.equals("CEO Shaner")){
                return ceoShanerCapa(scrap,dp,shopGeneral,shopExplorer,p,cp,listG,listE,current,pp,discp,chasseAlhomme,blob,starEmpire,machineCult,tradeFederation);
        }
        else if(h.name.equals("Chairman Haygan")){
            return chairmanHaygan(p,cp,current,pp,discp,chasseAlhomme,listG,listE,blob,starEmpire,machineCult,tradeFederation,shopExplorer,dp,shopGeneral);
        }
        else if(h.name.equals("Chancellor Hartman")){
            return chancellorHartman(p,cp,current,pp,discp,chasseAlhomme,listG,listE,blob,starEmpire,machineCult,tradeFederation,shopGeneral,shopExplorer,dp);
        }
        else if(h.name.equals("Commander Klik")){
            return commanderKlik(p,cp,current,pp,discp,chasseAlhomme,listG,listE,blob,starEmpire,machineCult,tradeFederation,shopGeneral,shopExplorer,dp,ia);
        }
        else if(h.name.equals("Commodore Zhang")){
            return commodoreZhang(scrap,p,cp,current,pp,discp,chasseAlhomme,listG,listE,blob,starEmpire,machineCult,tradeFederation,shopGeneral,shopExplorer,dp,ia);
        }
        else if(h.name.equals("Confessor Morris")){
            return confessorMorris(scrap,p,cp,current,pp,discp,chasseAlhomme,listG,listE,blob,starEmpire,machineCult,tradeFederation,shopGeneral,shopExplorer,dp);
        }
        else if(h.name.equals("Hive Lord")){
            return hiveLord(scrap,p,cp,current,pp,discp,chasseAlhomme,listG,listE,blob,starEmpire,machineCult,tradeFederation,shopGeneral,shopExplorer,dp,ia);
        }
        else if(h.name.equals("Screecher")){
            return screecher(scrap,p,cp,current,pp,discp,chasseAlhomme,listG,listE,blob,starEmpire,machineCult,tradeFederation,shopGeneral,shopExplorer,dp,ia);
        }
        return false;
    }
    
    /**
     *methode générique correspondant a la réactivation des capacité allié d'une carte
     */
    private static void allyXabilities(String ally, ArrayList<Player> p, ArrayList<CardPlayed> cp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ArrayList<CardPlayer> dp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation){
        for(Card s:cp.get(current).list){
        	if(!s.isHeroe()) {
        		Ship.allyCapacityShip((Ship) s,ally,current,chasseAlhomme,p,pp,dp,discp,cp,shopGeneral,listG,shopExplorer , listE,blob,starEmpire,machineCult,tradeFederation, true);
        	}
        }
        for(Card s: p.get(current).list){
        	if(!s.isHeroe()) {
        		Ship.allyCapacityShip((Ship) s,ally,current,chasseAlhomme,p,pp,dp,discp,cp,shopGeneral,listG,shopExplorer , listE,blob,starEmpire,machineCult,tradeFederation, true);
        	}
        }
    }

    
    
    
    /**
     * =======================================================================
     * Toute les méthodes ci dessous correspondent aux capacité des héros
     *========================================================================
     */
    private static boolean ceoShanerCapa(Boolean scrap,ArrayList<CardPlayer> dp, ShopGeneral shopGeneral, ShopExplorer shopExplorer, ArrayList<Player> p, ArrayList<CardPlayed> cp, ListGeneral listG, ListExplorer listE, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation){
        
        allyXabilities("Trade Federation",p,cp,current,pp,discp, chasseAlhomme, dp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
        if(scrap){
            Ship.drawOneCard(current,pp,dp,discp);
        }
        else{
            Ship.acquireShipValueXAndPutTop(3,shopGeneral,listG,shopExplorer,listE,current,p,dp);
        }
        return true;
    }


    private static boolean chairmanHaygan(ArrayList<Player> p, ArrayList<CardPlayed> cp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ListGeneral listG, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ShopExplorer shopExplorer, ArrayList<CardPlayer> dp, ShopGeneral shopGeneral){

        allyXabilities("Trade Federation",p,cp,current,pp,discp, chasseAlhomme, dp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
        p.get(current).manageHeal(5);
        return true;
    }

    private static boolean chancellorHartman(ArrayList<Player> p, ArrayList<CardPlayed> cp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ListGeneral listG, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ShopGeneral shopGeneral, ShopExplorer shopExplorer, ArrayList<CardPlayer> dp){

        allyXabilities("Machine Cult",p,cp,current,pp,discp, chasseAlhomme, dp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
        Ship.scrapGeneral(2,current,p,cp,dp,pp,shopGeneral,listG,shopExplorer,listE);
        return true;
    }

    private static boolean commanderKlik(ArrayList<Player> p, ArrayList<CardPlayed> cp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ListGeneral listG, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ShopGeneral shopGeneral, ShopExplorer shopExplorer, ArrayList<CardPlayer> dp, boolean ia){

        allyXabilities("Star Empire",p,cp,current,pp,discp, chasseAlhomme, dp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
        if(Ship.discardCard(ia, dp.get(current), discp.get(current))){
            Ship.drawOneCard(current,pp,dp,discp);
        }
        return true;
    }

    private static boolean commodoreZhang(Boolean scrap, ArrayList<Player> p, ArrayList<CardPlayed> cp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ListGeneral listG, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ShopGeneral shopGeneral, ShopExplorer shopExplorer, ArrayList<CardPlayer> dp, boolean ia){

        allyXabilities("Star Empire",p,cp,current,pp,discp, chasseAlhomme, dp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
        if(scrap){
            Ship.drawOneCard(current,pp,dp,discp);
        }else{
            p.get(current).manageDamage(4);
            Ship.targetOpponentDiscardCard(chasseAlhomme, ia,current,p,dp,discp);
        }
        return true;
    }

    private static boolean confessorMorris(Boolean scrap,ArrayList<Player> p, ArrayList<CardPlayed> cp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ListGeneral listG, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ShopGeneral shopGeneral, ShopExplorer shopExplorer, ArrayList<CardPlayer> dp){

        allyXabilities("Machine Cult",p,cp,current,pp,discp, chasseAlhomme, dp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
        if(scrap){
            Ship.drawOneCard(current,pp,dp,discp);
        }else{
            for(int i=0;i>2;i++){
                Ship.scrapGeneral(2,current,p,cp,dp,pp,shopGeneral,listG,shopExplorer,listE);
            }
        }
        return true;
    }

    private static boolean hiveLord(Boolean scrap,ArrayList<Player> p, ArrayList<CardPlayed> cp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ListGeneral listG, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ShopGeneral shopGeneral, ShopExplorer shopExplorer, ArrayList<CardPlayer> dp, boolean ia){

        allyXabilities("Blob",p,cp,current,pp,discp, chasseAlhomme, dp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
       if(scrap){
           Ship.drawOneCard(current,pp,dp,discp);
       }else{
           Ship.scrapCardTradeRow(ia,shopGeneral,listG,shopExplorer,listE);
           p.get(current).manageDamage(5);
       }

        return true;
    }

    private static boolean screecher(Boolean scrap,ArrayList<Player> p, ArrayList<CardPlayed> cp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ListGeneral listG, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ShopGeneral shopGeneral, ShopExplorer shopExplorer, ArrayList<CardPlayer> dp, boolean ia){

        allyXabilities("Blob",p,cp,current,pp,discp, chasseAlhomme, dp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
        if(!scrap){
            Ship.scrapCardTradeRow(ia,shopGeneral,listG,shopExplorer,listE);
        }
        p.get(current).manageDamage(2);
        return true;

    }

	@Override
	public boolean isTrash() {
		return true;
	}

	@Override
	public boolean isPriority() {
		return false;
	}
	
	/**
	 * méthode permettant d'avoir le format texte d'un héro
	 *
	 */
	public String toSave(){
        return "Heroe&"+name+"&"+price+"&"+capacity+"&"+scrapCapacity;
    }

	/**
	 * méthode permettant de recrée un Héro a partir de son format texte
	 *
	 */
    public static Heroes toReload(String line){
        String[] tblLine = line.split("&");
        return new Heroes(tblLine[1],Integer.parseInt(tblLine[2]),tblLine[3],tblLine[4]);
    }

}