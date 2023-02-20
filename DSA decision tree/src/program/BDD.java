package program;

public class BDD {
	private int nPremennych;
	private int size;
	private Node vrch;
	
	public BDD(Node vrch, int nPremennych){
		this.vrch = vrch;
		Vypis vypis = new Vypis();
		this.size = vypis.scitajNodes(vrch);
		this.nPremennych = nPremennych;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public Node getVrch() {
		return this.vrch;
	}
	
	public int getNPremennych() {
		return this.nPremennych;
	}
}
