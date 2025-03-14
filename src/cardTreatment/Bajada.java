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
	int[] palos;
	private int x, y;
	private boolean added;
	
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
			for (Map.Entry<Carta[], Rectangle> entrada : hitboxList.entrySet()) {
				System.out.println("he llegado");
				g.drawRect((int) entrada.getValue().getX(), (int) entrada.getValue().getY(), 
						(int) entrada.getValue().getWidth(), (int) entrada.getValue().getHeight());
			}
			for (Carta[] cA : slotsBajados) {
				if (getRoundEscaleras() > 0) {
					if (cA.length == 13) {
						for (int i = 0; i < cA.length; i++) {
							if (cA[i] != null) {
								cA[i].setX(xPos);
								cA[i].setY(yPos);
								cA[i].render(g, img, marco, 1);
								x += 22;
							}
						}
						y += 57;
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
								y += 57;
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
		if (bajada.size() == 0 || bajada == null) { return false; }
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
	
	public void bajarse() {
		slotsBajados = slots;
		makeHitboxes();
	}
	
	private void makeHitboxes() {
		int cont = 0, width = 0, height = 52, xPos = x, yPos = y;
		for (int i = 0; i < slotsBajados.size(); i++) {
			Carta[] cA = slotsBajados.get(i);
			for (Carta c : cA) {
				if (c != null) { cont++; }
			}
			width = 11 * (cont - 1) + 37;
			hitboxList.put(cA, new Rectangle(xPos, yPos, width, height));
			if (getRoundEscaleras() > 0) {
				y += 57;
			} else if (getRoundTrios() > 0) {
				for (int j = 0; j < slotsBajados.size(); j++) {
					if (cA.length == 4) {
						if (j == 2) {
							y += 57;
							x = xPos;
						} else { x += 5; }
					}
				}
			}
		}
	}

	private boolean canBeEscalera(Carta[] cartas) {
		ArrayList<Carta> lista = new ArrayList<>();
		for (Carta c : cartas) {
			if (c != null) { lista.add(c); }
		}
		Escalera escalera = new Escalera();
		boolean res = escalera.canBeEscalera(lista);
		if (res) { slots.add(escalera.getEscalera()); }
		return res;
	}

	private boolean canBeTrio(Carta[] cartas) {
		ArrayList<Carta> lista = new ArrayList<>();
		for (Carta c : cartas) {
			if (c != null) { lista.add(c); }
		}
		Trio trio = new Trio();
		boolean res = trio.canBeATrio(lista);
		if (res) { slots.add(trio.getTrio()); }
		return res;
	}

	public boolean isBajado() { return bajado; }
	
	public void setBajado(boolean bocairent) { bajado = bocairent; }
	
	public void mouseReleased(MouseEvent e, Carta selection) {
		for (Carta[] cA : slotsBajados) {
			if (hitboxList.get(cA).contains(e.getX(), e.getY()) && selection != null) {
				if (canBeAdded(cA, selection)) {
					add(cA, selection);
					added = true;
				}
			}
		}
	}

	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}

	private void add(Carta[] cA, Carta selection) {
		ArrayList<Carta> lista = new ArrayList<>();
		for (Carta c : cA) {
			if (c != null) { lista.add(c); }
		}
		if (cA.length == 4) {
			Trio trio = new Trio();
			if (trio.canBeATrio(lista)) {
				if (trio.canBeAdded(selection)) {
					trio.add(selection);
					for (Carta[] aux : slotsBajados) {
						if (cA == aux) {
							aux = trio.getTrio();
						}
					}
				}
			}
		} else {
			Escalera escalera = new Escalera();
			if (escalera.canBeEscalera(lista)) {
				if (escalera.canBeNewAdded(selection)) {
					escalera.add(selection);
					for (Carta[] aux : slotsBajados) {
						if (cA == aux) {
							aux = escalera.getEscalera();
						}
					}
				}
			}
		}
	}
	
	private boolean canBeAdded(Carta[] cA, Carta selection) {
		ArrayList<Carta> lista = new ArrayList<>();
		for (Carta c : cA) {
			if (c != null) { lista.add(c); }
		}
		if (cA.length == 4) {
			Trio trio = new Trio();
			if (trio.canBeATrio(lista)) {
				return trio.canBeAdded(selection);
			}
		} else {
			Escalera escalera = new Escalera();
			if (escalera.canBeEscalera(lista)) {
				return escalera.canBeNewAdded(selection);
			}
		}
		return false;
	}
}
