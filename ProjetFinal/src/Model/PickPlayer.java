package Model;

public class PickPlayer extends AbstractArrayListShuffle {

	public PickPlayer() {
		super();
	}
	
	/**
	 * méthode permettant de remplir la pioce du joeueur
	 */
	public void fillPick(DisclaimerPlayer dp) {
		list.addAll(dp.getList());
	}
}
