package Model;

import java.util.Collections;

public abstract class AbstractArrayListShuffle extends AbstractArrayListToString {
	
	//Permets aux classes qui l'�tendent d'avoir la m�thode shuffle, elle permet de m�langer la liste
	public void shuffle() {
		Collections.shuffle(list);
	}
}
