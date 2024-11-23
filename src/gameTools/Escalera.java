package gameTools;

import java.util.ArrayList;
import java.util.List;

public class Escalera {

	private Carta[] escalera;
	private int paloOfEscalera;
	private int[] numeros;
	private List<Carta> comodines;
	
	public Escalera() {
		escalera = new Carta[13];
		paloOfEscalera = 0;
		numeros = new int[13];
		comodines = new ArrayList<>();
	}
	
	public boolean canBeAdded(Carta carta) {
		return (paloOfEscalera == carta.getPalo() || carta.getPalo() == 5 || paloOfEscalera == 0)
				&& (carta.getNumber() == 14 || numeros[carta.getNumber() - 1] == 0);
	}
	
	public void clear() {
		escalera = new Carta[13];
		paloOfEscalera = 0;
		numeros = new int[13];
	}
	
	public Carta[] getEscalera() {
		return escalera;
	}
	
	public boolean canBeEscalera(List<Carta> selection) { //devuelve true si existe almenos una escalera
		clear();
		comodines = new ArrayList<>();
		List<Carta> temp = new ArrayList<>();
		for (int k = 0; k < selection.size(); k++) {
			if (selection.get(k).getNumber() == 14) {
				comodines.add(selection.get(k));
			} else { temp.add(selection.get(k)); }
		}
		selection.clear();
		selection.addAll(temp);
		int currentLength = 0;
		int maxLength = 0;
		int startIndex = -1;
		int currentStart = -1;
		for (int j = 1; j < 5; j++) {
			clear();
			int cont = 0;
			paloOfEscalera = j;
			for (int i = 0; i < selection.size(); i++) {
				if (canBeAdded(selection.get(i))) {
					add(selection.get(i));
				}
			}
			for (int i : numeros) {
				if (i > 0) { cont++; }
			}
			if (cont >= 4) {
				for (int k = 0; k < numeros.length; k++) {
					if (numeros[k] == 1) {
						if (currentLength == 0) { currentStart = k; }
						currentLength++;
					}
					else {
						maxLength = Math.max(maxLength, currentLength);
						startIndex = Math.max(startIndex,currentStart);
						currentLength = 0;
					}
				}
				maxLength = Math.max(maxLength, currentLength);
				startIndex = Math.max(startIndex,currentStart);
				
				if (maxLength >= 4) {
					for (int i = 0; i < startIndex - 1; i++) {
						escalera[i] = null;
						numeros[i] = 0;
					}
					for (int i = startIndex + maxLength; i < escalera.length; i++) {
						escalera[i] = null;
						numeros[i] = 0;
					}
					updateComodines(selection);
					return true;
				}
			}
			if (!comodines.isEmpty() && cont > 1) {
				int comodinesMax = comodines.size();
				int contComodines = 0;
				currentLength = 0;
				maxLength = 0;
				startIndex = -1;
				currentStart = -1;
				for (int i = 0; i < numeros.length; i++) {
					if (numeros[i] == 0) { add(comodines.get(comodines.size() - 1)); }
				}
				Carta tempCom = comodines.get(0);
				for (int k = 0; k < comodines.size(); k++) {
					comodinesMax = k + 1;
					for (int h = 0; h < numeros.length; h++) {
						System.out.println(escalera[h]);
						if (escalera[h].getNumber() != 14) {
							if (currentLength == 0) { currentStart = h; }
							currentLength++;
							if (!comodines.isEmpty()) { comodines.remove(0); }
							System.out.println(comodines);
							if (contComodines == comodinesMax && escalera[h + 1].getNumber() == 14) { 
								maxLength = Math.max(maxLength, currentLength);
								startIndex = Math.max(startIndex,currentStart);
								if (maxLength >= 4) {
									for (int i = 0; i < startIndex; i++) {
										escalera[i] = null;
										numeros[i] = 0;
									}
									for (int i = startIndex + maxLength; i < escalera.length; i++) {
										escalera[i] = null;
										numeros[i] = 0;
									}
									updateComodines(selection);
									return true;
								} else {
									for (int i = 0; i < contComodines; i++) {
										add(tempCom);
									}
									currentLength = 0;
									contComodines = 0;
									maxLength = 0;
									startIndex = -1;
									currentStart = -1;
								}
							}
						} else {
							if (currentLength > 0) {
								currentLength++;
								contComodines++;
								if (!comodines.isEmpty()) { comodines.remove(0); }
								if (contComodines == comodinesMax && escalera[h + 1].getNumber() == 14) { 
									maxLength = Math.max(maxLength, currentLength);
									startIndex = Math.max(startIndex,currentStart);
									if (maxLength >= 4) {
										for (int i = 0; i < startIndex; i++) {
											escalera[i] = null;
											numeros[i] = 0;
										}
										for (int i = startIndex + maxLength; i < escalera.length; i++) {
											escalera[i] = null;
											numeros[i] = 0;
										}
										updateComodines(selection);
										return true;
									} else {
										for (int i = 0; i < contComodines; i++) {
											add(tempCom);
										}
										currentLength = 0;
										contComodines = 0;
										maxLength = 0;
										startIndex = -1;
										currentStart = -1;
									}
								}
							}
						}
					}
				}
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
			while (i < numeros.length && numeros[i] != 0) {
				i++;
			}
			if (i < numeros.length) {
				escalera[i] = carta;
				numeros[i] = 1;
			}
		} else {
			if (numeros[carta.getNumber() - 1] == 0) {
				if (paloOfEscalera == 0) { paloOfEscalera = carta.getPalo(); }
				if (carta.getPalo() == paloOfEscalera) {
					escalera[carta.getNumber() - 1] = carta;
					numeros[carta.getNumber() - 1] = 1;
				}
			}
		}
	}
	
}
