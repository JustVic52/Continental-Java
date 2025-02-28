package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.Buttons.*;

public class GameButton {
	
	private int x, y, rowIndex, index = 0;
	private int xOffset = (B_WIDTH / 2);
	private Rectangle hitbox;
	private BufferedImage[] imgs;

	public GameButton(int xPos, int yPos, int rowI) {
		x = xPos;
		y = yPos;
		rowIndex = rowI;
		loadImgs();
		iniHitbox();
	}

	private void iniHitbox() {
		hitbox = new Rectangle(x - xOffset, y, B_WIDTH, B_HEIGHT);
	}

	private void loadImgs() {
		imgs = new BufferedImage[4];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BAJAR_BUTTON);
		for (int i = 0; i < imgs.length; i++) {
			imgs[i] = temp.getSubimage(i * B_WIDTH, rowIndex * B_HEIGHT, B_WIDTH, B_HEIGHT);
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], x - xOffset, y, B_WIDTH, B_HEIGHT, null);
	}
	
	public void setIndex(int i) { index = i; }
	
	public int getIndex() { return index; }

	public Rectangle getHitbox() {
		return hitbox;
	}
}
