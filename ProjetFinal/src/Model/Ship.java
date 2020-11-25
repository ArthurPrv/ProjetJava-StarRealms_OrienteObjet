package Model;

import View.Board;

import java.util.*;

import Controleur.Main;

public class Ship implements Card{
	
	private final String name;
    private final String[] faction;
    private final int price;
    
    private final int damage;
    private final int money;
    private final int heal;
    private final String capacity;
    private final boolean choose;
    private boolean capacityUsed;
    
    private final Couple allyDamage;
    private final Couple allyMoney;
    private final Couple allyHeal;
    private final boolean allyChoose;
    private Ensemble[] allyCapacity;
    private final boolean priority;
    boolean capacityPriority;


	public Ship(String name, String[] faction, int price, int damage, int money, int heal, String capacity, boolean choose, Couple allyDamage, Couple allyMoney, Couple allyHeal, boolean allyChoose, Ensemble[] allyCapacity, boolean priority, boolean capacityPriority) {
		this.name = Objects.requireNonNull(name);
		this.faction = faction;
		this.price = price;
		this.damage = damage;
		this.money = money;
		this.heal = heal;
		this.capacity = capacity;
		this.choose = choose;
		this.capacityUsed = false;
		this.allyDamage = allyDamage;
		this.allyMoney = allyMoney;
		this.allyHeal = allyHeal;
		this.allyChoose = allyChoose;
		this.allyCapacity = allyCapacity;
		this.priority = priority;
		this.capacityPriority = capacityPriority;
	}
	
	public Ship(String name, String[] faction, int price, int damage, int money, int heal, String capacity, boolean choose, boolean capacityUsed, Couple allyDamage, Couple allyMoney, Couple allyHeal, boolean allyChoose, Ensemble[] allyCapacity, boolean priority, boolean capacityPriority) {
		this.name = Objects.requireNonNull(name);
		this.faction = faction;
		this.price = price;
		this.damage = damage;
		this.money = money;
		this.heal = heal;
		this.capacity = capacity;
		this.choose = choose;
		this.capacityUsed = capacityUsed;
		this.allyDamage = allyDamage;
		this.allyMoney = allyMoney;
		this.allyHeal = allyHeal;
		this.allyChoose = allyChoose;
		this.allyCapacity = allyCapacity;
		this.priority = priority;
		this.capacityPriority = capacityPriority;
	}

	@Override
    public String toString() {
        return          infoShip()+
                (" ".repeat(12))+"|"+
                (" ".repeat(11))+"|"+
                (" ".repeat(10))+"|"+
                (" ".repeat(41))+"|"+
                (" ".repeat(6))+"|"+
                (" ".repeat(7))+"|";
    }

	/**
	 * méthode regroupand les info d'un ship avant l'affichage
	 */
    public String infoShip(){
        return "|"+name+(" ".repeat(20-name.length()))+"|"+
                price+(" ".repeat(5-1))+"|"+
                toStringFaction()+(" ".repeat(34-toStringFaction().length()))+"|"+
                damage+(" ".repeat(6-1))+"|"+
                money+(" ".repeat(5-1))+"|"+
                heal+(" ".repeat(4-1))+"|"+
                capacity+(" ".repeat(97-capacity.length()))+"|"+
                choose+(" ".repeat(6-((choose) ? 4 : 5)))+"|"+
                allyDamage+(" ".repeat(20-allyDamage.toString().length()))+"|"+
                allyMoney+(" ".repeat(20-allyMoney.toString().length()))+"|"+
                allyHeal+(" ".repeat(20-allyHeal.toString().length()))+"|"+
                toStringAllyCapacity()+(" ".repeat(150-toStringAllyCapacity().length()))+"|"+
        		allyChoose+(" ".repeat(11-((allyChoose) ? 4 : 5)))+"|";
    }
    
    
    private String toStringFaction() {
    	StringBuilder stringFaction  = new StringBuilder();
    	for(int i = 0; i<faction.length; i++) {
    		if(i == (faction.length-1)) {
    			stringFaction.append(faction[i]);
    		}
    		else {
    			stringFaction.append(faction[i]);
    			stringFaction.append(", ");
    		}
    	}
    	
    	return stringFaction.toString();
    }
    
    private String toStringAllyCapacity() {
    	StringBuilder stringAllyCapacity  = new StringBuilder();
    	for(int i = 0; i<allyCapacity.length; i++) {
    		if(i == (allyCapacity.length-1)) {
    			stringAllyCapacity.append(allyCapacity[i]);
    		}
    		else {
    			stringAllyCapacity.append(allyCapacity[i]);
    			stringAllyCapacity.append("; ");
    		}
    	}
    	
    	return stringAllyCapacity.toString();
    }
    
