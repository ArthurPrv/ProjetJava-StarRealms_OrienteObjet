package Model;

import java.util.Collections;

public abstract class AbstractArrayListShuffle extends AbstractArrayListToString {
	
	//Permets aux classes qui l'étendent d'avoir la méthode shuffle, elle permet de mélanger la liste
	public void shuffle() {
		Collections.shuffle(list);
	}
}
