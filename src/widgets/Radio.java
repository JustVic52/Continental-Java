package widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import utilz.Constants.RadioConstants;
import utilz.LoadSave;

public class Radio {

	private int x = 1134, y = 551;
	private int width = RadioConstants.RADIO_WIDTH, height = RadioConstants.RADIO_HEIGHT;
	private Rectangle hitbox;
	private Thread radioTh;
	private BufferedImage img;
	private int state = 0;
	private boolean muted = false;
	
	public Radio() {
		hitbox = new Rectangle(x, y, width, height);
		initThread();
		importImg();
	}

	private void importImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.RADIO);
	}

	private void initThread() {
		
	}
	
	public Rectangle getHitbox() { return hitbox; }
	
	public void setRadioState(int s) { state = s; }
	
	public int getRadioState() { return state; }
	
	public void render(Graphics g) {
		g.setColor(Color.PINK);
//		g.drawRect(x, y, width, height);´
		switch (state) {
		case RadioConstants.RADIO_PLAY:
			g.drawImage(img.getSubimage(0, 0, width, height), x, y, width, height, null);
			break;
		case RadioConstants.RADIO_SELECTED:
			g.drawImage(img.getSubimage(width, 0, width, height), x, y, width, height, null);
			break;
		case RadioConstants.RADIO_MUTED:
			g.drawImage(img.getSubimage(2 * width, 0, width, height), x, y, width, height, null);
			break;
		case RadioConstants.RADIO_MUTED_SELECTED:
			g.drawImage(img.getSubimage(3 * width, 0, width, height), x, y, width, height, null);
			break;
		}
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}
}