    public String getName() {
    	return name;
    }
    
	public int getDamage() {
		return damage;
	}

	public int getHeal() {
		return heal;
	}

	public int getMoney() {
		return money;
	}
	 
	public boolean getCapacityUsed() {
		return capacityUsed;
	}
	
	public boolean getChoose() {
		return choose;
	}
	
	public boolean getAllyChoose() {
		return allyChoose;
	}

	public int getPrice() {
		return price;
	}
	
	public String getCapacity() {
		return capacity;
	}
	
	public boolean isShip() {
    	return true;
    }
    
    public boolean isTrashShip() {
    	return false;
    }
    
	public boolean isBase() {
		return false;
	}

	public boolean isPriority() {
		return priority;
	}

	public Couple getAllyDamage() {
		return allyDamage;
	}

	public Couple getAllyHeal() {
		return allyHeal;
	}

	public Couple getAllyMoney() {
		return allyMoney;
	}
	
	public boolean haveAnAllyThing(){
		if(allyDamage.getValue()>=1 || allyHeal.getValue()>=1 ||  allyMoney.getValue()>=1 || allyCapacity.length >=1){
            return  true;
        }
        return  false;
    }

	public String[] getFaction() {
		return faction;
	}
	
	/**
	 * méthode permettant d'utiliser la capacité d'une carte
	 */
	public static void capacity(Ship m, int current, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> tradeFederation, ArrayList<Ship> machineCult) {
		boolean ia = p.get(current).isIa();
		
		if(m.capacityUsed == false) {
			if (m.capacity.equals("If two bases or more in play Draw two Cards")) {
				m.capacityUsed = twoBasesDrawTwoCards(current, p, pp, dp, discp);
			}
			
			else if (m.capacity.equals("Draw a Card")) {
				m.capacityUsed = drawOneCard(current, pp, dp, discp);
			}
			
			else if (m.capacity.equals("Draw two cards")) {
				
				if(drawOneCard(current, pp, dp, discp) || drawOneCard(current, pp, dp, discp)) {
					m.capacityUsed = true;
				}
				m.capacityUsed = false;
			}
			
			else if (m.capacity.equals("Draw two cards then discard a card")) {
				
				if(drawOneCard(current, pp, dp, discp) || drawOneCard(current, pp, dp, discp) || discardCard(ia, dp.get(current), discp.get(current))) {
					m.capacityUsed = true;
				}
				m.capacityUsed = false;
			}
			
			else if (m.capacity.equals("Scrap a card in the trade row")) {
				m.capacityUsed = scrapCardTradeRow(ia, shopGeneral, listG, shopExplorer, listE);
			}
			
			else if (m.capacity.equals("Target opponent discards a card")) {
				m.capacityUsed = targetOpponentDiscardCard(chasseAlhomme, ia, current, p, dp, discp);
			}
			
			else if (m.capacity.equals("Discard up to two cards then draw that many cards")) {
				m.capacityUsed = discard2CardsDraw2Cards(ia, current, pp, dp, discp);
			}
			
			else if (m.capacity.equals("Put the next ship you acquire this turn on top of your deck")) {
				m.capacityUsed = nextBuyOnTop(current, p);
			}
			
			else if (m.capacity.equals("Scrap a card in your hand or discard pile. Scrap a card in the trade row")) {
				
				if(scrapGeneral(2, current, p, cp, dp, pp, shopGeneral, listG, shopExplorer, listE) || scrapCardTradeRow(ia, shopGeneral, listG, shopExplorer, listE)) {
					m.capacityUsed = true;
				}
				m.capacityUsed = false;
			}
			
			else if (m.capacity.equals("All of your ships get Add 1 Combat")) {
				m.capacityUsed = allShipEarn1Damage(current, cp, p);
			}
			
			else if (m.capacity.equals("Draw a card for each Blob card that you've played this turn")) {
				m.capacityUsed = drawForEachBlob(current, dp, cp, pp, discp);
			}
			
			else if (m.capacity.equals("Destroy target base")) {
				m.capacityUsed = destroyBase(ia, chasseAlhomme, current, p, discp);
			}
			
			else if (m.capacity.equals("Draw a card then scrap a card from your hand")) {
				if(drawOneCard(current, pp, dp, discp) || scrapCardHand(ia, dp.get(current))) {
					m.capacityUsed = true;
				}
				m.capacityUsed = false;
			}
			
			else if (m.capacity.equals("Mech World counts as an ally for all factions")) {
				m.capacityUsed = mechIsAlly(blob, tradeFederation, machineCult, starEmpire);
			}
			
			else if (m.capacity.equals("Scrap a card in your hand or discard pile")) {
				m.capacityUsed = scrapGeneral(2, current, p, cp, dp, pp, shopGeneral, listG, shopExplorer, listE);
			}
			
			else if (m.capacity.equals("Scrap up to two cards from your hand or discard pile. Draw a card for each card scrapped this way")) {
				m.capacityUsed = scrap2CardsDraw2Cards(ia, current, p, cp, pp, dp, discp, shopGeneral, listG, shopExplorer, listE);
			}
			
			else if(m.capacity.equals("Draw a card. Destroy target base")) {
				if(drawOneCard(current, pp, dp, discp) || destroyBase(ia, chasseAlhomme, current, p, discp)) {
					m.capacityUsed = true;
				}
				m.capacityUsed = false;
			}
			
			else if(m.capacity.equals("Acquire a card of cost six or less for free and put it into your hand")) {
				m.capacityUsed = acquireShipValueXAndPutTop(6, shopGeneral, listG, shopExplorer, listE, current, p, dp);
			}
			
			else if(m.capacity.equals("Scrap up to two cards in the trade row")) {
				if(scrapCardTradeRow(ia, shopGeneral, listG, shopExplorer, listE) || scrapCardTradeRow(ia, shopGeneral, listG, shopExplorer, listE)) {
					m.capacityUsed = true;
				}
				m.capacityUsed = false;
			}
			
			else if(m.capacity.equals("Scrap a card in your hand")) {
				m.capacityUsed = scrapCardHand(ia, dp.get(current));
			}
			
			else if (m.capacity.equals("Scrap two cards in your hand or discard pile")) {
				if(scrapGeneral(2, current, p, cp, dp, pp, shopGeneral, listG, shopExplorer, listE) || scrapGeneral(2, current, p, cp, dp, pp, shopGeneral, listG, shopExplorer, listE)) {
					m.capacityUsed = true;
				}
				m.capacityUsed = false;
			}
			
			else if(m.capacity.equals("Whenever you play a Star Empire ship gain 2 Combat")) {
				m.capacityUsed = playStarEmpireGain2Combat(ia, p.get(current), cp.get(current));
			}
			
			else if(m.capacity.equals("Draw a card. Target opponent discards a card")) {
				if(drawOneCard(current, pp, dp, discp) || targetOpponentDiscardCard(chasseAlhomme, ia, current, p, dp, discp)) {
					m.capacityUsed = true;
				}
				m.capacityUsed = false;
			}
			
			else if(m.capacity.equals("If an opponent controls a base gain 2 Combat")) {
				m.capacityUsed = opponentControlsBaseGain2Combat(current, p);
			}
			
			else if(m.capacity.equals("Discard a card. If you do draw a card")) {
				m.capacityUsed = discardCardIfYouDoDrawCard(ia, 3, current, p, cp, dp, pp, discp, shopGeneral, listG, shopExplorer, listE);
			}
			
			else if(m.capacity.equals("Discard two cards. Gain 2 Trade or 2 Combat for each card discarded")) {
				m.capacityUsed = DiscardTwoCard2Trade2Combat(ia, dp.get(current), discp.get(current), p.get(current));
			}
			
			else if(m.capacity.equals("If you have three or more bases in play (including this one) gain 4 Authority and draw a card")) {
				m.capacityUsed = threeBasesPlayGain4Authority(current, p, pp, dp, discp);
			}
			
		}
	}
	
