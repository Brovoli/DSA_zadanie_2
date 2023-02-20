package program;

import java.util.ArrayList;

public class BDD_vstupy {
	String cleny = "";
	String dnf = "";
	ArrayList<String> vstupy = new ArrayList<>();
	String hodnota = "";
	public void addHodnota(char a) {
		this.hodnota = this.hodnota + a;
	}
	public void setCleny(String cleny){
		this.cleny = cleny;
	}
	public void addCleny(char a) {
		this.cleny = this.cleny + a;
	}
	public void setDnf(String dnf) {
		this.dnf = dnf;
	}
	public void addVstup(String vstup) {
		this.vstupy.add(vstup);
	}
	
	public String getCleny() {
		return this.cleny;
	}
	public String getDnf() {
		return this.dnf;
	}
	public ArrayList<String> getVstupy(){
		return  this.vstupy;
	}
	public String getHodnota() {
		return this.hodnota;
	}
}
