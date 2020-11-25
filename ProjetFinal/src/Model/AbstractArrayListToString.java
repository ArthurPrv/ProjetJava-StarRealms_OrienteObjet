package Model;

import java.util.ArrayList;

// on crée une classe paire qui va nous permettre de créer un objet qui contient une carte et un entie
class Pair {
	final Card c;
	final int position;
	
	public Pair(Card c, int position) {
		this.c = c;
		this.position = position;
	}
}

abstract class AbstractArrayListToString {
	
	final ArrayList<Card> list;
	
	public AbstractArrayListToString() {
		this.list = new ArrayList<>();
	}
	
	@Override
	public String toString() {
		return toStringCard(list);
	}
	
	public static String toStringCard(ArrayList<Card> list) {
		ArrayList<Pair> ship = new ArrayList<>();
		ArrayList<Pair> heroes = new ArrayList<>();
		for (int i = 0; i<list.size(); i++) {
			if(list.get(i).isHeroe()) {
				heroes.add(new Pair(list.get(i), i));
			}
			else {
				ship.add(new Pair(list.get(i), i));
			}
		}
		
		StringBuilder str = new StringBuilder();
		if(!ship.isEmpty()) {
			str.append("\nShip:");
			str.append("\n+--+--------------------+-----+----------------------------------+------+-----+----+-------------------------------------------------------------------------------------------------+------+--------------------+--------------------+--------------------+------------------------------------------------------------------------------------------------------------------------------------------------------+-----------+------------+-----------+----------+-----------------------------------------+------+-------+\n");
			str.append("|n°|Name                |Price|Faction                           |Damage|Money|Heal|Capacity                                                                                         |Choose|Ally Damage         |Ally Money          |Ally Heal           |Ally Capacity                                                                                                                                         |Ally Choose|Trash Damage|Trash Money|Trash Heal|Trash Capacity                           |Shield|Outpost|");
			str.append("\n+--+--------------------+-----+----------------------------------+------+-----+----+-------------------------------------------------------------------------------------------------+------+--------------------+--------------------+--------------------+------------------------------------------------------------------------------------------------------------------------------------------------------+-----------+------------+-----------+----------+-----------------------------------------+------+-------+\n");
			for (Pair p : ship) {
				str.append("|");
				str.append(p.position);
				str.append(" ");
				str.append(((Ship) p.c).toString());
				str.append("\n+--+--------------------+-----+----------------------------------+------+-----+----+-------------------------------------------------------------------------------------------------+------+--------------------+--------------------+--------------------+------------------------------------------------------------------------------------------------------------------------------------------------------+-----------+------------+-----------+----------+-----------------------------------------+------+-------+\n");
			}
			if(!heroes.isEmpty()) {
				str.append("\n");
			}
		}
		if(!heroes.isEmpty()) {
			str.append("Heroes:");
			str.append("\n+--+------------------+-----+---------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------------------------------------------------------------------------------------------------------------------------+\n");
			str.append("|n°|Name              |Price|Capacity                                                                                                                                                             |Trash Capacity                                                                                                            |");
			str.append("\n+--+------------------+-----+---------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------------------------------------------------------------------------------------------------------------------------+\n");
			for (Pair p : heroes) {
				str.append("|");
				str.append(p.position);
				str.append(" ");
				str.append(((Heroes) p.c).toString());
				str.append("\n+--+------------------+-----+---------------------------------------------------------------------------------------------------------------------------------------------------------------------+--------------------------------------------------------------------------------------------------------------------------+\n");
			}
		}
		
		return str.toString();
	}
	
	public Card remove(int i) {
		Card s = list.get(i);
		list.remove(list.get(i));
		return s;
	}
	
	public int size() {
		return list.size();
	}
	
	public boolean add(Card c) {
		return list.add(c);
	}
	
	public Card get(int i) {
		return list.get(i);
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public Card remove() {
		int taille = list.size()-1;
		Card s = list.get(taille);
		list.remove(list.get(taille));
		return s;
	}
	
	public ArrayList<Card> getList() {
		return list;
	}
	
	public void clear() {
		list.clear();
	}
	
	public String toSave(){
		StringBuilder str = new StringBuilder();

		if(!(list.isEmpty())){
			for(Card card : list){
				str.append("~"+card.toSave());
			}
			return str.toString().substring(1);
		}

		return str.toString();
	}

	public void toReload(String line){
		for(String carte : line.toString().split("~")){
			String[] firstWordGetter = carte.split("&");
			if(firstWordGetter[0].equals("Ship")){
				this.add(Ship.toReload(carte));
			}else if(firstWordGetter[0].equals("TrashShip")){
				this.add(TrashShip.toReload(carte));

			}else if(firstWordGetter[0].equals("Base")){
				this.add(Base.toReload(carte));

			}else if(firstWordGetter[0].equals("Mission")){
				this.add((Card) Mission.toReload(carte));

			}else if(firstWordGetter[0].equals("Heroe")){
				this.add(Heroes.toReload(carte));
			}
		}
	}
}
