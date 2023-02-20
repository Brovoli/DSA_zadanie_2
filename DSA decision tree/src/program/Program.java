package program;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

public class Program {
	public static void main(String[] args) {
		BDD_vstupy bdd_vstupy = new BDD_vstupy();
		Vypis vypis = new Vypis();
		
		try {	//načítavam údaje zo súboru
			String line;
			File file = new File("Test.txt");
			Scanner scanner = new Scanner(file);
			int i;
			i = 0;
			line = scanner.nextLine();
			while(line.charAt(i) != ' ') {
				bdd_vstupy.addCleny(line.charAt(i));
				i++;
			}
			bdd_vstupy.setDnf(line.substring(i));
			while(scanner.hasNext()) {
				line = scanner.nextLine();
				bdd_vstupy.addVstup(line.substring(0, line.length()-2));
				bdd_vstupy.addHodnota(line.charAt(line.length()- 1));
			}
			scanner.close();
		}catch(Exception e) {
			System.out.println("Error opening file");
			System.out.println(e.getMessage());
		}
			
			//kontrolny vypis
		/*System.out.println(bdd_vstupy.getCleny());
		System.out.println(bdd_vstupy.getDnf());
		for(int k = 0; k < bdd_vstupy.getVstupy().size(); k++){
			System.out.println(bdd_vstupy.getVstupy().get(k) + " " + bdd_vstupy.getHodnota().charAt(k));
		}*/
		

		BDD bdd, bdd_n;
		DiagramManager manager = new DiagramManager();
		Instant starts;
		Instant ends;
		
		String vstup_poradie = bdd_vstupy.getCleny();
		starts = Instant.now();
		bdd = manager.BDD_create(bdd_vstupy.getDnf(), vstup_poradie);
		ends = Instant.now();
		System.out.println("Počet nodes prvého stromu: " + bdd.getSize() + " Čas: " + Duration.between(starts, ends).toMillis() + "ms");
		
		ArrayList<String> scrambled;
		int bruteforce_attempts = 3 * vstup_poradie.length();
		scrambled = vypis.list_of_scrambled(vstup_poradie, bruteforce_attempts);
		
		int avg_bdd = 0;
		for(int i = 0; i < bruteforce_attempts; i++) {
		vstup_poradie = scrambled.get(i);
		
		starts = Instant.now();
		bdd_n = manager.BDD_create(bdd_vstupy.getDnf(), vstup_poradie);
		ends = Instant.now();
		avg_bdd += Duration.between(starts, ends).toMillis();
		//System.out.println("Dĺžka vytvárania: " + Duration.between(starts, ends).toMillis() + " ms");
		System.out.println("Počet nodes stromu: " + bdd_n.getSize());
		if(bdd_n.getSize() < bdd.getSize())
			bdd = bdd_n;
		}
		avg_bdd = avg_bdd / bruteforce_attempts;
		System.out.println("Priemerná dĺžka vytvárania: " + avg_bdd + " ms");

		char vystup;
		manager.setPoradie(bdd_vstupy.getCleny());
		int avg_search = 0;
		for(int k = 0; k < bdd_vstupy.getVstupy().size(); k++)
			{
			starts = Instant.now();
			vystup = manager.BDD_use(bdd.getVrch(), bdd_vstupy.getVstupy().get(k));
			ends = Instant.now();
			//System.out.println("Pre vstup: " + bdd_vstupy.getVstupy().get(k) + " výsledok: " + vystup);
			if(vystup != bdd_vstupy.getHodnota().charAt(k))
				System.out.println("Nastala chyba pri prehladávaní!");
			avg_search += Duration.between(starts, ends).toNanos();
			}
		avg_search = avg_search / bdd_vstupy.getVstupy().size();
		System.out.println("Priemerná dĺžka vyhladávania: " + avg_search + " nano sekúnd");
		int maxSize = (int) Math.pow(2, bdd.getNPremennych() + 1) - 1;
		System.out.println("Pôvodná velkosť: " + maxSize + "\tPočet premenných: " + bdd.getNPremennych());
		System.out.println("Počet nodes: " + bdd.getSize());
		System.out.println("Redukcia v %: " + (100 - bdd.getSize() * 100 / maxSize));
		}
	
}
