package program;

import java.util.ArrayList;
import java.util.Random;

public class Vypis {
	private ArrayList<Node> zoznam = new ArrayList<Node>();
	
	public ArrayList<String> list_of_scrambled(String vstup, int pocet){
		//vytvorí zoznam rôznych poradí Stringu
		ArrayList<String> vysledok = new ArrayList<>();
		String scrambled_vstup;
		boolean obsahuje;
		for(int i = 0; i < pocet; i++) {
			scrambled_vstup = this.scramble(vstup);
			obsahuje = false;
			for(int j = 0; j < vysledok.size(); j++) {
				if(vysledok.get(j).equals(scrambled_vstup)){
					obsahuje = true;
					break;
				}
			}
			if(obsahuje)
				i--;
			else
				vysledok.add(scrambled_vstup);
		}
		return vysledok;
	}
	
	public String scramble(String vstup) {
		String vysledok = "";
		int poradie[] = new int[vstup.length()], r;
		Random rand = new Random();
		for(int i = 0; i < vstup.length(); i++) {
			r = rand.nextInt(vstup.length());
			if(poradie[r] == 0)
				poradie[r] = i + 1;
			else i--;
		}
		for(int i = 0; i < vstup.length(); i++) {
			vysledok = vysledok + vstup.charAt(poradie[i] - 1);
		}
		return vysledok;
	}
	
	public int scitajNodes(Node vrch) {
		//vložím unikátne nodes do zoznamu
		if(vrch == null)
			return 0;
		if(!zoznam.contains(vrch))
			zoznam.add(vrch);
		scitajNodes(vrch.getNextNode(false));
		scitajNodes(vrch.getNextNode(true));
		
		return zoznam.size();
	}
	
	public void leftToRight(Node vrch, int depth) {
		//výpis diagramu, iba na testovanie, nepoužíva sa
		if(vrch == null)
			return;
		
		this.leftToRight(vrch.getNextNode(false), depth + 1);
		
		
		System.out.print(vrch.getZnak());
		if(vrch.getValue() == true)
			System.out.print("\ttrue");
		else
			System.out.print("\tfalse");
		System.out.print("\t" + "depth: " + depth + "\theight: " + vrch.getHeight());
		
		System.out.println();
		
		this.leftToRight(vrch.getNextNode(true), depth + 1);
	}
}
