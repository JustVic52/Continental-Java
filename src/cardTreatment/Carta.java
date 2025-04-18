package cardTreatment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import utilz.Constants;

public class Carta implements Serializable {
	
	private static final long serialVersionUID = 7866246894823092992L;
	
	private int value, palo, number, x, y, offsetX = 0, offsetY = 0;
	private boolean comodin, seleccionada, resguardada, lastOnBajada, clipped;
	public static final int PICAS = 3, CORAZONES = 4, TREVOLES = 1, DIAMANTES = 2;
	public static final int CARD_WIDTH = Constants.CardConstants.CARD_WIDTH, CARD_HEIGHT = Constants.CardConstants.CARD_HEIGHT;
	public static final int MARCO_WIDTH = Constants.CardConstants.MARCO_WIDTH, MARCO_HEIGHT = Constants.CardConstants.MARCO_HEIGHT;
	public static final int ACE = 1, JACK = 11, QUEEN = 12, KING = 13;
	private Rectangle hitbox, addHitbox;
	
	public Carta(int n, int p, int x, int y) { //Crea una carta de un valor N y un palo P, en una posición X e Y
		number = n;
		adjustValue(number);
		palo = p;
		seleccionada = false;
		resguardada = false;
		comodin = false;
		this.x = x;
		this.y = y;
		initHitbox();
	}

	public Carta(int x, int y) { //Crea un comodín en X e Y
		number = 14;
		adjustValue(number);
		palo = 5;
		comodin = true;
		seleccionada = false;
		resguardada = false;
		this.x = x;
		this.y = y;
		initHitbox();
	}
	
	private void initHitbox() {
		hitbox = new Rectangle(x, y, CARD_WIDTH - 4, CARD_HEIGHT - 8);
		addHitbox = new Rectangle(x, y, 22, CARD_HEIGHT - 8);
	}
	
	public void updateHitbox(double mult) {
		hitbox.x = x + 2;
		hitbox.y = y + 2;
		addHitbox.x = x + 2;
		addHitbox.y = y + 2;
		setCardDimensions(mult);
	}
	
	public void setCardDimensions(double mult) {
		double multed = mult * (CARD_WIDTH - 4);
		double addMulted = mult * 22;
		hitbox.width = (int) multed;
		if (lastOnBajada) {
			addHitbox.width = (int) multed;
		} else {
			addHitbox.width = (int) addMulted;
		}
		multed = mult * (CARD_HEIGHT - 8);
		addMulted = mult * 52;
		hitbox.height = (int) multed;
		if (clipped) {
			addHitbox.height = (int) addMulted;
		} else {
			addHitbox.height = (int) multed;
		}
	}

	public boolean isResguardada() {
		return resguardada;
	}

	public void setResguardada(boolean r) {
		resguardada = r;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public Rectangle getAddHitbox() {
		return addHitbox;
	}
	
	public void drawHitbox(Graphics g) {
		g.setColor(Color.pink);
//		g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		g.setColor(Color.green);
		g.drawRect(addHitbox.x, addHitbox.y, addHitbox.width, addHitbox.height);
	}
	
	private void adjustValue(int n) {
		switch (n) {
		case ACE:
		case JACK:
		case QUEEN:
		case KING:
			value = 10;
			break;
		case 14:
			value = 20;
			break;
		default:
			value = n;
			break;
		}
	}
	
	public void render(Graphics g, BufferedImage img, BufferedImage marco, double mult) {
		double mW = mult * CARD_WIDTH;
		double mH = mult * CARD_HEIGHT;
		updateHitbox(mult);
		if (comodin) {
			g.drawImage(img.getSubimage(CARD_WIDTH, (palo - 1) * CARD_HEIGHT, CARD_WIDTH , CARD_HEIGHT), x, (y - 1), (int) mW , (int) mH, null);
		} else {
			g.drawImage(img.getSubimage((number - 1) * CARD_WIDTH, (palo - 1) * CARD_HEIGHT, CARD_WIDTH , CARD_HEIGHT), x, (y - 1), (int) mW , (int) mH, null);
		}
		if (seleccionada) {
			g.drawImage(marco, x, y + 1, (int) mult * MARCO_WIDTH, (int) mult * MARCO_HEIGHT, null);
		}
//		drawHitbox(g);
	}
	
	public boolean isSeleccionada() { return seleccionada; }
	
	public void setSeleccionada(boolean s) { seleccionada = s; }
	
	public int getValue() {
		return value;
	}
	
	public boolean isComodin() {
		return comodin;
	}
	
	public int getPalo() {
		return palo;
	}
	
	public int getNumber() {
		return number;
	}
	
	public int getX() { return x; }

	public int getY() { return y; }
	
	public void setX(int dani) { x = dani; }

	public void setY(int furro) { y = furro; }
	
	public void setNumber (int n) { value = n; }
	
	public int getOffsetX() { return offsetX; }
	
	public int getOffsetY() { return offsetY; }
	
	public void setOffsetX(int o) { offsetX = o;}
	
	public void setOffsetY(int o) { offsetY = o;}
	
	public void setValue(int v) { value = v; }
	
	public void setPalo(int p) { palo = p; }
	
	public boolean isLastOnBajada() { return lastOnBajada; }

	public void setLastOnBajada(boolean simonChan) { lastOnBajada = simonChan; }

	public boolean isClipped() { return clipped; }

	public void setClipped(boolean luciaLaGuarra) { clipped = luciaLaGuarra; }

	public String getNombrePalo() {
		switch (palo) {
		case PICAS:
			return "♠";
		case CORAZONES:
			return "♥";
		case TREVOLES:
			return "♣";
		case DIAMANTES:
			return "♦";
		default:
			return "";
		}
	}
	
	public boolean equals(Object o) {
		Carta carta = (Carta) o;
		return this.number == carta.number && this.palo == carta.palo;
	}
	
	public String toString() {
		
		if (isComodin()) {
			return "C";
		}
		
		String res = "";
		switch (number) {
		case ACE:
			res += "A";
			break;
		case JACK:
			res += "J";
			break;
		case QUEEN:
			res += "Q";
			break;
		case KING:
			res += "K";
			break;
		default:
			res += number;
		}
		return res += getNombrePalo();
	}
}
