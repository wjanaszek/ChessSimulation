package model;

public class Punkt {
	private int x;
	private int y;
	
	public Punkt(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean rowneWspolrzedne(Punkt p1, Punkt p2){
		if(p1.x == p2.x && p1.y == p2.y){
			return true;
		}
		else {
			return false;
		}
	}
	
}
