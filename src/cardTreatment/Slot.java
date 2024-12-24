package cardTreatment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Slot {
	
	private Carta carta;
	private Rectangle hitbox;
	private int x, y, width = 78, height = 108;
	
	public Slot(int x, int y) {
		this.x = x;
		this.y = y;
		carta = null;
		hitbox = new Rectangle(x, y, width, height);
	}
	
	public void setSlotPos(int x, int y) {
		this.x = x;
		this.y = y;
		hitbox.x = x;
		hitbox.y = y;
	}
	
	public void add(Carta c) {
		carta = c;
	}
	
	public void remove() {
		carta = null;
	}
	
	public Carta getCarta() {
		return carta;
	}

	public void render(Graphics g, BufferedImage img) {
		g.setColor(Color.GREEN);
//		g.drawRect(x, y, width, height);
		if (carta != null) {
			carta.render(g, img, x, y, 1);
		}
	}
	
}
