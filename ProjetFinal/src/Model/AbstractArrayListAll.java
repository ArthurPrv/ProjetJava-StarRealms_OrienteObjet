package Model;

import java.util.ArrayList;

abstract class AbstractArrayListAll extends AbstractArrayListToString {
	
	//Permets aux classes qui l'�tendent d'avoir la m�thode addAll qui ajoute toutes les cartes dans la liste
	public void addAll(ArrayList<Card> list2) {
		list.addAll(list2);
	}
	
}
