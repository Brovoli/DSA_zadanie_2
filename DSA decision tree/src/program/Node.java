package program;

import java.util.ArrayList;

public class Node {
	private boolean value;
	private char znak;
	private Node leftNode;
	private Node rightNode;
	private String dnf = "";
	Node(){
		this.znak = 0;
		value = false;
		leftNode = null;
		rightNode = null;
	}
	Node(boolean value){
		this.znak = 0;
		this.value = value;
		leftNode = null;
		rightNode = null;
	}
	Node(char znak){
		this.znak = znak;
		value = false;
		leftNode = null;
		rightNode = null;
	}
	Node(char znak, ArrayList<String> formuly){
		this.znak = znak;
		value = false;
		leftNode = null;
		rightNode = null;
		dnf = dnf + formuly.get(0);
		for(int i = 1; i < formuly.size(); i++) {
			dnf = dnf + "+" + formuly.get(i);
		}
	}
	Node(char znak, boolean value){
		this.znak = znak;
		this.value = value;
		leftNode = null;
		rightNode = null;
		if(znak == '0')
			dnf = "0";
		if(znak == '1')
			dnf = "1";
	}
	

	public String getDnf() {
		return this.dnf;
	}
	
	public void setZnak(char znak) {
		this.znak = znak;
	}
	public char getZnak() {
		return znak;
	}
	
	public boolean getValue() {
		return this.value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}
	
	public void setNextNode(Node node, boolean value){
		if(value)
			this.rightNode = node;
		else
			this.leftNode = node;
	}
	public Node getNextNode(boolean value) {
		if(value)
			return this.rightNode;
		else
			return this.leftNode;
	}
	
	public int getHeight() {
		if(this.leftNode == null && this.rightNode == null)
			return 0;
		int lheight, rheight;
		lheight = this.leftNode.getHeight();
		rheight = this.rightNode.getHeight();
		if(lheight > rheight)
			return lheight + 1;
		return rheight + 1;
	}
}
