package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;

import audio.AudioPlayer;
import mainGame.Game;
import net.Server;
import ui.ComboButton;
import ui.URMButton;
import utilz.CuadroTexto;
import utilz.LoadSave;

public class Host extends State implements Statemethods {

	private static Server server;
	private BufferedImage overlay, background;
	private URMButton[] buttons = new URMButton[2];
	private ComboButton[] comboButtons = new ComboButton[2];
	private int x, y, width, height, numOfPlayers = 1;
	private CuadroTexto texto;
	
	public Host(Game g) {
		super(g);
		texto = new CuadroTexto(604, 354, 196, 19, 15);
		loadButtons();
		loadBackground();
		background = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
	}

	private void loadButtons() {
		buttons[0] = new URMButton(743, 464, 0);
		buttons[1] = new URMButton(485, 464, 2);
		comboButtons[0] = new ComboButton(772, 287, 0);
		comboButtons[1] = new ComboButton(660, 287, 1);
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
		for (ComboButton cb : comboButtons) {
			cb.resetBools();
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(overlay, x, y, width, height, null);
		
		for (URMButton urm : buttons) {
			urm.draw(g, 1);
		}
		for (ComboButton cb : comboButtons) {
			cb.draw(g);
		}
		g.drawString(numOfPlayers + "", 721, 321);
		g.drawString("Jugadores:", 478, 321);
		g.drawString("Nombre:", 480, 383);
		texto.draw(g);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (URMButton urm : buttons) {
			if (urm.getHitbox().contains(e.getX(), e.getY())) {
				game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
				urm.setMousePressed(true);
			}
		}
		for (ComboButton cb : comboButtons) {
			if (cb.getHitbox().contains(e.getX(), e.getY())) {
				game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
				cb.setMousePressed(true);
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
				game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
//				if (!texto.getTexto().equals("")) {
					buttons[0].setMousePressed(false);
					ServerSocket ss = null;
					try {
						ss = new ServerSocket(6020);
						server = new Server(ss, numOfPlayers, texto.getTexto());
						server.start();
						boolean clave = server.isAlive();
						while (clave) { clave = server.getClient() == null; }
						game.getAudioPlayer().startLoop(AudioPlayer.START_PLAYING);
								Gamestate.state = Gamestate.PLAYING;
					} catch (IOException e1) {
						try {
							if (ss != null) { ss.close(); }
							if (ss == null && server != null && server.isAlive()) { server.interrupt(); }
						} catch (IOException e2) {
							
						}
					}
//				}
			}
		}
		if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
			if (buttons[1].isMousePressed()) {
				game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
				Gamestate.state = Gamestate.MENU;
			}
		}
		if (comboButtons[0].getHitbox().contains(e.getX(), e.getY())) {
			if (comboButtons[0].isMousePressed()) {
				game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
				if (numOfPlayers < 4) { numOfPlayers++; }
			}
		}
		if (comboButtons[1].getHitbox().contains(e.getX(), e.getY())) {
			if (comboButtons[1].isMousePressed()) {
				game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
				if (numOfPlayers > 1) { numOfPlayers--; }
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
		for (ComboButton cb : comboButtons) {
			cb.setMouseOver(false);
		}
		for (ComboButton cb : comboButtons) {
			if (cb.getHitbox().contains(e.getX(), e.getY())) {
				cb.setMouseOver(true);
			}
		}
		texto.mouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			Gamestate.state = Gamestate.MENU;
	}
	
	public void keyTyped(KeyEvent e) {
		texto.keyTyped(e);
	}
	
	public static Server getServer() { return server; }
}
