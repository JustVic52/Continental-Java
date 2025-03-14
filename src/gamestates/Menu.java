package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import mainGame.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {
	
	private MenuButton[] buttons = new MenuButton[4];
	private BufferedImage overlay, background;
	private int x, y, width, height;

	public Menu(Game g) {
		super(g);
		loadButtons();
		loadBackground();
		background = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);		
	}

	private void loadBackground() {
		overlay = LoadSave.GetSpriteAtlas(LoadSave.MENU_OVERLAY);
		width = overlay.getWidth();
		height = overlay.getHeight();
		x = (Game.GAME_WIDTH / 2) - (width / 2);
		y = (Game.GAME_HEIGHT / 2) - (height / 2);
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, 266, 0, Gamestate.HOST);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, 332, 1, Gamestate.JOIN);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, 398, 2, Gamestate.HOST);
		buttons[3] = new MenuButton(Game.GAME_WIDTH / 2, 464, 3, Gamestate.QUIT);
	}

	private void resetButtons() {
		for (MenuButton mb : buttons)
			mb.resetBools();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(overlay, x, y, width, height, null);

		for (MenuButton mb : buttons) {
			mb.draw(g);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (mb.getHitbox().contains(e.getX(), e.getY())) {
				mb.setMousePressed(true);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (mb.getHitbox().contains(e.getX(), e.getY())) {
				if (mb.isMousePressed()) {
					mb.applyGamestate();
				}
			}
		}
		resetButtons();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons) {
			mb.setMouseOver(false);
		}
		for (MenuButton mb : buttons) {
			if (mb.getHitbox().contains(e.getX(), e.getY())) {
				mb.setMouseOver(true);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}
}
