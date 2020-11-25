package Model;

import java.util.ArrayList;
import java.util.Random;

import Controleur.Main;
import View.Board;

public class CardPlayer extends AbstractArrayListToString {
	
	public CardPlayer() {
		super();
	}
	
		
	public void fillDeck(PickPlayer pp, DisclaimerPlayer discp) {
		int numberCards = 5;
		int sizePick = pp.size();
		
		if (sizePick >= numberCards) {
			for (int i = 0; i < numberCards; i++) {
				list.add(pp.remove());
			}
			if (pp.size() == 0) {
				pp.fillPick(discp);
				discp.clear();
				pp.shuffle();
			}
		}
		else {
			numberCards-= sizePick;
			for (int i = 0; i < sizePick; i++) {
				list.add(pp.remove());
			}
			pp.fillPick(discp);
			discp.clear();
			pp.shuffle();
			
			for (int i = 0; i < numberCards; i++) {
				if(!pp.isEmpty()) {
					list.add(pp.remove());
				}
			}
						
		}
	}
	
	public boolean havePriority() {
		for (Card s : list) {
			if (s.isPriority()) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<String> addCardFaction(ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> tradeFederation, ArrayList<Ship> machineCult, Card ship ) {
		Ship s = (Ship) ship;
		
		String[] faction = s.getFaction();
		ArrayList<String> factionUsed = new ArrayList<>();
		
		for (int i = 0; i < faction.length; i++) {
			if (faction[i].equals("Blob")) {
				blob.add(s);
				factionUsed.add("Blob");
			}
			else if (faction[i].equals("Star Empire")) {
				starEmpire.add(s);
				factionUsed.add("Star Empire");
			}
			else if (faction[i].equals("Trade Federation")) {
				tradeFederation.add(s);
				factionUsed.add("Trade Federation");
			}
			else if (faction[i].equals("Machine Cult")) {
				machineCult.add(s);
				factionUsed.add("Machine Cult");
			}
		}
		return factionUsed;		
	}
	
	protected void addAllyShip(Ship s, String nameFaction, int courant, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation) {
		
		int damage = 0;
		if (nameFaction.equals(s.getAllyDamage().getFaction()) || s.getAllyDamage().getFaction().equals("All")) {
			damage = s.getAllyDamage().getValue();
		}
		
		int heal = 0;
		if (nameFaction.equals(s.getAllyHeal().getFaction()) || s.getAllyHeal().getFaction().equals("All")) {
			heal = s.getAllyHeal().getValue();
		}
		
		int money = 0;
		if (nameFaction.equals(s.getAllyMoney().getFaction()) || s.getAllyMoney().getFaction().equals("All")) {
			money = s.getAllyMoney().getValue();
		}
		
		if (s.getAllyChoose() == true) {
			int choix;
			if (!(p.get(courant).isIa())) {
				ArrayList<Card> listCard = new ArrayList<>();
				listCard.add(s);
				System.out.println(AbstractArrayListToString.toStringCard(listCard));
				
				choix = Main.chooseNumberBetween(0, 2, "Choissez une des deux option (soit 0 ou 1)" );
			}
			
			else {
				Random r = new Random();
				choix = r.ints(0, (2)).findFirst().getAsInt();
			}
			if (choix == 1) { // si le joueur a choisi la premiere option
				if (damage != 0) {
					p.get(courant).manageDamage(damage);
				}
				else if (money != 0) {
					p.get(courant).manageMoney(money);
				}
				else if (heal != 0) {
					p.get(courant).manageHeal(heal);
				}
			}
			else { // si le joueur a choisi la deuxieme option
				int i = 0;
				if (damage != 0) {
					i++;
				}
				else if (money != 0) {
					i++;
					if (i == choix) {
						p.get(courant).manageMoney(money);
					}					
				}
				else if (heal != 0) {
					i++;
					if (i == choix) {
						p.get(courant).manageHeal(heal);
					}
				}
				else if (s.getCapacity() != "") {
					i++;
					if (i == choix) {
						Ship.allyCapacityShip(s, nameFaction, courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, tradeFederation, machineCult, false);
					}
				}
			}
		}
		else {	
			p.get(courant).manageDamage(damage);
			p.get(courant).manageHeal(heal);
			p.get(courant).manageMoney(money);
			Ship.allyCapacityShip(s, nameFaction, courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, tradeFederation, machineCult, false);
		}
	}
		
	private void allyShip(int courant, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, Ship s, ArrayList<Ship> faction, String nameFaction, boolean used) {
		if (faction.size() == 2 && used == false) {			
			addAllyShip(s, nameFaction, courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
			addAllyShip(faction.get(0), nameFaction, courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
		}
		
		else if (faction.size() > 2 && used == false) {
			addAllyShip(s, nameFaction, courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
		}
		
		else if(faction.size() == 2 && used == true) {
			addAllyShip(faction.get(0), nameFaction, courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
		}
	}
	
	public void playShipAlly(int courant, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ArrayList<String> faction, Card ship) {

		Card s = ship;
		boolean used = false;
		
		if(!s.isHeroe()) {
			for (int i = 0; i < faction.size(); i++) {
				if (faction.get(i).equals("Blob")) {
					allyShip(courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, (Ship) s, blob, "Blob", used);
					used = true;
				}
				else if (faction.get(i).equals("Star Empire")) {
					allyShip(courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, (Ship) s, starEmpire, "Star Empire", used);
					used = true;
				}
				else if (faction.get(i).equals("Trade Federation")) {
					allyShip(courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, (Ship) s, tradeFederation, "Trade Federation", used);
					used = true;
				}
				else if (faction.get(i).equals("Machine Cult")) {
					allyShip(courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, (Ship) s, machineCult, "Machine Cult", used);
					used = true;
				}
			}
			
		}
	}
	
	public void playShip(int courant, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, Card ship) {
		
		Card c = ship;
		
		if(!c.isHeroe()) {
			Ship shipPlayed1 = (Ship) c;
			if (shipPlayed1.getChoose() == true) { // permet de gérer le OR
				int choix;

				if (!(p.get(courant).isIa())) {
						
					ArrayList<Card> listCard = new ArrayList<>();
					listCard.add(shipPlayed1);
					System.out.println(AbstractArrayListToString.toStringCard(listCard));
					
					choix = Main.chooseNumberBetween(0, 2, "Choissez une des deux option (soit 0 ou 1)");
				}
				
				else {
					Random r = new Random();
					choix = r.ints(0, (2)).findFirst().getAsInt();
				}
				
				if (choix == 1) { // si le joueur a choisi la premiere option
					if (shipPlayed1.getDamage() != 0) {
						p.get(courant).manageDamage(shipPlayed1.getDamage());
					}
					else if (shipPlayed1.getMoney() != 0) {
						p.get(courant).manageMoney(shipPlayed1.getMoney());
					}
					else if (shipPlayed1.getHeal() != 0) {
						p.get(courant).manageHeal(shipPlayed1.getHeal());
					}
					
				}
				else { // si le joueur a choisi la deuxieme option
					
					int i = 0;
					if (shipPlayed1.getDamage() != 0) {
						i++;
					}
					else if (shipPlayed1.getMoney() != 0) {
						i++;
						if (i == choix) {
							p.get(courant).manageMoney(shipPlayed1.getMoney());
						}					
					}
					else if (shipPlayed1.getHeal() != 0) {
						i++;
						if (i == choix) {
							p.get(courant).manageHeal(shipPlayed1.getHeal());
						}
					}
					else if (shipPlayed1.getCapacity() != "") {
						i++;
						if (i == choix) {
							Ship.capacity(shipPlayed1, courant, chasseAlhomme, p, pp, dp, discp, cp,  shopGeneral, listG, shopExplorer, listE, blob, starEmpire, tradeFederation, machineCult);
						}
					}
				}
			}
			
			else {
				p.get(courant).manageDamage(shipPlayed1.getDamage());
				p.get(courant).manageHeal(shipPlayed1.getHeal());
				p.get(courant).manageMoney(shipPlayed1.getMoney());
				Ship.capacity(shipPlayed1, courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, tradeFederation, machineCult);
			}

			ArrayList<String> faction = addCardFaction(blob, starEmpire, tradeFederation, machineCult, c);
			playShipAlly(courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, faction, c);
		}
		else {
			Heroes.stringToCapacity((Heroes) c, false, p, cp, dp, courant, pp, discp, chasseAlhomme, listG, listE, blob, starEmpire, machineCult, tradeFederation, shopGeneral, shopExplorer, p.get(courant).isIa());	
		}
	}

	private void playAll(int courant, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation) {

		while(!(isEmpty())) {
			Card c = get(0);
			if (c.isBase()) {
				p.get(courant).add((Ship) remove(0));
			}
			else if (c.isHeroe()) {
				p.get(courant).getHeroes().add((Heroes) remove(0));
			}
			else {
				cp.get(courant).add((Ship) remove(0));
			}
			playShip(courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, c);
		}
	}

	public void play(int courant, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation) {

		Boolean ia = p.get(courant).isIa();
		if (!(isEmpty())) {
			int choixPlay;
			int taille = (size()-1);
			
			if (havePriority()) {// Si on a des prioritaires
				if(!ia) {
					System.out.println(toString());
					Board.printMsg("Voici votre jeu, vous avez des carte prioritaire merci de les jouer en première\n");
				}

				do {
					if(!ia) {
						choixPlay = Main.chooseNumberBetween(0, size(), "Choisissez une carte en entrant sa position ou pour quitter : " + size());
					}
					else{
						Random r = new Random();
						choixPlay = r.ints(0, size()).findFirst().getAsInt();
					}
					if(choixPlay == size()) {
						break;
					}
				} while (!(get(choixPlay).isPriority()));
				if (choixPlay != (taille+1)) {
					Card c = get(choixPlay);
					if (c.isBase()) {
						p.get(courant).add((Ship) remove(choixPlay));
					}
					else if (c.isHeroe()) {
						p.get(courant).getHeroes().add((Heroes) remove(choixPlay));
					}
					else {
						cp.get(courant).add((Ship) remove(choixPlay));
					}
					playShip(courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, c);	
				}
			}
			else { // on a pas de prioritaires
				if(!ia) {
					choixPlay = Main.chooseNumberBetween(0, (size()+1), "Choisissez une carte en entrant sa position ou " + size() + " pour jouer toutes les cartes et pour quitter : " + (size() + 1));
				}
				else{
					Random r = new Random();
					choixPlay = r.ints(0, size()).findFirst().getAsInt();
				}
				if (choixPlay != (size()+1)) {
					if (choixPlay == size()) {
						playAll(courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
					}
					else {
						Card c = get(choixPlay);
						if (c.isBase()) {
							p.get(courant).add((Ship) remove(choixPlay));
						}
						else if (c.isHeroe()) {
							p.get(courant).getHeroes().add((Heroes) remove(choixPlay));
						}
						else {
							cp.get(courant).add((Ship) remove(choixPlay));
						}
						playShip(courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, c);
					}
				}
			}
		}
		else {
			if(!ia) {
				Board.printMsg("Vous n'avez plus de carte en main\n");
			}
		}
	}
}
