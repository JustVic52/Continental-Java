package gameDynamics;

import java.util.ArrayList;
import java.util.List;

import gameTools.Carta;
import gameTools.Escalera;
import gameTools.Trio;

public class Combinaciones {
	
	private int trios, escaleras;
	private boolean canEscalera, canTrio;
	private List<Trio> listaTrios;
	private List<Escalera> listaEscalera;
	
	
	public void sacarTrios(List<Carta> selection, int numTrios) {
		listaTrios = new ArrayList<>();
		int tempTrios = numTrios;
		List<Carta> temp = new ArrayList<>(selection);
		//desde la selección dada saca todos los trios que le piden y los almacena en listaTrios
		for (int i = 0; i < numTrios; i++) {
			Trio t = new Trio();
			if (t.canBeATrio(temp, tempTrios)) {
				listaTrios.add(t);
				tempTrios--;
				for (Carta c : t.getTrio()) {
					temp.remove(c);
				}
			}
		}
		if (listaTrios.size() == numTrios) {
			canTrio = true;
		}
		selection.clear();
		selection.addAll(temp);
	}

	public void sacarEscaleras(List<Carta> selection, int numEscaleras) {
		listaEscalera = new ArrayList<>();
		List<Carta> temp = new ArrayList<>(selection);
		System.out.println("selection: " + temp);
		//desde la selección dada saca todas las escaleras que le piden y las almacena en listaEscaleras
		for (int i = 0; i < numEscaleras; i++) {
			Escalera e = new Escalera();
			if (e.canBeEscalera(temp)) {
				System.out.println("selection updated: " + temp);
				listaEscalera.add(e);
				for (Carta c : e.getEscalera()) {
					if (c != null) { if (c.getNumber() != 14) { temp.remove(c); } }
	            }
			}
		}
		if (listaEscalera.size() == numEscaleras) {
			canEscalera = true;
		}
		selection.clear();
		selection.addAll(temp);
	}
	
	public int getTrios() {
		return trios;
	}
	
	public boolean canBajarse(int numTrios, int numEscaleras, List<Carta> selection) {
		// coge los requisitos de la ronda y comprueba si puede:
		// - Crear X escaleras y luego X trios.
		// Si puede, devuelve true
		if (listaTrios != null) { listaTrios.clear(); }
		if (listaEscalera != null) { listaEscalera.clear(); }
		List<Carta> temp = new ArrayList<>(selection);
		boolean res = false;
		if (numEscaleras > 0) {
			sacarEscaleras(selection, numEscaleras);
			if (canEscalera) {
				if (numTrios > 0) {
					sacarTrios(selection, numTrios);
					if (canTrio) {
						res = true;
					} else {
						sacarTrios(temp, numTrios);
						if (canTrio) {
							sacarEscaleras(temp, numEscaleras);
							if (canEscalera) {
								res = true;
							}
						}
					}
				} else {
					res = true;
				}
			}
			selection.clear();
			selection.addAll(temp);
		} else {
			sacarTrios(selection, numTrios);
			if (canTrio) {
				res = true;
			}
		}
		return res;
	}
	
	public int getEscaleras() {
		return escaleras;
	}
	
	public List<Escalera> getListaEscaleras() {
		return listaEscalera;
	}
	
	public List<Trio> getListaTrios() {
		return listaTrios;
	}
}
