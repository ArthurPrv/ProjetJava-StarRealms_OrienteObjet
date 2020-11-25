package Model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import Controleur.Main;
import View.Board;

import java.util.Random;

public class Player extends AbstractArrayListToString {

	private final String name;
	private int life;
	private int damage;
	private int money;
	private final boolean ia;
	private int heal = 0;
	private boolean priorityPurchase = false;
	private boolean completedMission = false;
	private ArrayList<Mission> missions = new ArrayList<>();
	private ArrayList<Heroes> heroes = new ArrayList<>();

	public Player(String name,boolean ia) {
		this.name = Objects.requireNonNull(name);
		this.life = 50;
		this.damage = 0;
		this.money = 0;
		this.ia = ia;
	}


	public Player(String name){
		this(name,false);
	}
	
	public Player(String name, int life, int damage, int money, boolean ia, int heal, boolean priorityPurchase, boolean completedMission,ArrayList<Mission> missions,ArrayList<Heroes> heroes) {
		this.name = name;
		this.life = life;
		this.damage = damage;
		this.money = money;
		this.ia = ia;
		this.heal = heal;
		this.priorityPurchase = priorityPurchase;
		this.completedMission = completedMission;
		this.heroes= heroes;
		this.missions= missions;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if(!isEmpty()) {
			str.append("+--+--------------------+-----+----------------------------------+------+-----+----+-------------------------------------------------------------------------------------------------+------+--------------------+--------------------+--------------------+------------------------------------------------------------------------------------------------------------------------------------------------------+-----------+------------+-----------+----------+-----------------------------------------+------+-------+\n");
			str.append("|n°|Name                |Price|Faction                           |Damage|Money|Heal|Capacity                                                                                         |Choose|Ally Damage         |Ally Money          |Ally Heal           |Ally Capacity                                                                                                                                         |Ally Choose|Trash Damage|Trash Money|Trash Heal|Trash Capacity                           |Shield|Outpost|");
			str.append("\n+--+--------------------+-----+----------------------------------+------+-----+----+-------------------------------------------------------------------------------------------------+------+--------------------+--------------------+--------------------+------------------------------------------------------------------------------------------------------------------------------------------------------+-----------+------------+-----------+----------+-----------------------------------------+------+-------+\n");
			for (int i = 0; i<list.size(); i++) {
				str.append("|");
				str.append(i);
				str.append(" ");
				str.append(((Base) list.get(i)).toString());
				str.append("\n+--+--------------------+-----+----------------------------------+------+-----+----+-------------------------------------------------------------------------------------------------+------+--------------------+--------------------+--------------------+------------------------------------------------------------------------------------------------------------------------------------------------------+-----------+------------+-----------+----------+-----------------------------------------+------+-------+\n");
			}
		}
		
		return str.toString();
	}

	public String getName() {
		return name;
	}

	public int getLife() {
		return life;
	}

	public int getDamage() {
		return damage;
	}

	public int getMoney() {
		return money;
	}
	
	public int getHeal() {
		return heal;
	}
	
	public ArrayList<Mission> getMission() {
		return missions;
	}
	
	public boolean getPriorityPurchase() {
		return priorityPurchase;
	}
	
	public void activatePriorityPurchase() {
		priorityPurchase = true;
	}

	public void manageDamage(int d) {
		damage += d;
	}

	public void manageHeal(int h) {
		life += h;
		if(h>0) {
			heal += h;
		}
	}

	public void manageMoney(int m) {
		money += m;
	}

	public void reset() {
		damage = 0;
		money = 0;
		heal = 0;
		priorityPurchase = false;
		completedMission = false;
	}

	public boolean haveOutpost() {
		for (Card b : list) {
			if (((Base) b).isOutPost()) {
				return true;
			}
		}
		return false;
	}

	public boolean attackPlayer(Player p2) {
		p2.manageHeal(-this.damage);
		this.manageDamage(-this.damage);
		return (p2.getLife() <= 0);
	}
	
	/**
	 * permettant d'avoir le shield le plus bas des bases d'un joueur en renvoyant l'entier correspondant
	 */
	public int getLowerShield(){
		int lower = 0;
		int tempShield;
		for(Card s : list){
			tempShield= ((Base) s).getShield();
			if(lower==0){
				lower=tempShield;
			} else if( lower>tempShield){
				lower=tempShield;
			}
		}
		return lower;
	}

