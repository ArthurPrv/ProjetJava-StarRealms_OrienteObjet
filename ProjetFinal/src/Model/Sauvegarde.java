package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class Sauvegarde {
	
	
	/**
	 * Cette méthode permet d'enregistrer une arraylist Selon un schéma 
	 * en renvoyant la String correspondant au format de l'arraylist dans le fichier sauvegarde
	 * 
	 */
    private static String toSave(ArrayList<Ship> arrayList){
        StringBuilder str = new StringBuilder();

        if(!(arrayList.isEmpty())){
            for(Card card : arrayList){
                str.append("~"+card.toSave());

            }
            return str.toString().substring(1);

        }
        return str.toString();
    }

    /**
     * Cette méthode enregistrer une partie en cours dans un fichier sauvegarde.txt
     *
     */	
    public static void saveGame(ArrayList<Ship> blob, ArrayList<Ship> tradeF, ArrayList<Ship> machineC, ArrayList<Ship> starE, int tour, boolean chasseAlhomme, String diri, String scenario, String extension, String shopGsave, String listGsave, String shopEsave, String listEsave, ArrayList<Player> players, ArrayList<CardPlayer> cardPlayers, ArrayList<DisclaimerPlayer> disclaimerPlayers, ArrayList<CardPlayed> cardPlayeds, ArrayList<PickPlayer> pickPlayers) throws IOException {
        Path dir = Path.of(diri);
        try (BufferedWriter writer = Files.newBufferedWriter(dir)) {
            //writer.append("scenario\nextension\n&\nShopGeneral\n&\nShopExplorer\n");
            writer.append(tour+"\n"+chasseAlhomme+"\n"+toSave(blob)+"\n"+toSave(tradeF)+"\n"+toSave(machineC)+"\n"+toSave(starE)+"\n"+scenario+"\n"+extension+"\n"+shopGsave+"\n"+listGsave+"\n"+shopEsave+"\n"+listEsave);

            for(int i=0; i<players.size();i++){
                //writer.append("#\nname\nlife\ndamage\nmoney\nia\nheal\npriorityPuchase\ncompeletedMission\n&mission+mission+mission\n&listeBase\n&listHeroes\n&cardplayed\n&deckP\n&discp\n&pickP");
                Player playerTemp = players.get(i);
                writer.append("\n##########\n");
                writer.append(playerTemp.getName()+"\n"+playerTemp.getLife()+"\n"+playerTemp.getDamage()+"\n"+
                              playerTemp.getMoney()+"\n"+playerTemp.isIa()+"\n"+playerTemp.getHeal()+"\n"+playerTemp.getPriorityPurchase()+"\n"+
                              playerTemp.isCompletedMission()+"\n"+playerTemp.toSaveMission()+"\n"+playerTemp.toSave()+"\n"+
                              playerTemp.toSaveHeroes()+"\n"+cardPlayeds.get(i).toSave()+"\n"+cardPlayers.get(i).toSave()+"\n"+
                              disclaimerPlayers.get(i).toSave()+"\n"+pickPlayers.get(i).toSave());
                writer.flush();
            }

        }

    }

    
    /**
     * Cette methode enregistre les score en fin de partie dans un fichier score
     *
     */
    public static void saveScore(String saveScore, ArrayList<Player> p,int winner) throws IOException {// pseudo  :  score
        Player pWin = p.get(winner);
        int score = pWin.getDamage()*200+pWin.getHeal()*20+pWin.getHeroes().size()*17+pWin.getList().size()*36+pWin.getMoney()*38;
        Path dir = Path.of(saveScore);
        String line;
        StringBuilder strBld= new StringBuilder();
        HashMap<String,Integer> compartor = new HashMap<>();
        try(BufferedReader reader = Files.newBufferedReader(dir)){
            while ((line = reader.readLine()) != null) {
                strBld.append(line);
                String[] splited = line.split(":");
                compartor.put(splited[0].replace(" ",""),Integer.parseInt(splited[1].replace(" ","")));
            }
            compartor.put(p.get(winner).getName(),score);
            compartor=sortByValue(compartor);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(dir)) {
            int count=compartor.size();
            for (Map.Entry<String, Integer> en : compartor.entrySet()) {
                System.out.println("Classement:"+count+"  Pseudo:"+en.getKey()+"  Score:"+en.getValue()+"\n");
                writer.append(en.getKey()+"  :  "+en.getValue()+"\n");
                writer.flush();
                count--;
            }

        }

    }

    /**
     * Cette méthode permet de trier une HashMap selon les valeurs 
     *
     */
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hashMap) {
        List<Map.Entry<String, Integer> > list = new LinkedList<>(hashMap.entrySet());
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> puter : list) {
            temp.put(puter.getKey(), puter.getValue());
        }
        return temp;
    }
}
