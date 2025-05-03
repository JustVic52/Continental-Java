package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audioClasses.AudioPlayer;
import mainGame.Game;
import ui.AudioOptions;
import ui.MenuButton;
import ui.URMButton;
import utilz.LoadSave;

public class Options extends State implements Statemethods {

	private AudioOptions audioOptions;
	private BufferedImage backgroundImg, optionsBackgroundImg, credits;
	private int bgX, bgY, bgW, bgH;
	private URMButton menuB, backButton;
	private MenuButton creditsB;
	private boolean active = false;

	public Options(Game game) {
		super(game);
		loadImgs();
		loadButtons();
		audioOptions = game.getAudioOptions();
	}

	private void loadButtons() {
		int menuX = 640 - 28;
		int menuY = 470;

		menuB = new URMButton(menuX, menuY, 2);
		backButton = new URMButton(500, 490, 1);
		creditsB = new MenuButton((Game.GAME_WIDTH / 2) - 1, 560, 4, Gamestate.OPTIONS);
	}

	private void loadImgs() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);
		credits = LoadSave.GetSpriteAtlas(LoadSave.CREDITS);

		bgW = optionsBackgroundImg.getWidth();
		bgH = optionsBackgroundImg.getHeight();
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = Game.GAME_HEIGHT / 2 - bgH / 2;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);
		g.drawString("v1.0.5 release", 10, Game.GAME_HEIGHT);

		menuB.draw(g, 1);
		audioOptions.draw(g);
		creditsB.draw(g);
		
		if (active) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			g.drawImage(credits, Game.GAME_WIDTH / 2 - credits.getWidth() / 2, Game.GAME_HEIGHT / 2 - credits.getHeight() / 2, credits.getWidth(), credits.getHeight(), null);
			backButton.draw(g, 1);
		}
		g.setColor(Color.black);
	}

	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!active) {
			if (menuB.getHitbox().contains(e.getX(), e.getY())) {
				menuB.setMousePressed(true);
				game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
			} else if (creditsB.getHitbox().contains(e.getX(), e.getY())) {
				creditsB.setMousePressed(true);
				game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
			} else {
				audioOptions.mousePressed(e);
			}
		} else {
			if (backButton.getHitbox().contains(e.getX(), e.getY())) {
				backButton.setMousePressed(true);
				game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!active) {
			if (menuB.getHitbox().contains(e.getX(), e.getY())) {
				if (menuB.isMousePressed())
					game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
					Gamestate.state = Gamestate.MENU;
			} else if (creditsB.getHitbox().contains(e.getX(), e.getY())) {
				if (creditsB.isMousePressed())
					game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
					active = true;
			} else {
				audioOptions.mouseReleased(e); 
			}
		} else {
			if (backButton.getHitbox().contains(e.getX(), e.getY())) {
				if (backButton.isMousePressed())
					game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
					active = false;
			}
		}
		menuB.resetBools();
		creditsB.resetBools();
		backButton.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		creditsB.setMouseOver(false);
		backButton.setMouseOver(false);

		if (!active) {
			if (menuB.getHitbox().contains(e.getX(), e.getY()))
				menuB.setMouseOver(true);
			else if (creditsB.getHitbox().contains(e.getX(), e.getY()))
				creditsB.setMouseOver(true);
			else
				audioOptions.mouseMoved(e);
		} else {
			if (backButton.getHitbox().contains(e.getX(), e.getY()))
				backButton.setMouseOver(true);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (active) active = false;
			else Gamestate.state = Gamestate.MENU; 
		}
	}

}
