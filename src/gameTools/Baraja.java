package gameTools;

import java.util.ArrayList;
import java.util.List;

public class Baraja {
	
	private List<Carta> baraja;
	
	public Baraja() { //Crea una baraja con 52 + 6 = 58 cartas para aleatorizar cuál extrae. Es una baraja con comodín
		baraja = new ArrayList<>();
		newBaraja();
	}
	
	public void newBaraja() {
		for (int i = 0; i < Carta.DIAMANTES; i++) {
			for (int j = 0; j < Carta.KING; j++) {
				baraja.add(new Carta(j + 1, i + 1));
			}
		}
		for (int i = 0; i < 6; i++) {
			baraja.add(new Carta());
		}
	}
	
	public Carta getCard(int num) {
		Carta carta = baraja.get(num);
		baraja.remove(num);
		if (baraja.size() == 0) {
			newBaraja();
		}
		return carta;
		
	}
	
	public int getSize() {
		return baraja.size();
	}
}
