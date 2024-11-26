package cardTreatment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Esto es tu mano personal. Aquí se acumulan las cartas que tengas en la mano y se eliminan una vez las descartes.

public class Mano extends Combinaciones {
	
	private List<Carta> mano;
	private List<Trio> bajadaTrios;
	private List<Escalera> bajadaEscalera;
	private List<Carta> selection;
	private List<Trio> resguardoTrios;
	private List<Escalera> resguardoEscaleras;
	private Baraja baraja;
	private Carta ultimaCartaEliminada; //para el retake
	private boolean canRetake = false;
	
	public Mano() {
		mano = new ArrayList<>();
		bajadaTrios = new ArrayList<>();
		bajadaEscalera = new ArrayList<>();
		selection = new ArrayList<>();
		baraja = new Baraja();
		resguardoTrios = new ArrayList<>();
		resguardoEscaleras = new ArrayList<>();
	}
	
	public void give() {
		Random random = new Random();
		int num = random.nextInt(baraja.getSize()); //genera un número aleatorio para la baraja (52 cartas y 6 comodines)
		mano.add(baraja.getCard(num)); //añade una carta de la baraja y la elimina de la lista. Si después de eso la baraja queda vacía, genera una nueva
	}
	
	public void discard(Carta carta) {
		mano.remove(carta);
		ultimaCartaEliminada = carta;
	}
	
	public void take(Carta carta) {
		mano.add(carta);
	}
	
	public void retake() {
		if (canRetake) { mano.add(ultimaCartaEliminada); }
	}
	
	public void select(int num) {
		if (!mano.get(num).isResguardada()) {
			selection.add(mano.get(num));
			mano.get(num).setSeleccionada(true);
		}
	}
	
	public void deselect(int num) {
		selection.get(num).setSeleccionada(false);
		selection.remove(num);
	}
	
	public void setRetake(boolean retake) {
		canRetake = retake;
	}
	
	public boolean getRetake() {
		return canRetake;
	}
	
	public void resguardarTrios() { //coge los tríos y los pasa al resguardo.
		for (Trio t : getListaTrios()) {
			resguardoTrios.add(t);
			for (int i = 0; i < t.getTrio().length; i++) {
				if (t.getTrio()[i] != null) {
					t.getTrio()[i].setResguardada(true);
					t.getTrio()[i].setSeleccionada(false);
				}
			}
		}
		for (int i = 0; i < selection.size(); i++) {
			selection.get(i).setSeleccionada(false);
		}
		if (!selection.isEmpty()) { selection.clear(); }
		updateManoTrios();
	}
	
	public void resguardarEscaleras() { //coge las escaleras y las pasa al resguardo.
		for (Escalera e : getListaEscaleras()) {
			resguardoEscaleras.add(e);
			for (int i = 0; i < e.getEscalera().length; i++) {
				if (e.getEscalera()[i] != null) {
					e.getEscalera()[i].setResguardada(true);
					e.getEscalera()[i].setSeleccionada(true);
				}
			}
		}
		for (int i = 0; i < selection.size(); i++) {
			selection.get(i).setSeleccionada(false);
		}
		if (!selection.isEmpty()) { selection.clear(); }
		updateManoEscaleras();
	}
	
	public void bajarTrios() {
		if (!resguardoTrios.isEmpty()) {
			for (Trio t : resguardoTrios) {
				bajadaTrios.add(t);
				for (int i = 0; i < t.getTrio().length; i++) {
					if (t.getTrio()[i] != null) { t.getTrio()[i].setResguardada(false); }
				}
			}
			if (getListaTrios() != null) { getListaTrios().clear(); }
			if (resguardoTrios.size() > 0) { resguardoTrios.clear(); }
		}
	}
	
	public void bajarEscaleras() {
		if (!resguardoEscaleras.isEmpty()) {
			for (Escalera e : resguardoEscaleras) {
				bajadaEscalera.add(e);
				for (int i = 0; i < e.getEscalera().length; i++) {
					if (e.getEscalera()[i] != null) { e.getEscalera()[i].setResguardada(false); }
				}
			}
			if (getListaEscaleras() != null) { getListaEscaleras().clear(); }
			if (resguardoEscaleras.size() > 0) { resguardoEscaleras.clear(); }
		}
	}

	private void updateManoTrios() {
		if (!resguardoTrios.isEmpty()) {
			Trio t = resguardoTrios.get(resguardoTrios.size() - 1);
			for (Carta c : t.getTrio()) {
				mano.remove(c);
			}
		}
	}
	
	private void updateManoEscaleras() {
		if (!resguardoEscaleras.isEmpty()) {
			Escalera e = resguardoEscaleras.get(resguardoEscaleras.size() - 1);
			for (Carta c : e.getEscalera()) {
				mano.remove(c);
			}
		}
	}
	
	public int getSize() {
		return mano.size();
	}
	
	public List<Carta> getMano() {
		return mano;
	}
	
	public List<Carta> getSelection() {
		return selection;
	}
	
	public List<Trio> getBajadaTrios() {
		return bajadaTrios;
	}
	
	public List<Escalera> getBajadaEscaleras() {
		return bajadaEscalera;
	}
	
	public String toString() {
		String res = "Cartas en la mano: " + mano.toString() + "\n";
		if (!bajadaEscalera.isEmpty()) { res += "Cartas bajadas (Escaleras): " + bajadaEscalera + "\n"; }
		if (!bajadaTrios.isEmpty()) { res += "Cartas bajadas (Tríos): " + bajadaTrios + "\n"; }
		if (!selection.isEmpty()) { res += "Cartas seleccionadas: " + selection + "\n"; }
		if (!resguardoEscaleras.isEmpty()) { res += "Resguardo (Escaleras): " + resguardoEscaleras + "\n"; }
		if (!resguardoTrios.isEmpty()) { res += "Resguardo (Tríos): " + resguardoTrios + "\n"; }
		return res;
	}
	
	public String bajadaToString() {
		String res = "";
		if (!bajadaEscalera.isEmpty()) { res += "Cartas bajadas (Escaleras): " + bajadaEscalera + "\n"; }
		if (!bajadaTrios.isEmpty()) { res += "Cartas bajadas (Tríos): " + bajadaTrios + "\n"; }
		return res;
	}
	
	public String selectionToString() {
		String res = "";
		if (!selection.isEmpty()) { res += "Cartas seleccionadas: " + selection + "\n"; }
		return res;
	}

	public Carta getUltimaCarta() {
		return ultimaCartaEliminada;
	}

	public List<Escalera> getResguardoEscaleras() {
		return resguardoEscaleras;
	}
	
	public List<Trio> getResguardoTrios() {
		return resguardoTrios;
	}
}
