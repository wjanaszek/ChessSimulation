package kontroler;

import model.Punkt;
import model.RodzajFigury;

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
		//
	}
}
