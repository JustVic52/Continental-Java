package gamestates;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import gameGraphics.GamePanel;
import mainGame.Game;
import net.Client;
import ui.URMButton;
import utilz.CuadroTexto;
import utilz.LoadSave;

public class Join extends State implements Statemethods {

	private static Client client = null;
	private BufferedImage overlay, background;
	private URMButton[] buttons = new URMButton[2];
	private int x, y, width, height;
	private CuadroTexto nombre, ip;
	
	public Join(Game g) {
		super(g);
		nombre = new CuadroTexto(604, 293, 196, 19, 15);
		ip = new CuadroTexto(604, 354, 196, 19, 15);
		loadButtons();
		loadBackground();
		background = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		
	}	

	private void loadButtons() {
		buttons[0] = new URMButton(743, 464, 0); //X - 422
		buttons[1] = new URMButton(485, 464, 2);
	}

	private void loadBackground() {
		overlay = LoadSave.GetSpriteAtlas(LoadSave.JOIN_OVERLAY);
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
		
		g.drawString("IP:", 480, 385);
		g.drawString("Nombre: ", 480, 324);
		nombre.draw(g);
		ip.draw(g);
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
		nombre.mouseClicked(e);
		ip.mouseClicked(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (buttons[0].getHitbox().contains(e.getX(), e.getY())) {
			if (buttons[0].isMousePressed()) {
//				if (!nombre.getTexto().equals("") && !ip.getTexto().equals("")) {
					buttons[0].setMousePressed(false);
					Socket s = null;
					try {
						s = new Socket(/*ip.getTexto()*/ InetAddress.getLocalHost(), 6020);
						s.setKeepAlive(true);
						client = new Client(s, nombre.getTexto());
						client.start();
						Gamestate.state = Gamestate.PLAYING;
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						try {
							if (s != null) { s.close(); }
							if (client != null && client.isAlive()) { client.interrupt(); }
						} catch (IOException e2) {
							
						}
					}
//				}
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
		if (nombre.getHitbox().contains(e.getX(), e.getY()) || ip.getHitbox().contains(e.getX(), e.getY())) {
			GamePanel.setIntCursor(Cursor.TEXT_CURSOR);
		} else GamePanel.setIntCursor(Cursor.DEFAULT_CURSOR);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}
	
	public void keyTyped(KeyEvent e) {
		nombre.keyTyped(e);
		ip.keyTyped(e);
	}
	
	public static Client getClient() { return client; }
}