	public static void allyCapacityShip(Ship s, String nameFaction, int current, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> tradeFederation, ArrayList<Ship> machineCult, boolean capacityHero) {
		
		for(Ensemble e : s.allyCapacity) {
			if(e.getUsed() == false || capacityHero) {
				if (e.getFaction().equals(nameFaction) || nameFaction.equals("All")) {
					allyCapacity(e, current, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, tradeFederation, machineCult);
				}
			}
		}
	}
	/**
	 * méthode permettant d'utiliser la capacité allié d'une carte
	 */
	private static void allyCapacity(Ensemble e, int current, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> tradeFederation, ArrayList<Ship> machineCult) {
		boolean ia = p.get(current).isIa();
		
		if (e.getCapacity().equals("Draw a Card")) {
			e.setUsed(drawOneCard(current, pp, dp, discp));
		}
		
		else if (e.getCapacity().equals("Acquire any ship without paying its cost and put it on top of your deck")) {
			e.setUsed(acquireShipValueXAndPutTop(100, shopGeneral, listG, shopExplorer, listE, current, p, dp));
		}
		
		else if (e.getCapacity().equals("Destroy target base or scrap a card in the trade row")) {
			if(destroyBase(ia, chasseAlhomme, current, p, discp) || scrapCardTradeRow(ia, shopGeneral, listG, shopExplorer, listE)) {
				e.setUsed(true);				
			}
			e.setUsed(false);
		}
		
		else if (e.getCapacity().equals("Target opponent discards a card")) {
			e.setUsed(targetOpponentDiscardCard(chasseAlhomme, ia, current, p, dp, discp)); 
		}
		
		else if (e.getCapacity().equals("Destroy target base")) {
			e.setUsed(destroyBase(ia, chasseAlhomme, current, p, discp)); 
		}
		
		else if (e.getCapacity().equals("Put the next ship you acquire this turn on top of your deck")) {
			e.setUsed(nextBuyOnTop(current, p));
		}
		
		else if (e.getCapacity().equals("Scrap a card in your hand or discard pile")) {
			e.setUsed(scrapGeneral(2, current, p, cp, dp, pp, shopGeneral, listG, shopExplorer, listE)); 
		}
		
		else if (e.getCapacity().equals("Scrap a card in the trade row")) {
			e.setUsed(scrapCardTradeRow(ia, shopGeneral, listG, shopExplorer, listE)); 
		}
		
		else if (e.getCapacity().equals("Scrap a card in your hand your discard pile or the trade row")) {
			e.setUsed(scrapGeneral(3, current, p, cp, dp, pp, shopGeneral, listG, shopExplorer, listE)); 
		}
		
		else if (e.getCapacity().equals("Choose a card of cost five or less in your discard pile and put it on top of your deck")) {
			e.setUsed(chooseCard(ia, discp.get(current), dp.get(current)));
		}
		
		else if(e.getCapacity().equals("Acquire a card of cost three or less for free and put it into your hand")) {
			e.setUsed(acquireShipValueXAndPutTop(3, shopGeneral, listG, shopExplorer, listE, current, p, dp));
		}
		
		else if(e.getCapacity().equals("Acquire a card of cost two or less for free and put it into your hand")) {
			e.setUsed(acquireShipValueXAndPutTop(2, shopGeneral, listG, shopExplorer, listE, current, p, dp));
		}
		
		else if (e.getCapacity().equals("Gain 2 Combat for each card scrapped from your hand or discard pile this turn")) {
			e.setUsed(scrapCardsGain2Damages(ia, current, p, cp, pp, dp, discp, shopGeneral, listG, shopExplorer, listE));
		}
		
		else if(e.getCapacity().equals("If an opponent controls a base gain 2 Combat")) {
			e.setUsed(opponentControlsBaseGain2Combat(current, p));
		}
		
		else if(e.getCapacity().equals("Discard a card. If you do draw a card")) {
			e.setUsed(discardCardIfYouDoDrawCard(ia, 3, current, p, cp, dp, pp, discp, shopGeneral, listG, shopExplorer, listE));
		}
		
		else if(e.getCapacity().equals("Discard two cards. Gain 2 Trade or 2 Combat for each card discarded")) {
			e.setUsed(DiscardTwoCard2Trade2Combat(ia, dp.get(current), discp.get(current), p.get(current)));
		}
		
		else if(e.getCapacity().equals("If you have three or more bases in play (including this one) gain 4 Authority and draw a card")) {
			e.setUsed(threeBasesPlayGain4Authority(current, p, pp, dp, discp));
		}
		
	}
	
	
	
