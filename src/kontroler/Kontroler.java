package kontroler;

import java.util.ArrayList;
import java.util.Random;

import model.Figura;
import model.KolorFigury;
import model.Model;
import model.Punkt;
import model.RodzajFigury;
import widok.Widok;

public class Kontroler {
	private Model model;
	private Widok widok;
	
	public Kontroler(Model model, Widok widok){
		this.model = model;
		this.widok = widok;
	}
	
	public void start(){
		int liczbaRuchow = 0;
		int tmp;
		boolean zbityKrol = false;
		widok.rysujPowitanie();
		widok.rysujPlansze();
		
		while(true){
			tmp = liczbaRuchow;
			tmp++;
			
			System.out.println();
			System.out.println("   Tura " + tmp);
			System.out.println();
			
			//System.out.println("Ruch z ... ");
			
			/* biale wykonuja ruch: */
			if(!bialeWykonajRuch(zbityKrol)){
				break;
			}
			widok.rysujPlansze();
			System.out.println();
			/*if(zbityKrol){
				System.out.println("! ! ! ! ! ! ! ! ! ! ! !");
				System.out.println("\t Zbito krola!");
				break;
			}*/
			
			/* czarne wykonuja ruch: */
			if(!czarneWykonajRuch(zbityKrol)){
				break;
			}
			widok.rysujPlansze();
			System.out.println();
			
			if(liczbaRuchow == 49){
				System.out.println("\t REMIS");
				break;
			}
			liczbaRuchow++;
		}
	}
	
	private boolean bialeWykonajRuch(boolean zbityKrol){
		Random r = new Random();
		//
		int index;
		Ruch doWykonania;
		ArrayList<Ruch> listaRuchow = new ArrayList<Ruch>();
		for(int i = 0; i < model.getFiguryBiale().size(); i++){
			//System.out.println(model.getFiguryBiale().get(i).getPunkt());
			switch(model.getFiguryBiale().get(i).getNazwa()){
			case PIONEK:
				listaRuchow.addAll(getMozliweRuchyPionka(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));
				break;
			case WIEZA:
				listaRuchow.addAll(getMozliweRuchyWiezy(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));
				break;
			case SKOCZEK:
				listaRuchow.addAll(getMozliweRuchySkoczka(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));
				break;
			case GONIEC:
				listaRuchow.addAll(getMozliweRuchyGonca(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));
				break;
			case HETMAN:
				listaRuchow.addAll(getMozliweRuchyHetmana(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));
				break;
			case KROL:
				listaRuchow.addAll(getMozliweRuchyKrola(model.getFiguryBiale().get(i).getPunkt(), KolorFigury.BIALY));
				break;
			}
		}
		if(listaRuchow.isEmpty()){
			return false;
		}
		//System.out.println(listaRuchow.size());
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
		for(int i = 0; i < model.getFiguryBiale().size(); i++){
			if(model.getFiguryBiale().get(i).getPunkt().getX() == zrX && 
					model.getFiguryBiale().get(i).getPunkt().getY() == zrY){
				model.getFiguryBiale().get(i).getPunkt().setX(docX);
				model.getFiguryBiale().get(i).getPunkt().setY(docY);
				model.getPlansza()[zrY][zrX] = '.';
				/*switch(doWykonania.getFigure()){
				case PIONEK:
					model.getPlansza()[docY][docX] = 'P';
					break;
				case WIEZA:
					model.getPlansza()[docY][docX] = 'W';
					break;
				case SKOCZEK:
					model.getPlansza()[docY][docX] = 'S';
					break;
				case GONIEC:
					model.getPlansza()[docY][docX] = 'G';
					break;
				case HETMAN:
					model.getPlansza()[docY][docX] = 'H';
					break;
				case KROL:
					model.getPlansza()[docY][docX] = 'K';
					break;
				}*/
				tmp = getCharFromRodzajFiguryAndKolorFigury(doWykonania.getFigure(), KolorFigury.BIALY);
				tmpY1 = zrY + 1;
				tmpY2= docY + 1;
				if(model.getPlansza()[docY][docX] != '.'){
					zbityX = docX;
					zbityY = docY;
					usunZbitaFigureZListy(model.getFiguryCzarne(), zbityX, zbityY);
				}
				model.getPlansza()[docY][docX] = tmp;
				System.out.println();
				System.out.println("Ruch " + tmp + " z " + getCharFromInt(zrX) + tmpY1 + " na " + getCharFromInt(docX) + tmpY2);
				System.out.println();
				break;
			}
		}
		if(zbityKrol){
			System.out.println();
			System.out.println("Biale zbily krola!");
			return false;
		}
		return true;
	}
	
