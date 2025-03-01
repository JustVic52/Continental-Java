package cardTreatment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Slot {
	
	private Carta carta;
	private Rectangle hitbox;
	private int x, y, width = 78, height = 108;
	private boolean inResguardo;
	
	public Slot(int x, int y, boolean in) {
		this.x = x;
		this.y = y;
		inResguardo = in;
		carta = null;
		hitbox = new Rectangle(x, y, width, height);
	}
	
	public boolean isIn() {
		return inResguardo;
	}

	public void setSlotPos(int x, int y) {
		this.x = x;
		this.y = y;
		hitbox.x = x;
		hitbox.y = y;
	}
	
	public void add(Carta c) {
		carta = c;
		carta.setX(x);
		carta.setY(y);
	}
	
	public void remove() {
		carta = null;
	}
	
	public Carta getCarta() {
		return carta;
	}

	public void render(Graphics g, BufferedImage img, BufferedImage marco) {
		g.setColor(Color.GREEN);
//		g.drawRect(x, y, width, height);
		if (carta != null) {
			carta.render(g, img, marco, 1);
		}
	}
	
	public int getX() { return x; }
	
	public int getY() { return y; }

	public Rectangle getHitbox() {
		return hitbox;
	}
}
