package cardTreatment;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Escalera {

	private Carta[] escalera;
	private int paloOfEscalera;
	private int[] numeros;
	private List<Carta> comodines;
	private Rectangle hitbox;
	
	public Escalera() {
		escalera = new Carta[13];
		paloOfEscalera = 0;
		numeros = new int[13];
		comodines = new ArrayList<>();
		hitbox = new Rectangle(0, 0, 338, 52);
	}
	
	public boolean canBeAdded(Carta carta) {
		return (paloOfEscalera == carta.getPalo() || carta.getPalo() == 5 || paloOfEscalera == 0)
				&& (carta.getNumber() == 14 || numeros[carta.getNumber() - 1] == 0 
				|| (escalera[carta.getNumber() - 1] != null && escalera [carta.getNumber() - 1].getNumber() == 14));
	}
	
	public boolean canBeNewAdded(Carta carta) {
		return (paloOfEscalera == carta.getPalo() || carta.getPalo() == 5 || paloOfEscalera == 0)
				&& (carta.getNumber() == 14 || numeros[carta.getNumber() - 1] == 0 
				|| (escalera[carta.getNumber() - 1] != null && escalera [carta.getNumber() - 1].getNumber() == 14))
				&& (numeros[carta.getNumber() - 2] == 1 || numeros[carta.getNumber()] == 1);
	}
	
	public void clear() {
		escalera = new Carta[13];
		paloOfEscalera = 0;
		numeros = new int[13];
	}
	
	public Carta[] getEscalera() {
		return escalera;
	}
	
	public boolean canBeEscalera(List<Carta> selection) { //verifica la escalera que le paso y la almacena
		clear();
		//Si la selección está vacía devuelve false
		if (selection.isEmpty() || selection.size() < 4) { return false; }
		//Sino, saco los comodines
		comodines = new ArrayList<>();
		List<Carta> temp = new ArrayList<>();
		for (int k = 0; k < selection.size(); k++) {
			if (selection.get(k).getNumber() == 14) {
				comodines.add(selection.get(k));
			} else { temp.add(selection.get(k)); }
		}
		selection.clear();
		selection.addAll(temp);
		//Si hay más de un palo no voy a hacer la escalera
		ArrayList<Integer> checked = new ArrayList<>();
		for (Carta c : selection) {
			if (!checked.contains(c.getPalo())) { checked.add(c.getPalo()); }
		}
		if (checked.size() != 1) { return false; }
		//Estas variables me ayudan a determinar la longitud de la escalera
		int currentLength = 0;
		int maxLength = 0;
		int startIndex = -1;
		int currentStart = -1;
		int cont = 0;
		//Añadimos las cartas a la escalera y contamos si hay al menos 1
		for (int i = 0; i < selection.size(); i++) {
			if (canBeAdded(selection.get(i))) {
				add(selection.get(i));
			}
		}
		for (int i : numeros) {
			if (i > 0) { cont++; }
		}
		if (cont < 1) { return false; }
		//Añado los comodines
		if (!comodines.isEmpty() && cont > 1) {
			for(Carta c : comodines) {
				add(c);
				cont++;
			}
			comodines.clear();
		}
		//Si hay 4 o más encontramos la escalera
		if (cont >= 4) {
			boolean vuelta = false;
			for (int k = 0; k < numeros.length; k++) {
				if (numeros[k] == 1) {
					if (currentLength == 0) { currentStart = k; }
					currentLength++;
					if (k == 12 && numeros[0] == 1 && !vuelta) {
						k = -1;
						vuelta = true;
					}
				} else {
					maxLength = Math.max(maxLength, currentLength);
					startIndex = Math.max(startIndex,currentStart);
					currentLength = 0;
					if (vuelta) { break; }
				}
			}
			maxLength = Math.max(maxLength, currentLength);
			startIndex = Math.max(startIndex,currentStart);
			
			if (maxLength >= 4) {
				if (startIndex + maxLength > 13) {
					for (int i = startIndex + maxLength - 13; i < startIndex; i++) {
						escalera[i] = null;
						numeros[i] = 0;
					}
				} else {
					for (int i = 0; i < startIndex; i++) {
						escalera[i] = null;
						numeros[i] = 0;
					}
					for (int i = startIndex + maxLength; i < numeros.length; i++) {
						escalera[i] = null;
						numeros[i] = 0;
					}
				}
				updateComodines(selection);
				return true;
			}
		}
		return false;
	}
	
	public void remove(int num) {
		escalera[num - 1] = null;
		numeros[num - 1] = 0;
	}
	
	private void updateComodines(List<Carta> selection) {
		if (!comodines.isEmpty()) {
			for (int i = 0; i < comodines.size(); i++) {
				selection.add(comodines.get(i));
			}
		}
	}
	
	public String toString() {
		String res = "[";
		boolean first = true;
		for (Carta c : escalera) {
			if (c != null) {
				if (!first) { res += ", "; }
				res += c;
				first = false;
			}
		}
		return res + "]";
	}
	
	public String numerosToString() {
		String res = "[";
		for (int i = 0; i < numeros.length - 1; i++) {
			res += numeros[i] + ", ";
		}
		res += numeros[12] + "]";
		return res;
	}

	public void add(Carta carta) {
		if (carta.getNumber() == 14) {
			int i = 0;
			while (i < numeros.length && numeros[i] != 1) {
				i++;
			}
			if (i < numeros.length) {
				int j = i;
				while (j < numeros.length && numeros[j] != 0) {
					j++;
				}
				if (j < numeros.length) {
					escalera[j] = carta;
					numeros[j] = 1;
				} else {
					escalera[i - 1] = carta;
					numeros[i - 1] = 1;
				}
			}
		} else {
			if (numeros[carta.getNumber() - 1] == 0) {
				if (paloOfEscalera == 0) { paloOfEscalera = carta.getPalo(); }
				if (carta.getPalo() == paloOfEscalera) {
					escalera[carta.getNumber() - 1] = carta;
					numeros[carta.getNumber() - 1] = 1;
				}
			} else {
				if (escalera[carta.getNumber() - 1].getNumber() == 14) {
					Carta temp = escalera[carta.getNumber() - 1];
					escalera[carta.getNumber() - 1] = carta;
					add(temp);
				}
			}
		}
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
	
}
