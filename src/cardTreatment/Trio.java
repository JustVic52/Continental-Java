package cardTreatment;

import java.util.*;

public class Trio {
	
	//catch (puedeQueMePiqueElCuloDentroDeDosDiasException pqmpecdddde) { }
	
	private List<Carta> trio;
	private int numOfTrio;
	private List<Integer> palos;
	
	public Trio() {
		trio = new ArrayList<>();
		palos = new ArrayList<>();
		numOfTrio = 0;
	}
	
	public boolean isDifferent(Carta carta) {
		if (numOfTrio == 0 && !carta.isComodin()) {
			numOfTrio = carta.getNumber();
			palos.add(carta.getPalo());
			return true;
		}
		if (palos.contains(carta.getPalo())) { return false; }
		if (!carta.isComodin()) {
			palos.add(carta.getPalo());
		}
		return true;
	}
	
	public void clear() {
		trio = new ArrayList<>();
		palos = new ArrayList<>();
		numOfTrio = 0;
	}
	
	public boolean canBeATrio(List<Carta> selection) { //verifica que el trío que le paso existe y lo almacena
		clear();
		int cont = 0;
		//Si la selección está vacía devuelve false
		if (selection.isEmpty() || selection.size() < 3 || selection.size() > 4) { return false; }
		//Recorro selection, si todas las cartas son del mismo número y distinto palo perfe
		for (Carta c : selection) {
			if (!c.isComodin() && numOfTrio != 0 && c.getNumber() != numOfTrio) { return false; }
			if (c.isComodin() || isDifferent(c)) { cont++; }
		}
		if (cont >= 3 && !palos.isEmpty()) {
			trio = selection;
			return true;
		}
		return false;
	}
	
	public boolean canBeAdded(List<Carta> aux, int i) {
		if (aux.size() <= 4) { return canBeATrio(aux); }
		if (aux.size() == 5 && i >= 0 && aux.get(i).isComodin()) {
			aux.remove(i);
			return canBeATrio(aux);
		}
		return false;
	}
	
	public String toString() {
		String res = "[";
		boolean first = true;
		for (Carta c : trio) {
			if (c != null) {
				if (!first) { res += ", "; }
				res += c;
				first = false;
			}
		}
		return res + "]";
	}

	public List<Carta> getTrio() {
		return trio;
	}
}
