package model;

import java.util.ArrayList;

public class Model {
	private char[][] plansza;
	private ArrayList<Figura> figuryBiale;
	private ArrayList<Figura> figuryCzarne;
	
	public Model(){
		plansza = new char[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				plansza[i][j] = '.';
			}
		}
		figuryBiale = new ArrayList<Figura>();
		figuryCzarne = new ArrayList<Figura>();
		dodajPionki();
		dodajSkoczki();
		dodajGonce();
		dodajWieze();
		dodajHetmanow();
		dodajKrolow();
	}
	
	private void dodajPionki(){
		for(int i = 0; i < 8; i++){
			plansza[1][i] = 'P';
			figuryBiale.add(new Figura(new Punkt(i, 1), RodzajFigury.PIONEK, KolorFigury.BIALY));
		}
		for(int i = 0; i < 8; i++){
			plansza[6][i] = 'p';
			figuryCzarne.add(new Figura(new Punkt(i, 6), RodzajFigury.PIONEK, KolorFigury.CZARNY));
		}
	}
	
	public char[][] getPlansza() {
		return plansza;
	}

	public ArrayList<Figura> getFiguryBiale() {
		return figuryBiale;
	}

	public ArrayList<Figura> getFiguryCzarne() {
		return figuryCzarne;
	}

	private void dodajSkoczki(){
		/* dodaj biale: */
		plansza[0][1] = 'S';
		plansza[0][6] = 'S';
		figuryBiale.add(new Figura(new Punkt(1, 0), RodzajFigury.SKOCZEK, KolorFigury.BIALY));
		figuryBiale.add(new Figura(new Punkt(6, 0), RodzajFigury.SKOCZEK, KolorFigury.BIALY));
		
		/* dodaj czarne: */
		plansza[7][1] = 's';
		plansza[7][6] = 's';
		figuryCzarne.add(new Figura(new Punkt(1, 7),  RodzajFigury.SKOCZEK, KolorFigury.CZARNY));
		figuryCzarne.add(new Figura(new Punkt(6, 7),  RodzajFigury.SKOCZEK, KolorFigury.CZARNY));
	}
	
	private void dodajGonce(){
		/* dodaj biale: */
		plansza[0][2] = 'G';
		plansza[0][5] = 'G';
		figuryBiale.add(new Figura(new Punkt(2, 0), RodzajFigury.GONIEC, KolorFigury.BIALY));
		figuryBiale.add(new Figura(new Punkt(5, 0), RodzajFigury.GONIEC, KolorFigury.BIALY));
		
		/* dodaj czarne: */
		plansza[7][2] = 'g';
		plansza[7][5] = 'g';
		figuryCzarne.add(new Figura(new Punkt(2, 7), RodzajFigury.GONIEC, KolorFigury.CZARNY));
		figuryCzarne.add(new Figura(new Punkt(5, 7), RodzajFigury.GONIEC, KolorFigury.CZARNY));
	}
	
	private void dodajWieze(){
		/* dodaj biale: */
		plansza[0][0] = 'W';
		plansza[0][7] = 'W';
		figuryBiale.add(new Figura(new Punkt(0, 0), RodzajFigury.WIEZA, KolorFigury.BIALY));
		figuryBiale.add(new Figura(new Punkt(7, 0), RodzajFigury.WIEZA, KolorFigury.BIALY));
		
		/* dodaj czarne: */
		plansza[7][0] = 'w';
		plansza[7][7] = 'w';
		figuryCzarne.add(new Figura(new Punkt(0, 7), RodzajFigury.WIEZA, KolorFigury.CZARNY));
		figuryCzarne.add(new Figura(new Punkt(7, 7), RodzajFigury.WIEZA, KolorFigury.CZARNY));
	}
	
	private void dodajHetmanow(){
		/* dodaj biale: */
		plansza[0][3] = 'H';
		figuryBiale.add(new Figura(new Punkt(3, 0), RodzajFigury.HETMAN, KolorFigury.BIALY));
		
		/* dodaj czarne: */
		plansza[7][3] = 'h';
		figuryCzarne.add(new Figura(new Punkt(3, 7), RodzajFigury.HETMAN, KolorFigury.CZARNY));
	}
	
	private void dodajKrolow(){
		/* dodaj biale: */
		plansza[0][4] = 'K';
		figuryBiale.add(new Figura(new Punkt(4, 0), RodzajFigury.KROL, KolorFigury.BIALY));
		
		/* dodaj czarne: */
		plansza[7][4] = 'k';
		figuryCzarne.add(new Figura(new Punkt(4, 7), RodzajFigury.KROL, KolorFigury.CZARNY));
	}
}
