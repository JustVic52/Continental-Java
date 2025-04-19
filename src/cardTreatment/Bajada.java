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
	private Map<List<Carta>, Rectangle> hitboxList, addHitbox;
	boolean bajado = false;
	int[] palos;
	private int x, y;
	private boolean added, canBajarse, dragged = false;
	
	public Bajada(int xPos, int yPos) {
		x = xPos;
		y = yPos;
		slots = new ArrayList<>();
		slotsBajados = new ArrayList<>();
		hitboxList = new HashMap<>();
		addHitbox = new HashMap<>();
	}
	
	public void draw(Graphics g, BufferedImage img, BufferedImage marco, BufferedImage marcoAdd, BufferedImage marcoClipped) {
		if (bajado) {
			for (List<Carta> cA : slotsBajados) {
				if (cA.get(0).isClipped()) {
					g.drawImage(marcoClipped, cA.get(0).getX() - 15, cA.get(0).getY() + 3, marcoClipped.getWidth(), marcoClipped.getHeight(), null);
				} else {
					g.drawImage(marcoAdd, cA.get(0).getX() - 15, cA.get(0).getY() + 3, marcoAdd.getWidth(), marcoAdd.getHeight(), null);
				}
				for (Carta c : cA) {
					c.render(g, img, marco, 1);
				}
			}
//			drawHitboxes(g);
		}
	}
	
	private void drawHitboxes(Graphics g) {
		for (Map.Entry<List<Carta>, Rectangle> entrada : addHitbox.entrySet()) {
			g.drawRect(entrada.getValue().x, entrada.getValue().y, entrada.getValue().width, entrada.getValue().height);
		}
		for (Map.Entry<List<Carta>, Rectangle> entrada : hitboxList.entrySet()) {
			g.drawRect(entrada.getValue().x, entrada.getValue().y, entrada.getValue().width, entrada.getValue().height);
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
	
	public boolean getCanBajarse() { return !bajado && canBajarse; }
	
	private void positionCards() {
		int xPos = x, yPos = y;
		for (int j = 0; j < slotsBajados.size(); j++) {
			List<Carta> cA = slotsBajados.get(j);
			hitboxList.get(cA).setLocation(xPos, yPos);
			addHitbox.get(cA).setLocation(xPos - 16, yPos);
			for (int i = 0; i < cA.size(); i++) {
				Carta c = cA.get(i);
				if (i == cA.size() - 1) { c.setLastOnBajada(true); }
				else { c.setLastOnBajada(false); }
				c.setX(xPos);
				c.setY(yPos);
				xPos += 22;
			}
			if (j == 1) {
				yPos += 52;
				xPos = x;
			} else { xPos += 79; }
		}
		clipCards();
	}
	
	private void clipCards() {
		int cont = 0;
		if (slotsBajados.size() >= 3) {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < slotsBajados.get(i).size(); j++) {
					Rectangle rec = new Rectangle(slotsBajados.get(i).get(j).getX(), slotsBajados.get(i).get(j).getY(), 74, 104);
					if (j < slotsBajados.get(2).size() && rec.contains(slotsBajados.get(2).get(j).getX(), slotsBajados.get(2).get(j).getY())) {
						if (cont == 0) {
							addHitbox.get(slotsBajados.get(i)).height = 52;
							hitboxList.get(slotsBajados.get(i)).height = 52;
							cont = 1;
						}
						slotsBajados.get(i).get(j).setClipped(true);
						if (j != slotsBajados.get(i).size() - 1) slotsBajados.get(i).get(j + 1).setClipped(true);
					}
					if (slotsBajados.size() == 4) {
						if (j < slotsBajados.get(3).size() && slotsBajados.get(i).get(j).getHitbox().contains(slotsBajados.get(3).get(j).getX(), slotsBajados.get(3).get(j).getY())) {
							if (cont == 0) {
								addHitbox.get(slotsBajados.get(i)).height = 52;
								hitboxList.get(slotsBajados.get(i)).height = 52;
								cont = 1;
							}
							slotsBajados.get(i).get(j).setClipped(true);
							if (j != slotsBajados.get(i).size() - 1) slotsBajados.get(i).get(j + 1).setClipped(true);
						}
					}
				}
				cont = 0;
			}
		}
	}

	public void bajarse() {
		slotsBajados = slots;
		makeHitboxes();
		positionCards();
	}
	
	private void makeHitboxes() {
		hitboxList.clear();
		addHitbox.clear();
		int width = 0, height = 104, xPos = x, yPos = y;
		for (int i = 0; i < slotsBajados.size(); i++) {
			List<Carta> cA = slotsBajados.get(i);
			width = 22 * (cA.size() - 1) + 74;
			hitboxList.put(cA, new Rectangle(xPos, yPos, width, height));
			addHitbox.put(cA, new Rectangle(xPos - 16, yPos, 16, height));
			if (getRoundEscaleras() > 0) {
				yPos += 57;
			} else if (getRoundTrios() > 0) {
				for (int j = 0; j < slotsBajados.size(); j++) {
					Trio trio = new Trio();
					if (trio.canBeATrio(cA)) {
						if (j == 2) {
							yPos += 52;
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
			if (addHitbox.get(cA) != null && addHitbox.get(cA).contains(e.getX(), e.getY()) && selection != null) {
				if (canBeAdded(cA, selection, -1)) {
					add(cA, selection, -1);
					added = true;
				}
			}
			if (hitboxList.get(cA) != null && hitboxList.get(cA).contains(e.getX(), e.getY()) && selection != null) {
				for (int i = 0; i < cA.size(); i++) {
					if (cA.get(i).getAddHitbox().contains(e.getX(), e.getY())) {
						if (canBeAdded(cA, selection, i)) {
							add(cA, selection, i);
							added = true;
						}
					}
				}
			}
		}
	}
	
	public void mouseDragged(MouseEvent e, Carta selection) {
		boolean res = false;
		for (List<Carta> cA : slots) {
			if (addHitbox.get(cA) != null && addHitbox.get(cA).contains(e.getX(), e.getY()) && selection != null) {
				selection.setSmall(true);
				res = true;
			}
			if (hitboxList.get(cA) != null && hitboxList.get(cA).contains(e.getX(), e.getY()) && selection != null) {
				selection.setSmall(true);
				res = true;
			}
		}
		dragged = res;
	}
	
	public boolean isDragged() { return dragged; }
	
	public void setDragged(boolean d) { dragged = d; }

	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}

	private void add(List<Carta> cA, Carta selection, int i) {
		Trio trio = new Trio();
		boolean silksong = trio.canBeATrio(cA);
		cA.add(i + 1, selection);
		if (i >= 0 && !cA.get(i + 1).isComodin() && cA.get(i).isComodin() && !(silksong && cA.size() != 5)) { cA.remove(i); }
		for (List<Carta> aux : slotsBajados) {
			if (cA == aux) {
				aux = cA;
				break;
			}
		}
		makeHitboxes();
		positionCards();
	}
	
	private boolean canBeAdded(List<Carta> cA, Carta selection, int i) {
		List<Carta> aux = new ArrayList<>(cA);
		Escalera escalera = new Escalera();
		Trio trio = new Trio();
		aux.add(i + 1, selection);
		return escalera.canBeAdded(aux, i) || trio.canBeAdded(aux, i);
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
