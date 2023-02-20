package program;

import java.util.ArrayList;

public class DiagramManager {
	
	private String poradie = "";
	private Node node0 = new Node('0', false);
	private Node node1 = new Node('1', true);
	ArrayList<Node> zoznam = new ArrayList<>();
	
	public BDD BDD_create(String vstup, String poradie) {
		this.poradie = poradie;
		Node vrch = new Node();
		
		String dnf = "";
		ArrayList<String> cleny = new ArrayList<String>();
		
		dnf = this.Dnf_simplify(vstup); //odstránim zbytočné znaky
		
		int f=0,l=0;//podelím dnf na formuly
		while(l<dnf.length()) {
		for(; l<dnf.length(); l++)
		{
			if(dnf.charAt(l) == '+')
				break;
		}
		cleny.add(dnf.substring(f,l));
		l++;
		f=l;
		}
		
		//zorganizuj podla dĺžky dnf
		cleny.sort((a,b)->Integer.compare(a.length(), b.length()));
		
		vrch = this.BDD_nextLayer(cleny, 0);
		
		//vrch = this.BDD_zjednodusNadbytocne(vrch);
		this.BDD_redukcia(vrch);
		
		BDD vysledok = new BDD(vrch, poradie.length());
		return vysledok;
	}
	
	private void zozbieraj(Node vrch, char a){ //zozbieram nodes podla ich znaku
		if(vrch == null)
			return;
		boolean left_found = false, right_found = false;
		if(vrch.getNextNode(false) != null && vrch.getNextNode(false).getZnak() == a) {
			if(!zoznam.contains(vrch))
				zoznam.add(vrch);
			left_found = true;
		}
		if(vrch.getNextNode(true) != null && vrch.getNextNode(true).getZnak() == a) {
			if(!zoznam.contains(vrch))
				zoznam.add(vrch);
			right_found = true;
		}
		if(!left_found)
			zozbieraj(vrch.getNextNode(false), a);
		if(!right_found)
			zozbieraj(vrch.getNextNode(true), a);
	}
	
	private void BDD_redukcia(Node vrch) {
		for(int k = poradie.length() - 1; k > 0; k--) {
		zoznam.clear();
		//char a = poradie.charAt(k);
		zozbieraj(vrch, poradie.charAt(k));
		//ArrayList<Node> zoznamTEST = zoznam;
		for(int i = 0; i < zoznam.size(); i++) {
			for(int j = i; j < zoznam.size(); j++) {
				if(i!=j && zoznam.get(i).getNextNode(false).getDnf().equals(zoznam.get(j).getNextNode(false).getDnf()))
					zoznam.get(j).setNextNode(zoznam.get(i).getNextNode(false), false);
				if(zoznam.get(i).getNextNode(false).getDnf().equals(zoznam.get(j).getNextNode(true).getDnf()))
					zoznam.get(j).setNextNode(zoznam.get(i).getNextNode(false), true);
				if(zoznam.get(i).getNextNode(true).getDnf().equals(zoznam.get(j).getNextNode(false).getDnf()))
					zoznam.get(j).setNextNode(zoznam.get(i).getNextNode(true), false);
				if(i!=j && zoznam.get(i).getNextNode(true).getDnf().equals(zoznam.get(j).getNextNode(true).getDnf()))
					zoznam.get(j).setNextNode(zoznam.get(i).getNextNode(true), true);
			}
		}
		}
	}
	
	/*private Node BDD_zjednodusNadbytocne(Node vrch) { //redukcia typu S, nepotrebná, lebo ju robím pri vytváraní diagramu
		if(vrch.getNextNode(false) == null || vrch.getNextNode(true) == null)
			return vrch;
		if(vrch.getNextNode(false).getHeight()>0)
			vrch.setNextNode(this.BDD_zjednodusNadbytocne(vrch.getNextNode(false)), false);
		if(vrch.getNextNode(true).getHeight()>0)
			vrch.setNextNode(this.BDD_zjednodusNadbytocne(vrch.getNextNode(true)), true);
		
		if(vrch.getNextNode(true) == vrch.getNextNode(false))
			return vrch.getNextNode(true);
		return vrch;
	}*/
	
