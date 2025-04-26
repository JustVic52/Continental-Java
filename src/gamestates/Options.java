package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import mainGame.Game;
import ui.AudioOptions;
import ui.URMButton;
import utilz.LoadSave;

public class Options extends State implements Statemethods {

	private AudioOptions audioOptions;
	private BufferedImage backgroundImg, optionsBackgroundImg;
	private int bgX, bgY, bgW, bgH;
	private URMButton menuB;

	public Options(Game game) {
		super(game);
		loadImgs();
		loadButton();
		audioOptions = game.getAudioOptions();
	}

	private void loadButton() {
		int menuX = 640 - 28;
		int menuY = 470;

		menuB = new URMButton(menuX, menuY, 2);
	}

	private void loadImgs() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);

		bgW = optionsBackgroundImg.getWidth();
		bgH = optionsBackgroundImg.getHeight();
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = Game.GAME_HEIGHT / 2 - bgH / 2;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);

		menuB.draw(g, 1);
		audioOptions.draw(g);
	}

	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (menuB.getHitbox().contains(e.getX(), e.getY())) {
			menuB.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
		} else {
			audioOptions.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (menuB.getHitbox().contains(e.getX(), e.getY())) {
			if (menuB.isMousePressed())
				game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
				Gamestate.state = Gamestate.MENU;
		} else {
			audioOptions.mouseReleased(e); 
		}
		menuB.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);

		if (menuB.getHitbox().contains(e.getX(), e.getY()))
			menuB.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			Gamestate.state = Gamestate.MENU;
	}

}
