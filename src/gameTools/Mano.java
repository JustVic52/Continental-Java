package gameTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gameDynamics.Combinaciones;

//Esto es tu mano personal. Aquí se acumulan las cartas que tengas en la mano y se eliminan una vez las descartes.

public class Mano extends Combinaciones {
	
	private List<Carta> mano;
	private List<Trio> bajadaTrios;
	private List<Escalera> bajadaEscalera;
	private List<Carta> selection;
	private Baraja baraja;
	private Carta ultimaCartaEliminada; //para el retake
	private boolean canRetake = false;
	
	public Mano() {
		mano = new ArrayList<>();
		bajadaTrios = new ArrayList<>();
		bajadaEscalera = new ArrayList<>();
		selection = new ArrayList<>();
		baraja = new Baraja();
	}
	
	public void give() {
		Random random = new Random();
		int num = random.nextInt(baraja.getSize()); //genera un número aleatorio para la baraja (52 cartas y 6 comodines)
		mano.add(baraja.getCard(num)); //añade una carta de la baraja y la elimina de la lista. Si después de eso la baraja queda vacía, genera una nueva
	}
	
	public void discard(Carta carta) {
		for (int i = 0; i < mano.size(); i++) {
			if (carta.equals(mano.get(i))) {
				mano.remove(carta);
				ultimaCartaEliminada = carta;
			}
		}
	}
	
	public void take(Carta carta) {
		mano.add(carta);
	}
	
	public void retake() {
		if (canRetake) { mano.add(ultimaCartaEliminada); }
	}
	
	public void select(int num) {
		selection.add(mano.get(num));
		mano.get(num).setSeleccionada(true);
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
	
	public void bajarTrios(List<Trio> bajar) { //coge los tríos y los pasa a la bajada.
		for (int i = 0; i < bajar.size(); i++) {
			bajadaTrios.add(bajar.get(i));
		}
		if (selection.size() > 0) { selection.clear(); }
		updateMano();
	}
	
	public void bajarEscaleras(List<Escalera> bajar) { //coge las escaleras y las pasa a la bajada.
		for (int i = 0; i < bajar.size(); i++) {
			bajadaEscalera.add(bajar.get(i));
		}
		if (selection.size() > 0) { selection.clear(); }
		updateMano();
	}

	private void updateMano() {
		if (!bajadaTrios.isEmpty()) {
			for (Trio t : bajadaTrios) {
				for (Carta c : t.getTrio()) {
					mano.remove(c);
				}
			}
		}
		if (!bajadaEscalera.isEmpty()) {
			for (Escalera e : bajadaEscalera) {
				for (Carta c : e.getEscalera()) {
					mano.remove(c);
				}
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
		if (!bajadaTrios.isEmpty()) { res += "Cartas bajadas (Trios): " + bajadaTrios.toString() + "\n"; }
		if (!bajadaEscalera.isEmpty()) { res += "Cartas bajadas (Escaleras): " + bajadaEscalera.toString() + "\n"; }
		if (!selection.isEmpty()) { res += "Cartas seleccionadas: " + selection.toString() + "\n"; }
		return res;
	}

	public Carta getUltimaCarta() {
		return ultimaCartaEliminada;
	}
}