	private String Dnf_simplify(String vstup) {
		String vysledok = "";
		for(int i = 0; i < vstup.length(); i++) {//odstránim zbytočné znaky
			if(vstup.charAt(i) == ' ' || vstup.charAt(i) == '*')
				continue;
			vysledok = vysledok + vstup.charAt(i);
		}
		return vysledok;
	}
	
	private char isTrue(String vstup, char hladany) {//pozrem či je v formuly a, !a alebo či tam nie je
		for(int i = 0; i < vstup.length(); i++) {
			if(vstup.charAt(i) == hladany) {
				if(i>0 && vstup.charAt(i - 1) == '!')
					return '0';
				else
					return '1';
			}
		}
		return '2';
	}
	
	private String Remove_character(String vstup, char deleted_char) { //odstránim daný charakter zo stringu a vrátim výsledný String
		String vysledok = "";
		if(vstup.charAt(0) == deleted_char) {
			return vstup.substring(1);
		}
		for(int i = 0; i < vstup.length(); i++) {
			if(vstup.charAt(i) == '!') {
				if(vstup.charAt(i+1) == deleted_char) {
					i = i+2;
				}
			}
			else if(vstup.charAt(i) == deleted_char)
				i++;
			if(i < vstup.length())
				vysledok = vysledok + vstup.charAt(i);
		}
		return vysledok;
	}
	
	private Node BDD_nextLayer(ArrayList<String> cleny, int index) {
		//rekurzívne vytváram diagram shannonovou dekompozíciou a redukujem
		char a = poradie.charAt(index);
		Node vrch = new Node(a, cleny);
		
		ArrayList<String> leftCleny = new ArrayList<>();
		ArrayList<String> rightCleny = new ArrayList<>();
		for(int i = 0; i < cleny.size(); i++) {
			switch(this.isTrue(cleny.get(i), a)) {
				case('0'):
					leftCleny.add(cleny.get(i));
				break;
				case('1'):
					rightCleny.add(cleny.get(i));
				break;
				case('2'):
					leftCleny.add(cleny.get(i));
					rightCleny.add(cleny.get(i));
				break;
			}
		}
		
		if(leftCleny.size() == 0) {
			vrch.setNextNode(node0, false);
		}
		else
		for(int i = 0; i < leftCleny.size(); i++) {
			leftCleny.set(i, this.Remove_character(leftCleny.get(i), a));
			if(leftCleny.get(i) == "")
				vrch.setNextNode(node1, false);
		}
		
		if(rightCleny.size() == 0) {
			vrch.setNextNode(node0, true);
		}
		else
		for(int i = 0; i < rightCleny.size(); i++) {
			rightCleny.set(i, this.Remove_character(rightCleny.get(i), a));
			if(rightCleny.get(i) == "")
				vrch.setNextNode(node1, true);
		}
		
		if(vrch.getNextNode(false) == null)
		vrch.setNextNode(this.BDD_nextLayer(leftCleny, index + 1), false);
		
		if(vrch.getNextNode(true) == null)
		vrch.setNextNode(this.BDD_nextLayer(rightCleny, index + 1), true);
		
		if(vrch.getNextNode(true).getDnf().equals(vrch.getNextNode(false).getDnf()))
			return vrch.getNextNode(false);
		return vrch;
	}
	
	public char BDD_use(Node vrch, String vstupy) {
		if(vrch == null)
			return '0';
		if(vrch.getValue())
			return '1';
		if(vrch.getZnak() == '0')
			return '0';
		
		int i = 0;
		while(vrch.getZnak() != poradie.charAt(i))
			i++;
		if(vstupy.charAt(i) == '1')
			return BDD_use(vrch.getNextNode(true), vstupy);
		if(vstupy.charAt(i) == '0')
			return BDD_use(vrch.getNextNode(false), vstupy);
		return '2';
	}
	
	public void setPoradie(String poradie) {
		this.poradie = poradie;
	}
}
