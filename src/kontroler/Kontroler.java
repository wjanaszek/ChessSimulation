package kontroler;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import model.Figura;
import model.KolorFigury;
import model.Model;
import model.Punkt;
import model.RodzajFigury;
import widok.Widok;

/*
 * Klasa sterujaca przebiegiem gry 
 */
public class Kontroler {
	private Model model;
	private Widok widok;
	
	public Kontroler(Model model, Widok widok){
		this.model = model;
		this.widok = widok;
	}
	
	/*
	 * Glowna petla programu. Steruje gra dwoch graczy. Najpierw ruch wykonuja biale (jesli nie maja zadnego ruchu do wykonania, to petla jest przerywana
	 * i program konczy prace). Potem ruch wykonuja czarne (i tak samo - jesli nie maja zadnego ruchu do wykonania, to program konczy prace). Wewnatrz metod wykonujacych
	 * ruch bialych lub czarnych bierek sprawdzane jest, czy nie zostal zbity krol przeciwnika, aby rowniez wtedy zakonczyc gre.
	 */
	public void start(){
		int liczbaRuchow = 0;
		int tmp;
		boolean zbityKrol = false;
		boolean bialeDrapiezne = false;
		boolean czarneDrapiezne = false;
		
		// ustaw zmienne bialeDrapiezne i czarneDrapiezne, odpowiadajace za to, czy gracz jest "drapiezny", czy tez nie
		ArrayList<Integer> drapiezni = znajdzDrapieznychGraczy();
		if(drapiezni.size() > 0){
			if(drapiezni.get(0) == 1){
				bialeDrapiezne = true;
			}
			else if(drapiezni.get(0) == 2){
				czarneDrapiezne = true;
			}
			
			if(drapiezni.size() > 1){
				if(drapiezni.get(1) == 1){
					bialeDrapiezne = true;
				}
				else if(drapiezni.get(1) == 2){
					czarneDrapiezne = true;
				}
			}
		}
		
		widok.rysujPowitanie();
		widok.rysujPlansze();
		
		// rozpocznij symulacje
		while(true){
			tmp = liczbaRuchow;
			tmp++;
			
			// wyswietl komunikat o numerze tury:
			System.out.println();
			System.out.println("   Tura " + tmp);
			System.out.println();
			
			/* biale wykonuja ruch: */
			if(!bialeWykonajRuch(zbityKrol, bialeDrapiezne)){
				break;
			}
			widok.rysujPlansze();
			System.out.println();
			
			/* czarne wykonuja ruch: */
			if(!czarneWykonajRuch(zbityKrol, czarneDrapiezne)){
				break;
			}
			widok.rysujPlansze();
			System.out.println();
			
			// skonczyly sie ruchy, remis
			if(liczbaRuchow == 49){
				System.out.println("\t REMIS");
				break;
			}
			liczbaRuchow++;
		}
	}
	
	/*
	 * Pomocnicza metoda zwracajaca liste nr graczy, ktorzy maja byc "drapiezni"
	 */
	private ArrayList<Integer> znajdzDrapieznychGraczy(){
		ArrayList<Integer> res = new ArrayList<Integer>();
		System.out.println("Podaj, czy ktorys z graczy ma byc \"drapiezny\" (mozliwe wybory: 1, 1:2, 2, ZADEN)");
		Scanner sc = new Scanner(System.in);
		String s = sc.nextLine();
		if(s.equals("ZADEN")){
			return res;
		}
		String[] tab = s.split(":");
		res.add(Integer.valueOf(tab[0]));
		if(tab.length > 1){
			res.add(Integer.valueOf(tab[1]));
		}
		return res;
	}
	
	/*
	 * Metoda sluzaca do wykonania ruchu przez biale bierki
	 * @returns false, gdy nie ma mozliwych ruchow do wykonania, lub zostal zbity krol przeciwnika. True w przeciwnym przypadku
	 */
	private boolean bialeWykonajRuch(boolean zbityKrol, boolean drapiezny){
		Random r = new Random();
		int index;
		Ruch doWykonania;
		ArrayList<Ruch> listaRuchow = new ArrayList<Ruch>();
		for(int i = 0; i < model.getFiguryBiale().size(); i++){
			switch(model.getFiguryBiale().get(i).getNazwa()){
			case PIONEK:
				listaRuchow.addAll(getMozliweRuchyPionka(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));		// scal listy
				break;
			case WIEZA:
				listaRuchow.addAll(getMozliweRuchyWiezy(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));		// scal listy
				break;
			case SKOCZEK:
				listaRuchow.addAll(getMozliweRuchySkoczka(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));	// scal listy
				break;
			case GONIEC:
				listaRuchow.addAll(getMozliweRuchyGonca(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));		// scal listy
				break;
			case HETMAN:
				listaRuchow.addAll(getMozliweRuchyHetmana(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));	// scal listy
				break;
			case KROL:
				listaRuchow.addAll(getMozliweRuchyKrola(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));		// scal listy
				break;
			}
		}
		if(listaRuchow.isEmpty()){		// oznacza to, że nie było żadnego ruchu do wykonania
			return false;
		}
		// wylosuj indeks (ruch):
		index = r.nextInt(listaRuchow.size());
		doWykonania = listaRuchow.get(index);	// wybierz element (ruch) znajdujacy sie pod tym indeksem
		
		zbityKrol = doWykonania.zbityKrol;
		int zrX, zrY, docX, docY, tmpY1, tmpY2;
		int zbityX, zbityY;
		char tmp;
		zrX = doWykonania.getPoczatkowy().getX();
		zrY = doWykonania.getPoczatkowy().getY();
		docX = doWykonania.getDocelowy().getX();
		docY = doWykonania.getDocelowy().getY();
		
		// znajdz wlasciwa figure w liscie figur danego gracza i zmien wartosci na planszy:
		for(int i = 0; i < model.getFiguryBiale().size(); i++){
			if(model.getFiguryBiale().get(i).getPunkt().getX() == zrX && 
					model.getFiguryBiale().get(i).getPunkt().getY() == zrY){
				model.getFiguryBiale().get(i).getPunkt().setX(docX);
				model.getFiguryBiale().get(i).getPunkt().setY(docY);
				model.getPlansza()[zrY][zrX] = '.';
				tmp = getCharFromRodzajFiguryAndKolorFigury(doWykonania.getFigure(), KolorFigury.BIALY);
				tmpY1 = zrY + 1;
				tmpY2= docY + 1;
				if(model.getPlansza()[docY][docX] != '.'){		// jesli zostanie zbita figura przeciwnika
					zbityX = docX;
					zbityY = docY;
					usunZbitaFigureZListy(model.getFiguryCzarne(), zbityX, zbityY);
				}
				model.getPlansza()[docY][docX] = tmp;
				
				// wyswietl komunikat o ruchu:
				System.out.println();
				System.out.println("Ruch " + tmp + " z " + getCharFromInt(zrX) + tmpY1 + " na " + getCharFromInt(docX) + tmpY2);
				System.out.println();
				break;
			}
		}
		if(zbityKrol){		// krol zostal zbity, nalezy przerwac rozrywke
			System.out.println();
			System.out.println("Biale zbily krola!");
			return false;
		}
		return true;
	}
	
