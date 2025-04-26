package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Gamestate;
import gamestates.Playing;
import mainGame.Game;
import utilz.Constants;
import utilz.LoadSave;
import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.URMButtons.*;
import static utilz.Constants.UI.VolumeButtons.*;

public class PauseOverlay {

	private boolean visible = false;
	private BufferedImage backgroundImg;
	private int bgX, bgY, bgW, bgH;
	private AudioOptions audioOptions;
	private URMButton quitOptions, openOptions;
	private Game game;

	public PauseOverlay(Game game) {
		this.game = game;
		loadBackground();
		createUrmButtons();
		audioOptions = game.getAudioOptions();
	}
	
	private void createUrmButtons() {
		int unpauseX = 640 - 28;
		int bY = 430;

		quitOptions = new URMButton(unpauseX, bY, 1);
		openOptions = new URMButton(1270 - 28, 8, 3);
	}

	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgW = backgroundImg.getWidth();
		bgH = backgroundImg.getHeight();
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = Game.GAME_HEIGHT / 2 - bgH / 2;

	}

	public void draw(Graphics g) {
		openOptions.draw(g, 0.5);
		if (visible) {
			// Background
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

			// Sound buttons
			audioOptions.draw(g);

			// UrmButtons
			quitOptions.draw(g, 1);
		}
	}

	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {
		if (visible) {
			if (quitOptions.getHitbox().contains(e.getX(), e.getY())) {
				quitOptions.setMousePressed(true);
				game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
			}
			else audioOptions.mousePressed(e);
		} else {
			if (openOptions.getHitbox().contains(e.getX(), e.getY())) {
				openOptions.setMousePressed(true);
				game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (visible) {
			if (quitOptions.getHitbox().contains(e.getX(), e.getY())) {
				if (quitOptions.isMousePressed()) { visible = false; game.getAudioPlayer().playEffect(AudioPlayer.FLACK); }
			} else audioOptions.mouseReleased(e);
		} else {
			if (openOptions.getHitbox().contains(e.getX(), e.getY()))
				if (openOptions.isMousePressed()) { visible = true; game.getAudioPlayer().playEffect(AudioPlayer.FLACK); }
		}
		quitOptions.resetBools();
		openOptions.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		quitOptions.setMouseOver(false);
		openOptions.setMouseOver(false);

		if (visible) {
			if (quitOptions.getHitbox().contains(e.getX(), e.getY()))
				quitOptions.setMouseOver(true);
			else audioOptions.mouseMoved(e);
		} else {
			if (openOptions.getHitbox().contains(e.getX(), e.getY()))
				openOptions.setMouseOver(true);
		}

	}
	
	public boolean isVisible() { return visible; }

	public void setVisible(boolean b) {
		visible = b;
	}

}
