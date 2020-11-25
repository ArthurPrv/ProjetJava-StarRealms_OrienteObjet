package Model;

import java.util.ArrayList;

import Controleur.Main;
import View.Board;

public class CardPlayed extends AbstractArrayListAll{
	
	public CardPlayed() {
		super();
	}

	public void trash(ArrayList<Player> p, ArrayList<CardPlayed> cp, ArrayList<CardPlayer> dp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ListGeneral listG, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ShopGeneral shopGeneral, ShopExplorer shopExplorer) {
		int choixTrash;
		int taille;
		int i = 0;
		TrashShip ts;
		Base b = null;
		Boolean baseTrash = p.get(current).haveTrashable();
		Boolean cbTrash = haveTrashable();
		
		if(baseTrash||cbTrash) {
			choixTrash = Main.chooseNumberBetween(1, 3, "Pour trash une base : 1 pour trash une carte jouée : 2 pour trash un héro : 3 ou pour quitter : 4\n");
			
			if (choixTrash != 4) {
				if (choixTrash == 1 && baseTrash) {
					taille = (p.size() - 1);
					do {
						if (i > 0) {
							Board.printMsg("La carte choisi ne peut pas s'autodétruire\n");
						}
						
						choixTrash = Main.chooseNumberBetween(-1, taille, "Vous vous appretez a trash une base, selectionner sa postition dans la liste des bases ou quitter : -1)\n");

						if (choixTrash == -1) {
							break;
						}

						b = (Base) p.get(current).get(choixTrash);
						i++;

					} while (!(b.isTrash()));
					if (choixTrash != -1) {
						trashBase(p, b, current, chasseAlhomme, pp, dp, cp, discp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, tradeFederation, machineCult);
						p.remove(choixTrash);
					}
				} 
				else if (choixTrash==2 && cbTrash){
					taille = (size() - 1);
					do {
						if (i > 0) {
							Board.printMsg("La carte choisi ne peut pas s'autodétruire\n");
						}
						
						choixTrash = Main.chooseNumberBetween(-1, taille, "Vous vous appretez a trash une carte jouée, selectionner sa postition dans la liste des cartes jouées ou quitter : -1)\n");
						
						if (choixTrash == -1) {
							break;
						}

						i++;
					} while (!(get(choixTrash).isTrash()));
					if (choixTrash != -1) {
						ts = (TrashShip) get(choixTrash);
						trashTrashShip(p, ts, current, chasseAlhomme, pp, dp, cp, discp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, tradeFederation, machineCult);
						remove(choixTrash);
					}
				}
				
				else if (choixTrash == 3 && p.get(current).haveHero()){
					taille = (p.get(current).getHeroes().size() - 1);
					do {
						if (i > 0) {
							Board.printMsg("La carte choisi ne peut pas s'autodétruire\n");
						}
						
						choixTrash = Main.chooseNumberBetween(-1, taille, "Vous vous appretez a trash une carte hero, selectionner sa postition dans la liste heroe ou quitter : -1)\n");
						
						if (choixTrash == -1) {
							break;
						}

						i++;
					} while (!(get(choixTrash).isTrash()));
					if (choixTrash != -1) {
						Heroes hero = p.get(current).getHeroes().get(choixTrash);
						trashHero(hero, p, cp, dp, current, pp, discp, chasseAlhomme, listG, listE, blob, starEmpire, machineCult, tradeFederation, shopGeneral, shopExplorer, p.get(current).isIa());
						remove(choixTrash);
					}
				}
			}
		}
		else {
			System.out.println("Vous n'avez de carte qui possède l'option trash");
		}
	}
	
	public void trashTrashShip(ArrayList<Player> p, TrashShip ts, int current, boolean chasseAlhomme, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<CardPlayed> cp, ArrayList<DisclaimerPlayer> discp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> tradeFederation, ArrayList<Ship> machineCult) {
        p.get(current).manageDamage(ts.getTrashDamage());
        p.get(current).manageHeal(ts.getTrashHeal());
        p.get(current).manageMoney(ts.getTrashMoney());
        TrashShip.trashCapacity(ts, chasseAlhomme, current, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, tradeFederation, machineCult);
	}
	
	public void trashBase(ArrayList<Player> p, Base b, int current, boolean chasseAlhomme, ArrayList<PickPlayer> pp, ArrayList<CardPlayer> dp, ArrayList<CardPlayed> cp, ArrayList<DisclaimerPlayer> discp, ShopGeneral shopGeneral, ListGeneral listG, ShopExplorer shopExplorer, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> tradeFederation, ArrayList<Ship> machineCult) {
		p.get(current).manageDamage(b.getTrashDamage());
		p.get(current).manageHeal(b.getTrashHeal());
		p.get(current).manageMoney(b.getTrashMoney());
        Base.trashCapacity(b, chasseAlhomme, current, p, pp, dp, discp, cp, shopGeneral, listG, shopExplorer, listE, blob, starEmpire, tradeFederation, machineCult);
	}
	
	public void trashHero(Heroes h, ArrayList<Player> p, ArrayList<CardPlayed> cp, ArrayList<CardPlayer> dp, int current, ArrayList<PickPlayer> pp, ArrayList<DisclaimerPlayer> discp, boolean chasseAlhomme, ListGeneral listG, ListExplorer listE, ArrayList<Ship> blob, ArrayList<Ship> starEmpire, ArrayList<Ship> machineCult, ArrayList<Ship> tradeFederation, ShopGeneral shopGeneral, ShopExplorer shopExplorer, boolean ia) {
		Heroes.stringToCapacity(h, true, p, cp, dp, current, pp, discp, chasseAlhomme, listG, listE, blob, starEmpire, machineCult, tradeFederation, shopGeneral, shopExplorer, ia);
	}
	
	public boolean haveTrashable(){
		for(Card s : list){
			if(s.isTrash()){
				return true;
			}
		}
		return false;
	}
}