	/*
	 * Analogiczna metoda jak dla bialych bierek - sluzy do wykonania ruchu przez czarne bierki
	 */
	private boolean czarneWykonajRuch(boolean zbityKrol, boolean drapiezny){
		Random r = new Random();
		int index;
		Ruch doWykonania;
		ArrayList<Ruch> listaRuchow = new ArrayList<Ruch>();
		for(int i = 0; i < model.getFiguryCzarne().size(); i++){
			switch(model.getFiguryCzarne().get(i).getNazwa()){
			case PIONEK:
				listaRuchow.addAll(getMozliweRuchyPionka(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));	// scal listy
				break;
			case WIEZA:
				listaRuchow.addAll(getMozliweRuchyWiezy(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));	// scal listy
				break;
			case SKOCZEK:
				listaRuchow.addAll(getMozliweRuchySkoczka(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));	// scal listy
				break;
			case GONIEC:
				listaRuchow.addAll(getMozliweRuchyGonca(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));	// scal listy
				break;
			case HETMAN:
				listaRuchow.addAll(getMozliweRuchyHetmana(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));	// scal listy
				break;
			case KROL:
				listaRuchow.addAll(getMozliweRuchyKrola(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));	// scal listy
				break;
			}
		}
		if(listaRuchow.isEmpty()){
			return false;
		}
		// losowanie ruchu:
		index = r.nextInt(listaRuchow.size());
		doWykonania = listaRuchow.get(index);
		
		zbityKrol = doWykonania.zbityKrol;
		int zrX, zrY, docX, docY, tmpY1, tmpY2;
		int zbityX, zbityY;
		char tmp;
		zrX = doWykonania.getPoczatkowy().getX();
		zrY = doWykonania.getPoczatkowy().getY();
		docX = doWykonania.getDocelowy().getX();
		docY = doWykonania.getDocelowy().getY();
		
		for(int i = 0; i < model.getFiguryCzarne().size(); i++){
			if(model.getFiguryCzarne().get(i).getPunkt().getX() == zrX && 
					model.getFiguryCzarne().get(i).getPunkt().getY() == zrY){
				model.getFiguryCzarne().get(i).getPunkt().setX(docX);
				model.getFiguryCzarne().get(i).getPunkt().setY(docY);
				model.getPlansza()[zrY][zrX] = '.';
				tmp = getCharFromRodzajFiguryAndKolorFigury(doWykonania.getFigure(), KolorFigury.CZARNY);
				if(model.getPlansza()[docY][docX] != '.'){
					zbityX = docX;
					zbityY = docY;
					usunZbitaFigureZListy(model.getFiguryBiale(), zbityX, zbityY);
				}
				model.getPlansza()[docY][docX] = tmp;
				tmpY1 = zrY + 1;
				tmpY2 = docY + 1;
				// wyswietlenie komunikatu:
				System.out.println();
				System.out.println("Ruch " + tmp + " z " + getCharFromInt(zrX) + tmpY1 + " na " + getCharFromInt(docX) + tmpY2);
				System.out.println();
				break;
			}
		}
		if(zbityKrol){		// zostal zbity krol przeciwnika, nalezy przerwac rozgrywke
			System.out.println();
			System.out.println("Czarne zbily krola!");
			return false;
		}
		return true;
	}
	
