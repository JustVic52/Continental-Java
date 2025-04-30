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
	private List<Slot[]> resguardo;
	private Carta selection;
	private Carta ultimaCartaEliminada; //para el retake
	private boolean canRetake = false;
	private BufferedImage img, marco, addMarco, clippedMarco;
	private Baraja baraja;
	private Descartes descartes;
	
	public Mano() {
		mano = new ArrayList<>();
		manoSlot = new ArrayList<>();
		resguardo = new ArrayList<>();
		initSlot();
		baraja = new Baraja();
		descartes = new Descartes();
		importImgs();
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

	private void importImgs() {
		img = LoadSave.GetSpriteAtlas(LoadSave.CARD_ATLAS);
		marco = LoadSave.GetSpriteAtlas(LoadSave.MARCO);
		addMarco = LoadSave.GetSpriteAtlas(LoadSave.ADDMARCO);
		clippedMarco = LoadSave.GetSpriteAtlas(LoadSave.ADDCLIPPEDMARCO);
	}
	
	public void renderSlots(Graphics g, List<Slot[]> slots, boolean active) {
		if (resguardo != slots) resguardo = slots;
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
	
	public int addToSlot(Carta c) {
		int i = 0;
		boolean added = false;
		while (i < manoSlot.size() && !added) {
			if (manoSlot.get(i).getCarta() == null) {
				manoSlot.get(i).add(c);
				added = true;
			}
			i++;
		}
		return i;
	}

	public boolean isEmpty() {
		boolean res = true;
		for (Slot[] sA : resguardo) {
			for (Slot s : sA) {
				if (s.getCarta() != null) res = false;
			}
		}
		return mano.isEmpty() && res;
	}
	
	public void discard(boolean in, int posI, int posJ) {
		if (in) {
			resguardo.get(posI)[posJ].remove();
		} else {
			manoSlot.get(posI).remove();
			updateSlots(posI);
			mano.remove(findPosI(posI));
		}
		ultimaCartaEliminada = selection;
		deselect();
	}
	
	public void discardWithoutUpdate(boolean in, int posI, int posJ) {
		if (in) {
			resguardo.get(posI)[posJ].remove();
		} else {
			manoSlot.get(posI).remove();
			mano.remove(findPosI(posI));
		}
		deselect();
	}

	public void moveBetweenResguardo(boolean isIn, int posI, int posJ, int finalI, int finalJ) {
		int cont = findPosI(posI);
		Carta change = resguardo.get(finalI)[finalJ].getCarta();
		if (isIn) {
			resguardo.get(posI)[posJ].remove();
		} else {
			manoSlot.get(posI).remove();
			mano.remove(cont);
		}
		resguardo.get(finalI)[finalJ].add(selection);
		if (change != null) {
			if (isIn) {
				resguardo.get(posI)[posJ].add(change);
			} else {
				manoSlot.get(posI).add(change);
				mano.add(cont, change);
			}
		}
	}
	
	private int findPosI(int posI) {
		int cont = 0;
		if (posI == 0) return 0;
		for (int i = 0; i < posI; i++) {
	        if (manoSlot.get(i).getCarta() != null) {
	            cont++;
	        }
	    }
		return cont;
	}

	public void moveBetweenSlots(boolean isIn, int i, int posI, int posJ) {
		int posI2 = findPosI(posI);
		Carta change = manoSlot.get(i).getCarta();
		if (isIn) {
			resguardo.get(posI)[posJ].remove();
		} else {
			mano.remove(posI2);
			manoSlot.get(posI).remove();
		}
		int i2 = findPosI(i + 1);
		manoSlot.get(i).add(selection);
		mano.add(i2, selection);
		if (change != null) {
			if (isIn) {
				resguardo.get(posI)[posJ].add(change);
			} else {
				manoSlot.get(posI).add(change);
				mano.add(posI2, change);
			}
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
		int posI = findPosI(addToSlot(carta) - 1);
		mano.add(posI, carta);
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
		for (Slot[] sA : resguardo) {
			for (Slot s : sA) {
				if (s.getCarta() != null && s.getCarta() == selection) {
					s.getCarta().setSeleccionada(false);
				}
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
	
	public List<Slot[]> getResguardo() {
		return resguardo;
	}
	
	public Carta getSelection() {
		return selection;
	}
	
	public String toString() {
		String res = "Cartas en la mano: " + mano.toString() + "\n";
		if (selection != null) { res += "Carta seleccionada: " + selection + "\n"; }
		return res;
	}
	
//	private String resguardoToString() {
//		String res = "[";
//		for (Slot[] sA : resguardo) {
//			res += "[";
//			for(int i = 0; i < sA.length; i++) {
//				if (i != 0) res += ", ";
//				res += sA[i].getCarta();
//			}
//			res += "]";
//		}
//		return res + "]";
//	}

	public Carta getUltimaCarta() {
		return ultimaCartaEliminada;
	}
	
	public List<Slot> getSlots() {
		return manoSlot;
	}

	public void setSelection(Carta carta) {
		selection = carta;
	}

	public Baraja getBaraja() {
		return baraja;
	}

	public void setMano(ArrayList<Carta> m) {
		if (m != null) {
			mano = m;
			for (Carta c : mano) {
				addToSlot(c);
			}
		}
	}

	public void setDescartes(ArrayList<Carta> albaChan) {
		descartes.setDescartes(albaChan);		
	}

	public Descartes getDescartes() {
		return descartes;
	}

	public BufferedImage getAddMarco() {
		return addMarco;
	}

	public BufferedImage getClippedMarco() {
		return clippedMarco;
	}

	public void bajarse(Bajada bajada) {
		for (int i = 0; i < bajada.getBajada().size(); i++) {
			for (Slot s : resguardo.get(i)) {
				s.remove();
			}
		}
	}
}
