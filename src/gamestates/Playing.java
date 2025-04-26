package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import audio.AudioPlayer;
import cardTreatment.Bajada;
import cardTreatment.Baraja;
import cardTreatment.Carta;
import cardTreatment.Descartes;
import cardTreatment.Slot;
import gameDynamics.Player;
import gameDynamics.Round;
import mainGame.Game;
import ui.GameButton;
import ui.PauseOverlay;
import ui.PointsOverlay;
import ui.ResguardoOverlay;
import utilz.Constants;
import utilz.LoadSave;

public class Playing extends State implements Statemethods {
	
	//Playing necesita el player, los botones de retake y bajar, el botón de pausa, y la radio

	private BufferedImage tablero, fondo;
	private Player player = null;
	private GameButton[] buttons = new GameButton[2];
	private ResguardoOverlay resguardo;
	private PointsOverlay pointDisplay;
	private PauseOverlay pauseOverlay;
	private Slot slot = null;
	private int posI = 0, posJ = 0, numActions = 0, justBajado = 0, contSound = 0;
	private ArrayList<Bajada> bajadas;
	private ArrayList<Boolean> aux;
	private Baraja baraja;
	private Descartes descartes;
	
	public Playing(Game g) {
		super(g);
		tablero = LoadSave.GetSpriteAtlas(LoadSave.TABLERO);
		fondo = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		resguardo = new ResguardoOverlay(1);
		pointDisplay = new PointsOverlay();
		pauseOverlay = new PauseOverlay(g);
		bajadas = new ArrayList<>();
		baraja = new Baraja();
		aux = new ArrayList<>();
		descartes = new Descartes();
		iniButtons();
	}
	
	private void iniButtons() {
		buttons[0] = new GameButton(1182, 481, 1);
		buttons[1] = new GameButton(1182, 629, 0);
	}

	@Override
	public void draw(Graphics g) {
		
		if (player == null) {
			if (Join.getClient() != null) {
				player = Join.getClient().getPlayer();
			} else {
				player = Host.getServer().getClient().getPlayer();
			}
		} else {
			if (baraja != player.getFullMano().getBaraja()) { baraja = player.getFullMano().getBaraja(); }
			if (descartes != player.getFullMano().getDescartes()) { descartes = player.getFullMano().getDescartes(); }
			if (bajadas != player.getListBajada()) {
				ArrayList<Bajada> aux = new ArrayList<>(bajadas);
				bajadas = player.getListBajada();
				if (bajadas != null) {
					for (int i = 0; i < aux.size(); i++) {
						if (aux.get(i) == null && bajadas.get(i) != null) {
							game.getAudioPlayer().playEffect(AudioPlayer.BAJAR);
						}
					}
				}
			}
			if (resguardo != player.getResguardo()) { resguardo = player.getResguardo(); }
			if (pointDisplay.getPointList() != player.getPointList()) { pointDisplay.setPointList(player.getPointList()); }
			if (pointDisplay.getNameList() != player.getNameList()) { pointDisplay.setNameList(player.getNameList()); }
			if (aux != player.getTurns()) { aux = player.getTurns(); if (player.isYourTurn()) game.getAudioPlayer().playEffect(AudioPlayer.TURN); }

			g.drawImage(tablero, 0, 0, Constants.TableroConstants.TABLERO_WIDTH, Constants.TableroConstants.TABLERO_HEIGHT, null);
			if (!resguardo.isActivated()) { buttons[0].draw(g); }
			buttons[1].draw(g);
			for (Bajada b : bajadas) {
				if (b != null && b.isBajado()) { b.draw(g, player.getFullMano().getImage(), player.getFullMano().getMarco(), player.getFullMano().getAddMarco(), player.getFullMano().getClippedMarco()); }
			}
			resguardo.draw(g, player.getFullMano().getImage());
			player.render(g, resguardo.getSlots(), resguardo.isActivated());
			printStrings(g);
			pauseOverlay.draw(g);
			if (Join.getClient() != null) {
				if (Join.getClient().isRoundOver()) {
					g.drawImage(fondo, 0, 0, Constants.TableroConstants.TABLERO_WIDTH, Constants.TableroConstants.TABLERO_HEIGHT, null);
					pointDisplay.draw(g, player.getRoundWinner());
					if (contSound == 0) {
						if (player.getRoundWinner()) game.getAudioPlayer().playEffect(AudioPlayer.LVL_COMPLETED);
						else game.getAudioPlayer().playEffect(AudioPlayer.LVL_LOST);
						contSound = 1;
					}
				}
			} else {
				if (Host.getServer().getClient().isRoundOver()) {
					g.drawImage(fondo, 0, 0, Constants.TableroConstants.TABLERO_WIDTH, Constants.TableroConstants.TABLERO_HEIGHT, null);
					pointDisplay.draw(g, player.getRoundWinner());
					if (contSound == 0) {
						if (player.getRoundWinner()) game.getAudioPlayer().playEffect(AudioPlayer.LVL_COMPLETED);
						else game.getAudioPlayer().playEffect(AudioPlayer.LVL_LOST);
						contSound = 1;
					}
				}
			}
		}
	}

