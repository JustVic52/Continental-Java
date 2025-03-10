package cardTreatment;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gameDynamics.Round;

public class Bajada extends Round {
	
	private List<Carta[]> slots, slotsBajados;
	private Map<Carta[], Rectangle> hitboxList;
	boolean bajado = false;
	private int x, y;
	
	public Bajada() {
		x = 0;
		y = 0;
		slots = new ArrayList<>();
		slotsBajados = new ArrayList<>();
		hitboxList = new HashMap<>();
	}
	
	public void draw(Graphics g, BufferedImage img, BufferedImage marco) {
		int xPos = x;
		int yPos = y;
		if (bajado) {
			for (Carta[] cA : slotsBajados) {
				if (getRoundEscaleras() > 0) {
					if (cA.length == 13) {
						for (int i = 0; i < cA.length; i++) {
							if (cA[i] != null) {
								cA[i].setX(xPos);
								cA[i].setY(yPos);
								cA[i].render(g, img, marco, 0.5);
								x += 22;
							}
						}
						y += 5;
						x = xPos;
					}
				} else if (getRoundTrios() > 0) {
					for (int j = 0; j < slotsBajados.size(); j++) {
						if (cA.length == 4) {
							for (int i = 0; i < cA.length; i++) {
								if (cA[i] != null) {
									cA[i].setX(xPos);
									cA[i].setY(yPos);
									cA[i].render(g, img, marco, 0.5);
									x += 22;
								}
							}
							if (j == 2) {
								y += 5;
								x = xPos;
							} else { x += 5; }
						}
					}
				}
			}
		}
	}
	
	public boolean canBajarse(List<Carta[]> bajada) {
		slots.clear();
		int numTrios = 0, numEscaleras = 0;
		for (Carta[] cartas : bajada) {
			if (cartas.length == 4) {
				if (canBeTrio(cartas)) { numTrios++; }
			} else {
				if (canBeEscalera(cartas)) { numEscaleras++; }
			}
		}
		slots = bajada;
		return numTrios == getRoundTrios() && numEscaleras == getRoundEscaleras();
	}
	
	public void bajarse() { slotsBajados = slots; }
	
	private boolean canBeEscalera(Carta[] cartas) {
		boolean res = false;
		
		return res;
	}

	private boolean canBeTrio(Carta[] cartas) {
		int res, num, i;
		res = num = i = 0;
		while (i < cartas.length && cartas[i] == null) {
			i++;
		}
		if (i >= cartas.length) { return false; }
		else {
			num = cartas[i].getNumber();
		}
		for(Carta c : cartas) {
			if (c != null && (c.getPalo() == 5) && c.getNumber() == num) {
				res++;
			}
		}
		return res >= 3;
	}

	public boolean isBajado() { return bajado; }
	
	public void setBajado(boolean bocairent) { bajado = bocairent; }
	
	public void mousePressed(MouseEvent e) {
		for (Carta[] cA : slotsBajados) {
			
		}
	}
}
