package model;

public class Figura {
	private RodzajFigury nazwa;
	private KolorFigury kolor;
	private Punkt punkt;
	
	public Figura(Punkt punkt, RodzajFigury nazwa, KolorFigury kolor) {
		this.punkt = punkt;
		this.nazwa = nazwa;
		this.kolor = kolor;
	}

	public Punkt getPunkt(){
		return punkt;
	}
	
	public RodzajFigury getNazwa() {
		return nazwa;
	}

	public KolorFigury getKolor() {
		return kolor;
	}
	
	public void setPunkt(Punkt punkt){
		this.punkt = punkt;

	}
	
}
