package utilz;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import gameGraphics.GamePanel;

public class CuadroTexto {
	
	private int x, y, width, height, length;
	private Rectangle hitbox;
	private boolean active = false;
	private String texto = "";
	private BufferedImage marco;
	private Timer caretTimer;
	private boolean caret = false;
	
	public CuadroTexto(int xPos, int yPos, int w, int h, int l) {
		x = xPos;
		y = yPos;
		width = w;
		height = h;
		length = l;
		hitbox = new Rectangle(x, y, width, height);
		marco = LoadSave.GetSpriteAtlas(LoadSave.MARCO_CUADRO);
		caretTimer = new Timer(500, e -> {
			caret = !caret;
		});
	}
	
	public void draw(Graphics g) {
//		g.drawRect(x, y, width, height);
		if (active) {
			g.drawImage(marco, x - 2, y - 2, marco.getWidth(), marco.getHeight(), null);
			if (caret) {
				int caretX = x + 1 + g.getFontMetrics().stringWidth(texto);
				g.drawLine(caretX, y + 1, caretX, y + 18);
			}
		}
		if (texto != null) {
			g.drawString(texto, x + 1, y + height + 11);
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		if (hitbox.contains(e.getX(), e.getY())) {
			GamePanel.setIntCursor(Cursor.TEXT_CURSOR);
		} else {
			GamePanel.setIntCursor(Cursor.DEFAULT_CURSOR);
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (hitbox.contains(e.getX(), e.getY())) {
			active = true;
			caretTimer.start();
		} else {
			active = false;
			caretTimer.stop();
		}
	}
	
	public void keyTyped(KeyEvent e) {
		if (active) {
			if (e.getKeyChar() == '\b' && texto.length() > 0) {
				texto = texto.substring(0, texto.length() - 1);
			} else if (texto == null || texto.length() <= length - 1) {
				texto += e.getKeyChar();
			}
		}
	}

	public boolean isActive() {
		return active;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getTexto() {
		return texto;
	}
}
