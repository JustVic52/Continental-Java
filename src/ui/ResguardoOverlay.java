package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cardTreatment.Carta;
import cardTreatment.Slot;
import gameDynamics.Round;
import utilz.LoadSave;

public class ResguardoOverlay extends Round {
	
	private List<Slot[]> slots;
	private BufferedImage tablero;
	private int x, y = 0, width, height, numRound;
	private boolean activated = false;
	private GameButton cancel;
	private static final int UPPER_Y = 37, MIDDLE_Y = 154, DOWN_Y = 271;
	private static final int ESCALERA_X = 215, TRIO_X = 935, ROUND8_X = 581;	

	public ResguardoOverlay() {
		numRound = 1;
		slots = new ArrayList<>();
		cancel = new GameButton(1182, 481, 2);
		loadTablero();
	}
	
	public void draw(Graphics g, BufferedImage img) {
		if (activated) {
			g.drawImage(tablero, x, y, width, height, null);
			cancel.draw(g);
		}
	}

	private void loadTablero() {
		tablero = LoadSave.GetSpriteAtlas(LoadSave.RESGUARDO + numRound + ".png");
		width = tablero.getWidth();
		height = tablero.getHeight();
		switch (numRound) {
	    case 2: case 5:
	        x = 908;
	        break;
	    case 8:
	        x = 554;
	        break;
	    default:
	    	x = 188;
	        break;
		}
		loadSlots();
	}
	
	private void loadSlots() {
		switch (numRound) {
		case 1:
			slots.add(new Slot[13]);
			break;
		case 2:
			slots.add(new Slot[4]);
			slots.add(new Slot[4]);
			break;
		case 3:
			slots.add(new Slot[13]);
			slots.add(new Slot[4]);
			break;
		case 4:
			slots.add(new Slot[13]);
			slots.add(new Slot[13]);
			break;
		case 5:
			slots.add(new Slot[4]);
			slots.add(new Slot[4]);
			slots.add(new Slot[4]);
			break;
		case 6:
			slots.add(new Slot[13]);
			slots.add(new Slot[4]);
			slots.add(new Slot[4]);
			break;
		case 7:
			slots.add(new Slot[13]);
			slots.add(new Slot[13]);
			slots.add(new Slot[4]);
			break;
		case 8:
			slots.add(new Slot[4]);
			slots.add(new Slot[4]);
			slots.add(new Slot[4]);
			slots.add(new Slot[4]);
			break;
		case 9:
			slots.add(new Slot[13]);
			slots.add(new Slot[13]);
			slots.add(new Slot[13]);
			break;
		case 10:
			slots.add(new Slot[13]);
			slots.add(new Slot[13]);
			slots.add(new Slot[4]);
			slots.add(new Slot[4]);
			break;
		}
		createSlots();
	}

	private void createSlots() {
		ArrayList<Slot[]> aux = new ArrayList<>(slots);
		int x = 0;
		int y = UPPER_Y;
		
		for (Slot[] ss : aux) {
			
			if (ss.length == 4) { 
				if (getNumRound() == 8) { x = ROUND8_X; }
				else { x = TRIO_X; }
			}
			else { x = ESCALERA_X; }
			
			switch (getNumRound()) {
			case 6: case 10:
				
				if (ss.length != 4) {
					for (int i = 0; i < ss.length; i++) {
						ss[i] = new Slot(x, y, true);
						x += 80;
					}
					if (y == MIDDLE_Y) { y = DOWN_Y; }
					if (y == UPPER_Y) { y = MIDDLE_Y; }
				} else {
					if (ss.equals(aux.get(aux.size() - 1))) { x = TRIO_X; }
					else { x = ROUND8_X; }
					for (int i = 0; i < ss.length; i++) {
						ss[i] = new Slot(x, y, true);
						x += 80;
					}
				}
				
				break;
			case 8:
				
				if (ss.equals(aux.get(1)) || ss.equals(aux.get(3))) { x = TRIO_X; }
				for (int i = 0; i < ss.length; i++) {
					ss[i] = new Slot(x, y, true);
					x += 80;
				}
				if (x == 1255 && y == UPPER_Y) { y = MIDDLE_Y; }
				break;
			default:
				
				for (int i = 0; i < ss.length; i++) {
					ss[i] = new Slot(x, y, true);
					x += 80;
				}
				if (y == MIDDLE_Y) { y = DOWN_Y; }
				if (y == UPPER_Y) { y = MIDDLE_Y; }
				
				break;
			}
		}
	}
	
	public void updateTablero(int kaladin) {
		if (numRound != kaladin) {
			numRound = kaladin;
			loadTablero();
		}
	}
	
	public List<List<Carta>> getCartas() {
		List<List<Carta>> aux = new ArrayList<>();
		for (int i = 0; i < slots.size(); i++) {
			ArrayList<Slot> sA = new ArrayList<>(Arrays.asList(slots.get(i)));
			ArrayList<Carta> cA = new ArrayList<>();
			for (Slot s : sA) {
				if (s.getCarta() != null) { cA.add(s.getCarta()); }
			}
			aux.add(cA);
		}
		return aux;
	}

	public GameButton getButton() {
		return cancel;
	}
	
	public List<Slot[]> getSlots() {
		return slots;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public void discard(int i, int j) {
		slots.get(i)[j].remove();
	}

	public void setSlots(List<Slot[]> lehanLin) {
		slots = lehanLin;
	}

	public void bajarse() {
		slots = new ArrayList<>();
		loadSlots();
	}
}
