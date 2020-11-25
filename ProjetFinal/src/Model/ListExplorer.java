package Model;


public class ListExplorer extends AbstractArrayListToString{
	
	public ListExplorer() {
		super();
	}
	
	
	/**
	 * 
	 *méthode permettant de remplir le shopExplorer
	 */
	public void fillShop(ShopExplorer shopExplorer) {
		if (list.isEmpty() && !(shopExplorer.isEmpty())) {
			list.add(shopExplorer.remove());
		}
	}
	
	/**
	 * 
	 *méthode permettant l'achat d'une carte dans le shop explorer et de retrourner le prix
	 */
	public int achatExplorer(ShopExplorer shopExplorer, DisclaimerPlayer disc, PickPlayer pp, Player p) {
		boolean ia = p.isIa();
		if (!(isEmpty())) {
			if(!ia) {
				System.out.println("Voici le shop : " + toString() + "\n");
			}
			int price = get(0).getPrice();

			if (price <= p.getMoney()) {
				p.manageMoney(-price);
				if(p.getPriorityPurchase()) {
					pp.add(remove());
				}
				else {
					disc.add(remove());
				}
				
				if(!ia) {
					System.out.println("Vouas avez acheté un explorer\n");
				}
				return 2;
			} else {
				if(!ia) {
					System.out.println("Vous n'avez pas assez d'argent pour acheter un explorer \n");
				}
				return 0;
			}
		} else {
			if(!ia) {
				System.out.println("Le shop explorer est vide\n");
			}
			return 0;
		}
	}

	
	/**
	 *méthode permettant d'avoir le prix le plus bas dans la listeExplorer (il est égale a 0 si il n'y a plus de carte 
	 *
	 */
	public int lowerPrice(){
		if (!this.isEmpty()){
			return 2;
		}
		return 0;
	}
}
