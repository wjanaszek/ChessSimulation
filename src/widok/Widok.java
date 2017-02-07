package widok;

import model.Model;

public class Widok {
	private Model model;
	private char[][] plansza;
	
	public Widok(Model model){
		this.model = model;
	}
	
	public void rysujPlansze(){
		plansza = model.getPlansza();
		int tmp;
		for(int i = 0; i < 2; i++){
			System.out.print(" ");
		}
		rysujLitery();
		for(int i = 0; i < plansza.length; i++){
			System.out.print(i + 1 + " ");
			for(int j = 0; j < plansza[i].length; j++){
				System.out.print(plansza[i][j]);
			}
			tmp = i;
			tmp++;
			System.out.println(" " + tmp);
		}
		for(int i = 0; i < 2; i++){
			System.out.print(" ");
		}
		rysujLitery();
	}
	
	public void rysujPowitanie(){
		System.out.println("Gracz nr 1: CPU #1 (biale)");
		System.out.println("Gracz nr 2: CPU #2 (czarne)");
	}
	
	private void rysujLitery(){
		char c = '-';
		for(int i = 0; i < 8; i++){
			if(i == 0){
				c = 'A';
			}
			if(i == 1){
				c = 'B';
			}
			if(i == 2){
				c = 'C';
			}
			if(i == 3){
				c = 'D';
			}
			if(i == 4){
				c = 'E';
			}
			if(i == 5){
				c = 'F';
			}
			if(i == 6){
				c = 'G';
			}
			if(i == 7){
				c = 'H';
			}
			System.out.print(c);
		}
		System.out.println();
	}
	//
}
