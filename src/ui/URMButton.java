package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

public class URMButton {
	
	private int x, y;
	private BufferedImage[] imgs;
	private int rowIndex, index;
	private boolean mouseOver, mousePressed;
	private Rectangle hitbox;

	public URMButton(int xPos, int yPos, int rowIndex) {
		x = xPos;
		y = yPos;
		this.rowIndex = rowIndex;
		loadImgs();
		iniHitbox();
	}

	private void iniHitbox() {
		hitbox = new Rectangle(x, y, B_SIDE, B_SIDE);
	}

	private void loadImgs() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
		imgs = new BufferedImage[3];
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = temp.getSubimage(i * B_SIDE, rowIndex * B_SIDE, B_SIDE, B_SIDE);
	}

	public void draw(Graphics g, double d) {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;
		
		int newSide = (int) (B_SIDE * d);
		
		if (hitbox.width != newSide) {
			hitbox.width = hitbox.height = newSide;
		}
		
		g.drawImage(imgs[index], x, y, newSide, newSide, null);
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
}