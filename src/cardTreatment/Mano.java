package cardTreatment;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import utilz.LoadSave;

//Esto es tu mano personal. Aquí se acumulan las cartas que tengas en la mano y se eliminan una vez las descartes.

public class Mano {
	
	private List<Carta> mano;
	private List<Slot> manoSlot;
	private Carta selection;
	private Carta cartaEscogida = null;
	private Carta ultimaCartaEliminada; //para el retake
	private boolean canRetake = false;
	private BufferedImage img, marco;
	private Baraja baraja;
	private Descartes descartes;
	
	public Mano() {
		mano = new ArrayList<>();
		manoSlot = new ArrayList<>();
		initSlot();
		baraja = new Baraja();
		descartes = new Descartes();
		importCards();
	}
	
	private void initSlot() {
		int x = 227;
		int y = 475;
		for (int i = 0; i < 22; i++) {
			manoSlot.add(new Slot(x, y, false));
			if (x == 1027) {
				x = 227;
				y = 585;
			} else { x += 80; }
		} 
	}

	private void importCards() {
		img = LoadSave.GetSpriteAtlas(LoadSave.CARD_ATLAS);
		marco = LoadSave.GetSpriteAtlas(LoadSave.MARCO);
	}
	
	public void renderSlots(Graphics g, List<Slot[]> slots, boolean active) {
		ArrayList<Slot> aux = new ArrayList<>(manoSlot);
		Slot auxS = null;
		for (Slot s : aux) {
			if (s.getCarta() == null || !s.getCarta().isSeleccionada()) { s.render(g, img, marco); }
			else { auxS = s; }
		}
		if (active) {
			for (Slot[] ss : slots) {
				for (Slot s : ss) {
					if (s.getCarta() == null || !s.getCarta().isSeleccionada()) { s.render(g, img, marco); }
					else { auxS = s; }
				}
			}
		}
		if (auxS != null) { auxS.render(g, img, marco); }
	}
	
	public BufferedImage getImage() {
		return img;
	}
	
	public BufferedImage getMarco() {
		return marco;
	}

	public void give() {
//		baraja = b;
		Carta c = baraja.give();
		cartaEscogida = c;
		mano.add(c); //añade una carta de la baraja y la elimina de la lista.
		addToSlot(c); //añade la carta al slot
	}
	
	public void addToSlot(Carta c) {
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

	public void discard(boolean in) {
		if (!in) {
			removeFromSlots(selection);
		}
		mano.remove(selection);
		ultimaCartaEliminada = selection;
		deselect();
	}
	
	private void removeFromSlots(Carta carta) {
		int i = 0;
		boolean removed = false;
		while (i < manoSlot.size() && !removed) {
			if (manoSlot.get(i).getCarta() != null && manoSlot.get(i).getCarta().equals(carta)) {
				manoSlot.get(i).remove();
				updateSlots(i);
				removed = true;
			}
			i++;
		}
	}
	
	private void removeWithoutUpdate(Carta carta) {
		int i = 0;
		boolean removed = false;
		while (i < manoSlot.size() && !removed) {
			if (manoSlot.get(i).getCarta() != null && manoSlot.get(i).getCarta().equals(carta)) {
				manoSlot.get(i).remove();
				removed = true;
			}
			i++;
		}
	}
	
	public void moveBetweenSlots(int i) {
		if (manoSlot.get(i).getCarta() == null) {
			removeWithoutUpdate(selection);
			manoSlot.get(i).add(selection);
		}
	}

	private void updateSlots(int pos) {
		if (mano.size() != 0) {
			int i = pos;
			while (i < manoSlot.size() - 1 && manoSlot.get(i + 1).getCarta() != null) {
				manoSlot.get(i).remove();
				manoSlot.get(i).add(manoSlot.get(i + 1).getCarta());
				i++;
			}
			manoSlot.get(i).remove();
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
			selection = mano.get(num);
			mano.get(num).setSeleccionada(true);
		}
	}
	
	public void select(Carta carta) {
		if (!carta.isResguardada()) {
			selection = carta;
			carta.setSeleccionada(true);
		}
	}
	
	public void deselect() {
		for (Carta c : mano) {
			if (c == selection) {
				c.setSeleccionada(false);
			}
		}
		selection = null;
	}
	
	public void setRetake(boolean retake) {
		canRetake = retake;
	}
	
	public boolean getRetake() {
		return canRetake;
	}
	
	public int getSize() {
		return mano.size();
	}
	
	public List<Carta> getMano() {
		return mano;
	}
	
	public Carta getSelection() {
		return selection;
	}
	
	public String toString() {
		String res = "Cartas en la mano: " + mano.toString() + "\n";
		if (selection != null) { res += "Carta seleccionada: " + selection + "\n"; }
		return res;
	}
	
	public String selectionToString() {
		String res = "";
		if (selection != null) { res += "Carta seleccionada: " + selection + "\n"; }
		return res;
	}

	public Carta getUltimaCarta() {
		return ultimaCartaEliminada;
	}
	
	public List<Slot> getSlots() {
		return manoSlot;
	}

	public void setSelection(Carta carta) {
		selection = carta;
	}
	
	public Carta getCartaEscogida() {
		return cartaEscogida;
	}

	public Baraja getBaraja() {
		return baraja;
	}

	public void setMano(ArrayList<Carta> m) {
		mano = m;
		for (Carta c : mano) {
			addToSlot(c);
		}
	}

	public void setDescartes(ArrayList<Carta> albaChan) {
		descartes.setDescartes(albaChan);		
	}

	public Descartes getDescartes() {
		return descartes;
	}
}
