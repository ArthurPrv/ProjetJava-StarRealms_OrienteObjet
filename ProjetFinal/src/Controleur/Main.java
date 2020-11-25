package Controleur;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import Model.*;
import View.Board;

//classe permettant de créer un objet qui contient les informations d'un fichier de sauvegarde
class Info{
	public final int tour;
	public final boolean chasseAlhomme;
	public final String scenario;
	public final String extend;

	public Info(int tour, boolean chasseAlhomme, String scenario, String extend) {
		this.tour = tour;
		this.chasseAlhomme = chasseAlhomme;
		this.scenario = scenario;
		this.extend = extend;
	}
}


public class Main {
	
	//Cette méthode permet de return une array list qui contient toutes les cartes contenue dans la chaine de caractere entrée en paramètre
	public static ArrayList<Object> toReload(String line){
		ArrayList<Object> arrayList = new ArrayList<>();
		for(String s : line.split("~")){
			String[] firstWordGetter = s.split("&");
			if(firstWordGetter[0].equals("Ship")){
				arrayList.add(Ship.toReload(s));
			}else if(firstWordGetter[0].equals("TrashShip")){
				arrayList.add(TrashShip.toReload(s));

			}else if(firstWordGetter[0].equals("Base")){
				arrayList.add(Base.toReload(s));

			}else if(firstWordGetter[0].equals("Mission")){
				arrayList.add(Mission.toReload(s));

			}else if(firstWordGetter[0].equals("Heroe")){
				arrayList.add(Heroes.toReload(s));
			}

		}
		return  arrayList;
	}
	
	//Cette méthode pmermet de parcourir tout le fichier sauvegarde afin de recréer d'appeler les méthodes qu'il faut pour recréer les cartes, les missions, les héros et les autres informations utiles pour pouvoir continuer la partie
	
