package gameTools;

import java.util.List;
import java.util.ArrayList;

public class Descartes {
	
	private List<Carta> descartes;
	
	public Descartes() {
		descartes = new ArrayList<>();
	}
	
	public void take(Carta carta) {
		descartes.add(carta);
	}
	
	public void remove() {
		if (!descartes.isEmpty()) {
            descartes.remove(getSize() - 1); // Elimina y devuelve el último elemento
        }
	}
	
	public Carta getCarta() {
		return descartes.get(getSize() - 1);
	}
	
	public int getSize() {
		return descartes.size();
	}
	
	public boolean isEmpty() {
		return descartes.size() == 0;
	}
	
	public String toString() {
		return "Carta superior de descartes: " + getCarta().toString() + "\n";
	}

	public void clear() {
		descartes.clear();
	}
	
}