	/*
	 * Metoda zwracajaca mozliwe ruchy pionka z pozycji okreslonej przez poczatkowyPunkt.
	 */
	private ArrayList<Ruch> getMozliweRuchyPionka(Punkt poczatkowyPunkt, KolorFigury kolor){
		ArrayList<Ruch> res = new ArrayList<Ruch>();
		Ruch tmp;
		final int x = poczatkowyPunkt.getX();
		final int y = poczatkowyPunkt.getY();
		
		if (kolor == KolorFigury.BIALY) {
			if (y + 1 != 8 && model.getPlansza()[y + 1][x] == '.') {
				res.add(new Ruch(poczatkowyPunkt, new Punkt(x, y + 1), RodzajFigury.PIONEK));
			}
			if (y + 1 != 8 && x + 1 != 8 && model.getPlansza()[y + 1][x + 1] != '.' 
					&& model.getPlansza()[y + 1][x + 1] != 'W' 
					&& model.getPlansza()[y + 1][x + 1] != 'S' 
					&& model.getPlansza()[y + 1][x + 1] != 'H' 
					&& model.getPlansza()[y + 1][x + 1] != 'K' 
					&& model.getPlansza()[y + 1][x + 1] != 'P' 
					&& model.getPlansza()[y + 1][x + 1] != 'G') {
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 1, y + 1), RodzajFigury.PIONEK);
				if(model.getPlansza()[y + 1][x + 1] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 1, y + 1)));
				res.add(tmp);
			} 
			if (y + 1 != 8 && x - 1 != -1 && model.getPlansza()[y + 1][x - 1] != '.' 
					&& model.getPlansza()[y + 1][x - 1] != 'W' 
					&& model.getPlansza()[y + 1][x - 1] != 'S'
					&& model.getPlansza()[y + 1][x - 1] != 'H' 
					&& model.getPlansza()[y + 1][x - 1] != 'K' 
					&& model.getPlansza()[y + 1][x - 1] != 'P' 
					&& model.getPlansza()[y + 1][x - 1] != 'G') {
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 1, y + 1), RodzajFigury.PIONEK);
				if(model.getPlansza()[y + 1][x - 1] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 1, y + 1)));
				res.add(tmp);
			}
		}
		else if(kolor == KolorFigury.CZARNY){
			if (y - 1 != -1 && model.getPlansza()[y - 1][x] == '.') {
				res.add(new Ruch(poczatkowyPunkt, new Punkt(x, y - 1), RodzajFigury.PIONEK));
			}
			if (y - 1 != -1 && x + 1 != 8 && model.getPlansza()[y - 1][x + 1] != '.' 
					&& model.getPlansza()[y - 1][x + 1] != 'p'
					&& model.getPlansza()[y - 1][x + 1] != 's'
					&& model.getPlansza()[y - 1][x + 1] != 'h'
					&& model.getPlansza()[y - 1][x + 1] != 'k'
					&& model.getPlansza()[y - 1][x + 1] != 'g'
					&& model.getPlansza()[y - 1][x + 1] != 'w') {
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 1, y - 1), RodzajFigury.PIONEK);
				if(model.getPlansza()[y - 1][x + 1] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 1, y + 1)));
				res.add(tmp);
			} 
			if (y - 1 != -1 && x - 1 != -1 && model.getPlansza()[y - 1][x - 1] != '.' 
					&& model.getPlansza()[y - 1][x - 1] != 'p'
					&& model.getPlansza()[y - 1][x - 1] != 's'
					&& model.getPlansza()[y - 1][x - 1] != 'h'
					&& model.getPlansza()[y - 1][x - 1] != 'k'
					&& model.getPlansza()[y - 1][x - 1] != 'g'
					&& model.getPlansza()[y - 1][x - 1] != 'w'){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 1, y - 1), RodzajFigury.PIONEK);
				if(model.getPlansza()[y - 1][x - 1] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 1, y - 1)));
				res.add(tmp);
			}
		}
		return res;
	}
	
	/*
	 * Metoda zwracajaca mozliwe ruchy wiezy z pozycji okreslonej przez poczatkowyPunkt.
	 */
	private ArrayList<Ruch> getMozliweRuchyWiezy(Punkt poczatkowyPunkt, KolorFigury kolor){
		ArrayList<Ruch> res = new ArrayList<Ruch>();
		Ruch tmp;
		final int x = poczatkowyPunkt.getX();
		final int y = poczatkowyPunkt.getY();
		switch(kolor){
		case BIALY:
			ArrayList<Punkt> wspBiale = getMozliwePunktyProsto(poczatkowyPunkt, kolor);		// znajdz punkty, do ktorych moze sie przemiescic wieza
			for(int i = 0; i < wspBiale.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, wspBiale.get(i), RodzajFigury.WIEZA);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(tmp.getDocelowy().getX(), tmp.getDocelowy().getY())));
				res.add(tmp);		// dodaj je do listy ruchow
			}
			break;
		case CZARNY:
			ArrayList<Punkt> wspCzarne = getMozliwePunktyProsto(poczatkowyPunkt, kolor);	// znajdz punkty, do ktorych moze sie przemiescic wieza
			for(int i = 0; i < wspCzarne.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, wspCzarne.get(i), RodzajFigury.WIEZA);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(tmp.getDocelowy().getX(), tmp.getDocelowy().getY())));
				res.add(tmp);		// dodaj je do listy ruchow
			}
			break;
		}
		
		return res;
	}
	
	/*
	 * Metoda zwracajaca mozliwe ruchy skoczka z pozycji okreslonej przez poczatkowyPunkt.
	 */
	private ArrayList<Ruch> getMozliweRuchySkoczka(Punkt poczatkowyPunkt, KolorFigury kolor){
		ArrayList<Ruch> res = new ArrayList<Ruch>();
		Ruch tmp;
		int x = poczatkowyPunkt.getX();
		int y = poczatkowyPunkt.getY();
		
		switch(kolor){
		case BIALY:
			if(x + 2 <= 7 && y + 1 <= 7 && (model.getPlansza()[y + 1][x + 2] == 'w'
				|| model.getPlansza()[y + 1][x + 2] == 's'
				|| model.getPlansza()[y + 1][x + 2] == 'h'
				|| model.getPlansza()[y + 1][x + 2] == 'k'
				|| model.getPlansza()[y + 1][x + 2] == 'p'
				|| model.getPlansza()[y + 1][x + 2] == 'g'
				|| model.getPlansza()[y + 1][x + 2] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 2, y + 1), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y + 1][x + 2] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 2, y + 1)));
				res.add(tmp);
			}
			if(x + 2 <= 7 && y - 1 >= 0 && (model.getPlansza()[y - 1][x + 2] == 'w'
				|| model.getPlansza()[y - 1][x + 2] == 's'
				|| model.getPlansza()[y - 1][x + 2] == 'h'
				|| model.getPlansza()[y - 1][x + 2] == 'k'
				|| model.getPlansza()[y - 1][x + 2] == 'p'
				|| model.getPlansza()[y - 1][x + 2] == 'g'
				|| model.getPlansza()[y - 1][x + 2] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 2, y - 1), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y - 1][x + 2] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 2, y - 1)));
				res.add(tmp);
			}
			if(x + 1 <= 7 && y + 2 <= 7 && (model.getPlansza()[y + 2][x + 1] == 'w'
				|| model.getPlansza()[y + 2][x + 1] == 's'
				|| model.getPlansza()[y + 2][x + 1] == 'h'
				|| model.getPlansza()[y + 2][x + 1] == 'k'
				|| model.getPlansza()[y + 2][x + 1] == 'p'
				|| model.getPlansza()[y + 2][x + 1] == 'g'
				|| model.getPlansza()[y + 2][x + 1] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 1, y + 2), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y + 2][x + 1] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 1, y + 2)));
				res.add(tmp);
			}
			if(x - 1 >= 0 && y + 2 <= 7 && (model.getPlansza()[y + 2][x - 1] == 'w'
				|| model.getPlansza()[y + 2][x - 1] == 's'
				|| model.getPlansza()[y + 2][x - 1] == 'h'
				|| model.getPlansza()[y + 2][x - 1] == 'k'
				|| model.getPlansza()[y + 2][x - 1] == 'p'
				|| model.getPlansza()[y + 2][x - 1] == 'g'
				|| model.getPlansza()[y + 2][x - 1] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 1, y + 2), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y + 2][x - 1] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 1, y + 2)));
				res.add(tmp);
			}
			if(x - 2 >= 0 && y + 1 <= 7 && (model.getPlansza()[y + 1][x - 2] == 'w'
				|| model.getPlansza()[y + 1][x - 2] == 's'
				|| model.getPlansza()[y + 1][x - 2] == 'h'
				|| model.getPlansza()[y + 1][x - 2] == 'k'
				|| model.getPlansza()[y + 1][x - 2] == 'p'
				|| model.getPlansza()[y + 1][x - 2] == 'g'
				|| model.getPlansza()[y + 1][x - 2] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 2, y + 1), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y + 1][x - 2] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 2, y + 1)));
				res.add(tmp);
			}
			if(x - 1 >= 0 && y - 2 >= 0 && (model.getPlansza()[y - 2][x - 1] == 'w'
				|| model.getPlansza()[y - 2][x - 1] == 's'
				|| model.getPlansza()[y - 2][x - 1] == 'h'
				|| model.getPlansza()[y - 2][x - 1] == 'k'
				|| model.getPlansza()[y - 2][x - 1] == 'p'
				|| model.getPlansza()[y - 2][x - 1] == 'g'
				|| model.getPlansza()[y - 2][x - 1] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 1, y - 2), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y - 2][x - 1] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 1, y - 2)));
				res.add(tmp);
			}
			if(x - 2 >= 0 && y - 1 >= 0 && (model.getPlansza()[y - 1][x - 2] == 'w'
				|| model.getPlansza()[y - 1][x - 2] == 's'
				|| model.getPlansza()[y - 1][x - 2] == 'h'
				|| model.getPlansza()[y - 1][x - 2] == 'k'
				|| model.getPlansza()[y - 1][x - 2] == 'p'
				|| model.getPlansza()[y - 1][x - 2] == 'g'
				|| model.getPlansza()[y - 1][x - 2] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 2, y - 1), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y - 1][x - 2] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 2, y - 1)));
				res.add(tmp);
			}
			if(x + 1 <= 7 && y - 2 >= 0 && (model.getPlansza()[y - 2][x + 1] == 'w'
				|| model.getPlansza()[y - 2][x + 1] == 's'
				|| model.getPlansza()[y - 2][x + 1] == 'h'
				|| model.getPlansza()[y - 2][x + 1] == 'k'
				|| model.getPlansza()[y - 2][x + 1] == 'p'
				|| model.getPlansza()[y - 2][x + 1] == 'g'
				|| model.getPlansza()[y - 2][x + 1] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 1, y - 2), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y - 2][x + 1] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 1, y - 2)));
				res.add(tmp);
			}
			break;
		case CZARNY:
			if(x + 2 <= 7 && y + 1 <= 7 && (model.getPlansza()[y + 1][x + 2] == 'W'
				|| model.getPlansza()[y + 1][x + 2] == 'S'
				|| model.getPlansza()[y + 1][x + 2] == 'H'
				|| model.getPlansza()[y + 1][x + 2] == 'K'
				|| model.getPlansza()[y + 1][x + 2] == 'P'
				|| model.getPlansza()[y + 1][x + 2] == 'G'
				|| model.getPlansza()[y + 1][x + 2] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 2, y + 1), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y + 1][x + 2] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 2, y + 1)));
				res.add(tmp);
			}
			if(x + 2 <= 7 && y - 1 >= 0 && (model.getPlansza()[y - 1][x + 2] == 'W'
				|| model.getPlansza()[y - 1][x + 2] == 'S'
				|| model.getPlansza()[y - 1][x + 2] == 'H'
				|| model.getPlansza()[y - 1][x + 2] == 'K'
				|| model.getPlansza()[y - 1][x + 2] == 'P'
				|| model.getPlansza()[y - 1][x + 2] == 'G'
				|| model.getPlansza()[y - 1][x + 2] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 2, y - 1), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y - 1][x + 2] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 2, y - 1)));
				res.add(tmp);
			}
			if(x + 1 <= 7 && y + 2 <= 7 && (model.getPlansza()[y + 2][x + 1] == 'W'
				|| model.getPlansza()[y + 2][x + 1] == 'S'
				|| model.getPlansza()[y + 2][x + 1] == 'H'
				|| model.getPlansza()[y + 2][x + 1] == 'K'
				|| model.getPlansza()[y + 2][x + 1] == 'P'
				|| model.getPlansza()[y + 2][x + 1] == 'G'
				|| model.getPlansza()[y + 2][x + 1] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 1, y + 2), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y + 2][x + 1] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 1, y + 2)));
				res.add(tmp);
			}
			if(x - 1 >= 0 && y + 2 <= 7 && (model.getPlansza()[y + 2][x - 1] == 'W'
				|| model.getPlansza()[y + 2][x - 1] == 'S'
				|| model.getPlansza()[y + 2][x - 1] == 'H'
				|| model.getPlansza()[y + 2][x - 1] == 'K'
				|| model.getPlansza()[y + 2][x - 1] == 'P'
				|| model.getPlansza()[y + 2][x - 1] == 'G'
				|| model.getPlansza()[y + 2][x - 1] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 1, y + 2), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y + 2][x - 1] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 1, y + 2)));
				res.add(tmp);
			}
			if(x - 2 >= 0 && y + 1 <= 7 && (model.getPlansza()[y + 1][x - 2] == 'W'
				|| model.getPlansza()[y + 1][x - 2] == 'S'
				|| model.getPlansza()[y + 1][x - 2] == 'H'
				|| model.getPlansza()[y + 1][x - 2] == 'K'
				|| model.getPlansza()[y + 1][x - 2] == 'P'
				|| model.getPlansza()[y + 1][x - 2] == 'G'
				|| model.getPlansza()[y + 1][x - 2] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 2, y + 1), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y + 1][x - 2] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 2, y + 1)));
				res.add(tmp);
			}
			if(x - 1 >= 0 && y - 2 >= 0 && (model.getPlansza()[y - 2][x - 1] == 'W'
				|| model.getPlansza()[y - 2][x - 1] == 'S'
				|| model.getPlansza()[y - 2][x - 1] == 'H'
				|| model.getPlansza()[y - 2][x - 1] == 'K'
				|| model.getPlansza()[y - 2][x - 1] == 'P'
				|| model.getPlansza()[y - 2][x - 1] == 'G'
				|| model.getPlansza()[y - 2][x - 1] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 1, y - 2), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y - 2][x - 1] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 1, y - 2)));
				res.add(tmp);
			}
			if(x - 2 >= 0 && y - 1 >= 0 && (model.getPlansza()[y - 1][x - 2] == 'W'
				|| model.getPlansza()[y - 1][x - 2] == 'S'
				|| model.getPlansza()[y - 1][x - 2] == 'H'
				|| model.getPlansza()[y - 1][x - 2] == 'K'
				|| model.getPlansza()[y - 1][x - 2] == 'P'
				|| model.getPlansza()[y - 1][x - 2] == 'G'
				|| model.getPlansza()[y - 1][x - 2] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 2, y - 1), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y - 1][x - 2] == 'K'){
					tmp.zbityKrol = true;
				}	
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 2, y - 1)));
				res.add(tmp);
			}
			if(x + 1 <= 7 && y - 2 >= 0 && (model.getPlansza()[y - 2][x + 1] == 'W'
				|| model.getPlansza()[y - 2][x + 1] == 'S'
				|| model.getPlansza()[y - 2][x + 1] == 'H'
				|| model.getPlansza()[y - 2][x + 1] == 'K'
				|| model.getPlansza()[y - 2][x + 1] == 'P'
				|| model.getPlansza()[y - 2][x + 1] == 'G'
				|| model.getPlansza()[y - 2][x + 1] == '.')){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 1, y - 2), RodzajFigury.SKOCZEK);
				if(model.getPlansza()[y - 2][x + 1] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 1, y - 2)));
				res.add(tmp);
			}
			break;
		}
		
		return res;
	}
	
	/*
	 * Metoda zwracajaca mozliwe ruchy gonca z pozycji okreslonej przez poczatkowyPunkt.
	 */
	private ArrayList<Ruch> getMozliweRuchyGonca(Punkt poczatkowyPunkt, KolorFigury kolor){
		ArrayList<Ruch> res = new ArrayList<Ruch>();
		Ruch tmp;
		switch(kolor){
		case BIALY:
			ArrayList<Punkt> wspBiale = getMozliwePunktyNaUkos(poczatkowyPunkt, kolor);		// znajdz wspolrzedne punktow, do ktorych moze sie przemiescic na ukos
			for(int i = 0; i < wspBiale.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, wspBiale.get(i), RodzajFigury.GONIEC);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(tmp.getDocelowy().getX(), tmp.getDocelowy().getY())));
				res.add(tmp);	// dodaj je do listy
			}
			break;
		case CZARNY:
			ArrayList<Punkt> wspCzarne = getMozliwePunktyNaUkos(poczatkowyPunkt, kolor);	// znajdz wspolrzedne punktow, do ktorych moze sie przemiesic na ukos
			for(int i = 0; i < wspCzarne.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, wspCzarne.get(i), RodzajFigury.GONIEC);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(tmp.getDocelowy().getX(), tmp.getDocelowy().getY())));
				res.add(tmp);	// dodaj je do listy
			}
			break;
		}
		return res;
	}

	/*
	 * Metoda zwracajaca mozliwe ruchy hetmana z pozycji okreslonej przez poczatkowyPunkt. "Sprawdza" punkty, do ktorych moze sie przemiescic na wprost, 
	 * i na ukos, i jesli nie sa zajete przez wlasne bierki, to dodaje ten ruch do listy mozliwych ruchow.
	 */
	private ArrayList<Ruch> getMozliweRuchyHetmana(Punkt poczatkowyPunkt, KolorFigury kolor){
		final int x = poczatkowyPunkt.getX();
		final int y = poczatkowyPunkt.getY();
		ArrayList<Ruch> res = new ArrayList<Ruch>();
		Ruch tmp;
		switch(kolor){
		case BIALY:
			ArrayList<Punkt> listaWspUkosBialy = getMozliwePunktyNaUkos(poczatkowyPunkt, kolor);	// znajdz wspolrzedne punktow, do ktorych moze sie przemiescic na ukos
			ArrayList<Punkt> listaWspProstoBialy = getMozliwePunktyProsto(poczatkowyPunkt, kolor);	// znajdz wspolrzedne punktow, do ktorych moze sie przemiescic na wprost
			for(int i = 0; i < listaWspUkosBialy.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, listaWspUkosBialy.get(i), RodzajFigury.HETMAN);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(tmp.getDocelowy().getX(), tmp.getDocelowy().getY())));
				res.add(tmp);	// dodaj te punkty do listy
			}
			for(int i = 0; i < listaWspProstoBialy.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, listaWspProstoBialy.get(i), RodzajFigury.HETMAN);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(tmp.getDocelowy().getX(), tmp.getDocelowy().getY())));
				res.add(tmp);	// dodaj te punkty do listy
			}
			break;
		case CZARNY:
			ArrayList<Punkt> listaWspUkosCzarny = getMozliwePunktyNaUkos(poczatkowyPunkt, kolor); 	// znajdz wspolrzedne punktow, do ktorych moze sie przemiescic na ukos
			ArrayList<Punkt> listaWspProstoCzarny = getMozliwePunktyProsto(poczatkowyPunkt, kolor);	// znajdz wspolrzedne punktow, do ktorych moze sie przemiescic na wprost
			for(int i = 0; i < listaWspUkosCzarny.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, listaWspUkosCzarny.get(i), RodzajFigury.HETMAN);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(tmp.getDocelowy().getX(), tmp.getDocelowy().getY())));
				res.add(tmp);	// dodaj te punkty do listy
			}
			for(int i = 0; i < listaWspProstoCzarny.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, listaWspProstoCzarny.get(i), RodzajFigury.HETMAN);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(tmp.getDocelowy().getX(), tmp.getDocelowy().getY())));
				res.add(tmp);	// dodaj te punkty do listy
			}
			break;
		}
		return res;
	}
	
	/*
	 * Metoda zwracajaca mozliwe ruchy krola z pozycji okreslonej przez poczatkowyPunkt. "Sprawdza" otoczenie krola - tzn. punkty,
	 * do ktorych moze sie przemiescic krol, i jesli nie sa zajete przez wlasne bierki, to dodaje ten ruch do listy mozliwych ruchow.
	 */
	private ArrayList<Ruch> getMozliweRuchyKrola(Punkt poczatkowyPunkt, KolorFigury kolor){
		ArrayList<Ruch> res = new ArrayList<Ruch>();
		Ruch tmp;
		final int x = poczatkowyPunkt.getX();
		final int y = poczatkowyPunkt.getY();
		switch(kolor){
		case BIALY:
			if(x - 1 != -1 && model.getPlansza()[y][x - 1] != 'W'
				&& model.getPlansza()[y][x - 1] != 'P'
				&& model.getPlansza()[y][x - 1] != 'S'
				&& model.getPlansza()[y][x - 1] != 'G'
				&& model.getPlansza()[y][x - 1] != 'H'){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 1, y), RodzajFigury.KROL);
				if(model.getPlansza()[y][x - 1] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 1, y)));
				res.add(tmp);
			}
			if(x + 1 != 8 && model.getPlansza()[y][x + 1] != 'W'
				&& model.getPlansza()[y][x + 1] != 'P'
				&& model.getPlansza()[y][x + 1] != 'S'
				&& model.getPlansza()[y][x + 1] != 'G'
				&& model.getPlansza()[y][x + 1] != 'H'){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 1, y), RodzajFigury.KROL);
				if(model.getPlansza()[y][x + 1] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 1, y)));
				res.add(tmp);
			}
			if(y - 1 != -1 && model.getPlansza()[y - 1][x] != 'W'
				&& model.getPlansza()[y - 1][x] != 'P'
				&& model.getPlansza()[y - 1][x] != 'S'
				&& model.getPlansza()[y - 1][x] != 'G'
				&& model.getPlansza()[y - 1][x] != 'H'){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x, y - 1), RodzajFigury.KROL);
				if(model.getPlansza()[y - 1][x] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x, y - 1)));
				res.add(tmp);
			}
			if(y + 1 != 8 && model.getPlansza()[y + 1][x] != 'W'
					&& model.getPlansza()[y + 1][x] != 'P'
					&& model.getPlansza()[y + 1][x] != 'S'
					&& model.getPlansza()[y + 1][x] != 'G'
					&& model.getPlansza()[y + 1][x] != 'H'){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x, y + 1), RodzajFigury.KROL);
				if(model.getPlansza()[y + 1][x] == 'k'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x, y + 1)));
				res.add(tmp);
			}
			break;
		case CZARNY:
			if(x - 1 != -1 && model.getPlansza()[y][x - 1] != 'w'
				&& model.getPlansza()[y][x - 1] != 'p'
				&& model.getPlansza()[y][x - 1] != 's'
				&& model.getPlansza()[y][x - 1] != 'h'
				&& model.getPlansza()[y][x - 1] != 'h'){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 1, y), RodzajFigury.KROL);
				if(model.getPlansza()[y][x - 1] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x - 1, y)));
				res.add(tmp);
			}
			if(x + 1 != 8 && model.getPlansza()[y][x + 1] != 'w'
				&& model.getPlansza()[y][x + 1] != 'p'
				&& model.getPlansza()[y][x + 1] != 's'
				&& model.getPlansza()[y][x + 1] != 'g'
				&& model.getPlansza()[y][x + 1] != 'h'){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 1, y), RodzajFigury.KROL);
				if(model.getPlansza()[y][x + 1] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x + 1, y)));
				res.add(tmp);
			}
			if(y - 1 != -1 && model.getPlansza()[y - 1][x] != 'w'
				&& model.getPlansza()[y - 1][x] != 'p'
				&& model.getPlansza()[y - 1][x] != 's'
				&& model.getPlansza()[y - 1][x] != 'g'
				&& model.getPlansza()[y - 1][x] != 'h'){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x, y - 1), RodzajFigury.KROL);
				if(model.getPlansza()[y - 1][x] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x, y - 1)));
				res.add(tmp);
			}
			if(y + 1 != 8 && model.getPlansza()[y + 1][x] != 'w'
				&& model.getPlansza()[y + 1][x] != 'p'
				&& model.getPlansza()[y + 1][x] != 's'
				&& model.getPlansza()[y + 1][x] != 'g'
				&& model.getPlansza()[y + 1][x] != 'h'){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x, y + 1), RodzajFigury.KROL);
				if(model.getPlansza()[y + 1][x] == 'K'){
					tmp.zbityKrol = true;
				}
				tmp.setZbitaFigura(getNazweZbitejFigury(new Punkt(x, y + 1)));
				res.add(tmp);
			}
			break;
		}
		
		return res;
	}
	
	/* 
	 * Pomocnicza metoda do znajdywania mozliwych ruchow na ukos
	 * @returns Liste mozliwych punktow ruchu na ukos 
	 * */
	private ArrayList<Punkt> getMozliwePunktyNaUkos(Punkt poczatkowyPunkt, KolorFigury kolor){
		final int x = poczatkowyPunkt.getX();
		final int y = poczatkowyPunkt.getY();
		ArrayList<Punkt> listaWsp = new ArrayList<Punkt>();
		if (kolor == KolorFigury.BIALY) {
			for (int i = 1; i < 8; i++) {
				if (y + i < 8 && x + i < 8) {
					if (model.getPlansza()[y + i][x + i] == '.') {
						listaWsp.add(new Punkt(x + i, y + i));
					}
					if (model.getPlansza()[y + i][x + i] == 'S'
							|| model.getPlansza()[y + i][x + i] == 'W'
							|| model.getPlansza()[y + i][x + i] == 'G'
							|| model.getPlansza()[y + i][x + i] == 'H'
							|| model.getPlansza()[y + i][x + i] == 'K'
							|| model.getPlansza()[y + i][x + i] == 'P') {
						break;	// poniewaz znaleziono wlasne bierki
					}
					if (model.getPlansza()[y + i][x + i] == 's'
							|| model.getPlansza()[y + i][x + i] == 'w'
							|| model.getPlansza()[y + i][x + i] == 'g'
							|| model.getPlansza()[y + i][x + i] == 'h'
							|| model.getPlansza()[y + i][x + i] == 'k'
							|| model.getPlansza()[y + i][x + i] == 'p') {
						listaWsp.add(new Punkt(x + i, y + i));
						break;	// poniewaz znaleziono bicie (a nie ma bicia w przelocie - w tym kierunku bierka nie moze juz dalej sie poruszac
					}
				}
			}
			for(int i = 1; i < 8; i++){
				if(y + i < 8 && x - i >= 0){
					if(model.getPlansza()[y + i][x - i] == '.'){
						listaWsp.add(new Punkt(x - i, y + i));
					}
					if(model.getPlansza()[y + i][x - i] == 'S'
							|| model.getPlansza()[y + i][x - i] == 'W'
							|| model.getPlansza()[y + i][x - i] == 'G'
							|| model.getPlansza()[y + i][x - i] == 'H'
							|| model.getPlansza()[y + i][x - i] == 'K'
							|| model.getPlansza()[y + i][x - i] == 'P'){
						break;	// poniewaz znaleziono wlasne bierki
					}
					if (model.getPlansza()[y + i][x - i] == 's'
							|| model.getPlansza()[y + i][x - i] == 'w'
							|| model.getPlansza()[y + i][x - i] == 'g'
							|| model.getPlansza()[y + i][x - i] == 'h'
							|| model.getPlansza()[y + i][x - i] == 'k'
							|| model.getPlansza()[y + i][x - i] == 'p') {
						listaWsp.add(new Punkt(x - i, y + i));
						break;	// poniewaz znaleziono bicie (a nie ma bicia w przelocie - w tym kierunku bierka nie moze juz dalej sie poruszac
					}
				}
			}
			for(int i = 1; i < 8; i++){
				if(y - i >= 0 && x + i < 8){
					if(model.getPlansza()[y - i][x + i] == '.'){
						listaWsp.add(new Punkt(x + i, y - i));
					}
					if(model.getPlansza()[y - i][x + i] == 'S'
							|| model.getPlansza()[y - i][x + i] == 'W'
							|| model.getPlansza()[y - i][x + i] == 'G'
							|| model.getPlansza()[y - i][x + i] == 'H'
							|| model.getPlansza()[y - i][x + i] == 'K'
							|| model.getPlansza()[y - i][x + i] == 'P'){
						break;	// poniewaz znaleziono wlasne bierki
					}
					if (model.getPlansza()[y - i][x + i] == 's'
							|| model.getPlansza()[y - i][x + i] == 'w'
							|| model.getPlansza()[y - i][x + i] == 'g'
							|| model.getPlansza()[y - i][x + i] == 'h'
							|| model.getPlansza()[y - i][x + i] == 'k'
							|| model.getPlansza()[y - i][x + i] == 'p') {
						listaWsp.add(new Punkt(x + i, y - i));
						break; // poniewaz znaleziono bicie (a nie ma bicia w przelocie - w tym kierunku bierka nie moze juz dalej sie poruszac
					}
				}
			}
			for(int i = 1; i < 8; i++){
				if(y - i >= 0 && x - i >= 0){
					if(model.getPlansza()[y - i][x - i] == '.'){
						listaWsp.add(new Punkt(x - i, y - i));
					}
					if(model.getPlansza()[y - i][x - i] == 'S'
							|| model.getPlansza()[y - i][x - i] == 'W'
							|| model.getPlansza()[y - i][x - i] == 'G'
							|| model.getPlansza()[y - i][x - i] == 'H'
							|| model.getPlansza()[y - i][x - i] == 'K'
							|| model.getPlansza()[y - i][x - i] == 'P'){
						break;	// poniewaz znaleziono wlasne bierki
					}
					if (model.getPlansza()[y - i][x - i] == 's'
							|| model.getPlansza()[y - i][x - i] == 'w'
							|| model.getPlansza()[y - i][x - i] == 'g'
							|| model.getPlansza()[y - i][x - i] == 'h'
							|| model.getPlansza()[y - i][x - i] == 'k'
							|| model.getPlansza()[y - i][x - i] == 'p') {
						listaWsp.add(new Punkt(x - i, y - i));
						break;	// poniewaz znaleziono bicie (a nie ma bicia w przelocie - w tym kierunku bierka nie moze juz dalej sie poruszac
					}
				}
			}
		}
		else if(kolor == KolorFigury.CZARNY){	// analogicznie jak dla bialych, z tym ze zmienia sie wielkosc liter
			for (int i = 1; i < 8; i++) {
				if (y + i < 8 && x + i < 8) {
					if (model.getPlansza()[y + i][x + i] == '.') {
						listaWsp.add(new Punkt(x + i, y + i));
					}
					if (model.getPlansza()[y + i][x + i] == 's'
							|| model.getPlansza()[y + i][x + i] == 'w'
							|| model.getPlansza()[y + i][x + i] == 'g'
							|| model.getPlansza()[y + i][x + i] == 'h'
							|| model.getPlansza()[y + i][x + i] == 'k'
							|| model.getPlansza()[y + i][x + i] == 'p') {
						break;
					}
					if (model.getPlansza()[y + i][x + i] == 'S'
							|| model.getPlansza()[y + i][x + i] == 'W'
							|| model.getPlansza()[y + i][x + i] == 'G'
							|| model.getPlansza()[y + i][x + i] == 'H'
							|| model.getPlansza()[y + i][x + i] == 'K'
							|| model.getPlansza()[y + i][x + i] == 'P') {
						listaWsp.add(new Punkt(x + i, y + i));
						break;
					}
				}
			}
			for(int i = 1; i < 8; i++){
				if(y + i < 8 && x - i >= 0){
					if(model.getPlansza()[y + i][x - i] == '.'){
						listaWsp.add(new Punkt(x - i, y + i));
					}
					if(model.getPlansza()[y + i][x - i] == 's'
							|| model.getPlansza()[y + i][x - i] == 'w'
							|| model.getPlansza()[y + i][x - i] == 'g'
							|| model.getPlansza()[y + i][x - i] == 'h'
							|| model.getPlansza()[y + i][x - i] == 'k'
							|| model.getPlansza()[y + i][x - i] == 'p'){
						break;
					}
					if (model.getPlansza()[y + i][x - i] == 'S'
							|| model.getPlansza()[y + i][x - i] == 'W'
							|| model.getPlansza()[y + i][x - i] == 'G'
							|| model.getPlansza()[y + i][x - i] == 'H'
							|| model.getPlansza()[y + i][x - i] == 'K'
							|| model.getPlansza()[y + i][x - i] == 'P') {
						listaWsp.add(new Punkt(x - i, y + i));
						break;
					}
				}
			}
			for(int i = 1; i < 8; i++){
				if(y - i >= 0 && x + i < 8){
					if(model.getPlansza()[y - i][x + i] == '.'){
						listaWsp.add(new Punkt(x + i, y - i));
					}
					if(model.getPlansza()[y - i][x + i] == 's'
							|| model.getPlansza()[y - i][x + i] == 'w'
							|| model.getPlansza()[y - i][x + i] == 'g'
							|| model.getPlansza()[y - i][x + i] == 'h'
							|| model.getPlansza()[y - i][x + i] == 'k'
							|| model.getPlansza()[y - i][x + i] == 'p'){
						break;
					}
					if (model.getPlansza()[y - i][x + i] == 'S'
							|| model.getPlansza()[y - i][x + i] == 'W'
							|| model.getPlansza()[y - i][x + i] == 'G'
							|| model.getPlansza()[y - i][x + i] == 'H'
							|| model.getPlansza()[y - i][x + i] == 'K'
							|| model.getPlansza()[y - i][x + i] == 'P') {
						listaWsp.add(new Punkt(x + i, y - i));
						break;
					}
				}
			}
			for(int i = 1; i < 8; i++){
				if(y - i >= 0 && x - i >= 0){
					if(model.getPlansza()[y - i][x - i] == '.'){
						listaWsp.add(new Punkt(x - i, y - i));
					}
					if(model.getPlansza()[y - i][x - i] == 's'
							|| model.getPlansza()[y - i][x - i] == 'w'
							|| model.getPlansza()[y - i][x - i] == 'g'
							|| model.getPlansza()[y - i][x - i] == 'h'
							|| model.getPlansza()[y - i][x - i] == 'k'
							|| model.getPlansza()[y - i][x - i] == 'p'){
						break;
					}
					if (model.getPlansza()[y - i][x - i] == 'S'
							|| model.getPlansza()[y - i][x - i] == 'W'
							|| model.getPlansza()[y - i][x - i] == 'G'
							|| model.getPlansza()[y - i][x - i] == 'H'
							|| model.getPlansza()[y - i][x - i] == 'K'
							|| model.getPlansza()[y - i][x - i] == 'P') {
						listaWsp.add(new Punkt(x - i, y - i));
						break;
					}
				}
			}
		}
		return listaWsp;
	}
	
	/*
	 * Pomocnicza metoda do znajdywania mozliwych ruchow na wprost.
	 * @returns liste punktow, do ktorych mozna przejsc na wprost
	 */
	private ArrayList<Punkt> getMozliwePunktyProsto(Punkt poczatkowyPunkt, KolorFigury kolor){
		final int x = poczatkowyPunkt.getX();
		final int y = poczatkowyPunkt.getY();
		ArrayList<Punkt> listaWsp = new ArrayList<Punkt>();
		switch(kolor){
		case BIALY:
			for(int i = 1; i < 8; i++){
				if(y + i < 8){
					if(model.getPlansza()[y + i][x] == '.'){
						listaWsp.add(new Punkt(x, y + i));
					}
					if (model.getPlansza()[y + i][x] == 'S'
							|| model.getPlansza()[y + i][x] == 'W'
							|| model.getPlansza()[y + i][x] == 'G'
							|| model.getPlansza()[y + i][x] == 'H'
							|| model.getPlansza()[y + i][x] == 'K'
							|| model.getPlansza()[y + i][x] == 'P') {
						break; // poniewaz znaleziono wlasne bierki
					}
					if (model.getPlansza()[y + i][x] == 's'
							|| model.getPlansza()[y + i][x] == 'w'
							|| model.getPlansza()[y + i][x] == 'g'
							|| model.getPlansza()[y + i][x] == 'h'
							|| model.getPlansza()[y + i][x] == 'k'
							|| model.getPlansza()[y + i][x] == 'p') {
						listaWsp.add(new Punkt(x, y + i));
						break;	// poniewaz znaleziono bicie (a nie ma bicia w przelocie - w tym kierunku bierka nie moze juz dalej sie poruszac
					}
				}
			}
			for(int i = 1; i < 8; i++){
				if (y - i >= 0) {
					if (model.getPlansza()[y - i][x] == '.') {
						listaWsp.add(new Punkt(x, y - i));
					}
					if (model.getPlansza()[y - i][x] == 'S' || model.getPlansza()[y - i][x] == 'W'
							|| model.getPlansza()[y - i][x] == 'G' || model.getPlansza()[y - i][x] == 'H'
							|| model.getPlansza()[y - i][x] == 'K' || model.getPlansza()[y - i][x] == 'P') {
						break;	// poniewaz znaleziono wlasne bierki
					}
					if (model.getPlansza()[y - i][x] == 's' || model.getPlansza()[y - i][x] == 'w'
							|| model.getPlansza()[y - i][x] == 'g' || model.getPlansza()[y - i][x] == 'h'
							|| model.getPlansza()[y - i][x] == 'k' || model.getPlansza()[y - i][x] == 'p') {
						listaWsp.add(new Punkt(x, y - i));
						break;	// poniewaz znaleziono bicie (a nie ma bicia w przelocie - w tym kierunku bierka nie moze juz dalej sie poruszac
					} 
				}
			}
			for(int i = 1; i < 8; i++){
				if(x + i < 8){
					if(model.getPlansza()[y][x + i] == '.'){
						listaWsp.add(new Punkt(x + i, y));
					}
					if (model.getPlansza()[y][x + i] == 'S'
							|| model.getPlansza()[y][x + i] == 'W'
							|| model.getPlansza()[y][x + i] == 'G'
							|| model.getPlansza()[y][x + i] == 'H'
							|| model.getPlansza()[y][x + i] == 'K'
							|| model.getPlansza()[y][x + i] == 'P') {
						break;	// poniewaz znaleziono wlasne bierki
					}
					if (model.getPlansza()[y][x + i] == 's'
							|| model.getPlansza()[y][x + i] == 'w'
							|| model.getPlansza()[y][x + i] == 'g'
							|| model.getPlansza()[y][x + i] == 'h'
							|| model.getPlansza()[y][x + i] == 'k'
							|| model.getPlansza()[y][x + i] == 'p') {
						listaWsp.add(new Punkt(x + i, y));
						break;	// poniewaz znaleziono bicie (a nie ma bicia w przelocie - w tym kierunku bierka nie moze juz dalej sie poruszac
					}
				}
			}
			for(int i = 1; i < 8; i++){
				if(x - i >= 0){
					if(model.getPlansza()[y][x - i] == '.'){
						listaWsp.add(new Punkt(x - i, y));
					}
					if (model.getPlansza()[y][x - i] == 'S'
							|| model.getPlansza()[y][x - i] == 'W'
							|| model.getPlansza()[y][x - i] == 'G'
							|| model.getPlansza()[y][x - i] == 'H'
							|| model.getPlansza()[y][x - i] == 'K'
							|| model.getPlansza()[y][x - i] == 'P') {
						break;	// poniewaz znaleziono wlasne bierki
					}
					if (model.getPlansza()[y][x - i] == 's'
							|| model.getPlansza()[y][x - i] == 'w'
							|| model.getPlansza()[y][x - i] == 'g'
							|| model.getPlansza()[y][x - i] == 'h'
							|| model.getPlansza()[y][x - i] == 'k'
							|| model.getPlansza()[y][x - i] == 'p') {
						listaWsp.add(new Punkt(x - i, y));
						break;	// poniewaz znaleziono bicie (a nie ma bicia w przelocie - w tym kierunku bierka nie moze juz dalej sie poruszac
					}
				}
			}
			break;	
		case CZARNY:	// analogicznie jak dla bialych bierek, zmienia sie tylko wielkosc liter
			for(int i = 1; i < 8; i++){
				if(y + i < 8){
					if(model.getPlansza()[y + i][x] == '.'){
						listaWsp.add(new Punkt(x, y + i));
					}
					if (model.getPlansza()[y + i][x] == 's'
							|| model.getPlansza()[y + i][x] == 'w'
							|| model.getPlansza()[y + i][x] == 'g'
							|| model.getPlansza()[y + i][x] == 'h'
							|| model.getPlansza()[y + i][x] == 'k'
							|| model.getPlansza()[y + i][x] == 'p') {
						break;
					}
					if (model.getPlansza()[y + i][x] == 'S'
							|| model.getPlansza()[y + i][x] == 'W'
							|| model.getPlansza()[y + i][x] == 'G'
							|| model.getPlansza()[y + i][x] == 'H'
							|| model.getPlansza()[y + i][x] == 'K'
							|| model.getPlansza()[y + i][x] == 'P') {
						listaWsp.add(new Punkt(x, y + i));
						break;
					}
				}
			}
			for(int i = 1; i < 8; i++){
				if (y - i >= 0) {
					if (model.getPlansza()[y - i][x] == '.') {
						listaWsp.add(new Punkt(x, y - i));
					}
					if (model.getPlansza()[y - i][x] == 's' || model.getPlansza()[y - i][x] == 'w'
							|| model.getPlansza()[y - i][x] == 'g' || model.getPlansza()[y - i][x] == 'h'
							|| model.getPlansza()[y - i][x] == 'k' || model.getPlansza()[y - i][x] == 'p') {
						break;
					}
					if (model.getPlansza()[y - i][x] == 'S' || model.getPlansza()[y - i][x] == 'W'
							|| model.getPlansza()[y - i][x] == 'G' || model.getPlansza()[y - i][x] == 'H'
							|| model.getPlansza()[y - i][x] == 'K' || model.getPlansza()[y - i][x] == 'P') {
						listaWsp.add(new Punkt(x, y - i));
						break;
					} 
				}
			}
			for(int i = 1; i < 8; i++){
				if(x + i < 8){
					if(model.getPlansza()[y][x + i] == '.'){
						listaWsp.add(new Punkt(x + i, y));
					}
					if (model.getPlansza()[y][x + i] == 's'
							|| model.getPlansza()[y][x + i] == 'w'
							|| model.getPlansza()[y][x + i] == 'g'
							|| model.getPlansza()[y][x + i] == 'h'
							|| model.getPlansza()[y][x + i] == 'k'
							|| model.getPlansza()[y][x + i] == 'p') {
						break;
					}
					if (model.getPlansza()[y][x + i] == 'S'
							|| model.getPlansza()[y][x + i] == 'W'
							|| model.getPlansza()[y][x + i] == 'G'
							|| model.getPlansza()[y][x + i] == 'H'
							|| model.getPlansza()[y][x + i] == 'K'
							|| model.getPlansza()[y][x + i] == 'P') {
						listaWsp.add(new Punkt(x + i, y));
						break;
					}
				}
			}
			for(int i = 1; i < 8; i++){
				if(x - i >= 0){
					if(model.getPlansza()[y][x - i] == '.'){
						listaWsp.add(new Punkt(x - i, y));
					}
					if (model.getPlansza()[y][x - i] == 's'
							|| model.getPlansza()[y][x - i] == 'w'
							|| model.getPlansza()[y][x - i] == 'g'
							|| model.getPlansza()[y][x - i] == 'h'
							|| model.getPlansza()[y][x - i] == 'k'
							|| model.getPlansza()[y][x - i] == 'p') {
						break;
					}
					if (model.getPlansza()[y][x - i] == 'S'
							|| model.getPlansza()[y][x - i] == 'W'
							|| model.getPlansza()[y][x - i] == 'G'
							|| model.getPlansza()[y][x - i] == 'H'
							|| model.getPlansza()[y][x - i] == 'K'
							|| model.getPlansza()[y][x - i] == 'P') {
						listaWsp.add(new Punkt(x - i, y));
						break;
					}
				}
			}
			break;
		}
		return listaWsp;
	}
	
	/*
	 * Pomocnicza metoda zwracajaca rodzaj figury pod zadanymi wspolrzednymi
	 */
	private RodzajFigury getNazweZbitejFigury(Punkt wsp){
		final int x = wsp.getX();
		final int y = wsp.getY();
		if(model.getPlansza()[y][x] == 'k' || model.getPlansza()[y][x] == 'K'){
			return RodzajFigury.KROL;
		}
		else if(model.getPlansza()[y][x] == 's' || model.getPlansza()[y][x] == 'S'){
			return RodzajFigury.SKOCZEK;
		}
		else if(model.getPlansza()[y][x] == 'h' || model.getPlansza()[y][x] == 'H'){
			return RodzajFigury.HETMAN;
		}
		else if(model.getPlansza()[y][x] == 'w' || model.getPlansza()[y][x] == 'W'){
			return RodzajFigury.WIEZA;
		}
		else if(model.getPlansza()[y][x] == 'p' || model.getPlansza()[y][x] == 'P'){
			return RodzajFigury.PIONEK;
		}
		else if(model.getPlansza()[y][x] == 'g' || model.getPlansza()[y][x] == 'G'){
			return RodzajFigury.GONIEC;
		}
		else {	// nigdy nie nastapi
			return RodzajFigury.PIONEK;
		}
	}
	
	/*
	 * Pomocnicza metoda do usuwania z listy figur figury o zadanych wspolrzednych x i y
	 */
	private void usunZbitaFigureZListy(ArrayList<Figura> lista, int x, int y){
		int index = -1;
		for(int i = 0; i < lista.size(); i++){
			if(lista.get(i).getPunkt().getX() == x && lista.get(i).getPunkt().getY() == y){
				index = i;
				break;
			}
		}
		if(index != -1){
			lista.remove(index);
		}
	}
	
	/*
	 * Pomocnicza metoda do konwersji indeksu w postaci liczby na odpowiednia litere
	 */
	private char getCharFromInt(int index){
		if(index == 0){
			return 'A';
		}
		else if(index == 1){
			return 'B';
		}
		else if(index == 2){
			return 'C';
		}
		else if(index == 3){
			return 'D';
		}
		else if(index == 4){
			return 'E';
		}
		else if(index == 5){
			return 'F';
		}
		else if(index == 6){
			return 'G';
		}
		else if(index == 7){
			return 'H';
		}
		else return '-';
	}
	
	/*
	 * Pomocnicza metoda do konwersji figury z typu wyliczeniowego RodzajFigury na jej odpowiednik np. PIONEK = 'P'
	 */
	private char getCharFromRodzajFiguryAndKolorFigury(RodzajFigury f, KolorFigury c){
		switch(c){
		case BIALY:
			switch(f){
			case PIONEK:
				return 'P';
			case WIEZA:
				return 'W';
			case SKOCZEK:
				return 'S';
			case GONIEC:
				return 'G';
			case HETMAN:
				return 'H';
			case KROL:
				return 'K';
			}
		case CZARNY:
			switch(f){
			case PIONEK:
				return 'p';
			case WIEZA:
				return 'w';
			case SKOCZEK:
				return 's';
			case GONIEC:
				return 'g';
			case HETMAN:
				return 'h';
			case KROL:
				return 'k';
			}	
		}
		return '-';
	}
}