	/**
	 * méthode permettant l'attaque des bases
	 */
	public void attackBase(Player p2, DisclaimerPlayer discp2, Boolean outpost) {
		if (getDamage() >= p2.getLowerShield() && p2.getLowerShield() != 0) {
			boolean ia = isIa();
			int choixAttack = -20;
			int taille = (p2.size() - 1);
			boolean isNotOutpost = true;
			boolean verif;
			Base b = null;
			if(!ia) {
				System.out.println(p2.toString());
			}
			if (outpost) {
				do {
					if (!ia) {
						do {
							System.out.println("L'adversaire possède au moins un Outpost que vous devez obligatoirement attaquer entrer la bonne position ou pour quitter -1 :");
							Scanner sc = new Scanner(System.in);
							verif = sc.hasNextInt();
							if (verif) {
								choixAttack = sc.nextInt();
							}
						} while(!(choixAttack>=-1 && choixAttack<=taille));
						
					} 
					else {
						Random r = new Random();
						choixAttack = r.ints(0, ((taille) + 1)).findFirst().getAsInt();
					}
					if(choixAttack==-1){
						isNotOutpost=false;
						
					} else if(choixAttack>=-1 && choixAttack<=taille){
						b = (Base) p2.get(choixAttack);
						isNotOutpost = !(b.isOutPost());
					}

				} while (isNotOutpost);
			} 
			else {
				do {
					if (!ia) {
						System.out.println("Vous avez choisi d'attaquer une base entre sa position ou pour quitter : -1");
						Scanner sc = new Scanner(System.in);
						verif = sc.hasNextInt();
						if (verif) {
							choixAttack = sc.nextInt();
						}
					} else {
						Random r = new Random();
						choixAttack = r.ints(0, ((taille) + 2)).findFirst().getAsInt();
					}
				} while (!(choixAttack>=-1 && choixAttack<=taille));
			}
			if (choixAttack != -1) {
				b = (Base) p2.get(choixAttack);
				discp2.add(p2.remove(choixAttack));
				this.manageDamage(-b.getShield());
			}
		}
	}

	
	/**
	 * avoir la vie minimum des outpost d'un joueur et renvoye 0 si il n'y en pas
	 */
	public int outpostLowerHP(){
		int mini = 0 ;
		if(!(this.haveOutpost())){
			return mini;
		}
		else{
			for(Card s : list){
				if(mini == 0 && ((Base) s).isOutPost()) {
					mini = ((Base) s).getShield();
				}
				else if(((Base) s).isOutPost()){
					mini = Math.min(mini,((Base) s).getShield());
				}
			}
			return mini;
		}
	}
	