	/**
	 * =============================================
	 * Les méthode en dessous correspondent aux capacité des cartes
	 * =============================================
	 */
	
	protected static boolean drawOneCard(int current, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp) {

		int taille = pp.get(current).size();

		if (taille > 0) {
			dp.get(current).add(pp.get(current).remove());
			if (taille == 1) {
				pp.get(current).fillPick(discp.get(current));
			}
			return true;
		}
		else {
			pp.get(current).fillPick(discp.get(current));
			if (pp.get(current).size() > 0) {
				dp.get(current).add(pp.get(current).remove());
				return true;
			}
			return false;
		}
	}
	
	protected static boolean twoBasesDrawTwoCards(int current, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp) {
		if (p.get(current).size() >= 2) {
			for (int i = 0; i < 2; i++) {
				drawOneCard(current, pp, dp, discp);
			}
			return true;
		}
		return false;
	}

	protected static boolean drawForEachBlob(int current, ArrayList<CardPlayer> dp,ArrayList<CardPlayed> cp,ArrayList<PickPlayer> pp,ArrayList<DisclaimerPlayer> discp ){
		int count=0;

		for( Card s: cp.get(current).list){
			if(!s.isHeroe()) {
				for(String faction : ((Ship) s).getFaction()){
					if(faction.equals("Blob")){
						count+=1;
					}
				}
			}
		}

		for(int i = 0; i<count ; i++){
			drawOneCard(current,pp,dp,discp);
		}
		return true;
	}

