package gameTools;

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
					if (carta.getNumber() != 14) { numOfTrio = carta.getNumber(); }
					added = true;
				}
				i++;
			}
		}
	}
	
	public void remove(int num) {
		trio[num - 1] = null;
		palos[num - 1] = 0;
	}
	
	public boolean canBeAdded(Carta carta) {
		return trio[3] == null && isDifferent(carta)
				&& (numOfTrio == carta.getNumber() || carta.getNumber() == 14 || numOfTrio == 0);
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
	
	public boolean canBeATrio(List<Carta> selection, int tempTrios) { //devuelve true si existe almenos un trío dentro de la selección y lo almacena
		clear();
		int j = 0;
		int cont = 0;
		comodines = new ArrayList<>();
		List<Carta> temp = new ArrayList<>();
		for (int k = 0; k < selection.size(); k++) {
			if (selection.get(k).getPalo() == 5) {
				comodines.add(selection.get(k));
			} else { temp.add(selection.get(k)); }
		}
		selection.clear();
		selection.addAll(temp);
		while (trio[3] == null && j < selection.size()) {
			ArrayList<Integer> checked = new ArrayList<>();
			for (Carta c : selection) {
				if (!checked.contains(c.getNumber())) { checked.add(c.getNumber()); }
			}
			cont = checked.size();
			int i = j;
			while (trio[3] == null && i < selection.size()) {
				if (canBeAdded(selection.get(i))) {
					add(selection.get(i));
				}
				if (trio[1] == null && comodines.size() > 1 && cont == 1) { break; }
				if (trio[2] != null && !comodines.isEmpty() && tempTrios > 3) { break; }
				i++;
			} 
			if (trio[2] != null) { //primero comprueba que el trío se ha formado y finaliza
				updateComodines(selection);
				return true; 
			} else if (trio[1] != null && !comodines.isEmpty()) { //sino, comprueba que se puede añadir 1 comodín y finaliza
				add(comodines.get(0));
				updateComodines(selection);
				return true;
			} else if (trio[0] != null && comodines.size() > 1) { //sino, si ya no quedan cartas y hay 2 comodines se añaden y finaliza
				for (int h = 0; h < 2; h++) {
				    add(comodines.get(h));
				}
				updateComodines(selection);
				return true;
			} else { //y por último, si no se puede hacer nada de lo anterior, avanza
				clear();
				j++;
			}
		}
		updateComodines(selection);
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
	
}
