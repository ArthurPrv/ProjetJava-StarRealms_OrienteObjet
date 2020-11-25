package Model;

import java.util.ArrayList;
import java.util.Arrays;

public class Base extends TrashShip {
	
	private final boolean outPost;
    private final int shield;

    public Base(String name, String[] faction, int price, int damage, int money, int heal, String capacity, boolean choose, Couple allyDamage, Couple allyMoney, Couple allyHeal, boolean allyChoose, Ensemble[] allyCapacity, boolean priority, boolean capacityPriority, int trashDamage, int trashMoney, int trashHeal, String trashCapacity, boolean outPost, int shield) {
        super(name, faction, price, damage, money, heal, capacity, choose, allyDamage, allyMoney, allyHeal, allyChoose, allyCapacity, priority, capacityPriority, trashDamage, trashMoney, trashHeal, trashCapacity);
        this.outPost = outPost;
        this.shield = shield;
    }
    
    public Base(String name, String[] faction, int price, int damage, int money, int heal, String capacity, boolean choose, boolean capacityUsed, Couple allyDamage, Couple allyMoney, Couple allyHeal, boolean allyChoose, Ensemble[] allyCapacity, boolean priority, boolean capacityPriority, int trashDamage, int trashMoney, int trashHeal, String trashCapacity, boolean trashCapacityUsed, boolean outPost, int shield) {
        super(name, faction, price, damage, money, heal, capacity, choose, capacityUsed, allyDamage, allyMoney, allyHeal, allyChoose, allyCapacity, priority, capacityPriority, trashDamage, trashMoney, trashHeal, trashCapacity, trashCapacityUsed);
        this.outPost = outPost;
        this.shield = shield;
    }

    @Override
	public String toString() {
		return super.infoShip()+
				shield+(" ".repeat(6-1))+"|"+
				outPost+(" ".repeat(7- ((outPost) ? 4 : 5)))+"|";
	}
    
    public boolean isOutPost() {
        return outPost;
    }
    
    public int getShield() {
        return shield;
    }
    
    public boolean isShip() {
    	return false;
    }
    
    public boolean isTrashShip() {
    	return false;
    }

    @Override
    public boolean isBase() {
    	return true;
    }
    
    public boolean isHeroe() {
        return false;
    }
    
    @Override
    public boolean isTrash() {
        return super.getTrashDamage()!=0 || super.getTrashHeal()!=0 || super.getTrashMoney()!=0 || !(super.getTrashCapacity().equals(""));
    }
    
    /**
     * Méthode permettant de gérer les capacité des bases lorsqu'elles sont déclanché par le scrap
     *
     */
    public static void trashCapacity(Base s, boolean chasseAlhomme, int current, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> tradeFederation, ArrayList<Ship> machineCult) {
		
		if (s.getTrashCapacity().equals("Draw a Card")) {
			s.setTrashCapacityUsed(drawOneCard(current, pp, dp, discp));
		}
		
		else if (s.getTrashCapacity().equals("Draw a card and destroy target base.")) {
			s.setTrashCapacityUsed(drawOneCard(current, pp, dp, discp));
		}
		
		else if (s.getTrashCapacity().equals("Target opponent discards a card.")) {
			s.setTrashCapacityUsed(targetOpponentDiscardCard(chasseAlhomme, p.get(current).isIa(), current, p, dp, discp));
		}
		
		else if (s.getTrashCapacity().equals("Scrap a card in your hand or discard pile.")) {
			s.setTrashCapacityUsed(scrapGeneral(2, current, p, cp, dp, pp, shopGeneral, listG, shopExplorer, listE));
		}
	}
    
    
    /**
     * 
     *methode permettant de préparer la sauvegarde d'une base dans le format fichier texte
     */
    public String preSave(){
        return super.preSave() +"&"+ outPost +"&"+ shield;
    }
    
    
    /**
     * Methode permettant de retourner le format d'une base pour le format texte
     *
     */
    public  String toSave(){
        return "Base&"+ preSave();
    }

    
    /**
     * Cette méthode permet de recréer une Base a partie de la chaine de caractère lui correspondant dans le fichier texte
     *
     */
    public static Base toReload(String line){

        String[] lineTbl = line.split("&");
        String[] fac= (lineTbl[2].substring(1).replace("]","").replace(", ",",").split(","));

        String[] allydomage=lineTbl[10].split("@");
        String[] allymoney=lineTbl[11].split("@");
        String[] allyheal=lineTbl[12].split("@");

        String[] allycapacity = lineTbl[14].substring(1).replace("]","").replace(", ",",").split(",");

        ArrayList<Ensemble> strToEnsArray = new ArrayList<>();

        for(String s :allycapacity){
            String[] tblInfo = s.split("@");
            if(lineTbl[14].equals("[]")){
                strToEnsArray.add(new Ensemble());
            }else{
                strToEnsArray.add(new Ensemble(tblInfo[0],tblInfo[1],Boolean.parseBoolean(tblInfo[2])));

            }
        }
        Ensemble[] allyCapacityEnd = new Ensemble[strToEnsArray.size()];
        for(int i=0;i<strToEnsArray.size();i++){
            allyCapacityEnd[i]=strToEnsArray.get(i);
        }

        return new Base(lineTbl[1], fac,Integer.parseInt(lineTbl[3]),Integer.parseInt(lineTbl[4]),Integer.parseInt(lineTbl[5]),Integer.parseInt(lineTbl[6]),lineTbl[7],Boolean.parseBoolean(lineTbl[8]),Boolean.parseBoolean(lineTbl[9]), new Couple(allydomage[0],Integer.parseInt(allydomage[1])),new Couple(allymoney[0],Integer.parseInt(allymoney[1])),new Couple(allyheal[0],Integer.parseInt(allyheal[1])),Boolean.parseBoolean(lineTbl[13]),allyCapacityEnd,Boolean.parseBoolean(lineTbl[15].toString()),Boolean.parseBoolean(lineTbl[16].toString()),Integer.parseInt(lineTbl[17].toString()),Integer.parseInt(lineTbl[18].toString()),Integer.parseInt(lineTbl[19].toString()),lineTbl[20].toString(),Boolean.parseBoolean(lineTbl[21].toString()),Boolean.parseBoolean(lineTbl[22].toString()),Integer.parseInt(lineTbl[23].toString()));
    }
}
