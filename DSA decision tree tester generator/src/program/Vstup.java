package program;

public class Vstup {
	private String hladany = "";
	private char value = '0';
	
	public void addHladany(char a){
		hladany = hladany + a;
	}
	public String getHladany() {
		return this.hladany;
	}
	
	public void setValueTrue() {
		this.value = '1';
	}
	public char getValue() {
		return this.value;
	}
}
