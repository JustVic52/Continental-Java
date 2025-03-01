package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import mainGame.Game;
import net.Client;
import net.Server;
import ui.ComboButton;
import ui.URMButton;
import utilz.CuadroTexto;
import utilz.LoadSave;

public class Join extends State implements Statemethods {

	private static Client client = null;
	private BufferedImage overlay, background;
	private URMButton[] buttons = new URMButton[2];
	private int x, y, width, height;
	private CuadroTexto texto;
	
	public Join(Game g) {
		super(g);
		
		texto = new CuadroTexto(604, 312, 196, 19, 14);
		loadButtons();
		loadBackground();
		background = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		
	}	

	private void loadButtons() {
		buttons[0] = new URMButton(743, 422, 0);
		buttons[1] = new URMButton(485, 422, 2);
	}

	private void loadBackground() {
		overlay = LoadSave.GetSpriteAtlas(LoadSave.HOST_OVERLAY);
		width = overlay.getWidth();
		height = overlay.getHeight();
		x = (Game.GAME_WIDTH / 2) - (width / 2);
		y = (Game.GAME_HEIGHT / 2) - (height / 2);
	}
	
	private void resetButtons() {
		for (URMButton urm : buttons) {
			urm.resetBools();
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(overlay, x, y, width, height, null);
		
		for (URMButton urm : buttons) {
			urm.draw(g);
		}
		
		g.drawString("Nombre:", 480, 341);
		texto.draw(g);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (URMButton urm : buttons) {
			if (urm.getHitbox().contains(e.getX(), e.getY())) {
				urm.setMousePressed(true);
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		texto.mouseClicked(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (buttons[0].getHitbox().contains(e.getX(), e.getY())) {
			if (buttons[0].isMousePressed()) {
				try {
					Socket s = new Socket(InetAddress.getLocalHost(), 6020);
					client = new Client(s);
					client.run();
					Gamestate.state = Gamestate.PLAYING;
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
			if (buttons[1].isMousePressed()) {
				Gamestate.state = Gamestate.MENU;
			}
		}
		resetButtons();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (URMButton urm : buttons) {
			urm.setMouseOver(false);
		}
		for (URMButton urm : buttons) {
			if (urm.getHitbox().contains(e.getX(), e.getY())) {
				urm.setMouseOver(true);
			}
		}
		texto.mouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void keyTyped(KeyEvent e) {
		texto.keyTyped(e);
	}
	
	public static Client getClient() { return client; }
}
