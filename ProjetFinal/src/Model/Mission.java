package Model;

import View.Board;

import java.util.*;

import Controleur.Main;

public class Mission {
	
    private final String objectifName;
    private final String description;
    private boolean completed;
    
    public Mission(String objectifName, String description) {
		this.objectifName = Objects.requireNonNull(objectifName);
		this.description = Objects.requireNonNull(description);
		this.completed = false;
	}
    
    public Mission(String objectifName, String description,Boolean completed) {
        this.objectifName = Objects.requireNonNull(objectifName);
        this.description = Objects.requireNonNull(description);
        this.completed = completed;
    }
  
    public String toString() {
    	String texte = "|"+objectifName+(" ".repeat(11-objectifName.length()))+"|"+
                description+(" ".repeat(204-description.length()))+"|"
                +completed+(" ".repeat(9-((completed) ? 4 : 5)))+"|";
    	return texte;
               
    }
    
    public String getObjectifName() {
		return objectifName;
	}
    
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean b){
        completed=b;
    }

    
    /**
	 * Méthode permettant de lancer une mission a partir de son nom
	 */
    public static boolean launchMission(String nom, ShopExplorer shopExplorer, ShopGeneral shopGeneral, boolean combatAlhomme, ArrayList<CardPlayed> cp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayer> dp, int current, ArrayList<Player> p, ListGeneral listGeneral, ListExplorer listExplorer, ArrayList<PickPlayer> pp){
        if(nom.equals("Ally")){
            return useAllyFromTwoFaction(current, cp, p,shopGeneral, listGeneral,shopExplorer, listExplorer,dp);
        }
        else if(nom.equals("Armada")){
            return playMoreThanSevenShip(current, pp, dp,  discp,cp,listExplorer, shopExplorer);
        }
        else if(nom.equals("Colonize")){
            return haveTwoBasesOfSameFaction(p,current, pp, dp, discp);
        }
        else if(nom.equals("Convert")){
            return cultShipHaveBase(p, cp,current,pp,  dp, discp);
        }
        else if(nom.equals("Defend")){
            return twoOutpostInGame(combatAlhomme,current, pp, dp, discp,p);
        }
        else if(nom.equals("Diversify")){
            return fourMoneyFiveAttackThreeHeal(p,current);
        }
        else if(nom.equals("Dominate")){
            return starEmpireShipHaveBase(p,current,cp,pp, dp, discp);
        }
        else if(nom.equals("Exterminate")){
            return blobShipHaveBase(p,current,cp,listGeneral,listExplorer,shopExplorer,shopGeneral);
        }
        else if(nom.equals("Influence")){
            return threeShipOrBaseSameFaction(shopExplorer, listExplorer,cp,p,current, pp, dp, discp);
        }
        else if(nom.equals("Monopolize")){
            return tradeFederationShipHaveBase(p, current,cp);
        }
        else if(nom.equals("Rule")){
            return twoBasesFromTwoFaction(p,shopGeneral, listGeneral,shopExplorer, listExplorer,pp,current);
        }
        else if(nom.equals("Unite")){
            return threeShipsDifferentFaction(cp, p,current,pp, dp, discp);
        }
        else{
            return false;
        }
    }
    
    /**
     * ========================================================================
	 * Les méthode ci dessous correspondent aux capacité que forme les missions
	 * ========================================================================
	 */
    public static void drawOneCard(int courant, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp) {

        int taille = pp.get(courant).size();

        if (taille > 0) {
            dp.get(courant).add(pp.get(courant).remove());
            if (taille == 1) {
                pp.get(courant).fillPick(discp.get(courant));
            }
        }
        else {
            pp.get(courant).fillPick(discp.get(courant));
            if (pp.get(courant).size() > 0) {
                dp.get(courant).add(pp.get(courant).remove());
            }
        }
    }
    
    public static boolean useAllyFromTwoFaction(int current, ArrayList<CardPlayed> cp, ArrayList<Player> p,ShopGeneral shopGeneral, ListGeneral listGeneral, ShopExplorer shopExplorer, ListExplorer listExplorer,ArrayList<CardPlayer> dp) {
        HashMap<String, ArrayList<Ship>> comparator = new HashMap<>();
        
        ArrayList<Ship> tradeFederation = new ArrayList<>();
        ArrayList<Ship> machinCult = new ArrayList<>();
        ArrayList<Ship> blob = new ArrayList<>();
        ArrayList<Ship> starEmpire = new ArrayList<>();
        
        comparator.put("Trade Federation", tradeFederation);
        comparator.put("Machine Cult", machinCult);
        comparator.put("Blob", blob);
        comparator.put("Star Empire", starEmpire);
        
        for (Card s : p.get(current).list) {

            for (String faction : ((Ship) s).getFaction()) {
            	if(!faction.equals("")) {
	            	ArrayList<Ship> value = comparator.get(faction);
	                value.add((Ship) s);
	                comparator.put(faction, value);
            	}
            }
        }
        
        for (Card s : cp.get(current).getList()) {
        	if(!s.isHeroe()) {
                for (String faction : ((Ship) s).getFaction()) {
                	if(!faction.equals("")) {
                		ArrayList<Ship> value = comparator.get(faction);
                        value.add((Ship) s);
                        comparator.put(faction, value);
                	}
                }
        	}
        }

        for (Map.Entry mapEntry : comparator.entrySet()) {
            int count = 0;
            ArrayList<Ship> value = (ArrayList<Ship>) mapEntry.getValue();
            if (value.size() >= 2) {
                for (Ship s : value) {
                    if (s.haveAnAllyThing()) {
                        count += 1;
                    }
                }
            }
            if (count >= 2) {
                Ship.acquireShipValueXAndPutTop(3,shopGeneral, listGeneral, shopExplorer, listExplorer,current,p, dp);
                return true;
            }

        }
        return false;
    }
    
    public static boolean playMoreThanSevenShip(int current, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp,ArrayList<CardPlayed> cp,ListExplorer listExplorer, ShopExplorer shopExplorer){
        int count = cp.get(current).size();
        
        if(count>=7){
            drawOneCard(current,pp,dp,discp);

            if(listExplorer.lowerPrice()==2){
                pp.get(current).add(listExplorer.remove());
                listExplorer.fillShop(shopExplorer);
            }
            return true;
        } 
        else{
            return false;
        }
    }

    public static boolean haveTwoBasesOfSameFaction(ArrayList<Player> p, int current, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp){
        HashMap<String,Integer> comparator = new HashMap<String, Integer>();
      
        for(Card b : p.get(current).list){
            for(String faction : ((Ship) b).getFaction()){
                if(comparator.containsKey(faction)){
                    comparator.put(faction,comparator.get(faction)+1);
                } else {
                    comparator.put(faction,1);
                }
            }
        }
        
        for(Integer i : comparator.values()){
            if(i>=2){
                for(int j=0;j<2;j++){
                    drawOneCard(current,pp,dp,discp);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean cultShipHaveBase(ArrayList<Player> p, ArrayList<CardPlayed> cp, int current,ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp){
        int countCult =0;
        boolean ia = p.get(current).isIa();
        boolean verif;
        
        for(Card base : p.get(current).list){
            for(String faction : ((Ship) base).getFaction()){
                if(faction.contentEquals("Machine Cult")){
                    countCult+=1;
                }
            }
        }
        
        if(countCult!=0) {
            for(Card ship: cp.get(current).list){
                if(!ship.isHeroe()) {
                	for(String faction : ((Ship) ship).getFaction()){
                        if(faction.equals("Machine Cult")){

                            if(pp.size()<3){
                                pp.get(current).fillPick(discp.get(current));
                            }
                            if(pp.get(current).size() >= 3) {
                                if(!ia) {
                                	ArrayList<Card> listCard = new ArrayList<>();
                                	for (int i = 0; i < 3; i++) {
                                        listCard.add(pp.get(current).get((pp.get(current).size()-1)-i));
                                    }
                                	System.out.println(AbstractArrayListToString.toStringCard(listCard));
                                }
                                
                                int choix;
                                if (!ia) {
                                	choix = Main.chooseNumberBetween(0, 2, "Selectionner une de ces trois cartes pour l'avoir dans votre main\n");
                                }
                                else {
                            		Random r = new Random();
                            		choix = r.ints(0, 3).findFirst().getAsInt();
                            	}                            
                                dp.get(current).add(pp.get(current).get(pp.get(current).size()-1-choix));

                                int choix2 = -99;
                                
                                if (!ia) {
                                	do {
                                        Board.printMsg("Selectionner une de ces deux cartes restantes pour la mettre dans votre défausse \n la dernière sera ainsi sur le haut du deck\n");
                                        Scanner sc = new Scanner(System.in);
                                		verif = sc.hasNextInt();
                                		if (verif) {
                                			choix2 = sc.nextInt();
                                		}
                                    }while(!(choix2>=0 && choix2<=2 && choix != choix2));
                                }
                                else {
                            		Random r = new Random();
                            		do {
                                		choix2 = r.ints(0, 3).findFirst().getAsInt();                        			
                            		} while (!(choix != choix2));
                            	} 
                                  
                                discp.get(current).add(pp.get(current).get(pp.get(current).size()-1-choix2));
                                if(choix > choix2) {
                                	pp.get(current).remove(choix);
                                    pp.get(current).remove(choix2);
                                }
                                else {
                                	pp.get(current).remove(choix2);
                                    pp.get(current).remove(choix);
                                }
                                
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean twoOutpostInGame(boolean combatAlhomme, int current, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp,ArrayList<Player> p){
        int count = 0;
        boolean ia = p.get(current).isIa();
        boolean verif;
        
        for(Card s : p.get(current).list){ 
           if(((Base) s).isOutPost()){
               count += 1;
           }
        }
        if(count>=2){
            drawOneCard(current,pp,dp,discp);
            
            
            int choix = -99;
            if(!ia) {
            	if(combatAlhomme) {
            		do {
            			System.out.println(Board.printPlayer(p));
                        Board.printMsg("Selectionner un joueur situé à votre droite ou a votre gauche dont vous voulez détruire la base ou pour quitter : "+p.size());
                        Scanner sc = new Scanner(System.in);
                		verif = sc.hasNextInt();
                		if (verif) {
                			choix = sc.nextInt();
                		}
                    }while (!(choix == (current-1) || choix == (current+1) || choix == p.size()));
            	}
            	else {
            		do {
            			System.out.println(Board.printPlayer(p));
                        Board.printMsg("Selectionner un joueur dont vous voulez détruire la base ou pour quitter : "+p.size());
                        Scanner sc = new Scanner(System.in);
                		verif = sc.hasNextInt();
                		if (verif) {
                			choix = sc.nextInt();
                		}
                    }while (!(choix != current && choix >=0 && choix <= p.size()));
            	}
            }
            else {
            	if(combatAlhomme) {
            		if (current == 0) {
            			choix = p.size()-1;
            		}
            		else if(choix == (p.size()-1)) {
            			choix = 0;
            		}
            		else {
            			choix = current-1;
            		}	
            	}
            	else {
            		Random r = new Random();
            		do {
                		choix = r.ints(0, p.size()).findFirst().getAsInt();                        			
            		} while (!(choix != current));
            	}
            }
            
            if(choix != p.size()) {
            	int choix2;
            	
            	if(p.get(choix).isEmpty()) {
            		return false;
            	}
            	if(!ia) {
            		Board.printMsg("Voici la liste des bases ennemies");
                    Board.printMsg(p.get(choix).toString());
                    choix2 = Main.chooseNumberBetween(0, p.get(choix).size()-1, "Selectionner une base ennemi à détruire en entrant sa position :");
            	}
            	else {
            		Random r = new Random();
                	choix2 = r.ints(0, p.get(choix).size()).findFirst().getAsInt();
            	}      	
                dp.get(choix).add(p.get(choix).remove(choix2));
                
            }
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean fourMoneyFiveAttackThreeHeal(ArrayList<Player> p,int current){
        Player player=p.get(current);
        int choix;
        boolean ia = p.get(current).isIa();
        
        if(player.getDamage()>=5 && player.getMoney()>=4 && player.getHeal()>=3){
        	if(!ia) {
        		choix = Main.chooseNumberBetween(1, 3, "Souhaitez-vous prendre : 1:(4 Money) 2:(5 Damages) 3:(6 Heal)");
        	}
        	else {
        		Random r = new Random();
            	choix = r.ints(1, 4).findFirst().getAsInt();
        	}
            
            if(choix==1){
                player.manageMoney(4);
            }
            else if(choix==2){
                player.manageDamage(5);
            }
            else{
                player.manageHeal(6);
            }
            return true;
        }
        return false;
    }

    public static boolean starEmpireShipHaveBase(ArrayList<Player> p,int current,ArrayList<CardPlayed> cp,ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp){
        int countStar = 0;
        CardPlayed cardPlayed = cp.get(current);
        Player player=p.get(current);
        for(Card base : player.list){
            for(String faction : ((Ship) base).getFaction()){
                if(faction.equals("Star Empire")){
                    countStar+=1;
                }
            }
        }
        
        if(countStar!=0) {
            for(Card ship: cardPlayed.list){
                for(String faction : ((Ship) ship).getFaction()){
                    if(faction.equals("Star Empire")){
                        player.manageDamage(3);
                        drawOneCard(current,pp,dp,discp);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean blobShipHaveBase(ArrayList<Player> p,int current,ArrayList<CardPlayed> cp,ListGeneral listGeneral,ListExplorer listExplorer, ShopExplorer shopExplorer, ShopGeneral shopGeneral){
        CardPlayed cardPlayed = cp.get(current);
        Player player=p.get(current);
        int countBlob =0;
        boolean ia = p.get(current).isIa();
        
        for(Card base : player.list){
            for(String faction : ((Ship) base).getFaction()){
                if(faction.equals("Blob")){
                    countBlob+=1;
                }
            }
        }
        if( countBlob!=0) {
            for(Card ship: cardPlayed.list){
                if(!ship.isHeroe()) {
                	for(String faction : ((Ship) ship).getFaction()){
                        if(faction.equals("Blob")){
                        	
                            player.manageDamage(3);
                            int size=0;
                            size+=listExplorer.size();
                            size+=listGeneral.size();
                            for(int i=0;i<size;i++){
                                int choix;

                                if(!ia) {
                                	System.out.println(Board.printShop(listGeneral,listExplorer));
                                    choix = Main.chooseNumberBetween(-1, listGeneral.size(), "Selectionner dans quel shop voulez-vous détruire une carte : 0 pour le shop explorer et  1 pour le shopGeneral et pour quitter -1");
                                }
                                else {
                                	Random r = new Random();
                                	choix = r.ints(-1, 2).findFirst().getAsInt();
                                }
                                

                                if(choix==0){
                                	if(!listExplorer.isEmpty()) {
                                		listExplorer.remove();
                                        listExplorer.fillShop(shopExplorer);
                                	}   
                                }
                                else if(choix == -1) {
                                	break;
                                }
                                else {
                                	if(!listGeneral.isEmpty()) {
                                		if(!ia) {
                                        	System.out.println(Board.printListG(listGeneral));
                                            choix = Main.chooseNumberBetween(0, (listGeneral.size()-1), "Selectionner une carte a détruire en entrant sa position :");
                                        }
                                        else {
                                        	Random r = new Random();
                                        	choix = r.ints(0, listGeneral.size()).findFirst().getAsInt();
                                        }
                                        listGeneral.remove(choix);
                                        listGeneral.fillShop(shopGeneral);
                                	}
                                }
                            }

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean threeShipOrBaseSameFaction(ShopExplorer shopExplorer, ListExplorer listExplorer,ArrayList<CardPlayed> cp,ArrayList<Player> p,int current, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp){
        HashMap<String,Integer> comparator = new HashMap();
        
        for(Card s: p.get(current).getList()){
            String[] fac = ((Ship) s).getFaction();
            for(String faction : fac){
            	Integer value = comparator.get(faction);
            	if (value != null) {
            		comparator.put(faction,(value+1));
                }
                else {
                	comparator.put(faction,1);
                }
            }
        }
        
        for(Card s: cp.get(current).getList()){
            if(!s.isHeroe()) {
            	String[] fac = ((Ship) s).getFaction();
                for(String faction : fac){
                	Integer value = comparator.get(faction);
                	if (value != null) {
                		comparator.put(faction,(value+1));
                    }
                    else {
                    	comparator.put(faction,1);
                    }
                }
            }
        }
        
        for(Map.Entry mapentry : comparator.entrySet()){
            if(((int) mapentry.getValue())>=3){

                for(int i = 0; i<2; i++){
                    if(!listExplorer.isEmpty()){
                        dp.get(current).add(listExplorer.remove());
                        listExplorer.fillShop(shopExplorer);
                    }
                    else {
                    	break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static boolean tradeFederationShipHaveBase(ArrayList<Player> p,int current,ArrayList<CardPlayed> cp){
        CardPlayed cardPlayed = cp.get(current);
        Player player=p.get(current);
        int countTrade =0;
        for(Card base : player.list){
            for(String faction : ((Ship) base).getFaction()){
                if(faction.equals("Trade Federation")){
                    countTrade+=1;
                }
            }
        }
        if( countTrade!=0) {
            for(Card ship: cardPlayed.list){
                if(!ship.isHeroe()) {
                	for(String faction : ((Ship) ship).getFaction()){
                        if(faction.equals("Trade Federation")){
                        	p.get(current).manageHeal(10);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean twoBasesFromTwoFaction(ArrayList<Player> p, ShopGeneral shopGeneral, ListGeneral listGeneral, ShopExplorer shopExplorer, ListExplorer listExplorer,ArrayList<PickPlayer> pp, int current) {
        TreeSet<String> comparator = new TreeSet<>();
        boolean ia = p.get(current).isIa();

        for (Card b : p.get(current).list) {
            for (String faction : ((Ship) b).getFaction()) {
                comparator.add(faction);
            }
        }
        if(comparator.size() >= 2){
            p.get(current).manageHeal(10);
            int choix;
            
            if(!listGeneral.isEmpty() && !listExplorer.isEmpty()) {
            	
            	if(!ia) {
            		System.out.println(Board.printShop(listGeneral,listExplorer));
            		choix = Main.chooseNumberBetween(0, 1, "Selectionner le shop pour obtenir une carte qui coute 3 ou moins : 0 Shop explorer ou 1 Shop General");
            	}
            	else {
            		Random r = new Random();
                	choix = r.ints(0, 2).findFirst().getAsInt();
            	}
            	 
                
                if(choix == 0){
                    if(!listExplorer.isEmpty()){
                        pp.get(current).add(listExplorer.remove());
                        listExplorer.fillShop(shopExplorer);
                    }
                }
                else if(choix == 1){
                	if(!listGeneral.isEmpty()) {
                		if(!ia) {
                        	System.out.println(Board.printListG(listGeneral));
                        	choix = Main.chooseNumberBetween(0, (listGeneral.size()-1), "Selectionner une carte qui coute 3 ou moins pour l'obtenir en entrant sa position :");
                        }
                        else {
                        	Random r = new Random();
                            choix = r.ints(0, listGeneral.size()).findFirst().getAsInt();
                        }
                		
                		if(listGeneral.get(choix).getPrice() <= 3) {
                			pp.get(current).add(listGeneral.remove(choix));
        	                listGeneral.fillShop(shopGeneral);
                		}
                		else {
                			if(!ia) {
                				System.out.println("Vous avez choisi une carte qui a un prix supérieure à 3");
                			}
                		}
    	                
                	}
                }
            }
            return true;
        }
        return false;
    }

    public static boolean threeShipsDifferentFaction(ArrayList<CardPlayed> cp, ArrayList<Player> p,int current,ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp) {
        TreeSet<String> comparator = new TreeSet<>();
        Player player=p.get(current);
        for (Card b : cp.get(current).list) {
            if(!b.isHeroe()) {
            	for (String faction : ((Ship) b).getFaction()) {
                    comparator.add(faction);
                }
            }
        }
        
        if(comparator.size() >= 3){
            player.manageHeal(5);
            drawOneCard(current,pp,dp,discp);
            return true;
        }
        return false;
    }
    
    public String toSave(){
        return  objectifName +"&"+ description +"&"+ completed;

    }

    public static Mission toReload(String line){
        String[] tblLine = line.split("&");
        return new Mission(tblLine[0],tblLine[1],Boolean.parseBoolean(tblLine[2]));
    }

}
