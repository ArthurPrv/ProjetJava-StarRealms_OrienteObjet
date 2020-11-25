package Model;

import java.util.ArrayList;

public class TrashShip extends Ship{

	private final int trashDamage;
    private final int trashMoney;
    private final int trashHeal;
    private final String trashCapacity;
    private boolean trashCapacityUsed ;

    public TrashShip(String name, String[] faction, int price, int damage, int money, int heal, String capacity, boolean choose, Couple allyDamage, Couple allyMoney, Couple allyHeal, boolean allyChoose, Ensemble[] allyCapacity, boolean priority, boolean capacityPriority, int trashDamage, int trashMoney, int trashHeal, String trashCapacity) {
        super(name, faction, price, damage, money, heal, capacity, choose, allyDamage, allyMoney, allyHeal, allyChoose, allyCapacity, priority, capacityPriority);
        this.trashDamage = trashDamage;
        this.trashMoney = trashMoney;
        this.trashHeal = trashHeal;
        this.trashCapacity = trashCapacity;
        this.trashCapacityUsed = false;
    }
    
    public TrashShip(String name, String[] faction, int price, int damage, int money, int heal, String capacity, boolean choose, boolean capacityUsed, Couple allyDamage, Couple allyMoney, Couple allyHeal, boolean allyChoose, Ensemble[] allyCapacity, boolean priority, boolean capacityPriority, int trashDamage, int trashMoney, int trashHeal, String trashCapacity, boolean trashCapacityUsed) {
        super(name, faction, price, damage, money, heal, capacity, choose, capacityUsed, allyDamage, allyMoney, allyHeal, allyChoose, allyCapacity, priority, capacityPriority);
        this.trashDamage = trashDamage;
        this.trashMoney = trashMoney;
        this.trashHeal = trashHeal;
        this.trashCapacity = trashCapacity;
        this.trashCapacityUsed = trashCapacityUsed;
    }

    @Override
	public String toString() {
		return  infoShip()+
				(" ".repeat(6))+"|"+
				(" ".repeat(7))+"|";
	}
    
    @Override
	public String infoShip(){
		return  super.infoShip()+
				trashDamage+(" ".repeat(12-1))+"|"+
				trashMoney+(" ".repeat(11-1))+"|"+
				trashHeal+(" ".repeat(10-1))+"|"+
				trashCapacity+(" ".repeat(41-trashCapacity.length()))+"|";
	}
    
    public boolean isShip() {
    	return false;
    }
    
    public boolean isTrashShip() {
    	return true;
    }
    
    public boolean isTrash() {
        return true;
    }
    
    public boolean isBase() {
    	return false;
    }
    
    public boolean isHeroe() {
        return false;
    }

    public int getTrashDamage() {
        return trashDamage;
    }

    public int getTrashHeal() {
        return trashHeal;
    }

    public int getTrashMoney() {
        return trashMoney;
    }

    public String getTrashCapacity() {
        return trashCapacity;
    }
    
    public boolean getTrashCapacityUsed() {
        return trashCapacityUsed;
    }
    
    public void setTrashCapacityUsed(boolean b) {
        trashCapacityUsed = b;
    }
    
    
	/**
	 * Méthode permettant d'activé la capacité trash d'une carte
	 */
    public static void trashCapacity(TrashShip s, boolean chasseAlhomme, int current, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> tradeFederation, ArrayList<Ship> machineCult) {
		
		if (s.trashCapacity.equals("Draw a Card")) {
			s.trashCapacityUsed = drawOneCard(current, pp, dp, discp);
		}
		
		else if (s.trashCapacity.equals("Draw a card and destroy target base")) {
			s.trashCapacityUsed = drawOneCard(current, pp, dp, discp);
		}
		
		else if (s.trashCapacity.equals("Target opponent discards a card")) {
			s.trashCapacityUsed = targetOpponentDiscardCard(chasseAlhomme, p.get(current).isIa(), current, p, dp, discp);
		}
		
		else if (s.trashCapacity.equals("Scrap a card in your hand or discard pile.")) {
			s.trashCapacityUsed = scrapGeneral(2, current, p, cp, dp, pp, shopGeneral, listG, shopExplorer, listE);
		}
	}
    
	/**
	 * méthode permettant de recuillir les information d'une carte pour avoir son format texte
	 */
    public String preSave(){
        return super.preSave() +"&"+ trashDamage +"&"+ trashMoney +"&"+ trashHeal +"&"+ trashCapacity +"&"+ trashCapacityUsed;
    }

    
	/**
	 * méthode permettant d'avoir le format texte d'un traship
	 */
    public  String toSave(){
        return "TrashShip&"+ preSave();
    }
    
	/**
	 * méthode permettant de crée un traship a partir du format texte correspondant
	 */
    public static TrashShip toReload(String line){


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

        return new TrashShip(lineTbl[1], fac,Integer.parseInt(lineTbl[3]),Integer.parseInt(lineTbl[4]),Integer.parseInt(lineTbl[5]),Integer.parseInt(lineTbl[6]),lineTbl[7],Boolean.parseBoolean(lineTbl[8]),Boolean.parseBoolean(lineTbl[9]), new Couple(allydomage[0],Integer.parseInt(allydomage[1])),new Couple(allymoney[0],Integer.parseInt(allymoney[1])),new Couple(allyheal[0],Integer.parseInt(allyheal[1])),Boolean.parseBoolean(lineTbl[13]),allyCapacityEnd,Boolean.parseBoolean(lineTbl[15].toString()),Boolean.parseBoolean(lineTbl[16].toString()),Integer.parseInt(lineTbl[17].toString()),Integer.parseInt(lineTbl[18].toString()),Integer.parseInt(lineTbl[19].toString()),lineTbl[20].toString(),Boolean.parseBoolean(lineTbl[21].toString()));
    }
}