	protected static boolean mechIsAlly(ArrayList<Ship> blob, ArrayList<Ship> tradeFederation, ArrayList<Ship> machineCult, ArrayList<Ship> starEmpire) {
		blob.add(new Ship("Mech World", new String[]{"Blob"}, 0, 0, 0, 0, "", false, new Couple(), new Couple(), new Couple(), false, new Ensemble[] {}, false, false));
		tradeFederation.add(new Ship("Mech World", new String[]{"Trade Federation"},0,0,0,0,"",false,new Couple(),new Couple(),new Couple(),false, new Ensemble[]{}, false, false));
		starEmpire.add(new Ship("Mech World", new String[]{"Star Empire"},0,0,0,0,"",false,new Couple(),new Couple(),new Couple(),false, new Ensemble[]{}, false, false));
		return true;
	}

	protected static boolean allShipEarn1Damage(int current, ArrayList<CardPlayed> cp,ArrayList<Player> p){
		p.get(current).manageDamage(cp.get(current).size());
		return true;
	}

	protected static boolean nextBuyOnTop(int current, ArrayList<Player> p){
		p.get(current).activatePriorityPurchase();
		return true;
	}

	protected static boolean acquireShipValueXAndPutTop(int value, ShopGeneral shopGeneral, ListGeneral listGeneral,ShopExplorer shopExplorer, ListExplorer listExplorer,int current, ArrayList<Player> p, ArrayList<CardPlayer> dp){
			int fin=listGeneral.size();
			int choix;
			boolean ia = p.get(current).isIa();
			
			if(!listGeneral.isEmpty() || !listExplorer.isEmpty()) {
				if(!ia) {
					System.out.println(Board.printShop(listGeneral, listExplorer));
					choix = Main.chooseNumberBetween(-1, 1, "Sélectionner le shop que vous voulez pour obtenir une carte qui coûte "+value+" ou moins : 0 pour le shop explorer ou de 1 à pour le shopGeneral et pour quitter -1");
				}
				else {
					Random r = new Random();
	        		choix = r.ints(0, 2).findFirst().getAsInt();
				}

				if (choix == 0){
					if(!listExplorer.isEmpty()){
						if(listExplorer.get(0).getPrice() <= value) {
							dp.get(current).add(listExplorer.remove());
							listExplorer.fillShop(shopExplorer);
							return true;
						}
						else {
							if(!ia) {
								System.out.println("La carte choisi est au dessus du prix démande");
							}
						}
					}
					return false;
				} 
				else if(choix == 1){
					
					if(!listGeneral.isEmpty()) {
						if(!ia) {
							System.out.println(Board.printListG(listGeneral));
							choix = Main.chooseNumberBetween(0, (listGeneral.size()-1), "Sélectionner la carte que vous voulez achater (prix max : "+value+") :");
						}
						else {
							Random r = new Random();
			        		choix = r.ints(0, listGeneral.size()).findFirst().getAsInt();
						}
						
						if(listGeneral.get(choix).getPrice() <= value){
							dp.get(current).add(listGeneral.remove(choix));
							listGeneral.fillShop(shopGeneral);
							return true;
						}
						return false;
					}
					return false;
				}
			}
			return false;
	}
	
	protected static boolean scrapGeneral(int valeur, int current, ArrayList<Player> p, ArrayList<CardPlayed> cp, ArrayList<CardPlayer> dp, ArrayList<PickPlayer> pp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE) {
		
		boolean ia = p.get(current).isIa();
		int choix;
		
		if(valeur == 2) {
			if(!ia) {
				System.out.println(Board.printDp(dp.get(current)));
				System.out.println(Board.printPick(pp.get(current)));
				choix = Main.chooseNumberBetween(-1, 2, "Voulez-vous : (0) détruire une carte de votre main ou (1) détruire une carte de votre pioche ? ");
			}
			else {
				Random r = new Random();
        		choix = r.ints(-1, 2).findFirst().getAsInt();
			}
			
			if(choix == 0) {
				return scrapCardHand(ia, dp.get(current));
			}
			else if(choix == 1) {
				return scrapCardPile(ia, pp.get(current));
			}
			return false;
		}
		else if(valeur == 3) {
			if(!ia) {
				System.out.println(Board.printDp(dp.get(current)));
				System.out.println(Board.printPick(pp.get(current)));
				System.out.println(Board.printShop(listG, listE));
				choix = Main.chooseNumberBetween(-1, 3, "Voulez-vous : (0) détruire une carte de votre main ou (1) détruire une carte de votre pioche (2) détruire une carte de le pioche ?");
			}
			else {
				Random r = new Random();
        		choix = r.ints(0, 3).findFirst().getAsInt();
			}
			
			if(choix == 0) {
				return scrapCardHand(ia, dp.get(current));
			}
			else if(choix == 1) {
				return scrapCardPile(ia,  pp.get(current));
			}
			else if(choix == 2) {
				return scrapCardTradeRow(ia, shopGeneral, listG, shopExplorer, listE);
			}
		}
		return false;
	}
	