	/**
	 * methode correspondant a l'attaque du joueur en jeux
	 */
	public boolean attack(boolean chasseAlhomme, ArrayList<Player> p, ArrayList<DisclaimerPlayer> discp, int current, int  enemy) {
		if(!(getDamage()==0)) {
			int baseOrPlayer;
			boolean dead = false;
			int choix = 0;
			boolean verif;
			Player p2;
			DisclaimerPlayer discp2;
			
			baseOrPlayer = Main.chooseNumberBetween(1, 3, "Vous pouvez attaquer un joueur(1) ou une bases d'un joueur(2) ou pour quitter(3)");
			
			
			if (baseOrPlayer == 1) { //attaquer joueur 
				
				if(p.size() == 2) {
					p2 = p.get(enemy);
					discp2 = discp.get(enemy);
				}
				
				else if(chasseAlhomme) {
					if(current == 0) {
						p2 = p.get(p.size()-1);
						discp2 = discp.get(discp.size()-1);
					}
					else if(current == p.size()-1) {
						p2 = p.get(0);
						discp2 = discp.get(0);
					}
					else {
						p2 = p.get(current-1);
						discp2 = discp.get(current-1);
					}
					Board.printMsg("Avec ce mode de jeu vous ne pouvez qu'attaquer le joueur de votre gauche c'est à dire "+p2.getName());
				}
				
				else {
					String texte = p.toString();
					texte += "Choisiez quel joueur voulez-vous attaquer entrer sa position :\n";
					do {
						choix = Main.chooseNumberBetween(0, p.size()-1, texte);
					}while(!(choix >= 0 && choix <= (p.size()-1) && choix != current));

					p2 = p.get(choix);
					discp2 = discp.get(choix);
				}
				
				if (p2.haveOutpost()) {
					this.attackBase(p2, discp2, true);
				} 
				else {
					dead = this.attackPlayer(p2);
				}
				return dead;
			}
			
			
			else if(baseOrPlayer == 2) { //attaquer base
				
				if(p.size() == 2) {
					p2 = p.get(enemy);
					discp2 = discp.get(enemy);
				}
				
				else if(chasseAlhomme) {
					System.out.println(Board.printPlayer(p));
					Board.printMsg("Avec ce mode de jeu vous ne pouvez qu'attaquer les bases des joueurs de votre gauche et de votre droite");
					if(current == 0) {
						do {
							Board.printMsg("Choisissez le joueur en entrant sa position pour attaquer ces bases :");
							Scanner sc = new Scanner(System.in);
							verif = sc.hasNextInt();
							if (verif) {
								choix = sc.nextInt();
							}
						} while(!(choix == (p.size()-1) || choix == 1));
					}

					else if(current == p.size()-1) {
						do {
							Board.printMsg("Choisissez le joueur en entrant sa position pour attaquer ces bases :");
							Scanner sc = new Scanner(System.in);
							verif = sc.hasNextInt();
							if (verif) {
								choix = sc.nextInt();
							}
						} while(!(choix == (p.size()-2) || choix == 0));
					}
					
					else {
						do {
							Board.printMsg("Choisissez le joueur en entrant sa position pour attaquer ces bases :");
							Scanner sc = new Scanner(System.in);
							verif = sc.hasNextInt();
							if (verif) {
								choix = sc.nextInt();
							}
						} while(!(choix == (current+1) || choix == (current+1)));
					}
					p2 = p.get(choix);
					discp2 = discp.get(choix);					
				}
								
				else {
					String texte = p.toString();
					texte += "Choisissez le joueur en entrant sa position pour attaquer ces bases :\n";
					do {
						choix = Main.chooseNumberBetween(0, p.size()-1, texte);
					}while(!(choix >= 0 && choix <= (p.size()-1) && choix != current));
					p2 = p.get(choix);
					discp2 = discp.get(choix);
				}
				
				if (p2.haveOutpost()) {
					this.attackBase(p2, discp2, true);
				} 
				else {
					if (p.size() != 0) {
						this.attackBase(p2, discp2, false);
					}
					else {
						System.out.println("Le joueur ne possède pas de base");
					}
				}
				return false;
			}
			
			else { //pour quitter
				return false;
			}
		}
		else {
			System.out.println("Vous ne pouvez pas attaquer car vos dégats sont à 0");
		}
		return false;
	}
	
