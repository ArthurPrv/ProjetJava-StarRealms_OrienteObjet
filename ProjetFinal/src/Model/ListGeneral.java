package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import Controleur.Main;

public class ListGeneral extends AbstractArrayListToString{
	
	public ListGeneral() {
		super();
	}
	
	
	/**
	 *méthode permettant de remplir le shopGénéral
	 */
	public void fillShop(ShopGeneral shopGeneral) {
		if (size() == 4 && !(shopGeneral.isEmpty())) {
			add(shopGeneral.remove());
		}
	}
	
	/**
	 * méthode permettant d'acquérir une carte dans le shop général et de retourner le prix
	 */
	public int achatGeneral(int courant, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation) {
		boolean ia = p.get(courant).isIa();
		if (!(isEmpty())) {
			if(!ia) {
				System.out.println("Voici le shop Generla que voulez vous acheter? \n\n" + "Shop general: " + toString() + "\n");
			}
			int choixAchat;
			int taille = size() - 1;
			
			if(!ia){
				choixAchat = Main.chooseNumberBetween(0,(taille+1),"Choisir la position de la carte dans la liste pour l'acheter ou pour quitter le shop tapez "+(taille+1)+" :");
			}
			else{
				Random r = new Random();
				choixAchat = r.ints(0, (taille + 1)).findFirst().getAsInt();
			}

			if (choixAchat != (taille+1)) {
				int price = get(choixAchat).getPrice();
				if (price <= p.get(courant).getMoney()) {
					p.get(courant).manageMoney(-price);
					if(get(choixAchat).isHeroe()) {
						Card c = get(choixAchat);
						p.get(courant).getHeroes().add((Heroes) remove(choixAchat));
						dp.get(courant).playShip(courant, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, c);
					}
					else if(p.get(courant).getPriorityPurchase()) {
						dp.get(courant).add(remove(choixAchat));
					}
					else {
						discp.get(courant).add(remove(choixAchat));
					}
					if(!ia) {
						System.out.println("Votre achat a bien été éffectué\n");
					}
					return price;
				} 
				else {
					if(!ia) {
						System.out.println("Vous n'avez pas assez d'argent pour achetre cette carte\n");
					}
					return 0;
				}
			}
			return 0;
		} 
		else {
			if(!ia) {
				System.out.println("Le shop general est vide\n");
			}
			return 0;
		}

	}

	
	/**
	 * méthode retournant le prix le plus bas du Shop
	 */
	public int lowerPrice(){
		ArrayList<Integer> prix = new ArrayList<>();
		if (!this.isEmpty()){
			for (Card s : list) {
				prix.add(s.getPrice());
			}
			return Collections.min(prix);
		}
		return 0;
	}

}
