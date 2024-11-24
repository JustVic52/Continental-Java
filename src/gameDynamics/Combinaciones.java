package gameDynamics;

import java.util.ArrayList;
import java.util.List;

import gameTools.Carta;
import gameTools.Escalera;
import gameTools.Trio;

public class Combinaciones {
	
	private int trios, escaleras;
	private boolean canEscalera, canTrio, canAdded;
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
			trios++;
		}
		selection.clear();
		selection.addAll(temp);
	}
	
	public void comprobarTrio(Carta carta, Trio t) {
		canAdded = false;
		if (t.canBeAdded(carta)) {
			t.add(carta);
			canAdded = true;
		}
	}
	
	public boolean getAdded() {
		return canAdded;
	}

	public void sacarEscaleras(List<Carta> selection) {
		canEscalera = false;
		List<Carta> temp = new ArrayList<>(selection);
		listaEscaleras = new ArrayList<>();
		//desde la selección dada saca la escalera que le piden y la almacena en listaEscaleras
		Escalera e = new Escalera();
		if (e.canBeEscalera(temp)) {
			listaEscaleras.add(e);
			for (Carta c : e.getEscalera()) {
				temp.remove(c);
            }
			canEscalera = true;
			escaleras++;
		}
		selection.clear();
		selection.addAll(temp);
	}
	
	public void comprobarEscalera(Carta carta, Escalera e) {
		canAdded = false;
		if (e.canBeAdded(carta)) {
			e.add(carta);
			canAdded = true;
		}
	}
	
	public int getTrios() {
		return trios;
	}
	
	public void setTrios(int t) {
		trios = t;
	}
	
	public boolean canBajarse(int numTrios, int numEscaleras) {
		// coge los requisitos de la ronda y comprueba si se cumplen con listaTrios y listaEscaleras
		// Si puede, devuelve true
		boolean resTrios = false;
		boolean resEscaleras = false;
		if (numEscaleras != 0) {
			if (listaEscaleras != null && escaleras == numEscaleras) {
				resEscaleras = true;
				if (numTrios != 0) {
					if (listaTrios != null && trios == numTrios) {
						resTrios = true;
						return resTrios && resEscaleras;
					}
				} else { return resEscaleras; }
			}
		} else {
			if (trios == numTrios) {
				resTrios = true;
				return resTrios;
			}
		}
		return resTrios && resEscaleras;
	}
	
	public int getEscaleras() {
		return escaleras;
	}
	
	public void setEscaleras(int e) {
		escaleras = e;
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
