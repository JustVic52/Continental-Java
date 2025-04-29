package cardTreatment;

import java.util.ArrayList;
import java.util.List;

public class Escalera {

	private List<Carta> escalera;
	private int paloOfEscalera;
	
	public Escalera() {
		escalera = new ArrayList<>();
		paloOfEscalera = 0;
	}
	
	public void clear() {
		escalera = new ArrayList<>();
		paloOfEscalera = 0;
	}
	
	public List<Carta> getEscalera() {
		return escalera;
	}
	
	public boolean canBeEscalera(List<Carta> selection) { //verifica la escalera que le paso y la almacena
		clear();
		//Si la selección está vacía o no tiene suficiente devuelve false
		if (selection.isEmpty() || selection.size() < 4 || selection.size() > 13) { return false; }
		//Recorro selection. Si encuentro una sucesión de cartas/comodines que es una escalera, la guardo.
		int cont = 0;
		for (int i = 0; i < selection.size(); i++) {
			if (!selection.get(i).isComodin() && paloOfEscalera == 0) { paloOfEscalera = selection.get(i).getPalo(); }
			if (i == selection.size() - 1) {
				if (selection.get(i).isComodin() || selection.get(i).getPalo() == paloOfEscalera) { cont++; }
				else return false;
			} else {
				if (selection.get(i).isComodin()) { cont++; }
				else {
					if (selection.get(i).getPalo() == paloOfEscalera) {
						if (selection.get(i + 1).isComodin()) {
							//Si la carta no es comodín y la siguiente si, voy a ir hasta la siguiente carta que no sea comodín y compruebo
							//si la resta de esos valores - 1 es igual a la cantidad de comodines entre ellos. Si es así, es correcto.
							//si no hay más cartas sin comodín, contar y ya.
							int contAux = 0;
							int cond = -1;
							int j = i + 1;
							while (j < selection.size() && selection.get(j).isComodin()) {
								contAux++;
								j++;
							}
							if (j == selection.size()) j--;
							if (selection.get(j).isComodin()) cont++;
							else if (selection.get(i).getNumber() > selection.get(j).getNumber()) {
								cond = 13 - selection.get(i).getNumber() + selection.get(j).getNumber() - 1;
								if (cond == contAux) cont++;
								else return false;
							} else { 
								cond = selection.get(j).getNumber() - selection.get(i).getNumber() - 1;
								if (cond == contAux) cont++;
								else return false;
							}
						}
						else if (selection.get(i + 1).getNumber() - selection.get(i).getNumber() == 1
								|| selection.get(i + 1).isComodin()) { cont++; }
						else if (selection.get(i).getNumber() == 13 && (selection.get(i + 1).getNumber() == 1 || selection.get(i + 1).isComodin())) { cont++; }
						else return false;
					} else return false; 
				}
			}
		}
		
		if (cont >= 4 && paloOfEscalera != 0) {
			escalera = selection;
			return true;
		}
		
		return false;
	}
	
//	private String numerosToString() {
//		String res = "[";
//		boolean first = true;
//		for (int i : numeros) {
//			if (!first) { res += ", "; }
//			res += i;
//			first = false;
//		}
//		return res + "]";
//	}

	public boolean canBeAdded(List<Carta> aux, int i) {
		if (i >= 0 && aux.get(i + 1).isComodin()) { return canBeEscalera(aux); }
		if (i >= 0 && aux.get(i).isComodin()) {
			if (aux.size() > 13) aux.remove(i);
			else if (!canBeEscalera(aux)) aux.remove(i);
		}
		return canBeEscalera(aux);
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
	//No lo voy a quitar ahora porque ya es la cuarta vez que lo pongo pensando que solo lo voy a usar una vez xd
//	public static void main(String[] args) {
//		
//		ArrayList<Carta> aux = new ArrayList<>();
//		aux.add(new Carta(5, 3, 100,100));
//		aux.add(new Carta(6, 3, 100,100));
//		aux.add(new Carta(7, 3, 100,100));
//		aux.add(new Carta(100,100));
//		aux.add(new Carta(9, 3, 100,100));
//		aux.add(new Carta(2, 3,100,100));
//		Escalera escalera = new Escalera();
//		System.out.println(aux);
//		System.out.println(escalera.canBeAdded(aux, 3));
//		System.out.println(escalera.getEscalera());
//		
//	}
}
