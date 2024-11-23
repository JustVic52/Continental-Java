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
	private List<Escalera> listaEscaleras;
	
	
	public void sacarTrios(List<Carta> selection) {
		canTrio = false;
		listaTrios = new ArrayList<>();
		List<Carta> temp = new ArrayList<>(selection);
		//desde la selección dada saca el trío que le piden y lo almacena en listaTrios
		Trio t = new Trio();
		if (t.canBeATrio(temp)) {
			listaTrios.add(t);
			for (Carta c : t.getTrio()) {
				temp.remove(c);
			}
			canTrio = true;
		}
		selection.clear();
		selection.addAll(temp);
	}

	public void sacarEscaleras(List<Carta> selection) {
		canEscalera = false;
		listaEscaleras = new ArrayList<>();
		List<Carta> temp = new ArrayList<>(selection);
		System.out.println("selection: " + temp);
		//desde la selección dada saca la escalera que le piden y la almacena en listaEscaleras
		Escalera e = new Escalera();
		if (e.canBeEscalera(temp)) {
			listaEscaleras.add(e);
			for (Carta c : e.getEscalera()) {
				temp.remove(c);
            }
			canEscalera = true;
		}
		selection.clear();
		selection.addAll(temp);
	}
	
	public int getTrios() {
		return trios;
	}
	
	public boolean canBajarse(int numTrios, int numEscaleras) {
		// coge los requisitos de la ronda y comprueba si se cumplen con listaTrios y listaEscaleras
		// Si puede, devuelve true
		boolean resTrios = false;
		boolean resEscaleras = false;
		if (listaEscaleras != null && listaEscaleras.size() == numEscaleras) {
			resEscaleras = true;
		}
		if (listaTrios != null && listaTrios.size() == numTrios) {
			resTrios = true;
		}
		return resTrios && resEscaleras;
	}
	
	public int getEscaleras() {
		return escaleras;
	}
	
	public List<Escalera> getListaEscaleras() {
		return listaEscaleras;
	}
	
	public List<Trio> getListaTrios() {
		return listaTrios;
	}
	
	public boolean getCanEscalera() {
		return canEscalera;
	}
	
	public boolean getCanTrio() {
		return canTrio;
	}
}
