package cardTreatment;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gameDynamics.Round;

public class Bajada extends Round implements Serializable {
	
	private static final long serialVersionUID = 2593544615184734293L;
	
	private List<List<Carta>> slots, slotsBajados;
	private Map<List<Carta>, Rectangle> hitboxList;
	boolean bajado = false;
	int[] palos;
	private int x, y;
	private boolean added, canBajarse;
	
	public Bajada(int xPos, int yPos) {
		x = xPos;
		y = yPos;
		slots = new ArrayList<>();
		slotsBajados = new ArrayList<>();
		hitboxList = new HashMap<>();
	}
	
	public void draw(Graphics g, BufferedImage img, BufferedImage marco) {
		if (bajado) {
			for (Map.Entry<List<Carta>, Rectangle> entrada : hitboxList.entrySet()) {
				g.drawRect(entrada.getValue().x, entrada.getValue().y, entrada.getValue().width, entrada.getValue().height);
			}
			for (List<Carta> cA : slotsBajados) {
				for (Carta c : cA) {
					c.render(g, img, marco, 1);
				}
			}
		}
	}
	
	public boolean canBajarse(List<List<Carta>> bajada) {
		if (bajado || bajada.isEmpty() || bajada == null) { return false; }
		int numTrios = 0, numEscaleras = 0;
		for (List<Carta> cartas : bajada) {
			if (cartas.isEmpty()) { return false; }
			if (canBeTrio(cartas)) { numTrios++; }
			if (canBeEscalera(cartas)) { numEscaleras++; }
		}
		if (numTrios == getRoundTrios() && numEscaleras == getRoundEscaleras()) {
			slots.clear();
			slots = bajada;
			canBajarse = true;
			return true;
		}
		return false;
	}
	
	public boolean getCanBajarse() { return canBajarse; }
	
	private void positionCards() {
		int xPos = x, yPos = y;
		Escalera escalera = new Escalera();
		for (List<Carta> cA : slotsBajados) {
			if (getRoundEscaleras() > 0) {
				if (escalera.canBeEscalera(cA)) {
					for (Carta c : cA) {
						c.setX(xPos);
						c.setY(yPos);
						xPos += 22;
					}
					yPos += 57;
					xPos = x;
				}
			} else if (getRoundTrios() > 0) {
				for (int j = 0; j < slotsBajados.size(); j++) {
					if (!escalera.canBeEscalera(cA)) {
						for (Carta c : cA) {
							c.setX(xPos);
							c.setY(yPos);
							xPos += 22;
						}
						if (j == 2) {
							yPos += 57;
							xPos = x;
						} else { xPos += 5; }
					}
				}
			}
		}
	}
	
	public void bajarse() {
		slotsBajados = slots;
		makeHitboxes();
		positionCards();
	}
	
	private void makeHitboxes() {
		int width = 0, height = 104, xPos = x + 2, yPos = y + 2;
		for (int i = 0; i < slotsBajados.size(); i++) {
			List<Carta> cA = slotsBajados.get(i);
			width = 22 * (cA.size() - 1) + 74;
			hitboxList.put(cA, new Rectangle(xPos, yPos, width, height));
			if (getRoundEscaleras() > 0) {
				yPos += 57;
			} else if (getRoundTrios() > 0) {
				for (int j = 0; j < slotsBajados.size(); j++) {
					Trio trio = new Trio();
					if (trio.canBeATrio(cA)) {
						if (j == 2) {
							yPos += 57;
							xPos = x;
						} else { xPos += width + 5; }
					}
				}
			}
		}
	}

	private boolean canBeEscalera(List<Carta> cartas) {
		Escalera escalera = new Escalera();
		boolean res = escalera.canBeEscalera(cartas);
		if (res) { slots.add(escalera.getEscalera()); }
		return res;
	}

	private boolean canBeTrio(List<Carta> cartas) {
		Trio trio = new Trio();
		boolean res = trio.canBeATrio(cartas);
		if (res) { slots.add(trio.getTrio()); }
		return res;
	}

	public boolean isBajado() { return bajado; }
	
	public void setBajado(boolean bocairent) { bajado = bocairent; }
	
	public void mouseReleased(MouseEvent e, Carta selection) {
		for (List<Carta> cA : slots) {
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

	private void add(List<Carta> cA, Carta selection) {
		Trio trio = new Trio();
		Escalera escalera = new Escalera();
		cA.add(selection);
		if (trio.canBeATrio(cA)) {
			for (List<Carta> aux : slotsBajados) {
				if (cA == aux) {
					aux = trio.getTrio();
				}
			}
		} else {
			for (List<Carta> aux : slotsBajados) {
				if (cA == aux) {
					aux = escalera.getEscalera();
				}
			}
		}
		makeHitboxes();
		positionCards();
	}
	
	private boolean canBeAdded(List<Carta> cA, Carta selection) {
		Escalera escalera = new Escalera();
		Trio trio = new Trio();
		cA.add(selection);
		return escalera.canBeEscalera(cA) || trio.canBeATrio(cA);
	}

	public int getX() {
		return x;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return slotsBajados.size();
	}
	
	public String toString() {
		return slotsBajados.toString();
	}
}
