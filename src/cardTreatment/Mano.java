package cardTreatment;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import gameDynamics.Partida;
import utilz.LoadSave;

//Esto es tu mano personal. Aquí se acumulan las cartas que tengas en la mano y se eliminan una vez las descartes.

public class Mano extends Combinaciones {
	
	private List<Carta> mano;
	private List<Slot> manoSlot;
	private List<Trio> bajadaTrios;
	private List<Escalera> bajadaEscalera;
	private List<Carta> selection;
	private List<Trio> resguardoTrios;
	private List<Escalera> resguardoEscaleras;
	private Carta ultimaCartaEliminada; //para el retake
	private boolean canRetake = false;
	private BufferedImage img;
	
	public Mano() {
		mano = new ArrayList<>();
		manoSlot = new ArrayList<>();
		initSlot();
		bajadaTrios = new ArrayList<>();
		bajadaEscalera = new ArrayList<>();
		selection = new ArrayList<>();
		resguardoTrios = new ArrayList<>();
		resguardoEscaleras = new ArrayList<>();
		importCards();
	}
	
	private void initSlot() {
		int x = 227;
		int y = 474;
		for (int i = 0; i < 22; i++) {
			manoSlot.add(new Slot(x, y));
			if (x == 1027) {
				x = 227;
				y = 584;
			} else { x += 80; }
		} 
	}

	private void importCards() {
		img = LoadSave.GetSpriteAtlas(LoadSave.CARD_ATLAS);
	}
	
	public void updateMano() {
		for (Slot s : manoSlot) {
			s.getCarta().updateHitbox();
		}
	}
	
	public void renderSlots(Graphics g) {
		ArrayList<Slot> aux = new ArrayList<>(manoSlot);
		for (Slot s : aux) {
			s.render(g, img);
		}
	}
	
	public BufferedImage getImage() {
		return img;
	}

	public void give() {
		Random random = new Random();
		int num = random.nextInt(Partida.baraja.getSize()); //genera un número aleatorio para la baraja (52 cartas y 6 comodines)
		Carta c = Partida.baraja.getCard(num);
		mano.add(c); //añade una carta de la baraja y la elimina de la lista.
		addToSlot(c); //añade la carta al slot
	}
	
	private void addToSlot(Carta c) {
		int i = 0;
		boolean added = false;
		while (i < manoSlot.size() && !added) {
			if (manoSlot.get(i).getCarta() == null) {
				manoSlot.get(i).add(c);
				added = true;
			}
			i++;
		}
	}

	public void discard(Carta carta) {
		mano.remove(carta);
		removeFromSlots(carta);
		ultimaCartaEliminada = carta;
	}
	
	private void removeFromSlots(Carta carta) {
		int i = 0;
		boolean removed = false;
		while (i < manoSlot.size() && !removed) {
			if (manoSlot.get(i).getCarta().equals(carta)) {
				manoSlot.get(i).remove();
				updateSlots(i);
				removed = true;
			}
			i++;
		}
	}

	private void updateSlots(int pos) {
		for (int i = pos; i < manoSlot.size() - 1; i++) {
			manoSlot.get(i).add(manoSlot.get(i + 1).getCarta());
		}
	}

	public void take(Carta carta) {
		mano.add(carta);
		addToSlot(carta);
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
