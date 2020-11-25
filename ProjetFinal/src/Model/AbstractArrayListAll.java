package Model;

import java.util.ArrayList;

abstract class AbstractArrayListAll extends AbstractArrayListToString {
	
	//Permets aux classes qui l'étendent d'avoir la méthode addAll qui ajoute toutes les cartes dans la liste
	public void addAll(ArrayList<Card> list2) {
		list.addAll(list2);
	}
	
}
