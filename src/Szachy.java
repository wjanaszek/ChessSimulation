import kontroler.Kontroler;
import model.Model;
import widok.Widok;

/*
 * Punkt wejsciowy programu
 */
public class Szachy {

	public static void main(String[] args) {
		Model model = new Model();
		Widok widok = new Widok(model);
		Kontroler ktr = new Kontroler(model, widok);
		ktr.start();
	}

}