	private boolean czarneWykonajRuch(boolean zbityKrol){
		Random r = new Random();
		int index;
		Ruch doWykonania;
		ArrayList<Ruch> listaRuchow = new ArrayList<Ruch>();
		for(int i = 0; i < model.getFiguryCzarne().size(); i++){
			switch(model.getFiguryCzarne().get(i).getNazwa()){
			case PIONEK:
				listaRuchow.addAll(getMozliweRuchyPionka(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));
				break;
			case WIEZA:
				listaRuchow.addAll(getMozliweRuchyWiezy(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));
				break;
			case SKOCZEK:
				listaRuchow.addAll(getMozliweRuchySkoczka(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));
				break;
			case GONIEC:
				listaRuchow.addAll(getMozliweRuchyGonca(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));
				break;
			case HETMAN:
				listaRuchow.addAll(getMozliweRuchyHetmana(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));
				break;
			case KROL:
				listaRuchow.addAll(getMozliweRuchyKrola(model.getFiguryCzarne().get(i).getPunkt(), KolorFigury.CZARNY));
				break;
			}
		}
		if(listaRuchow.isEmpty()){
			return false;
		}
		//System.out.println(listaRuchow.size());
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
				System.out.println();
				System.out.println("Ruch " + tmp + " z " + getCharFromInt(zrX) + tmpY1 + " na " + getCharFromInt(docX) + tmpY2);
				System.out.println();
				break;
			}
		}
		if(zbityKrol){
			System.out.println();
			System.out.println("Czarne zbily krola!");
			return false;
		}
		return true;
	}
	
	private ArrayList<Ruch> getMozliweRuchyPionka(Punkt poczatkowyPunkt, KolorFigury kolor){
		ArrayList<Ruch> res = new ArrayList<Ruch>();
		Ruch tmp;
		final int x = poczatkowyPunkt.getX();
		final int y = poczatkowyPunkt.getY();
		
		if (kolor == KolorFigury.BIALY) {
			if (y + 1 != 8 && model.getPlansza()[y + 1][x] == '.') {
				res.add(new Ruch(poczatkowyPunkt, new Punkt(x, y + 1), RodzajFigury.PIONEK));
			}
			if (y + 1 != 8 && x + 1 != 8 && model.getPlansza()[y + 1][x + 1] != '.' && model.getPlansza()[y + 1][x + 1] != 'W' 
					&& model.getPlansza()[y + 1][x + 1] != 'S' 
					&& model.getPlansza()[y + 1][x + 1] != 'H' 
					&& model.getPlansza()[y + 1][x + 1] != 'K' 
					&& model.getPlansza()[y + 1][x + 1] != 'P' 
					&& model.getPlansza()[y + 1][x + 1] != 'G') {
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 1, y + 1), RodzajFigury.PIONEK);
				if(model.getPlansza()[y + 1][x + 1] == 'k'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			} 
			if (y + 1 != 8 && x - 1 != -1 && model.getPlansza()[y + 1][x - 1] != '.' && model.getPlansza()[y + 1][x - 1] != 'W' 
					&& model.getPlansza()[y + 1][x - 1] != 'S'
					&& model.getPlansza()[y + 1][x - 1] != 'H' 
					&& model.getPlansza()[y + 1][x - 1] != 'K' 
					&& model.getPlansza()[y + 1][x - 1] != 'P' 
					&& model.getPlansza()[y + 1][x - 1] != 'G') {
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 1, y + 1), RodzajFigury.PIONEK);
				if(model.getPlansza()[y + 1][x - 1] == 'k'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			}
		}
		else if(kolor == KolorFigury.CZARNY){
			if (y - 1 != -1 && model.getPlansza()[y - 1][x] == '.') {
				res.add(new Ruch(poczatkowyPunkt, new Punkt(x, y - 1), RodzajFigury.PIONEK));
			}
			if (y - 1 != -1 && x + 1 != 8 && model.getPlansza()[y - 1][x + 1] != '.' && model.getPlansza()[y - 1][x + 1] != 'p'
					&& model.getPlansza()[y - 1][x + 1] != 's'
					&& model.getPlansza()[y - 1][x + 1] != 'h'
					&& model.getPlansza()[y - 1][x + 1] != 'k'
					&& model.getPlansza()[y - 1][x + 1] != 'g'
					&& model.getPlansza()[y - 1][x + 1] != 'w') {
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x + 1, y - 1), RodzajFigury.PIONEK);
				if(model.getPlansza()[y - 1][x + 1] == 'K'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			} 
			if (y - 1 != -1 && x - 1 != -1 && model.getPlansza()[y - 1][x - 1] != '.' && model.getPlansza()[y - 1][x - 1] != 'p'
					&& model.getPlansza()[y - 1][x - 1] != 's'
					&& model.getPlansza()[y - 1][x - 1] != 'h'
					&& model.getPlansza()[y - 1][x - 1] != 'k'
					&& model.getPlansza()[y - 1][x - 1] != 'g'
					&& model.getPlansza()[y - 1][x - 1] != 'w'){
				tmp = new Ruch(poczatkowyPunkt, new Punkt(x - 1, y - 1), RodzajFigury.PIONEK);
				if(model.getPlansza()[y - 1][x - 1] == 'K'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			}
		}
		return res;
	}
	
	private ArrayList<Ruch> getMozliweRuchyWiezy(Punkt poczatkowyPunkt, KolorFigury kolor){
		ArrayList<Ruch> res = new ArrayList<Ruch>();
		Ruch tmp;
		final int x = poczatkowyPunkt.getX();
		final int y = poczatkowyPunkt.getY();
		switch(kolor){
		case BIALY:
			ArrayList<Punkt> wspBiale = getMozliwePunktyProsto(poczatkowyPunkt, kolor);
			for(int i = 0; i < wspBiale.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, wspBiale.get(i), RodzajFigury.WIEZA);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'k'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			}
			break;
		case CZARNY:
			ArrayList<Punkt> wspCzarne = getMozliwePunktyProsto(poczatkowyPunkt, kolor);
			for(int i = 0; i < wspCzarne.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, wspCzarne.get(i), RodzajFigury.WIEZA);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'k'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			}
			break;
		}
		/*boolean moznaDodawacPlusX = true;
		boolean moznaDodawacMinusX = true;
		boolean moznaDodawacPlusY = true;
		boolean moznaDodawacMinusY = true;
		switch(kolor){
		case BIALY:
			//sprawdz X:
			for(int i = 1; i < 8; i++){
				if(x + i < 8 && (model.getPlansza()[y][x + i] == '.'
						|| model.getPlansza()[y][x + i] == 's'
						|| model.getPlansza()[y][x + i] == 'g'
						|| model.getPlansza()[y][x + i] == 'k'
						|| model.getPlansza()[y][x + i] == 'h'
						|| model.getPlansza()[y][x + i] == 'w'
						|| model.getPlansza()[y][x + i] == 'p')){
					tmp = new Ruch(poczatkowyPunkt, new Punkt(x + i, y), RodzajFigury.WIEZA);
					if(model.getPlansza()[y][x + i] == 'k'){
						tmp.zbityKrol = true;
					}
					if(moznaDodawacPlusX){
						res.add(tmp);
					}
				}
				else if (x + i < 8 && (model.getPlansza()[y][x + i] == 'S'
						|| model.getPlansza()[y][x + i] == 'G'
						|| model.getPlansza()[y][x + i] == 'K'
						|| model.getPlansza()[y][x + i] == 'H'
						|| model.getPlansza()[y][x + i] == 'W'
						|| model.getPlansza()[y][x + i] == 'P')){
					moznaDodawacPlusX = false;
				}
				if(x - i >= 0 && (model.getPlansza()[y][x - i] == '.'
						|| model.getPlansza()[y][x - i] == 's'
						|| model.getPlansza()[y][x - i] == 'g'
						|| model.getPlansza()[y][x - i] == 'k'
						|| model.getPlansza()[y][x - i] == 'h'
						|| model.getPlansza()[y][x - i] == 'w'
						|| model.getPlansza()[y][x - i] == 'p')){
					tmp = new Ruch(poczatkowyPunkt, new Punkt(x - i, y), RodzajFigury.WIEZA);
					if(model.getPlansza()[y][x - i] == 'k'){
						tmp.zbityKrol = true;
					}
					if(moznaDodawacMinusX){
						res.add(tmp);
					} 
				}
				else if(x - i >= 0 && (model.getPlansza()[y][x - i] == 'S'
						|| model.getPlansza()[y][x - i] == 'G'
						|| model.getPlansza()[y][x - i] == 'K'
						|| model.getPlansza()[y][x - i] == 'H'
						|| model.getPlansza()[y][x - i] == 'W'
						|| model.getPlansza()[y][x - i] == 'P')) {
					moznaDodawacMinusX = false;
				}
			}
			//sprawdz Y:
			for(int i = 0; i < 8; i++){
				if(y + i < 8 && (model.getPlansza()[y  + i][x] == '.'
						|| model.getPlansza()[y + i][x] == 's'
						|| model.getPlansza()[y + i][x] == 'g'
						|| model.getPlansza()[y + i][x] == 'k'
						|| model.getPlansza()[y + i][x] == 'h'
						|| model.getPlansza()[y + i][x] == 'w'
						|| model.getPlansza()[y + i][x] == 'p')){
					tmp = new Ruch(poczatkowyPunkt, new Punkt(x, y + i), RodzajFigury.WIEZA);
					if(model.getPlansza()[y + i][x] == 'k'){
						tmp.zbityKrol = true;
					}
					if(moznaDodawacPlusY){
						res.add(tmp);
					}
				}
				else if(y + i < 8 && (model.getPlansza()[y + i][x] == 'S'
						|| model.getPlansza()[y + i][x] == 'G'
						|| model.getPlansza()[y + i][x] == 'K'
						|| model.getPlansza()[y + i][x] == 'H'
						|| model.getPlansza()[y + i][x] == 'W'
						|| model.getPlansza()[y + i][x] == 'P')){
					moznaDodawacPlusY = false;
				}
				if(y - i >= 0 && (model.getPlansza()[y  - i][x] == '.'
						|| model.getPlansza()[y - i][x] == 's'
						|| model.getPlansza()[y - i][x] == 'g'
						|| model.getPlansza()[y - i][x] == 'k'
						|| model.getPlansza()[y - i][x] == 'h'
						|| model.getPlansza()[y - i][x] == 'w'
						|| model.getPlansza()[y - i][x] == 'p')){
					tmp = new Ruch(poczatkowyPunkt, new Punkt(x, y - i), RodzajFigury.WIEZA);
					if(model.getPlansza()[y - i][x] == 'k'){
						tmp.zbityKrol = true;
					}
					if(moznaDodawacMinusY){
						res.add(tmp);
					}
				}
				else if(y - i >= 0 && (model.getPlansza()[y - i][x] == 'S'
						|| model.getPlansza()[y - i][x] == 'G'
						|| model.getPlansza()[y - i][x] == 'K'
						|| model.getPlansza()[y - i][x] == 'H'
						|| model.getPlansza()[y - i][x] == 'W'
						|| model.getPlansza()[y - i][x] == 'P')){
					moznaDodawacMinusY = false;
				}
			}
			break;
		case CZARNY:
			//sprawdz X:
			for(int i = 1; i < 8; i++){
				if(x + i < 8 && (model.getPlansza()[y][x + i] == '.'
						|| model.getPlansza()[y][x + i] == 'S'
						|| model.getPlansza()[y][x + i] == 'G'
						|| model.getPlansza()[y][x + i] == 'K'
						|| model.getPlansza()[y][x + i] == 'H'
						|| model.getPlansza()[y][x + i] == 'W'
						|| model.getPlansza()[y][x + i] == 'P')){
					tmp = new Ruch(poczatkowyPunkt, new Punkt(x + i, y), RodzajFigury.WIEZA);
					if(model.getPlansza()[y][x + i] == 'K'){
						tmp.zbityKrol = true;
					}
					if(moznaDodawacPlusX){
						res.add(tmp);
					}
				}
				else if(x + i < 8 && (model.getPlansza()[y][x + i] == 's'
						|| model.getPlansza()[y][x + i] == 'g'
						|| model.getPlansza()[y][x + i] == 'k'
						|| model.getPlansza()[y][x + i] == 'h'
						|| model.getPlansza()[y][x + i] == 'w'
						|| model.getPlansza()[y][x + i] == 'p')){
					moznaDodawacPlusX = false;
				}
				if(x - i >= 0 && (model.getPlansza()[y][x - i] == '.'
						|| model.getPlansza()[y][x - i] == 'S'
						|| model.getPlansza()[y][x - i] == 'G'
						|| model.getPlansza()[y][x - i] == 'K'
						|| model.getPlansza()[y][x - i] == 'H'
						|| model.getPlansza()[y][x - i] == 'W'
						|| model.getPlansza()[y][x - i] == 'P')){
					tmp = new Ruch(poczatkowyPunkt, new Punkt(x - i, y), RodzajFigury.WIEZA);
					if(model.getPlansza()[y][x - i] == 'K'){
						tmp.zbityKrol = true;
					}
					if(moznaDodawacMinusX){
						res.add(tmp);
					} 
				}
				else if(x - i >= 0 && (model.getPlansza()[y][x - i] == 's'
						|| model.getPlansza()[y][x - i] == 'g'
						|| model.getPlansza()[y][x - i] == 'k'
						|| model.getPlansza()[y][x - i] == 'h'
						|| model.getPlansza()[y][x - i] == 'w'
						|| model.getPlansza()[y][x - i] == 'p')){
					moznaDodawacMinusX = false;
				}
			}
			//sprawdz Y:
			for(int i = 0; i < 8; i++){
				if(y + i < 8 && (model.getPlansza()[y  + i][x] == '.'
						|| model.getPlansza()[y + i][x] == 'S'
						|| model.getPlansza()[y + i][x] == 'G'
						|| model.getPlansza()[y + i][x] == 'K'
						|| model.getPlansza()[y + i][x] == 'H'
						|| model.getPlansza()[y + i][x] == 'W'
						|| model.getPlansza()[y + i][x] == 'P')){
					tmp = new Ruch(poczatkowyPunkt, new Punkt(x, y + i), RodzajFigury.WIEZA);
					if(model.getPlansza()[y + i][x] == 'K'){
						tmp.zbityKrol = true;
					}
					if(moznaDodawacPlusY){
						res.add(tmp);
					}
				}
				else if(y + i < 8 && (model.getPlansza()[y + i][x] == 's'
						|| model.getPlansza()[y + i][x] == 'g'
						|| model.getPlansza()[y + i][x] == 'k'
						|| model.getPlansza()[y + i][x] == 'h'
						|| model.getPlansza()[y + i][x] == 'w'
						|| model.getPlansza()[y + i][x] == 'p')){
					moznaDodawacPlusY = false;
				}
				if(y - i >= 0 && (model.getPlansza()[y  - i][x] == '.'
						|| model.getPlansza()[y - i][x] == 'S'
						|| model.getPlansza()[y - i][x] == 'G'
						|| model.getPlansza()[y - i][x] == 'K'
						|| model.getPlansza()[y - i][x] == 'H'
						|| model.getPlansza()[y - i][x] == 'W'
						|| model.getPlansza()[y - i][x] == 'P')){
					tmp = new Ruch(poczatkowyPunkt, new Punkt(x, y - i), RodzajFigury.WIEZA);
					if(model.getPlansza()[y - i][x] == 'K'){
						tmp.zbityKrol = true;
					}
					if(moznaDodawacMinusY){
						res.add(tmp);
					}
				}
				else if(y - i >= 0 && (model.getPlansza()[y - i][x] == 's'
						|| model.getPlansza()[y - i][x] == 'g'
						|| model.getPlansza()[y - i][x] == 'k'
						|| model.getPlansza()[y - i][x] == 'h'
						|| model.getPlansza()[y - i][x] == 'w'
						|| model.getPlansza()[y - i][x] == 'p')){
					moznaDodawacMinusY = false;
				}
			}
			break;
		}
		/*System.out.println("----------------------------------------------");
		System.out.println("ruchy wiezy mozliwe: " + res.size());
		System.out.println("----------------------------------------------");*/
		return res;
	}
	
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
				res.add(tmp);
			}
			break;
		}
		
		return res;
	}
	
	private ArrayList<Ruch> getMozliweRuchyGonca(Punkt poczatkowyPunkt, KolorFigury kolor){
		ArrayList<Ruch> res = new ArrayList<Ruch>();
		Ruch tmp;
		switch(kolor){
		case BIALY:
			ArrayList<Punkt> wspBiale = getMozliwePunktyNaUkos(poczatkowyPunkt, kolor);
			for(int i = 0; i < wspBiale.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, wspBiale.get(i), RodzajFigury.GONIEC);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'k'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			}
			break;
		case CZARNY:
			ArrayList<Punkt> wspCzarne = getMozliwePunktyNaUkos(poczatkowyPunkt, kolor);
			for(int i = 0; i < wspCzarne.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, wspCzarne.get(i), RodzajFigury.GONIEC);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'K'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			}
			break;
		}
		return res;
	}
	
	private ArrayList<Ruch> getMozliweRuchyHetmana(Punkt poczatkowyPunkt, KolorFigury kolor){
		final int x = poczatkowyPunkt.getX();
		final int y = poczatkowyPunkt.getY();
		ArrayList<Ruch> res = new ArrayList<Ruch>();
		Ruch tmp;
		switch(kolor){
		case BIALY:
			ArrayList<Punkt> listaWspUkosBialy = getMozliwePunktyNaUkos(poczatkowyPunkt, kolor);
			ArrayList<Punkt> listaWspProstoBialy = getMozliwePunktyProsto(poczatkowyPunkt, kolor);
			for(int i = 0; i < listaWspUkosBialy.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, listaWspUkosBialy.get(i), RodzajFigury.HETMAN);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'k'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			}
			for(int i = 0; i < listaWspProstoBialy.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, listaWspProstoBialy.get(i), RodzajFigury.HETMAN);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'k'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			}
			break;
		case CZARNY:
			ArrayList<Punkt> listaWspUkosCzarny = getMozliwePunktyNaUkos(poczatkowyPunkt, kolor);
			ArrayList<Punkt> listaWspProstoCzarny = getMozliwePunktyProsto(poczatkowyPunkt, kolor);
			for(int i = 0; i < listaWspUkosCzarny.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, listaWspUkosCzarny.get(i), RodzajFigury.HETMAN);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'K'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			}
			for(int i = 0; i < listaWspProstoCzarny.size(); i++){
				tmp = new Ruch(poczatkowyPunkt, listaWspProstoCzarny.get(i), RodzajFigury.HETMAN);
				if(model.getPlansza()[tmp.getDocelowy().getY()][tmp.getDocelowy().getX()] == 'K'){
					tmp.zbityKrol = true;
				}
				res.add(tmp);
			}
			break;
		}
		return res;
	}
	
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
				res.add(tmp);
			}
			break;
		}
		
		return res;
	}
	
	/* 
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
						break;
					}
					if (model.getPlansza()[y + i][x + i] == 's'
							|| model.getPlansza()[y + i][x + i] == 'w'
							|| model.getPlansza()[y + i][x + i] == 'g'
							|| model.getPlansza()[y + i][x + i] == 'h'
							|| model.getPlansza()[y + i][x + i] == 'k'
							|| model.getPlansza()[y + i][x + i] == 'p') {
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
					if(model.getPlansza()[y + i][x - i] == 'S'
							|| model.getPlansza()[y + i][x - i] == 'W'
							|| model.getPlansza()[y + i][x - i] == 'G'
							|| model.getPlansza()[y + i][x - i] == 'H'
							|| model.getPlansza()[y + i][x - i] == 'K'
							|| model.getPlansza()[y + i][x - i] == 'P'){
						break;
					}
					if (model.getPlansza()[y + i][x - i] == 's'
							|| model.getPlansza()[y + i][x - i] == 'w'
							|| model.getPlansza()[y + i][x - i] == 'g'
							|| model.getPlansza()[y + i][x - i] == 'h'
							|| model.getPlansza()[y + i][x - i] == 'k'
							|| model.getPlansza()[y + i][x - i] == 'p') {
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
					if(model.getPlansza()[y - i][x + i] == 'S'
							|| model.getPlansza()[y - i][x + i] == 'W'
							|| model.getPlansza()[y - i][x + i] == 'G'
							|| model.getPlansza()[y - i][x + i] == 'H'
							|| model.getPlansza()[y - i][x + i] == 'K'
							|| model.getPlansza()[y - i][x + i] == 'P'){
						break;
					}
					if (model.getPlansza()[y - i][x + i] == 's'
							|| model.getPlansza()[y - i][x + i] == 'w'
							|| model.getPlansza()[y - i][x + i] == 'g'
							|| model.getPlansza()[y - i][x + i] == 'h'
							|| model.getPlansza()[y - i][x + i] == 'k'
							|| model.getPlansza()[y - i][x + i] == 'p') {
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
					if(model.getPlansza()[y - i][x - i] == 'S'
							|| model.getPlansza()[y - i][x - i] == 'W'
							|| model.getPlansza()[y - i][x - i] == 'G'
							|| model.getPlansza()[y - i][x - i] == 'H'
							|| model.getPlansza()[y - i][x - i] == 'K'
							|| model.getPlansza()[y - i][x - i] == 'P'){
						break;
					}
					if (model.getPlansza()[y - i][x - i] == 's'
							|| model.getPlansza()[y - i][x - i] == 'w'
							|| model.getPlansza()[y - i][x - i] == 'g'
							|| model.getPlansza()[y - i][x - i] == 'h'
							|| model.getPlansza()[y - i][x - i] == 'k'
							|| model.getPlansza()[y - i][x - i] == 'p') {
						listaWsp.add(new Punkt(x - i, y - i));
						break;
					}
				}
			}
		}
		else if(kolor == KolorFigury.CZARNY){
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
						break;
					}
					if (model.getPlansza()[y + i][x] == 's'
							|| model.getPlansza()[y + i][x] == 'w'
							|| model.getPlansza()[y + i][x] == 'g'
							|| model.getPlansza()[y + i][x] == 'h'
							|| model.getPlansza()[y + i][x] == 'k'
							|| model.getPlansza()[y + i][x] == 'p') {
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
					if (model.getPlansza()[y - i][x] == 'S' || model.getPlansza()[y - i][x] == 'W'
							|| model.getPlansza()[y - i][x] == 'G' || model.getPlansza()[y - i][x] == 'H'
							|| model.getPlansza()[y - i][x] == 'K' || model.getPlansza()[y - i][x] == 'P') {
						break;
					}
					if (model.getPlansza()[y - i][x] == 's' || model.getPlansza()[y - i][x] == 'w'
							|| model.getPlansza()[y - i][x] == 'g' || model.getPlansza()[y - i][x] == 'h'
							|| model.getPlansza()[y - i][x] == 'k' || model.getPlansza()[y - i][x] == 'p') {
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
					if (model.getPlansza()[y][x + i] == 'S'
							|| model.getPlansza()[y][x + i] == 'W'
							|| model.getPlansza()[y][x + i] == 'G'
							|| model.getPlansza()[y][x + i] == 'H'
							|| model.getPlansza()[y][x + i] == 'K'
							|| model.getPlansza()[y][x + i] == 'P') {
						break;
					}
					if (model.getPlansza()[y][x + i] == 's'
							|| model.getPlansza()[y][x + i] == 'w'
							|| model.getPlansza()[y][x + i] == 'g'
							|| model.getPlansza()[y][x + i] == 'h'
							|| model.getPlansza()[y][x + i] == 'k'
							|| model.getPlansza()[y][x + i] == 'p') {
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
					if (model.getPlansza()[y][x - i] == 'S'
							|| model.getPlansza()[y][x - i] == 'W'
							|| model.getPlansza()[y][x - i] == 'G'
							|| model.getPlansza()[y][x - i] == 'H'
							|| model.getPlansza()[y][x - i] == 'K'
							|| model.getPlansza()[y][x - i] == 'P') {
						break;
					}
					if (model.getPlansza()[y][x - i] == 's'
							|| model.getPlansza()[y][x - i] == 'w'
							|| model.getPlansza()[y][x - i] == 'g'
							|| model.getPlansza()[y][x - i] == 'h'
							|| model.getPlansza()[y][x - i] == 'k'
							|| model.getPlansza()[y][x - i] == 'p') {
						listaWsp.add(new Punkt(x - i, y));
						break;
					}
				}
			}
			break;
		case CZARNY:
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