	public static Info launchWithASave(String src, ArrayList<Ship> blob, ArrayList<Ship> tradeF, ArrayList<Ship> machineC, ArrayList<Ship> starE, ShopGeneral shopGeneral, ListGeneral listGeneral, ShopExplorer shopExplorer, ListExplorer listExplorer, ArrayList<Player> players, ArrayList<CardPlayer> cardPlayers, ArrayList<DisclaimerPlayer> disclaimerPlayers, ArrayList<CardPlayed> cardPlayeds, ArrayList<PickPlayer> pickPlayers) {
		Path source = Path.of(src);
		try (BufferedReader reader = Files.newBufferedReader(source)) {
			//on déclare toutes les variables pour parcourir le fichier
			int playerNumber = -1;
			int countLine = 1 ;
			int countForPlayer=0;
			String line;
			
			//variables contenu dans le main
			int tour=0;
			boolean chasseAlhomme=false;
			String extension="";
			String scenario="";
			
			//variable pour la classe player
			String name ="";
			int life=0;
			int damage=0;
			int money=0;
			boolean ia=false;
			int heal=0;
			boolean priorityPurchase=false;
			boolean completedMission=false;
			final ArrayList<Mission> missions = new ArrayList<>();
			final ArrayList<Heroes> heroes  = new ArrayList<>();
			final ArrayList<Base> listBase = new ArrayList<>();
			
			// cette boucle va parcourir tout le fichier
			while ((line = reader.readLine()) != null) {
				
				//chaque joueur est séparé par la chaine de carctere ##########
				//chaque informations que contient un joeur est mis sur une ligne différente
				//Donc on peut connaitre a quoi correspond chaque ligne et donc appeler la bonne méthode pour pouvoir traiter l'information
				//afin de pourvoir appeler le constructeur joueur pour lui dfonner toutes les onformatios nécessaires

				if(playerNumber!=-1){
					countForPlayer+=1;

					if ((line.toString()).equals("##########")) {
						missions.clear();
						heroes.clear();
						listBase.clear();
						playerNumber += 1;
						countForPlayer=0;
					} else if(countForPlayer==1){
						name=line.toString();

					}else if(countForPlayer==2){
						life=Integer.parseInt(line.toString());
					}else if(countForPlayer==3){
						damage=Integer.parseInt(line.toString());
					}else if(countForPlayer==4){
						money=Integer.parseInt(line.toString());
					}else if(countForPlayer==5){
						ia=Boolean.parseBoolean(line.toString());
					}else if(countForPlayer==6){
						heal=Integer.parseInt(line.toString());
					}else if(countForPlayer==7){
						priorityPurchase=Boolean.parseBoolean(line.toString());
					}else if(countForPlayer==8){
						completedMission=Boolean.parseBoolean(line.toString());
					}else if(countForPlayer==9){
						if(extension.equals("United")) {
							for(String s : line.toString().split("~")){
								missions.add(Mission.toReload(s));
							}
						}
					}else if(countForPlayer==10){
						if(!line.toString().equals("")){
							for(String s : line.toString().split("~")){
								listBase.add(Base.toReload(s));
							}
						}

					}else if(countForPlayer==11){
						Player temp;
						
						if(!line.toString().equals("")) {
							heroes.clear();
							for (String s : line.toString().split("~")) {
								heroes.add(Heroes.toReload(s));
							}
							temp = new Player(name,life,damage,money,ia,heal,priorityPurchase,completedMission,missions, (ArrayList<Heroes>) heroes.clone());
						}else{
							temp = new Player(name,life,damage,money,ia,heal,priorityPurchase,completedMission,missions,new ArrayList<Heroes>());
						}
						for(Card b : listBase){
							temp.add((Ship) b);
						}
						players.add(temp);
					}else if(countForPlayer==12){
						if(!line.toString().equals("")) {
							CardPlayed cardPlayed = new CardPlayed();
							cardPlayed.toReload(line);
							cardPlayeds.add(cardPlayed);

						}else{
							cardPlayeds.add(new CardPlayed());
						}
					}else if(countForPlayer==13){
						if(!line.toString().equals("")) {
							CardPlayer cardPlayer = new CardPlayer();
							cardPlayer.toReload(line);
							cardPlayers.add(cardPlayer);

						}else{
							cardPlayers.add(new CardPlayer());
						}
					}else if(countForPlayer==14){
						if(!line.toString().equals("")) {
							DisclaimerPlayer disclaimerPlayer = new DisclaimerPlayer();
							disclaimerPlayer.toReload(line);
							disclaimerPlayers.add(disclaimerPlayer);
						}else{
							disclaimerPlayers.add(new DisclaimerPlayer());
						}
					}else if(countForPlayer==15){
						if(!line.toString().equals("")) {
							PickPlayer pickPlayer = new PickPlayer();
							pickPlayer.toReload(line);
							pickPlayers.add(pickPlayer);
						}else{
							pickPlayers.add(new PickPlayer());
						}

					}


				
				//permet de récupérer les informations qui ne sont pasd liées aux joueurs comme les shops, le numéros du tour ...
				}else {
					if ((line.toString()).equals("##########")) {
						playerNumber += 1;
						countForPlayer=0;
					} else if (countLine == 1) {
						tour = Integer.parseInt(line.toString());
					} else if (countLine == 2) {
						chasseAlhomme = Boolean.parseBoolean(line.toString());
					} else if (countLine == 3) {
						ArrayList<Object> shipArrayList = toReload(line.toString());
						for(Object s : shipArrayList){
							blob.add((Ship) s);
						}
					} else if (countLine == 4) {
						ArrayList<Object> shipArrayList = toReload(line.toString());
						for(Object s : shipArrayList){
							tradeF.add((Ship) s);
						}
					} else if (countLine == 5) {
						ArrayList<Object> shipArrayList = toReload(line.toString());
						for(Object s : shipArrayList){
							machineC.add((Ship) s);
						}
					} else if (countLine == 6) {
						ArrayList<Object> shipArrayList = toReload(line.toString());
						for(Object s : shipArrayList){
							starE.add((Ship) s);
						}
					} else if (countLine == 7) {
						scenario = line.toString();
					} else if (countLine == 8) {
						extension = line.toString();
					} else if (countLine == 9) {
						shopGeneral.toReload(line);
					} else if (countLine == 10) {
						listGeneral.toReload(line);
					} else if (countLine == 11) {
						shopExplorer.toReload(line);
					} else if (countLine == 12) {
						listExplorer.toReload(line);
					}
				}

			countLine++;
			}
			return new Info(tour,chasseAlhomme,scenario,extension);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//méthode qui demande de saisir un chiffre selon un minimum et un maximum en affichant un message pour orienter l'utilisateur
	public static int chooseNumberBetween(int min,int max,String message){
		int choix = -100;
		boolean verif;
		do{
			Board.printMsg(message+"\n");
			Scanner sc = new Scanner(System.in);
			verif = sc.hasNextInt();
			if (verif) {
				choix = sc.nextInt();
			}
			
		}while(!(choix>=min && choix<=max));
		
		return choix;
	}
	
	//méthode permettant à l'utilisateur de choisir une extension
	private static String choiceExt() {
		String[] extension = {"Sans extension", "United", "Colony Wars", "scenario"};
		
		StringBuilder text = new StringBuilder();
		text.append("Vous de vez choisir si vous voulez jouer avec ou sans extension :\n");
		for (int i = 0; i < extension.length; i++) {
			text.append("- ");
			text.append(i);
			text.append(" : ");
			text.append(extension[i]);
			if(i != (extension.length-1)) {
				text.append("\n");
			}
		}
		
		return extension[Main.chooseNumberBetween(0, 3, text.toString())];
	}
	
	//méthode permettant de mélanger une liste
	public static void shuffle(AbstractArrayListShuffle list) {
		list.shuffle();
	}
	
	//méthode pour ajouter dans les shops les cartes du cor set
	public static void starter(ShopGeneral shopGeneral, ShopExplorer shopExplorer, String scenario) {
		
		for (int i = 0; i < 1; i++) {
			shopGeneral.add(new TrashShip("Battle Blob", new String[]{"Blob"},6,8,0,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card","Blob")},false,false, 4, 0, 0, ""));
			shopGeneral.add(new Ship("Blob Carrier", new String[]{"Blob"},6,7,0,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Acquire any ship without paying its cost and put it on top of your deck","Blob")},false,false));
			shopGeneral.add(new Base("Blob World", new String[]{"Blob"},8,5,0,0,"Draw a card for each Blob card that you've played this turn",true, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", false, 7));
			shopGeneral.add(new Ship("Mothership", new String[]{"Blob"},7,6,0,0,"Draw a card",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card","Blob")},false,false));
			shopGeneral.add(new Base("The Hive", new String[]{"Blob"},5,3,0,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Draw a card","Blob")},false,false, 0, 0, 0, "", false, 5));
			shopGeneral.add(new Ship("Battle Mech", new String[]{"Machine Cult"},5,4,0,0,"Scrap a card in your hand or discard pile",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card","Machine Cult")},true,false));
			shopGeneral.add(new Base("Brain World", new String[]{"Machine Cult"},8,0,0,0,"Scrap up to two cards from your hand or discard pile. Draw a card for each card scrapped this way",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},true,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Base("Junkyard", new String[]{"Machine Cult"},6,0,0,0,"Scrap a card in your hand or discard pile",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},true,false, 0, 0, 0, "", true, 5));
			shopGeneral.add(new Base("Machine Base", new String[]{"Machine Cult"},7,0,0,0,"Draw a card then scrap a card from your hand",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},true,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Base("Mech World", new String[]{"Machine Cult"},5,0,0,0,"Mech World counts as an ally for all factions",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Ship("Missile Mech", new String[]{"Machine Cult"},6,6,0,0,"Destroy target base",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card", "Machine Cult")},false,false));
			shopGeneral.add(new Ship("Strealth Needle", new String[]{"Machine Cult"},4,0,0,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new TrashShip("Battlecruiser", new String[]{"Star Empire"},6,5,0,0,"Draw a card",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Target opponent discards a card","Star Empire")},false,false, 0, 0, 0, "Draw a card and destroy target base"));
			shopGeneral.add(new TrashShip("Dreadnaught", new String[]{"Star Empire"},7,7,0,0,"Draw a card",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 5, 0, 0, ""));
			shopGeneral.add(new Base("Fleet HQ", new String[]{"Star Empire"},8,0,0,0,"All of your ships get Add 1 Combat",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", false, 8));
			shopGeneral.add(new Base("Royal Reboubt", new String[]{"Star Empire"},6,3,0,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Target opponent discards a card","Star Empire")},false,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Base("War World", new String[]{"Star Empire"},5,3,0,0,"",false, new Couple("Star Empire", 4),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 4));
			shopGeneral.add(new Ship("Command Ship", new String[]{"Trade Federation"},8,5,0,4,"Draw two cards",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Destroy target base","Trade Federation")},false,false));
			shopGeneral.add(new Base("Defense Center", new String[]{"Trade Federation"},5,2,3,0,"",true, new Couple("Trade Federation", 2),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 5));
			shopGeneral.add(new Base("Port of Call", new String[]{"Trade Federation"},6,0,3,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "Draw a card and destroy target base", true, 6));
			shopGeneral.add(new Ship("Trade Escort", new String[]{"Trade Federation"},5,4,0,4,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card","Trade Federation")},false,false));
			shopGeneral.add(new Ship("Flagship", new String[]{"Trade Federation"},6,5,0,0,"Draw a card",false, new Couple(),new Couple(),new Couple("Trade Federation", 5),false,new Ensemble[]{},false,false));
		}

		for (int i = 0; i < 2; i++) {
			shopGeneral.add(new Ship("Battle Pod", new String[]{"Blob"},2,4,0,0,"Scrap a card in the trade row",false, new Couple("Blob", 2),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Ship("Blob Destroyer", new String[]{"Blob"},4,6,0,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Destroy target base or scrap a card in the trade row","Blob")},false,false));
			shopGeneral.add(new TrashShip("Ram", new String[]{"Blob"},3,5,0,0,"",false, new Couple("Blob", 2),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 0, 3, 0, ""));
			shopGeneral.add(new Base("Battle Station", new String[]{"Machine Cult"},3,0,0,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 5, 0, 0, "", true, 5));
			shopGeneral.add(new Ship("Patrol Mech", new String[]{"Machine Cult"},4,5,3,0,"",true, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Scrap a card in your hand or discard pile","Machine Cult")},false,false));
			shopGeneral.add(new Ship("Corvette", new String[]{"Star Empire"},2,1,0,0,"Draw a card",false, new Couple("Star Empire", 2),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Base("Recycling Station", new String[]{"Star Empire"},4,0,1,0,"Discard up to two cards then draw that many cards",true, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 4));
			shopGeneral.add(new Base("Space Station", new String[]{"Star Empire"},4,2,0,0,"",false, new Couple("Star Empire", 2),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 4, 0, "", true, 4));
			shopGeneral.add(new Base("Barter World", new String[]{"Trade Federation"},4,0,2,2,"",true, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 5, 0, 0, "", true, 4));
			shopGeneral.add(new Base("Central Office", new String[]{"Trade Federation"},7,0,2,0,"Put the next ship you acquire this turn on top of your deck",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Draw a card","Trade Federation")},false,false, 0, 0, 0, "", false, 6));
			shopGeneral.add(new Base("Trading Post", new String[]{"Trade Federation"},3,0,1,1,"",true, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 3, 0, 0, "", true, 4));
			shopGeneral.add(new Ship("Embassy Yacht", new String[]{"Trade Federation"},3,0,2,3,"If you have two or more bases in play draw two cards",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Ship("Freighter", new String[]{"Trade Federation"},4,0,4,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Put the next ship you acquire this turn on top of your deck","Trade Federation")},false,false));
		}

		for (int i = 0; i < 3; i++) {
			shopGeneral.add(new Ship("Blob Fighter", new String[]{"Blob"},1,3,0,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a acrd","Blob")},false,false));
			shopGeneral.add(new Base("Blob Wheel", new String[]{"Blob"},3,1,0,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 3, 0, "", false, 5));
			shopGeneral.add(new Ship("Trade Pod", new String[]{"Blob"},2,0,3,0,"",false, new Couple("Blob", 2),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Ship("Missile Bot", new String[]{"Machine Cult"},2,2,0,0,"Scrap a card in your hand or discard pile",false, new Couple("Machine Cult", 2),new Couple(),new Couple(),false,new Ensemble[]{},true,false));
			shopGeneral.add(new Ship("Supply Bot", new String[]{"Machine Cult"},3,2,0,0,"Scrap a card in your hand or discard pile",false, new Couple("Machine Cult", 2),new Couple(),new Couple(),false,new Ensemble[]{},true,false));
			shopGeneral.add(new Ship("Trade Bot", new String[]{"Machine Cult"},1,1,0,0,"Scrap a card in your hand or discard pile",false, new Couple("Machine Cult", 2),new Couple(),new Couple(),false,new Ensemble[]{},true,false));
			shopGeneral.add(new Ship("Imperail Fighter", new String[]{"Star Empire"},1,2,0,0,"Target opponent discards a card",false, new Couple("Star Empire", 2),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new TrashShip("Imperial Frigate", new String[]{"Star Empire"},3,4,0,0,"Target opponent discards a card",false, new Couple("Star Empire", 2),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 0, 0, 0, "Draw a card"));
			shopGeneral.add(new TrashShip("Survey Ship", new String[]{"Star Empire"},3,0,1,0,"Draw a card",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 0, 0, 0, "Target opponent discards a card"));
			shopGeneral.add(new Ship("Cutter", new String[]{"Trade Federation"},2,0,2,4,"",false, new Couple("Trade Federation", 4),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Ship("Federation Shuttle", new String[]{"Trade Federation"},1,0,2,0,"",false, new Couple(),new Couple(),new Couple("Trade Federation", 4),false,new Ensemble[]{},false,false));
		}

		for (int i = 0; i < 10; i++) {
			if(scenario.equals("Commitment to the Cause")) {
				shopExplorer.add(new TrashShip("Explorer", new String[]{""},2,0,3,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 2, 0, 0, ""));
			}
			else {
				shopExplorer.add(new TrashShip("Explorer", new String[]{""},2,0,2,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 2, 0, 0, ""));
			}
		}
	}
	
	//méthode pour ajouter dans les shops les cartes de l'extension United
	public static void starterUnited(ShopGeneral shopGeneral, ArrayList<Mission> missions) {

		for (int i = 0; i < 1; i++) {
			shopGeneral.add(new Base("Union Stronghold", new String[]{"Blob", "Star Empire"},5,3,0,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Scrap a card in the trade row","Blob"), new Ensemble("Target opponent discards a card","Star Empire")},false,false, 0, 0, 0, "", false, 5));
			shopGeneral.add(new Base("Lookout Post", new String[]{"Machine Cult", "Trade Federation"},7,0,0,0,"Draw a card",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Base("Embassy Base", new String[]{"Star Empire", "Trade Federation"},8,0,0,0,"Draw two cards then discard a card",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", false, 6));
			shopGeneral.add(new Base("Exchange Point", new String[]{"Machine Cult", "Blob"},6,2,0,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Scrap a card in your hand your discard pile or the trade row","All")},false,false, 0, 0, 0, "", false, 7));
			shopGeneral.add(new Base("Alliance Landing", new String[]{"Star Empire", "Trade Federation"},5,0,2,0,"",false, new Couple("All", 2),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 5));
			shopGeneral.add(new Base("Coalition", new String[]{"Machine Cult", "Trade Federation"},6,0,2,0,"",false, new Couple("All", 2),new Couple(),new Couple("All", 3),true, new Ensemble[]{},false,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Base("Union Cluster", new String[]{"Blob", "Star Empire"},8,4,0,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Draw a card","All")},false,false, 0, 0, 0, "", false, 8));
			shopGeneral.add(new Base("Unity Station", new String[]{"Machine Cult", "Blob"},7,0,0,0,"Scrap a card in your hand or discard pile. You may scrap a card in the trade row",false, new Couple("All", 4),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 6));
		
			missions.add(new Mission("Ally", "Objective: Use Ally abilities from two different factions in the same turn. Reward: Acquire a ship or base of cost four or less for free and put it on top of your deck."));
			missions.add(new Mission("Armada", "Objective: Play seven or more ships in the same turn. Reward: Draw a card. Acquire an explorer for free and put it in your hand."));
			missions.add(new Mission("Colonize", "Objective: Have two or more bases of the same faction in play. Reward: Draw two cards."));
			missions.add(new Mission("Convert", "Objective: Play a Machine Cult ship while you have a Machine Cult base in play. Reward: Reveal the top three cards of your deck. Put one in your hand, one in you discard pile, and one on top of your deck."));
			missions.add(new Mission("Defend", "Objective: Have two or more outposts in play. Reward: Draw a card. Return target base to it's controller's hand."));
			missions.add(new Mission("Diversify", "Objective: In a single turn, gain: 4 Trade and 5 Combat and 3 Authority. Reward: Add 4 Trade or 5 Combat or 6 Authority."));
			missions.add(new Mission("Dominate", "Objective: Play a Star Empire ship while you have a Star Empire base in play. Reward: Add 3 Combat. Draw a card."));
			missions.add(new Mission("Exterminate", "Objective: Play a Blob ship while you have a Blob base in play. Reward: Add 3 Combat. Scrap any number of cards currently in the trade row."));
			missions.add(new Mission("Influence", "Objective: Have at least three ships and/or bases of the same faction in play. Reward: Acquire two explorers for free and put them both into your hand."));
			missions.add(new Mission("Monopolize", "Objective: Play a Trade Federation ship while you have a Trade Federation base in play. Reward: Add 10 Authority."));
			missions.add(new Mission("Rule", "Objective: Have bases from two or more factions in play. Reward: Acquire a card of cost three or less for free and put it in your hand."));
			missions.add(new Mission("Unite", "Objective: Play three ships from different factions in the same turn. Reward: Add 5 Authority. Draw a card."));
			
			
			shopGeneral.add(new Heroes("CEO Shaner", 5, "Until end of turn, you may use all of your Trade Federation Ally abilities. You may acquire a ship or base of cost 3 or less for free and put it on top of your deck.","Until end of turn, you may use all of your Trade Federation Ally abilities. Draw a card."));
			shopGeneral.add(new Heroes("Commodore Zhang", 5, "Until end of turn, you may use all of your Star Empire Ally abilities. Add 4 Combat. Target opponent discards a card.", "Until end of turn, you may use all of your Star Empire Ally abilities. Draw a card."));
			shopGeneral.add(new Heroes("Confessor Morris", 5, "Until end of turn, you may use all of your Machine Cult Ally abilities. You may scrap up to two cards in your hand and/or discard pile.", "Until end of turn, you may use all of your Machine Cult Ally abilities. Draw a card."));
			shopGeneral.add(new Heroes("Hive Lord", 5, "Until end of turn, you may use all of your Blob Ally abilities. Add 5 Combat. Scrap any number of cards currently in the trade row.", "Until end of turn, you may use all of your Blob Ally abilities. Draw a card."));
		}

		for (int i = 0; i < 2; i++) {
			shopGeneral.add(new TrashShip("Trade Star", new String[]{"Star Empire", "Blob"},1,0,2,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 2, 0, 0, ""));
			shopGeneral.add(new Ship("Alliance Transport", new String[]{"Star Empire", "Trade Federation"},2,0,2,0,"",false, new Couple(),new Couple(),new Couple("Trade Federation", 4),false, new Ensemble[]{new Ensemble("Target opponent discards a card","Star Empire")},false,false));
			shopGeneral.add(new Ship("Blob Bot", new String[]{"Machine Cult", "Blob"},3,5,0,0,"",false, new Couple(),new Couple("Blob", 2),new Couple(),false, new Ensemble[]{new Ensemble("Scrap a card in your hand or discard pile","Machine Cult")},false,false));
			shopGeneral.add(new Ship("Coalition Messenger", new String[]{"Machine Cult", "Trade Federation"},3,0,2,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Choose a card of cost five or less in your discard pile and put it on top of your deck","All")},false,false));
			shopGeneral.add(new Ship("Alloiance Frigate", new String[]{"Star Empire", "Trade Federation"},3,4,0,0,"",false, new Couple("Star Empire", 3),new Couple(),new Couple("Trade Federation", 5),false, new Ensemble[]{},false,false));
			shopGeneral.add(new Ship("Assault Pod", new String[]{"Star Empire", "Blob"},2,3,0,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Draw a card","All")},false,false));
			shopGeneral.add(new Ship("Coalition Freighter", new String[]{"Machine Cult", "Trade Federation"},4,0,3,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Put the next ship you acquire this turn on top of your deck","Trade Federation"), new Ensemble("Scrap a card in your hand or discard pile","Machine Cult")},false,false));
			shopGeneral.add(new TrashShip("Unity Fighter", new String[]{"Machine Cult", "Blob"},1,3,0,0,"Scrap a card in the trade row",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "Scrap a card in your hand or discard pile"));
			
			shopGeneral.add(new Heroes("Chairman Haygan", 3, "Until end of turn, you may use all of your Trade Federation Ally abilities. Add 4 Authority", "Until end of turn, you may use all of your Trade Federation Ally abilities. Add 4 Authority"));
			shopGeneral.add(new Heroes("Chancellor Hartman", 4, "Until end of turn, you may use all of your Machine Cult Ally abilities. You may scrap a card in your hand or discard pile.", "Until end of turn, you may use all of your Machine Cult Ally abilities. You may scrap a card in your hand or discard pile."));
			shopGeneral.add(new Heroes("Commander Klik", 4, "Until end of turn, you may use all of your Star Empire Ally abilities. You may discard a card. If you do, draw a card.", "Until end of turn, you may use all of your Star Empire Ally abilities. You may discard a card. If you do, draw a card."));
			shopGeneral.add(new Heroes("Sreecher", 3, "Until end of turn, you may use all of your Blob Ally abilities. Add 2 Combat. You may scrap a card in the trade row.", "Until end of turn, you may use all of your Blob Ally abilities. Add 2 Combat. You may scrap a card in the trade row."));
		}
	}
	
	//méthode pour ajouter dans les shops les cartes de l'extension Colony Wars
	public static void starterColonyWars(ShopGeneral shopGeneral, ShopExplorer shopExplorer) {
		
		for (int i = 0; i < 1; i++) {
			shopGeneral.add(new Ship("Leviathan", new String[]{"Blob"},8,9,0,0,"Draw a card. Destroy target base",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Acquire a card of cost three or less for free and put it into your hand","Blob")},false,false));
			shopGeneral.add(new Ship("Parasite", new String[]{"Blob"},5,6,0,0,"Acquire a card of cost six or less for free and put it into your hand",true, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card","Blob")},false,false));
			shopGeneral.add(new Ship("Moonwurn", new String[]{"Blob"},7,8,0,0,"Draw a card",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Acquire a card of cost two or less for free and put it into your hand","Blob")},false,false));
			shopGeneral.add(new Base("Plasma Vent", new String[]{"Blob"},6,4,0,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 3, 0, "", false, 4));
			shopGeneral.add(new Base("Frontier Station",new String[]{"Machine Cult"},6,3,2,0,"",true, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Ship("Mech Cruiser",new String[]{"Machine Cult"},5,6,0,0,"Scrap a card in your hand or discard pile",false,new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Destroy target base","Machine Cult")},false,false));
			shopGeneral.add(new Ship("Peacekeeper",new String[]{"Trade Federation"},6,6,0,6,"",false,new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card", "Trade Federation")},false,false));
			shopGeneral.add(new Base("Factory World",new String[]{"Trade Federation"},8,0,3,0,"Put the next ship you acquire this turn on top of your deck",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Base("Federation Shipyard",new String[]{"Trade Federation"},6,0,2,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Put the next ship you acquire this turn on top of your deck", "Trade Federation")},false,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Base("Loyal Colony",new String[]{"Trade Federation"},7,3,3,3,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", false, 6));
			shopGeneral.add(new Base("The Incinerator",new String[]{"Machine Cult"},8,0,0,0,"Scrap two cards in your hand or discard pile",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Gain 2 Combat for each card scrapped from your hand or discard pile this turn", "Machine Cult")},false,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Base("The Oracle",new String[]{"Machine Cult"},4,0,0,0,"Scrap a card in your hand",false, new Couple("Machine Cult", 3),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 5));
			shopGeneral.add(new Ship("The Wrecker",new String[]{"Machine Cult"},7,6,0,0,"Scrap two cards in your hand or discard pile",false,new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card", "Machine Cult")},false,false));
			shopGeneral.add(new TrashShip("Aging Battleship",new String[]{"Star Empire"},5,5,0,0,"",false,new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a crad", "Star Empire")},false,false,2,0,0,"Draw two cards"));
			shopGeneral.add(new TrashShip("Heavy Cruiser",new String[]{"Star Empire"},5,4,0,0,"Draw a card",false,new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card", "Star Empire")},false,false,0,0,0,""));
			shopGeneral.add(new Base("Imperial Palace",new String[]{"Star Empire"},4,0,0,0,"Draw a card. Target opponent discards a card",false, new Couple("Star Empire", 4),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 6));
			shopGeneral.add(new Base("Supply Depot", new String[]{"Star Empire"},6,4,0,0,"Discard two cards. Gain 2 Trade or 2 Combat for each card discarded",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Draw a card","Star Empire")},false,false, 0, 3, 0, "", true, 5));
		}

		for (int i = 0; i < 2; i++) {
			shopGeneral.add(new Ship("Ravager",new String[]{"Blob"},3,6,0,0,"Scrap up to two cards in the trade row",false,new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Base("Bioformer", new String[]{"Blob"},4,3,0,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 3, 0, "", false, 4));
			shopGeneral.add(new Ship("Mining Mech",new String[]{"Machine Cult"},4,3,0,0,"Scrap a card in your hand or discard pile",false,new Couple("Machine Culte",3),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Base("Storage Silo", new String[]{"Trade Federation"},2,0,0,2,"",false, new Couple(),new Couple("Trade Federation",2),new Couple(),false, new Ensemble[]{},false,false, 0, 3, 0, "", false, 3));
			shopGeneral.add(new TrashShip("Frontier Ferry",new String[]{"Trade Federation"},4,0,3,4,"",false,new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false,0,0,0,"Destroy target base"));
			shopGeneral.add(new Base("Command Center", new String[]{"Star Empire"},4,0,2,0,"Whenever you play a Star Empire ship gain 2 Combat",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", true, 4));
			shopGeneral.add(new TrashShip("Falcon",new String[]{"Star Empire"},3,2,0,0,"Draw a card",false,new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false,0,0,0,"Target opponent discards a card"));
			shopGeneral.add(new TrashShip("Gunship",new String[]{"Star Empire"},4,5,0,0,"Target opponent discards a card",false,new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false,0,4,0,""));
			shopGeneral.add(new Base("Central Station", new String[]{"Trade Federation"},4,2,0,0,"If you have three or more bases in play (including this one) gain 4 Authority and draw a card",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 0, 0, 0, "", false, 5));
		}

		for (int i = 0; i < 3; i++) {
			shopGeneral.add(new Ship("Predator",new String[]{"Blob"},2,4,0,0,"",false,new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card", "Blob")},false,false));
			shopGeneral.add(new TrashShip("Cargo Pod", new String[]{"Blob"},3,0,3,0,"",false, new Couple("Blob", 3),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 3, 0, 0, ""));
			shopGeneral.add(new Base("Stellar Reef",new String[]{"Blob"},2,0,1,0,"",false, new Couple(),new Couple(),new Couple(),false, new Ensemble[]{},false,false, 3, 0, 0, "", false, 3));
			shopGeneral.add(new Ship("Swarmer",new String[]{"Blob"},1,3,0,0,"Scrap a card in the trade row",false,new Couple("Blob",2),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Ship("Battle Bot",new String[]{"Machine Cult"},1,2,0,0,"Scrap a card in your hand",false,new Couple("Blob",2),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Ship("Convoy Bot",new String[]{"Machine Cult"},3,4,0,0,"Scrap a card in your hand or discard pile",false,new Couple("Blob",2),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
			shopGeneral.add(new TrashShip("Repair Bot", new String[]{"Machine Cult"},2,0,2,0,"Scrap a card in your hand or discard pile",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 2, 0, 0, ""));
			shopGeneral.add(new Ship("Trade Hauler", new String[]{"Trade Federation"},2,0,3,0,"",false, new Couple(),new Couple(),new Couple("Trade Federation", 3),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Ship("Solar Skiff", new String[]{"Trade Federation"},1,0,2,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Draw a card", "Trade Federation")},false,false));
			shopGeneral.add(new Ship("Patrol Cutter", new String[]{"Trade Federation"},3,3,2,0,"",false, new Couple(),new Couple(),new Couple("Trade Federation", 4),false,new Ensemble[]{},false,false));
			shopGeneral.add(new Ship("Star Barge", new String[]{"Star Empire"},1,0,2,0,"",false, new Couple("Star Empire",2),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Target opponent discards a card","Star Empire")},false,false));
			shopGeneral.add(new Ship("Lancer", new String[]{"Star Empire"},2,4,0,0,"If an opponent controls a base gain 2 Combat",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{new Ensemble("Target opponent discards a card", "Star Empire")},false,false));
			shopGeneral.add(new Base("Orbital Platform",new String[]{"Star Empire"},3,0,0,0,"",false, new Couple("Star Empire", 3),new Couple(),new Couple(),false, new Ensemble[]{new Ensemble("Discard a card. If you do draw a card","Star Empire")},false,false, 0, 0, 0, "", false, 4));
		}
		
		for (int i = 0; i < 16; i++) {
			shopGeneral.add(new Ship("Scout", new String[]{""},0,0,1,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
		}
		
		for (int i = 0; i < 4; i++) {
			shopGeneral.add(new Ship("Viper", new String[]{""},0,1,0,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false));
		}

		for (int i = 0; i < 10; i++) {
			shopExplorer.add(new TrashShip("Explorer", new String[]{""},2,0,2,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 2, 0, 0, ""));
		}
	}
	
	//méthode pour lancer une partie à partir d'une sauvegarde ou non
	public static void launch(int nbrPlayer,int nbrIa, String extend, boolean save) throws IOException {
		
		//initialisation de toutes les variables et arrayList qu'on va utilisé dans la méthode
		ShopExplorer shopExplorer = new ShopExplorer();
		ShopGeneral shopGeneral = new ShopGeneral();
		
		ArrayList<Mission> missions = new ArrayList<>();
		String scenario = "";
		
		ArrayList<CardPlayed> cp = new ArrayList<>();
		ArrayList<CardPlayer> dp = new ArrayList<>();
		ArrayList<DisclaimerPlayer> discp = new ArrayList<>();
		ArrayList<PickPlayer> pickp = new ArrayList<>();
		ArrayList<Player> p = new ArrayList<>();
		
		boolean chasseAlhomme = false;
		
		ListExplorer listE = new ListExplorer();
		ListGeneral listG = new ListGeneral();

		Board bord = new Board();
		InteractionsShip interactionsShip = new InteractionsShip();
		
		int tour = 0;
		int role;
		int role2;
		
		ArrayList<Ship> blob = new ArrayList<>();
		ArrayList<Ship> starEmpire = new ArrayList<>();
		ArrayList<Ship> tradeFederation = new ArrayList<>();
		ArrayList<Ship> machineCult = new ArrayList<>();
		
		//si l'utilisateur lance une partiuie normal
		if(!save) {
			int totalP = nbrIa+nbrPlayer;
			int repeat = totalP%2==0 ? totalP/2 : ((totalP+1)/2);

			
			ArrayList<String> scenarios = new ArrayList<>() {{{
				add("Border Skirmish");
				add("Commitment to the Cause");
				add("Frantic Preparations");
				add("Frontier Expedition");
				add("Maximum Warp");
				add("Prolonged Conflict");
				add("Ruthless Efficiency");
				add("Total War");
			}}};
			
			//si l'extensionj choisi est un scénario, on tire un scénario aléatoirement
			if(extend.equals("scenario")) {
				Collections.shuffle(scenarios);
				scenario = scenarios.get(0);
			}
			
			//on complète le shop en fonction du nombre de joueur et de l'extension choisie
			for(int i=0;i<repeat;i++) {
				starter(shopGeneral, shopExplorer, scenario);
				
				if (extend.equals("United")) {
					starterUnited(shopGeneral, missions);
				}
				
				else if (extend.equals("Colony Wars")) {
					starterColonyWars(shopGeneral, shopExplorer);
				}
			}

			
			//on rajoute  un objet card player, card played, disclaimer player et pickp player dans les arrayLists appropriées par rapport au nombre de joueur 
			for (int i = 0; i < totalP; i++) {
				cp.add(new CardPlayed());
				dp.add(new CardPlayer());
				discp.add(new DisclaimerPlayer());
				pickp.add(new PickPlayer());
			}
			
			//on créer les objets player en leur demand de saisir leur noms
			for (int i = 0; i < nbrIa; i++) {
				Board.printMsg("Entrez le nom de l'ia "+(i+1)+" :");
				p.add(new Player(new Scanner(System.in).nextLine(),true));
			}
			for (int i = 0; i < nbrPlayer; i++) {
				Board.printMsg("Entrez le nom du jour "+(i+1)+" :");
				p.add(new Player(new Scanner(System.in).nextLine()));
			}
			
			Collections.shuffle(p);
			Collections.shuffle(missions);
			
			//si l'extension choisie est united on donne trois missions a chaque joueur			
			if(extend.equals("United")) {
				for(Player player : p) {
					player.addMission(missions.get(0));
					missions.remove(0);
					player.addMission(missions.get(0));
					missions.remove(0);
					player.addMission(missions.get(0));
					missions.remove(0);
				}
			}
			
			//On demande aux joueurs quel mode de jeu ils veulent jouer s'ils sont au moins 3
			
			int combat = 0;
			if(p.size()>=3){
				combat = chooseNumberBetween(1, 2, "Voulez vous jouer :\n \t- 1 combat Ã  mort\n \t- 2 à la chasse Ã  l'homme");
			}
			
			if(combat == 2) {
				chasseAlhomme = true;
			}
			
			
			// selon le scénario choisi en début de partie on applique les modifications si besoin
			int viper = 2;
			int scout = 8;
			
			if(scenario.equals("Frantic Preparations")) {
				viper = 1;
				scout = 7;
			}
			else if(scenario.equals("Frontier Expedition")) {
				scout = 6;
			}
			
			for(int j=0; j < p.size();j++){
				for (int i = 0; i < scout; i++) { //scout
					pickp.get(j).add(new Ship("Scout", new String[]{""},0,0,1,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false, false));
				}
				if(scenario.equals("Frontier Expedition")) {
					pickp.get(j).add(new TrashShip("Explorer", new String[]{""},2,0,2,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 2, 0, 0, ""));
					pickp.get(j).add(new TrashShip("Explorer", new String[]{""},2,0,2,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false,false, 2, 0, 0, ""));
				}
			}
			for (int j = 0; j < p.size() ; j++){
				for (int i = 0; i < viper; i++) { //viper
					if(scenario.equals("Commitment to the Cause")) {
						pickp.get(j).add(new Ship("Viper", new String[]{""},0,2,0,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false, false));
					}
					else {
						pickp.get(j).add(new Ship("Viper", new String[]{""},0,1,0,0,"",false, new Couple(),new Couple(),new Couple(),false,new Ensemble[]{},false, false));
					}
				}
			}

			
			shuffle(shopGeneral);

			//on met en place la liste des shop qui seront sur le terrain
			for (int i = 0; i < 5; i++) {
				listG.add(shopGeneral.remove());
			}


			listE.add(shopExplorer.remove());
			
			//on complète les deck de player selon leur pioches 

			for(int i = 0;i<p.size();i++){
				shuffle(pickp.get(i));
				if(i==0){
					for(int j=0;j<3;j++){
						dp.get(i).add(pickp.get(0).remove());
					}
				}
				else if(i==p.size()-1){
					dp.get(i).fillDeck(pickp.get(i), discp.get(i));
				}
				else if(i==1){
					for(int j=0;j<4;j++){
						dp.get(i).add(pickp.get(0).remove());
					}
				}
				else{
					dp.get(i).fillDeck(pickp.get(i), discp.get(i));
				}
			}

			//selon l'extension on applique les modification si besoin 
			if(scenario.equals("Border Skirmish")) {
				for(Player p1 : p) {
					p1.manageHeal(-20);
				}
			}
			else if(scenario.equals("Prolonged Conflict")) {
				for(Player p1 : p) {
					p1.manageHeal(30);
				}
			}
		}
		
		// si l'utilisateur lance une sauvegarde on saute la partie du dessus et on appel la méthode de lecture d'un fichier de sauvegarde
		else {
			
			 Info infoGame = launchWithASave("sauvegardeTest.txt",blob,tradeFederation,machineCult,starEmpire,shopGeneral,listG,shopExplorer,listE,p,dp,discp,cp,pickp);
					tour = infoGame.tour;
					extend = infoGame.extend;
					scenario = infoGame.scenario;
					chasseAlhomme = infoGame.chasseAlhomme;
		}
		
		int exit = 0;
		boolean winner = false;
		
		// puis on lance la partie 
		while (exit == 0) {
			int price = 0;
			boolean used = false;
			
			role = (tour % p.size());
			if(role == 0) {
				role2 = 1;
			}
			else if(role == (p.size()-1)) {
				role2 = 0;
			}
			else {
				role2 = role + 1; 
			} 
			
			//si l'extension choisi est Maximum Warp on fait piocher le joueur
			if(scenario.equals("Maximum Warp")) {
				if(!pickp.get(role).isEmpty()) {
					dp.get(role).add(pickp.get(role).remove());
				}
				else {
					pickp.get(role).fillPick(discp.get(role));
					if(!pickp.get(role).isEmpty()) {
						dp.get(role).add(pickp.get(role).remove());
					}
				}
			}
			
			//si le joueur possèdent des bases ou des héros on les rejouent pour que le joueur bénéficie des propriétés de la carte
			for (int i = 0; i < p.get(role).size(); i++) {
				Card c = p.get(role).get(i);
				dp.get(role).playShip(role, chasseAlhomme, p, pickp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, c);
			}
			
			for (int i = 0; i < p.get(role).getHeroes().size(); i++) {
				Card c = p.get(role).getHeroes().get(i);
				dp.get(role).playShip(role, chasseAlhomme, p, pickp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation, c);
			}
			
			// si le joueur n'est pas un ia on lui demande l'action qui veut faire
			int choix;
			if (!(p.get(role).isIa())) {
				do {
					Board.printMsg(bord.toString(p,role,(tour+1), listG, listE, cp.get(role),discp.get(role), pickp.get(role), dp.get(role), extend));
					choix = chooseNumberBetween(1, 8,"Quelle action voulez-vous faire ?( 1:Jouer carte; 2:Afficher le shop; 3:Acheter Une carte; 4:Trash une carte; 5:Attaquer; 6:Fin du tour; 7:Sauvegarder la apartie; 8:Quitter le jeu.");
					//TODO : modifier la valeur dans le choix
					
					switch (choix) {
					case 1:
						//le joueur joue une ou toutes ces cartes
						dp.get(role).play(role, chasseAlhomme, p, pickp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);
						break;
					case 2:
						//ca affiche le shop explorer et general
						int choixShop;
						System.out.println(Board.printShop(listG, listE));
						do {
							System.out.println("\nPour quitter taper 1: \n");
							choixShop = new Scanner(System.in).nextInt();			
							
						} while (choixShop != 1);
						break;
					case 3:
						//le joueur achete une carte et selon l'extension un bonus peut etre activé
						price = interactionsShip.achat(role, chasseAlhomme, listE, listG, shopExplorer, shopGeneral, discp, pickp, dp, cp, p, blob, starEmpire, machineCult, tradeFederation);
						if(price > 0 && used == false) {
							if(scenario.equals("Ruthless Efficiency")) {
								used = true;
								Ship.scrapCardHand(false, dp.get(role));
							}
							else if(scenario.equals("Total War")) {
								used = true;
								p.get(role).manageMoney(3);
							}
						}
						break;
					case 4:
						//le joueur trash une carte 
						cp.get(role).trash(p, cp, dp, role, pickp, discp, chasseAlhomme, listG, listE, blob, starEmpire, machineCult, tradeFederation, shopGeneral, shopExplorer);
						break;
					case 5:
						//le joueur choisi d'attaquer
						if(p.size()>2){
							p.get(role).attack(chasseAlhomme, p, discp, role, role2);
							
						}else {
							if (p.get(role).attack(chasseAlhomme, p, discp, role, role2) && p.size()==1) {
								choix = 6;
							}
						}
						break;
	
					case 6:
						//finir son tour
						break;
						
					case 7:
						//sauvegarde la partie
						Sauvegarde.saveGame(blob,tradeFederation,machineCult,starEmpire,tour,chasseAlhomme, "sauvegardeTest.txt",scenario,extend,shopGeneral.toSave(),listG.toSave(),shopExplorer.toSave(),listE.toSave(),p,dp,discp,cp,pickp);
						break;
	
					case 8:
						//met fin au programme
						Board.printMsg("Vous avez quitter le jeu");
						System.exit(1);
						break;
	
					default:
						// si l'utilisateur met un autre choix non définit dans le switch
						Board.printMsg("Vous n'avez pas sÃ©lectionnÃ© d'option\n");
						choix = 0;
						break;
					}
					
					//on vérifie si le joueur a validé une mission et s'il aréussi a terminer tout c'est mission  
					if(Player.missionByRound(shopExplorer, shopGeneral, chasseAlhomme, cp, discp, dp, role, p, listG, listE, pickp)) {
						winner = true;
						choix = 6;
					}
				} while (choix != 6);
			}
			// si le joueur est une ia on appel la méthode de l'ia brain qui vga gérer toutes les actions de l'ia
			else {
				p.get(role).iaBrain(scenario, role, chasseAlhomme, p, pickp, dp, discp, cp, blob, starEmpire, tradeFederation, machineCult, listE, listG, shopExplorer, shopGeneral, interactionsShip,chasseAlhomme);
				if(extend.contentEquals("United")) {
					winner = Player.missionByRound(shopExplorer, shopGeneral, chasseAlhomme, cp, discp, dp, role, p, listG, listE, pickp);
				}
			}
			
			//on ajoute les cartes non joué par l'utilisateur dans les cartes jouées puis on ajoute toutes les cartes jouées dans la défausse
			tour++;
			cp.get(role).addAll((dp.get(role).getList()));
			discp.get(role).addAll((cp.get(role).getList()));
			
			//on vide toutes les liste qui doivent être vide
			cp.get(role).clear();
			dp.get(role).clear();
			//on rempli le deck du joueur 
			dp.get(role).fillDeck(pickp.get(role), discp.get(role));
			//on reset les valeur du joueur comme l'attaque et l'argent
			p.get(role).reset();
			// on vide les arraylist des 4 factions qui contient les cartes jouées durant ce tour
			machineCult.clear();
			blob.clear();
			starEmpire.clear();
			tradeFederation.clear();
			
			//on vérifie si le joueur a gagtné à l'aide des missions 
			if(winner) {
				Board.printMsg(p.get(role).getName()+" à gagné la partie à l'aide des missions");
				System.out.println("\n=============================== Score ==================================\n");
				Sauvegarde.saveScore("score.txt",p,role);
				System.out.println("\n========================================================================\n");
				exit = 1;
			}

			//on vérifie s'il y a pas des joueurs qui possèdent un nombre de point de vie inférieur ou égale à 0
			//si c'est le cas on les enlèves des arraylist
			for(int i=0;i<p.size();i++){
				if(p.get(i).getLife()<=0){
					cp.remove(i);
					dp.remove(i);
					discp.remove(i);
					pickp.remove(i);
					p.remove(i);
				}
			}
			
			//on vérifie si la liste qui contient tous les jouers a une taille de 1
			//si c'est le cas le joueur restant a gagné la partie
			if(p.size()==1){
				Board.printMsg(p.get(0).getName()+" à gagné la partie");
				System.out.println("\n=============================== Score ==================================\n");
				Sauvegarde.saveScore("score.txt",p,0);
				System.out.println("\n========================================================================\n");
				exit=1;
			}
		}
	}

	//Le main qui demande à l'utilisateur s'il veut lancer une sauvegarde ou une partie
	
	public static void main(String[] args) throws IOException {
		int choix;
		int nbPlayer = 0;
		int nbIA = 0;
		int save;
		
		do {
			save = chooseNumberBetween(0, 1, "Voulez-vous lancer une sauvegarde (1) ou lancer une partie (0) ?");
			if(save == 1) {
				launch(0,0,"",true);
			}
			else {
				do {
					//on demande aux utilisateurs de rentrer le nombre de joueur et de choisir l'extension
					System.out.println("Vous devez etre au moins deux joueurs (joueur ou IA) pour lancer une partie. (maximmum : 10)\n");
					nbPlayer = chooseNumberBetween(0, 10, "Combien de joueur réels etes-vous ?");
					nbIA = chooseNumberBetween(0, 10, "Avec combien d'IA voulez-vous jouer ?");
				} while(!((nbPlayer + nbIA) >= 2 && (nbPlayer + nbIA) <= 10));
				launch(nbPlayer, nbIA, choiceExt(), false);
			}
			//on demande s'il veut relancer une nouvelle partie
			 choix = chooseNumberBetween(0, 1, "Voulez vous rejouer ? (Oui : 1 ou Non : 0)");
			
		} while (choix == 1);

		Board.printMsg("Vous avez quitter le jeu");
	}

}
