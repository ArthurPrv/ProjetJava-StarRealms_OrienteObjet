package View;


import java.util.ArrayList;

import Model.CardPlayed;
import Model.CardPlayer;
import Model.DisclaimerPlayer;
import Model.Heroes;
import Model.ListExplorer;
import Model.ListGeneral;
import Model.Mission;
import Model.PickPlayer;
import Model.Player;
import Model.Ship;
import Model.ShopExplorer;
import Model.ShopGeneral;

public class Board {

	//méthode permettant d'afficher le board
	public String toString(ArrayList<Player> p, int current, int tour, ListGeneral listG, ListExplorer listE, CardPlayed cardP, DisclaimerPlayer disP, PickPlayer pickP, CardPlayer deckP, String extend) {
		String text = "\n=================================================================\n"
				+"Tour "+tour+":\n"+
				printEnnemy(p,current)
				+"Shop : Pour accï¿½der au shop tapez 2 (pour l'afficher) ou 3 (pour acheter des cartes)\n"
				+"Base : "+p.get(current).toString()+"\n";
				if(extend.equals("United")) {
					text += "Heroes : "+ printHero(p, current)+"\n";
				}
				text +="Card played : "+cardP.toString()+"\n"
				+"Disclaimer : "+disP.size()+"\n"
				+"Pick : "+pickP.size()+" cards\n"
				+p.get(current).getName()+" Player: "+p.get(current).getLife()+" Hp\n"
				+"Damage : "+p.get(current).getDamage()+"\n"
				+"Money : "+p.get(current).getMoney()+"\n"
				+"Deck : "+deckP.toString()+"\n";
		if(extend.equals("United")) {
			text += "Missions : "+printMission(p, current)+"\n";
		}
		text +="=================================================================\n";
		
		return text;
	}
	
	public static void printMsg(String msg) {
		System.out.println(msg);
	}
	
	//méthode permettant d'afficher les ennemys avec leur base
	private String printEnnemy(ArrayList<Player> p, int Current){
		StringBuilder ennemyPrint = new StringBuilder();
		for(int i=0;i<p.size();i++){
			if(i!=Current){
			ennemyPrint.append(p.get(i).getName()+" Player: "+p.get(i).getLife()+" Hp\n");
			ennemyPrint.append("Base :\n"+p.get(i).toString()+"\n");
			}
		}
		return ennemyPrint.toString();
	}
	
	//méthode permettant d'afficher les missions d'un joueur
	public static String printMission(ArrayList<Player> p, int current) {
		StringBuilder missions = new StringBuilder();
		ArrayList<Mission> m = p.get(current).getMission();
		missions.append("\n+--+-----------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------+\n");
		missions.append("|n°|Name       |Description                                                                                                                                                                                                 |Completed|");
		missions.append("\n+--+-----------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------+\n");
		for (int i = 0; i<m.size(); i++) {
			missions.append("|");
			missions.append(i);
			missions.append(" ");
			missions.append(m.get(i).toString());
			missions.append("\n+--+-----------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+---------+\n");
		}
		return missions.toString();
	}                                                               
	
	//méthode permettant d'afficher les héros d'un joueur
	public static String printHero(ArrayList<Player> p, int current) {
		if(!p.get(current).getHeroes().isEmpty()) {
			StringBuilder heroes = new StringBuilder();
			ArrayList<Heroes> h = p.get(current).getHeroes();
			heroes.append("\n+--+------------------+-----+---------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------------------------------------------------------------------------------------------------------------------------+\n");
			heroes.append("|n°|Name              |Price|Capacity                                                                                                                                                             |Trash Capacity                                                                                                            |");
			heroes.append("\n+--+------------------+-----+---------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------------------------------------------------------------------------------------------------------------------------+\n");
			for (int i = 0; i<h.size(); i++) {
				heroes.append("|");
				heroes.append(i);
				heroes.append(" ");
				heroes.append(h.get(i).toString());
				heroes.append("\n+--+------------------+-----+---------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------------------------------------------------------------------------------------------------------------------------+\n");
			}
			return heroes.toString();
		}
		else {
			return " Vous n'avez pas de carte de type héro en jeu";
		}
	}
	
	//méthode permettant d'afficher le shop
	public static String printShop(ListGeneral listG, ListExplorer listE) {
		return "\n========================  Shop  =================================\n"
				+"Shop : "+listG.toString()+"\n"
				+"Explorer : "+listE.toString()+"\n"
				+"=================================================================\n";
	}
	
	//méthode permettant d'afficher la défausse du player
	public static String printDiscp(DisclaimerPlayer discp) {
		return "\n========================  Disclaimer Player  =================================\n"
				+discp.toString()+"\n"
				+"==============================================================================\n";
	}
	
	//méthode permettant d'afficher la pioche du player
	public static String printPick(PickPlayer pp) {
		return "\n========================  Pick Player  =================================\n"
				+pp.toString()+"\n"
				+"==============================================================================\n";
	}
	
	//méthode permettant d'afficher les cartes du player
	public static String printDp(CardPlayer dp) {
		return "\n========================  Card Player  =================================\n"
				+dp.toString()+"\n"
				+"========================================================================\n";
	}
	
	//méthode permettant d'afficher le shop general
	public static String printListG(ListGeneral listG) {
		return "\n========================  Shop  =================================\n"
				+"Shop : "+listG.toString()+"\n"
				+"=================================================================\n";
	}
	
	//méthode permettant d'afficher le shop explorer
	public static String printListE(ListExplorer listE) {
		return "\n========================  Shop  =================================\n"
				+"Explorer : "+listE.toString()+"\n"
				+"=================================================================\n";
	}
	
	//méthode permettant d'afficher les players avec leur noms et leur vie
	public static String printPlayer(ArrayList<Player> p) {
		int taille = 0;
		int tailleName = 4;
		int tailleLife = 1;
		
		for (int i = 0; i<p.size(); i++) {
			taille = p.get(i).getName().length();
			if(tailleName < taille) {
				tailleName = taille;
			}
		}
		
		StringBuilder player = new StringBuilder();
		player.append("========================  Players  =================================\n");
		player.append("\n+--+"+("-".repeat(tailleName))+"+----+\n");
		player.append("|n°|Name"+(" ".repeat(tailleName-4))+"|Life|");
		player.append("\n+--+"+("-".repeat(tailleName))+"+----+\n");
		for (int i = 0; i<p.size(); i++) {
			player.append("|");
			player.append(i);
			player.append(" ");
			player.append("|");
			player.append(p.get(i).getName()+(" ".repeat(tailleName-p.get(i).getName().length())));
			player.append("|");
			if(p.get(i).getLife() < 100) {
				tailleLife = 2;
				if(p.get(i).getLife() < 10) {
					tailleLife = 3;
				}
			}
			player.append(p.get(i).getLife()+(" ".repeat(tailleLife)));
			player.append("|");
			player.append("\n+--+"+("-".repeat(tailleName))+"+----+\n");
		}
		player.append("=================================================================\n");
		
		return player.toString();
	}
}