	private void printStrings(Graphics g) {
		try {
			Font fuente = Font.createFont(Font.TRUETYPE_FONT, new File("res/fuentes/Minecraftia-Regular.ttf"));
			fuente = fuente.deriveFont(Font.PLAIN, 14);
			g.setFont(fuente);
			g.setColor(Color.white);
			g.drawString("Objetivo:", 22, 592);
			g.drawString(player.getBajada().getRound().sacarObjetivosEscaleras(), 22, 609);
			if (player.getBajada().getRound().sacarObjetivosEscaleras().equals("")) {
				g.drawString(player.getBajada().getRound().sacarObjetivosTrios(), 22, 609);
			} else { g.drawString(player.getBajada().getRound().sacarObjetivosTrios(), 22, 626); }
			int yPos = 648;
			for (int i = 0; i < player.getTurns().size(); i++) {
				g.setColor(Color.white);
				if (i == player.getTurno()) g.drawString(player.getNameList().get(i) + " (you)", 42, yPos);
				else g.drawString(player.getNameList().get(i), 42, yPos);
				if (player.getTurns().get(i)) {
					g.setColor(new Color(47, 143, 33, 255));
					g.fillRect(24, yPos - 19, 8, 8);
				}
				yPos += 20;
			}
		}
		catch (FontFormatException | IOException e) {}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		pauseOverlay.mousePressed(e);
		
		if (!pauseOverlay.isVisible()) {
			if (baraja.getHitbox().contains(e.getX(), e.getY()) 
					&& numActions == 0 && player.isYourTurn()) { baraja.setSelected(true); }
			
			if (descartes.getHitbox().contains(e.getX(), e.getY()) && !descartes.isEmpty() 
					&& numActions == 0 && player.isYourTurn()) { descartes.setSelected(true); }
			
			for (int i = 0; i < player.getFullMano().getSlots().size(); i++) {
				Carta c = player.getFullMano().getSlots().get(i).getCarta();
				if (c != null && c.getHitbox().contains(e.getX(), e.getY())) {
					c.setSeleccionada(true);
					c.setY(c.getY() - 10);
					c.setOffsetX(e.getX() - c.getX());
					c.setOffsetY(e.getY() - c.getY());
					player.getFullMano().select(c);
					slot = player.getFullMano().getSlots().get(i);
					posI = i;
				}
			}
			
			if (resguardo.isActivated()) {
				if (resguardo.getButton().getHitbox().contains(e.getX(), e.getY())) {
					game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
					resguardo.getButton().setIndex(1);
				}
				for (int i = 0; i < resguardo.getSlots().size(); i++) {
					for (int j = 0; j < resguardo.getSlots().get(i).length; j++) {
						Carta c = resguardo.getSlots().get(i)[j].getCarta();
						if (c != null && c.getHitbox().contains(e.getX(), e.getY())) {
							c.setSeleccionada(true);
							c.setY(c.getY() - 10);
							c.setOffsetX(e.getX() - c.getX());
							c.setOffsetY(e.getY() - c.getY());
							player.getFullMano().select(c);
							slot = resguardo.getSlots().get(i)[j];
							posI = i;
							posJ = j;
						}
					}
				}
			} else {
				if (buttons[0].getHitbox().contains(e.getX(), e.getY())) {
					buttons[0].setIndex(buttons[0].getIndex() + 1);
					game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
				}
			}
			
			if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
				game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
				if (player.getBajada() != null && !player.getBajada().isBajado() && player.getBajada().canBajarse(resguardo.getCartas())) {
					buttons[1].setIndex(3);
				} else { buttons[1].setIndex(1); }
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		int i;
		
		pauseOverlay.mouseReleased(e);
		
		if (!pauseOverlay.isVisible()) {
			for (i = 0; i < player.getFullMano().getSlots().size(); i++) {
				Carta c = player.getFullMano().getSlots().get(i).getCarta();
				if (c != null && c.isSeleccionada()) {
					c.setSeleccionada(false);
					c.setX(player.getFullMano().getSlots().get(i).getX());
					c.setY(player.getFullMano().getSlots().get(i).getY());
					c.setSmall(false);
				}
			}
			
			for (i = 0; i < player.getFullMano().getSlots().size(); i++) {
				Slot s = player.getFullMano().getSlots().get(i);
				if (s.getCarta() == null && s.getHitbox().contains(e.getX(), e.getY()) && player.getFullMano().getSelection() != null) {
					game.getAudioPlayer().playEffect(AudioPlayer.CARDINSLOT);
					if (slot.isIn()) {
						resguardo.getSlots().get(posI)[posJ].remove();
					}
					player.getFullMano().moveBetweenSlots(slot.isIn(), i, posI, posJ);
					player.getFullMano().getSelection().setSmall(false);
					player.deselect();
				}
			}
			
			if (resguardo.isActivated()) {
				if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
					game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
					if (numActions == 1 && player.getBajada() != null && !player.getBajada().isBajado() && player.getBajada().canBajarse(resguardo.getCartas())) {
						game.getAudioPlayer().playEffect(AudioPlayer.BAJAR);
						contSound = 0;
						justBajado = 1;
						buttons[1].setIndex(2);
						player.getBajada().bajarse();
						player.getBajada().setBajado(true);
						resguardo.bajarse();
						player.getFullMano().bajarse(player.getBajada());
					} else { buttons[1].setIndex(0); }
				} else { buttons[1].setIndex(0); }
				if (resguardo.getButton().getHitbox().contains(e.getX(), e.getY())) {
					game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
					resguardo.getButton().setIndex(0);
					resguardo.setActivated(false);
				} else { resguardo.getButton().setIndex(0); }
				for (i = 0; i < resguardo.getSlots().size(); i++) {
					for (int j = 0; j < resguardo.getSlots().get(i).length; j++) {
						Carta c = resguardo.getSlots().get(i)[j].getCarta();
						if (c != null && c.isSeleccionada()) {
							c.setSeleccionada(false);
							c.setX(resguardo.getSlots().get(i)[j].getX());
							c.setY(resguardo.getSlots().get(i)[j].getY());
							game.getAudioPlayer().playEffect(AudioPlayer.CARDINSLOT);
						}
					}
				}
				for (i = 0; i < resguardo.getSlots().size(); i++) {
					for (int j = 0; j < resguardo.getSlots().get(i).length; j++) {
						Slot s = resguardo.getSlots().get(i)[j];
						//para meterlo en el resguardo
						if (s.getCarta() == null && s.getHitbox().contains(e.getX(), e.getY()) && player.getFullMano().getSelection() != null) {
							game.getAudioPlayer().playEffect(AudioPlayer.CARDINSLOT);
							if (slot.isIn()) {
								resguardo.getSlots().get(posI)[posJ].remove();
							}
							player.getFullMano().moveBetweenResguardo(slot.isIn(), posI, posJ, i, j);
							s.add(player.getSelection());
							player.deselect();
						}
					}
				}
			} else {
				if (buttons[0].getHitbox().contains(e.getX(), e.getY())) {
					game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
					if (buttons[0].getIndex() == 1) { buttons[0].setIndex(2); }
					if (buttons[0].getIndex() == 3) { buttons[0].setIndex(0); }
				} else { 
					if (buttons[0].getIndex() == 1) { buttons[0].setIndex(0); }
					if (buttons[0].getIndex() == 3) { buttons[0].setIndex(2); }
				}
				if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
					game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
					resguardo.setActivated(true);
					if (player.canBajarse()) {
						buttons[1].setIndex(2);
					} else { buttons[1].setIndex(0); }
				} else { buttons[1].setIndex(0); }
			}
			
			if (player.getBajada() != null && player.getBajada().isBajado()) {
				for (Bajada b : bajadas) {
					if (b != null && b.isBajado() && justBajado == 0 && numActions == 1) {
						b.mouseReleased(e, player.getSelection());
						if (b.isAdded()) {
							game.getAudioPlayer().playEffect(AudioPlayer.AÑADIR);
							if (slot.isIn()) {
								resguardo.getSlots().get(posI)[posJ].remove();
							}
							player.getFullMano().discardWithoutUpdate(slot.isIn(), posI, posJ);
							player.deselect();
							b.setAdded(false);
						}
					}
				}
			}
			
			if (baraja.isSelected() && numActions == 0 && baraja.getHitbox().contains(e.getX(), e.getY())) {
				game.getAudioPlayer().playEffect(AudioPlayer.ROBAR);
				player.setBaraja(true);
				numActions = 1;
				baraja.setSelected(false);
			}
			
			if (descartes.isSelected() && numActions == 0 && descartes.getHitbox().contains(e.getX(), e.getY())) {
				game.getAudioPlayer().playEffect(AudioPlayer.ROBAR);
				player.setDescartes(true);
				descartes.remove();
				numActions = 1;
	    		descartes.setSelected(false);
			}
			else if (descartes.getHitbox().contains(e.getX(), e.getY()) && player.getFullMano().getSelection() != null && numActions == 1) {
				game.getAudioPlayer().playEffect(AudioPlayer.DESCARTAR);
				if (slot.isIn()) {
					resguardo.getSlots().get(posI)[posJ].remove();
				}
				player.getFullMano().discard(slot.isIn(), posI, posJ);
				descartes.take(player.getUltimaCarta());
				player.setDescartado(true);
			} else {
				player.deselect();
			}
			
			if (player.isDescartado()) {
				numActions = 0;
				justBajado = 0;
				player.setDescartado(false);
			}
		}
	}
	

	@Override
	public void mouseMoved(MouseEvent e) {
//		System.out.println("X: " + e.getX() + " || Y: " + e.getY());
		pauseOverlay.mouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (pauseOverlay.isVisible()) pauseOverlay.setVisible(false);
			else pauseOverlay.setVisible(true);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		pauseOverlay.mouseDragged(e);
		for (Carta c : player.getMano()) {
			if (c.isSeleccionada()) {
				c.setX(e.getX() - c.getOffsetX());
				c.setY(e.getY() - c.getOffsetY());
			}
		}
		for (Slot[] sA : resguardo.getSlots()) {
			for (Slot s : sA) {
				if (s.getCarta() != null && s.getCarta().isSeleccionada()) {
					s.getCarta().setX(e.getX() - s.getCarta().getOffsetX());
					s.getCarta().setY(e.getY() - s.getCarta().getOffsetY());
				}
			}
		}
		boolean res = false;
		for (Bajada b : bajadas) {
			if (b != null && b.isBajado()) { 
				b.mouseDragged(e, player.getSelection());
				res = b.isDragged();
			}
		}
		if (player.getSelection() != null && !res) { player.getSelection().setSmall(false); }
	}
}
