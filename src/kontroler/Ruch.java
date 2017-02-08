package kontroler;

import model.Punkt;
import model.RodzajFigury;

/*
 * Klasa reprezentujaca ruch - zawiera punkt poczatkowy, punkt docelowy, oraz czy nastapiloby (gdyby ruch ten zostal wybrany) zbicie krola
 * konczace rozgrywke
 */
public class Ruch {
	private Punkt poczatkowy;
	private Punkt docelowy;
	public boolean zbityKrol = false;
	private RodzajFigury figura;
	
	public Ruch(Punkt poczatkowy, Punkt docelowy, RodzajFigury figura){
		this.poczatkowy = poczatkowy;
		this.docelowy = docelowy;
		this.figura = figura;
	}

	public Punkt getPoczatkowy() {
		return poczatkowy;
	}

	public Punkt getDocelowy() {
		return docelowy;
	}
	
	public RodzajFigury getFigure(){
		return figura;
	}
}
