package cardTreatment;

import java.util.*;

public class Trio {
	
	//catch (puedeQueMePiqueElCuloDentroDeDosDiasException pqmpecdddde) { }
	
	private Carta[] trio;
	private int numOfTrio;
	private int[] palos;
	private List<Carta> comodines;
	
	public Trio() {
		trio = new Carta[4];
		palos = new int[4];
		numOfTrio = 0;
		comodines = new ArrayList<>();
	}
	
	public boolean isDifferent(Carta carta) {
		for (int i = 0; i < palos.length; i++) {
			if (palos[i] == carta.getPalo() && carta.getPalo() != 5) {
				return false;
			}
		}
		return true;
	}
	
	public void add(Carta carta) {
		int i = 0;
		boolean added = false;
		if (canBeAdded(carta)) {
			while (i < trio.length && !added) {
				if (trio[i] == null) {
					trio[i] = carta;
					palos[i] = carta.getPalo();
					if (numOfTrio == 0 && carta.getNumber() != 14) { numOfTrio = carta.getNumber(); }
					added = true;
				} else if (trio[i] != null && trio[i].getNumber() == 14 && (trio[3] == null || (trio[3] != null && trio[3].getNumber() == 14))) {
					trio[i] = carta;
					palos[i] = carta.getPalo();
					added = true;
				}
				i++;
			}
		}
	}

	public int getNumOfTrio() {
		return numOfTrio;
	}
	
	public void remove(int num) {
		trio[num - 1] = null;
		palos[num - 1] = 0;
	}
	
	public boolean canBeAdded(Carta carta) {
		return (trio[3] == null || (trio[3] != null && trio[3].getNumber() == 14)) && isDifferent(carta) && (carta.getNumber() == numOfTrio || carta.getNumber() == 14 || numOfTrio == 0);
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
	
	public void clear() {
		trio = new Carta[4];
		palos = new int[4];
		numOfTrio = 0;
	}
	
	public boolean canBeATrio(List<Carta> selection) { //verifica que el trío que le paso existe y lo almacena
		clear();
		//Si la selección está vacía devuelve false
		if (selection.isEmpty()) { return false; }
		//Sino, Saco los comodines de la selección
		comodines = new ArrayList<>();
		List<Carta> temp = new ArrayList<>();
		for (int k = 0; k < selection.size(); k++) {
			if (selection.get(k).getPalo() == 5) {
				comodines.add(selection.get(k));
			} else { temp.add(selection.get(k)); }
		}
		selection.clear();
		selection.addAll(temp);
		//Compruebo si hay más de un número, si lo hay, no puedo hacer el trío
		ArrayList<Integer> checked = new ArrayList<>();
		for (Carta c : selection) {
			if (!checked.contains(c.getNumber())) { checked.add(c.getNumber()); }
		}
		if (checked.size() != 1) { return false; }
		//Formo el trío con las cartas que no son comodines
		for (int i = 0; i < selection.size(); i++) {
			if (canBeAdded(selection.get(i))) { add(selection.get(i)); }
			if (trio[2] != null && comodines.isEmpty()) {
				updateComodines(selection);
				return true;
			}
		}
		//Si aún faltan huecos, relleno con comodines
		if (!comodines.isEmpty()) {
			for (Carta c : comodines) {
				add(c);
				if (trio[3] != null) {
					updateComodines(selection);
					return true;
				}
			}
			if (trio[2] != null) {
				updateComodines(selection);
				return true;
			}
		}
		//Si nada de eso ha funcionado, pues no es un trío.
		return false;
	}
	
	private void updateComodines(List<Carta> selection) {
		if (!comodines.isEmpty()) {
			for (int i = 0; i < comodines.size(); i++) {
				selection.add(comodines.get(i));
			}
		}
	}

	public Carta[] getTrio() {
		return trio;
	}
	
	public int[] getPalos() {
		return palos;
	}
}
