package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cardTreatment.Bajada;
import cardTreatment.Baraja;
import cardTreatment.Carta;
import cardTreatment.Descartes;
import cardTreatment.Slot;
import gameDynamics.Player;
import mainGame.Game;
import ui.GameButton;
import ui.ResguardoOverlay;
import utilz.Constants;
import utilz.LoadSave;
import widgets.Radio;

public class Playing extends State implements Statemethods {
	
	//Playing necesita el player, los botones de retake y bajar, el botón de pausa, y la radio

	private BufferedImage tablero;
	private Player player = null;
	private GameButton[] buttons = new GameButton[2];
	private Radio radio;
	private ResguardoOverlay resguardo;
	private Slot slot = null;
	private int posI = 0, posJ = 0, numActions = 0;
	private ArrayList<Bajada> bajadas;
	private Baraja baraja;
	private Descartes descartes;
	
	public Playing(Game g) {
		super(g);
		tablero = LoadSave.GetSpriteAtlas(LoadSave.TABLERO);
		radio = new Radio();
		resguardo = new ResguardoOverlay();
		bajadas = new ArrayList<>();
		baraja = new Baraja();
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
			if (descartes != player.getFullMano().getDescartes()){ descartes = player.getFullMano().getDescartes(); }
			if (bajadas != player.getListBajada()) { bajadas = player.getListBajada(); }
			
			g.drawImage(tablero, 0, 0, Constants.TableroConstants.TABLERO_WIDTH, Constants.TableroConstants.TABLERO_HEIGHT, null);
			if (!resguardo.isActivated()) { buttons[0].draw(g); }
			buttons[1].draw(g);
			radio.render(g);
			for (Bajada b : bajadas) {
				if (b != null && b.isBajado()) { b.draw(g, player.getFullMano().getImage(), player.getFullMano().getMarco(), player.getFullMano().getAddMarco()); }
			}
			resguardo.draw(g, player.getFullMano().getImage());
			player.render(g, resguardo.getSlots(), resguardo.isActivated());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
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
			}
			if (radio.getHitbox().contains(e.getX(), e.getY())) {
				if (radio.isMuted()) {
					radio.setRadioState(3);
				} else { radio.setRadioState(1); }
			}
		}
		
		if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
			if (!bajadas.get(player.getTurno()).isBajado() && bajadas.get(player.getTurno()).canBajarse(resguardo.getCartas())) {
				buttons[1].setIndex(3);
			} else { buttons[1].setIndex(1); }
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		int i;
		
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
				if (slot.isIn()) {
					resguardo.getSlots().get(posI)[posJ].remove();
					s.add(player.getSelection());
				} else {
					player.getFullMano().moveBetweenSlots(i);
				}
				player.deselect();
			}
		}
		
		if (resguardo.isActivated()) {
			if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
				if (!bajadas.get(player.getTurno()).isBajado() && bajadas.get(player.getTurno()).canBajarse(resguardo.getCartas())) {
					buttons[1].setIndex(2);
					bajadas.get(player.getTurno()).bajarse();
					bajadas.get(player.getTurno()).setBajado(true);
					resguardo.bajarse();
				} else { buttons[1].setIndex(0); }
			} else { buttons[1].setIndex(0); }
			if (resguardo.getButton().getHitbox().contains(e.getX(), e.getY())) {
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
					}
				}
			}
			for (i = 0; i < resguardo.getSlots().size(); i++) {
				for (int j = 0; j < resguardo.getSlots().get(i).length; j++) {
					Slot s = resguardo.getSlots().get(i)[j];
					//para meterlo en el resguardo
					if (s.getCarta() == null && s.getHitbox().contains(e.getX(), e.getY()) && player.getFullMano().getSelection() != null) {
						if (slot.isIn()) {
							resguardo.getSlots().get(posI)[posJ].remove();
						} else {
							player.getFullMano().getSlots().get(posI).remove();
						}
						s.add(player.getSelection());
						player.deselect();
					}
				}
			}
		} else {
			if (buttons[0].getHitbox().contains(e.getX(), e.getY())) {
				if (buttons[0].getIndex() == 1) { buttons[0].setIndex(2); }
				if (buttons[0].getIndex() == 3) { buttons[0].setIndex(0); }
			} else { 
				if (buttons[0].getIndex() == 1) { buttons[0].setIndex(0); }
				if (buttons[0].getIndex() == 3) { buttons[0].setIndex(2); }
			}
			if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
				resguardo.setActivated(true);
				if (player.canBajarse()) {
					buttons[1].setIndex(2);
				} else { buttons[1].setIndex(0); }
			} else { buttons[1].setIndex(0); }
			if (radio.getHitbox().contains(e.getX(), e.getY())) {
				if (radio.isMuted()) {
					radio.setRadioState(0);
					radio.setMuted(false);
				} else {
					radio.setRadioState(2);
					radio.setMuted(true);
				}
			}
		}
		if (bajadas.get(player.getTurno()).isBajado()) {
			for (Bajada b : bajadas) {
				if (b != null && b.isBajado()) {
					b.mouseReleased(e, player.getSelection());
					if (b.isAdded()) {
						if (slot.isIn()) {
							resguardo.getSlots().get(posI)[posJ].remove();
						} else {
							player.getFullMano().getSlots().get(posI).remove();
						}
						b.setAdded(false);
					}
				}
			}
		}
		
		if (baraja.isSelected()) {
			player.setBaraja(true);
			numActions = 1;
			baraja.setSelected(false);
		}
		
		if (descartes.isSelected()) {
			player.setDescartes(true);
			descartes.remove();
			numActions = 1;
    		descartes.setSelected(false);
		}
		if (descartes.getHitbox().contains(e.getX(), e.getY()) && player.getFullMano().getSelection() != null && numActions == 1) {
			if (slot.isIn()) {
				resguardo.getSlots().get(posI)[posJ].remove();
			}
			player.getFullMano().discard(slot.isIn());
			descartes.take(player.getUltimaCarta());
			player.setDescartado(true);
		} else {
			player.deselect();
		}
		
		if (player.isDescartado()) {
			numActions = 0;
			player.setDescartado(false);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
//		System.out.println("X: " + e.getX() + " || Y: " + e.getY());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		for (Carta c : player.getMano()) {
			if (c.isSeleccionada()) {
				c.setX(e.getX() - c.getOffsetX());
				c.setY(e.getY() - c.getOffsetY());
			}
		}
		for (Bajada b : bajadas) {
			if (b != null && b.isBajado()) b.mouseDragged(e, player.getSelection());
		}
	}
}