	protected static boolean scrapCardTradeRow(boolean ia, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE) {
		int choix;
		
		if(!ia) {
			System.out.println(Board.printShop(listG, listE));
			choix = Main.chooseNumberBetween(0, 1, "Voulez-vous détruire une carte du shop explorer (0) ou une carte du shop général (1) :");
		}
		else {
			Random r = new Random();
    		choix = r.ints(0, 2).findFirst().getAsInt();
		}
		
		if (choix == 0) {
			
			if(!listE.isEmpty()){
				listE.remove();
				listE.fillShop(shopExplorer);
				return true;
			}
			if(!ia) {
				System.out.println("La boutique explorer est vide.");
			}
			return false;
		}
		else if(choix == 1) {
			if(!listG.isEmpty()) {
				if(!ia) {
					System.out.println(Board.printListG(listG));
					choix = Main.chooseNumberBetween(0, (listG.size()-1), "Choisissez la carte que vous voulez détruire en entranht sa position :");
				}
				else {
					Random r = new Random();
		    		choix = r.ints(0, listG.size()).findFirst().getAsInt();
				}
				listG.remove(choix);
				listG.fillShop(shopGeneral);				
				return true;
			}
			if(!ia) {
				System.out.println("La boutique générale est vide.");
			}
			return false;
		}
		
		return false;
	}
	
	public static boolean scrapCardHand(boolean ia, CardPlayer dp) {
		int choix;
		
		if(!dp.isEmpty()) {
			if(!ia) {
				System.out.println(Board.printDp(dp));
				choix = Main.chooseNumberBetween(0, (dp.size()-1), "Choisissez une carte que vous voulez détruire en entrant sa position :");
			}
			else {
				Random r = new Random();
	    		choix = r.ints(0, dp.size()).findFirst().getAsInt();
			}
			dp.remove(choix);
			return true;
		}
		if(!ia) {
			System.out.println("Votre deck est vide");
		}
		return false;
	}
	
	protected static boolean scrapCardPile(boolean ia, PickPlayer pp) {
		int choix;
		
		if(!pp.isEmpty()) {
			if(!ia) {
				System.out.println(Board.printPick(pp));
				choix = Main.chooseNumberBetween(0, (pp.size()-1), "Choisissez la carte que vous coulez détruire en entrant sa position :");
			}
			else {
				Random r = new Random();
	    		choix = r.ints(0, pp.size()).findFirst().getAsInt();
			}
			pp.remove(choix);
			return true;
		}
		if(!ia) {
			System.out.println("Votre pioche est vide");
		}
		return false;
	}
	
	protected static boolean targetOpponentDiscardCard(boolean chasseAlhomme, boolean ia, int current, ArrayList<Player> p, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp) {
		int choix = -99;
		boolean verif;
		
		if(!ia) {
			if(chasseAlhomme) {
        		do {
        			System.out.println(Board.printPlayer(p));
                    Board.printMsg("Choisissez un joueur situé à votre droite ou a votre gauche en entrant sa position, il devra se défaussé une carte");
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
                    Board.printMsg("Choisissez un joueur en entrant sa position, il devra se défaussé une carte ");
                    Scanner sc = new Scanner(System.in);
            		verif = sc.hasNextInt();
            		if (verif) {
            			choix = sc.nextInt();
            		}
                }while (!(choix != current && choix >=0 && choix <= p.size()));
        	}
		}
		else {
			if(chasseAlhomme) {
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
            		choix = r.ints(0, (p.size())).findFirst().getAsInt();                        			
        		} while (!(choix != current));
        	}
		}
		
