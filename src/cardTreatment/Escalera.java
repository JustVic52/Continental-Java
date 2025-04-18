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
			} else {
				if (selection.get(i).isComodin() || selection.get(i).getPalo() == paloOfEscalera) {
					if (selection.get(i).isComodin() || selection.get(i + 1).getNumber() - selection.get(i).getNumber() == 1
							|| selection.get(i + 1).isComodin()) {
						cont++;
					}
					if (selection.get(i).getNumber() == 13 && selection.get(i + 1).getNumber() == 1) { cont++; }
				}
			}
		}
		
		if (cont >= 4) {
			escalera = selection;
			return true;
		}
		
		return false;
	}
	
	public boolean canBeAdded(List<Carta> aux, int i) {
		if (i >= 0 && aux.get(i + 1).isComodin()) { ;return canBeEscalera(aux); }
		if (i >= 0 && aux.get(i).isComodin()) { aux.remove(i); }
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
}