	/**
	 * Méthode correspondant au cerveau de l'ia, il fonctionne principalement par 
	 * une suite d'action aléatoire mais vérifie toujour la pérénité de ces dernières
	 */
	public void iaBrain(String scenario, int current, boolean chasseAlhomme, ArrayList<Player> p, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayed> cp, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> tradeFederation, ArrayList<Ship> machineCult,ListExplorer listE, ListGeneral listG, ShopExplorer shopExplorer, ShopGeneral shopGeneral,InteractionsShip interactionsShip, Boolean chasseAlHomme){

		int price = 0;
		boolean used = false;

		while(!(dp.get(current).isEmpty())){
			dp.get(current).play(current, chasseAlhomme, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, machineCult, tradeFederation);			
		}
		
		
		
		boolean BuyPossibility= p.get(current).getMoney() > 0;
		
		if(listE.isEmpty() && listG.isEmpty()){BuyPossibility=false;}
		while (BuyPossibility){
			if((interactionsShip.lowerPrice(listE,listG)!=0) && (p.get(current).getMoney()>interactionsShip.lowerPrice(listE,listG))) {
				interactionsShip.achat(current, chasseAlHomme, listE, listG, shopExplorer, shopGeneral, discp, pp, dp, cp, p, blob, starEmpire, machineCult, tradeFederation);
			} else {
				BuyPossibility = false;
			}
			
			if(price > 0 && used == false) {
				if(scenario.equals("Ruthless Efficiency")) {
					used = true;
					Ship.scrapCardHand(false, dp.get(current));
				}
				else if(scenario.equals("Total War")) {
					used = true;
					p.get(current).manageMoney(3);
				}
			}
		}
		
		

		
		
		Random r = new Random();
		int enemy;
		boolean AttackPossibility1=true;
		do{
			if(!chasseAlHomme) {
				enemy = r.ints(0, p.size()).findFirst().getAsInt();
			}
			else{
				if(current == 0){
					enemy = p.size()-1;
				} 
				else if (current == p.size()-1){
					enemy = p.size()-2;
				} 
				else{
					enemy = current-1;
				}
			}
		}while(enemy == current);
		Player pEnemy = p.get(enemy);
		DisclaimerPlayer discpEnemy = discp.get(enemy);


		boolean AttackPossibility = p.get(current).getDamage() > 0;
		
		while(AttackPossibility){
			if(pEnemy.outpostLowerHP()>p.get(current).getDamage()){
				AttackPossibility=false;
			}
			
			else if(pEnemy.outpostLowerHP()<p.get(current).getDamage() && pEnemy.outpostLowerHP()!=0){
				p.get(current).attackBase(pEnemy,discpEnemy,true);
			}
			
			else if(!pEnemy.isEmpty()){
				int base = r.ints(0, 2).findFirst().getAsInt();
				if(base == 0){
					boolean outpost = pEnemy.haveOutpost();
					p.get(current).attackBase(pEnemy,discpEnemy,outpost);
				} 
				else {
					p.get(current).attackPlayer(pEnemy);
				}
			}
			
			else{
				p.get(current).attackPlayer(pEnemy);
			}
			
			if(p.get(current).getDamage()==0){
				AttackPossibility=false;
			}
		}
	}

	public boolean isIa() {
		return ia;
	}

	/**
	 * Métode permettant de savoir si un joueur a une carte trashable
	 */
	public boolean haveTrashable(){
		for(Card s : list){
			if(s.isTrash()){
				return true;
			}
		}
		return false;
	}
	
	public boolean addMission(Mission m) {
		return missions.add(m);
	}
	
	public ArrayList<Heroes> getHeroes() {
		return heroes;
	}
	
	public boolean haveHero() {
		if(!heroes.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public boolean isCompletedMission() {
		return completedMission;
	}
	
	/**
	 * Méthode permettant de vérifier qu'un joueur n'a bien validé ou non qu'une mission pendant le tour
	 */
	public static boolean missionByRound(ShopExplorer shopExplorer, ShopGeneral shopGeneral, boolean chasseAlhomme, ArrayList<CardPlayed> cp, ArrayList<DisclaimerPlayer> discp, ArrayList<CardPlayer> dp, int current, ArrayList<Player> p, ListGeneral listGeneral, ListExplorer listExplorer, ArrayList<PickPlayer> pp){
		int count=0;
		for(Mission m : p.get(current).missions){
			if(!m.isCompleted()){
				if(!p.get(current).completedMission) {
					if(Mission.launchMission(m.getObjectifName(),shopExplorer,shopGeneral,chasseAlhomme,cp,discp,dp,current,p,listGeneral,listExplorer,pp)){
						p.get(current).completedMission = true;
						m.setCompleted(true);
						count += 1;
					}
				}
			}
			else{
				count+=1;
			}
		}
		return count==3;
	}
	
	/**
	 * méthode permettant d'avoir le format texte de l'arrayList D'héro
	 */
	public String toSaveHeroes(){
		StringBuilder str = new StringBuilder();

		if(!(heroes.isEmpty())){
			for(Heroes card : heroes){
				str.append(card.toSave());
			}
			return str.toString().substring(0);
		}

		return str.toString();
	}

	/**
	 * méthode permettant d'avoir le format texte de l'arrayList De Mission
	 */

	public String toSaveMission(){
		StringBuilder str = new StringBuilder();

		if(!(missions.isEmpty())){
			for(Mission card : missions){
				str.append("~"+card.toSave());
			}
			return str.toString().substring(1);

		}

		return str.toString();
	}

}
