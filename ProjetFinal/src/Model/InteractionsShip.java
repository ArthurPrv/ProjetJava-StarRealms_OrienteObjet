package Model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import Controleur.Main;
import View.Board;

public class InteractionsShip {
	
	/**
	 * méthode permettant l'achat d'une carte 
	 *
	 */
	public int achat(int current, boolean chasseAlhomme, ListExplorer listE, ListGeneral listG, ShopExplorer shopExplorer, ShopGeneral shopGeneral, ArrayList<DisclaimerPlayer> disc, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<CardPlayed> cp, ArrayList<Player> p, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation) {
		Boolean ia = p.get(current).isIa();
		int price = 0;
		if (p.get(current).getMoney() >= lowerPrice(listE,listG)) {
			if(!ia){
				Board.printMsg("Voici le shop : \n " + "Shop general: " + listG.toString() + "\n" + "Shop d'explorer: "+ listE.toString() + "\n");
			}
			int choixAchat;
			if(!ia){
				choixAchat = Main.chooseNumberBetween(0,2,"Voulez-vous aller dans le shop Explorer : 0 ou dans le shop General : 1 ou voulez vous quitter : 2 ?");
			}
			else{
				Random r = new Random();
				choixAchat = r.ints(0, 2).findFirst().getAsInt();
			}

			if (choixAchat == 0) {
				price = listE.achatExplorer(shopExplorer, disc.get(current), pp.get(current), p.get(current));
				listE.fillShop(shopExplorer);
				return price;
			} 
			else if (choixAchat == 1){
				price =  listG.achatGeneral(current, chasseAlhomme, p, pp, dp, disc, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
				listG.fillShop(shopGeneral);
				return price;
			}
			return 0;
		} 
		else {
			if(!ia) {
				Board.printMsg("Vous n'avez pas assez d'argent pour acheter des cartes\n");
			}
			return 0;
		}
	}

	/**
	 * métode permettant de savoir quelle est le prix le plus bas disponnible dans le shop
	 *
	 */
	public int lowerPrice(ListExplorer listE, ListGeneral listG) {
		int v1=listE.lowerPrice();
		int v2=listG.lowerPrice();
		if(v1==0 && v2==0){
			return 0;
		}
		else if(v1==0){
			return v2;
		}
		else if(v2==0){
			return v1;
		}
		return Math.min(listE.lowerPrice(),listG.lowerPrice());
	}

}
