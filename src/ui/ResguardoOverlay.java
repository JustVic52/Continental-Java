package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import cardTreatment.Slot;
import gameDynamics.Round;
import utilz.LoadSave;

public class ResguardoOverlay extends Round {
	
	private List<Slot[]> slots;
	private BufferedImage tablero;
	private int x, y = 0, width, height;
	private boolean activated = false;
	private GameButton cancel;
	private static final int UPPER_Y = 0, MIDDLE_Y = 0, DOWN_Y = 0;

	public ResguardoOverlay() {
		slots = new ArrayList<>();
		cancel = new GameButton(1182, 481, 2);
		loadTablero();
	}
	
	public void draw(Graphics g) {
		if (activated) {
			g.drawImage(tablero, x, y, width, height, null);
			cancel.draw(g);
		}
	}
	
	private void loadTablero() {
		tablero = LoadSave.GetSpriteAtlas(LoadSave.RESGUARDO + getNumRound() + ".png");
		width = tablero.getWidth();
		height = tablero.getHeight();
		switch (getNumRound()) {
	    case 1: case 3: case 4: case 6: case 7: case 9: case 10:
	        x = 188;
	        break;
	    case 2: case 5:
	        x = 908;
	        break;
	    case 8:
	        x = 554;
	        break;
		}
		loadSlots();
	}
	
	private void loadSlots() {
		switch (getNumRound()) {
		case 1:
			
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		case 4:
			
			break;
		case 5:
			
			break;
		case 6:
			
			break;
		case 7:
			
			break;
		case 8:
			
			break;
		case 9:
			
			break;
		case 10:
			
			break;
		}
	}

	public GameButton getButton() {
		return cancel;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}
}
