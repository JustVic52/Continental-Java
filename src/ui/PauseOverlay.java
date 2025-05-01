package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audioClasses.AudioPlayer;
import gamestates.Gamestate;
import mainGame.Game;
import utilz.LoadSave;

public class PauseOverlay {

	private boolean visible = false;
	private BufferedImage backgroundImg;
	private int bgX, bgY, bgW, bgH;
	private AudioOptions audioOptions;
	private URMButton quitOptions, openOptions, home;
	private Game game;

	public PauseOverlay(Game game) {
		this.game = game;
		loadBackground();
		createUrmButtons();
		audioOptions = game.getAudioOptions();
	}
	
	private void createUrmButtons() {
		int bY = 430;

		quitOptions = new URMButton(682, bY, 1);
		openOptions = new URMButton(1242, 8, 3);
		home = new URMButton(540, bY, 2);
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
			home.draw(g, 1);
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
			if (home.getHitbox().contains(e.getX(), e.getY())) {
				home.setMousePressed(true);
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
			}
			if (home.getHitbox().contains(e.getX(), e.getY())) {
				if (home.isMousePressed()) {
					game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
					game.getAudioPlayer().stopLoop();
					Gamestate.state = Gamestate.QUIT;
				}
			}
			else audioOptions.mouseReleased(e);
		} else {
			if (openOptions.getHitbox().contains(e.getX(), e.getY()))
				if (openOptions.isMousePressed()) { visible = true; game.getAudioPlayer().playEffect(AudioPlayer.FLACK); }
		}
		quitOptions.resetBools();
		openOptions.resetBools();
		home.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		quitOptions.setMouseOver(false);
		openOptions.setMouseOver(false);
		home.setMouseOver(false);

		if (visible) {
			if (quitOptions.getHitbox().contains(e.getX(), e.getY()))
				quitOptions.setMouseOver(true);
			if (home.getHitbox().contains(e.getX(), e.getY()))
				home.setMouseOver(true);
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