		int choix2;
		if(!dp.get(choix).isEmpty()) {
			if(!p.get(choix).isIa()) {
				System.out.println(Board.printDp(dp.get(choix)));
	        	choix2 = Main.chooseNumberBetween(0, dp.get(choix).size()-1, "Le joueur choisi doit choisir une des cartes à mettre dans sa défausse en entrant sa position :");
			}
			else {
				Random r = new Random();
	        	choix2 = r.ints(0, dp.get(choix).size()).findFirst().getAsInt();
			}
			
			discp.get(choix).add(dp.get(choix).remove(choix2));
		}
		return true;
	}
	
	protected static boolean destroyBase(boolean ia, boolean chasseAlhomme, int current, ArrayList<Player> p, ArrayList<DisclaimerPlayer> discp) {
		int choix = -99;
		boolean verif;
		
        if(!ia) {
        	if(chasseAlhomme) {
        		do {
        			System.out.println(Board.printPlayer(p));
                    Board.printMsg("Choisissez un joueur situé à votre droite ou a votre gauche en entrant sa position dont vous voulez détruire une base ou pour quitter : "+p.size());
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
                    Board.printMsg("Choisissez un joueur dont vous voulez détruire une base en entrant sa position ou pour quitter : "+p.size());
                    Scanner sc = new Scanner(System.in);
            		verif = sc.hasNextInt();
            		if (verif) {
            			choix = sc.nextInt();
            		}
                }while (!(choix != current && choix >=0 && choix <= p.size()));
        	}
        }
        else {
        	if(chasseAlhomme) {
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
            		choix = r.ints(0, (p.size())).findFirst().getAsInt();                        			
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
                choix2 = Main.chooseNumberBetween(0, (p.get(choix).size()-1), "Selectionner une base ennemi à détruire en entrant sa position :");
        	}
        	else {
        		Random r = new Random();
            	choix2 = r.ints(0, p.get(choix).size()).findFirst().getAsInt();
        	}      	
            discp.get(choix).add(p.get(choix).remove(choix2));
        }
        return true;
	}
	
	protected static boolean chooseCard(boolean ia, DisclaimerPlayer discp, CardPlayer dp) {
		int choix = -99;
		
		if(!ia) {
			System.out.println(Board.printDiscp(discp));
			do {
				choix = Main.chooseNumberBetween(0, discp.size()-1, "Choisissez une carte qui a un coût maximum de 5 pour la placer dans votre deck en entrant sa position ou pour quitter -1");
			} while(!(discp.get(choix).getPrice() <= 5 || choix == -1));
        	
		}
		else {
			Random r = new Random();
			do {
        		choix = r.ints(-1, discp.size()).findFirst().getAsInt();    
			} while(!(discp.get(choix).getPrice() <= 5 || choix == -1));
		}
		if(choix == -1) {
			return false;
		}
		dp.add(discp.remove(choix));
		return true; 
	}
	
	protected static boolean discardCard(boolean ia, CardPlayer dp, DisclaimerPlayer discp) {
		int choix;
		
		if(!dp.isEmpty()) {
			if(!ia) {
				System.out.println(Board.printDp(dp));
				choix = Main.chooseNumberBetween(0, dp.size()-1, "Choisissez une carte pour la placer dans votre défausse en entrant sa position :");
			}
			else {
				Random r = new Random();
				if (!dp.isEmpty()) {
					choix = r.ints(0, dp.size()).findFirst().getAsInt(); 
				}
				else {
					choix = -1;
				}   
			}
			if(choix == -1) {
				return false;
			}
			discp.add(dp.remove(choix));
			return true;
		}
		return false;
	}
	
	protected static boolean discard2CardsDraw2Cards(boolean ia, int current, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp) {
		int count = 0;
		for(int i = 0; i<2; i++) {
			if (discardCard(ia, dp.get(current), discp.get(current))) {
				count += 1;
			}
			else {
				break;
			}
		}
		if(count == 0) {
			return false;
		}
		for (int i = 0; i<count; i++) {
			drawOneCard(current, pp, dp, discp);
		}
		return true;
	}
	
	protected static boolean scrap2CardsDraw2Cards(boolean ia, int current, ArrayList<Player> p, ArrayList<CardPlayed> cp, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE) {
		int count = 0;
		for(int i = 0; i<2; i++) {
			if (scrapGeneral(2, current, p, cp, dp, pp, shopGeneral, listG, shopExplorer, listE)) {
				count += 1;
			}
			else {
				break;
			}
		}
		if(count == 0) {
			return false;
		}
		for (int i = 0; i<count; i++) {
			drawOneCard(current, pp, dp, discp);
		}
		return true;
	}
	
	protected static boolean scrapCardsGain2Damages(boolean ia, int current, ArrayList<Player> p, ArrayList<CardPlayed> cp, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE) {
		int count = 0;
		int total = (dp.get(current).size())+(discp.get(current).size());
		int damage;
		
		for(int i = 0; i<total; i++) {
			if (scrapGeneral(2, current, p, cp, dp, pp, shopGeneral, listG, shopExplorer, listE)) {
				count += 1;
			}
			else {
				break;
			}
		}
		if(count == 0) {
			return false;
		}
		damage = count*2;
		p.get(current).manageDamage(damage);
		
		return true;
	}
	
	protected static boolean playStarEmpireGain2Combat(boolean ia, Player p, CardPlayed cp) {
		
		int count = 0;
		int damage;
		
		for (Card c : p.list) {
			for (String fac : ((Ship) c).getFaction()) {
				if(fac.equals("Star Empire")) {
					count += 1;
				}
			}
		}
		
		for (Card c : cp.list) {
			for (String fac : ((Ship) c).getFaction()) {
				if(fac.equals("Star Empire")) {
					count += 1;
				}
			}
		}
		
		if(count == 0) {
			return false;
		}
		
		damage = count*2;
		
		p.manageDamage(damage);
		
		return true;
	}
	
	protected static boolean opponentControlsBaseGain2Combat(int current, ArrayList<Player> p) {
		
		boolean verif = false;
		
		for(int i = 0; i<p.size(); i++) {
			if(i != current) {
				if(p.get(i).size() > 0) {
					verif = true;
					break;
				}
			}
		}
		
		if(verif) {
			p.get(current).manageDamage(2);
		}
		
		return verif;
	}
	
	protected static boolean discardCardIfYouDoDrawCard(boolean ia, int valeur, int current, ArrayList<Player> p, ArrayList<CardPlayed> cp, ArrayList<CardPlayer> dp, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE) {
		
		if(discardCard(ia, dp.get(current), discp.get(current))) {
			drawOneCard(current, pp, dp, discp);
			return true;
		}
		
		return false;
	}
	
	protected static boolean DiscardTwoCard2Trade2Combat(boolean ia, CardPlayer dp, DisclaimerPlayer discp, Player p) {
		
		for(int i = 0; i<2; i++) {
			if(discardCard(ia, dp, discp)) {
				int choix;
				if(!ia) {
					choix = Main.chooseNumberBetween(0, 2, "Choisissez entre (0) gain 2 Trade or (1) gain 2 combat:");
				}
				else {
					Random r = new Random();
		    		choix = r.ints(0, 2).findFirst().getAsInt();
				}
				if(choix == 0) {
					p.manageMoney(2);
				}
				else if(choix == 1) {
					p.manageDamage(2);
				}
			}
		}
		
		return true;
	}
	
	protected static boolean threeBasesPlayGain4Authority(int current, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp) {
		
		if(p.get(current).size() >= 3) {
			p.get(current).manageHeal(4);
		}
		
		drawOneCard(current, pp, dp, discp);
		
		return true;
	}
	
	@Override
	public boolean isHeroe() {
        return false;
    }

	@Override
	public boolean isTrash() {
		return false;
	}
	
	public static Ship toReload(String line){


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

		return new Ship(lineTbl[1], fac,Integer.parseInt(lineTbl[3]),Integer.parseInt(lineTbl[4]),Integer.parseInt(lineTbl[5]),Integer.parseInt(lineTbl[6]),lineTbl[7],Boolean.parseBoolean(lineTbl[8]),Boolean.parseBoolean(lineTbl[9]), new Couple(allydomage[0],Integer.parseInt(allydomage[1])),new Couple(allymoney[0],Integer.parseInt(allymoney[1])),new Couple(allyheal[0],Integer.parseInt(allyheal[1])),Boolean.parseBoolean(lineTbl[13]),allyCapacityEnd,Boolean.parseBoolean(lineTbl[15].toString()),Boolean.parseBoolean(lineTbl[16].toString()));
	}
	/**
	 * méthode permettant de recuillir les information d'un ship
	 */
	public String preSave(){
		ArrayList<String> arrayEnTbl = new ArrayList<>();

		for(Ensemble e : allyCapacity){
				arrayEnTbl.add(e.toSave());
		}
		String[] transfoEnseTbl = new String[arrayEnTbl.size()];
		for(int i=0;i<arrayEnTbl.size();i++){
			transfoEnseTbl[i]=arrayEnTbl.get(i);
		}

		return name+"&"+ Arrays.toString(faction) +"&"+ price+"&"+ damage +"&"+money +"&"+heal +"&"+capacity+"&"+choose+"&"+capacityUsed+"&"+allyDamage.toSave()+"&"+allyMoney.toSave()+"&"+allyHeal.toSave()+"&"+allyChoose+"&"+Arrays.toString(transfoEnseTbl)+"&"+priority+"&"+capacityPriority;

		}

	/**
	 * méthode permettant d'avoir les informations nécéssaire a l'enregistrement d'un ship en format texte
	 */
	public  String toSave(){
		return "Ship&"+preSave();
	}
}
