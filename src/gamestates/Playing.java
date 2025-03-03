package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import cardTreatment.Baraja;
import cardTreatment.Carta;
import cardTreatment.Descartes;
import cardTreatment.Slot;
import gameDynamics.Player;
import mainGame.Game;
import net.Client;
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
	private Descartes descartes;
	
	public Playing(Game g) {
		super(g);
		tablero = LoadSave.GetSpriteAtlas(LoadSave.TABLERO);
		radio = new Radio();
		resguardo = new ResguardoOverlay();
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
			g.drawImage(tablero, 0, 0, Constants.TableroConstants.TABLERO_WIDTH, Constants.TableroConstants.TABLERO_HEIGHT, null);
			if (!resguardo.isActivated()) { buttons[0].draw(g); }
			buttons[1].draw(g);
			radio.render(g);
			resguardo.draw(g, player.getFullMano().getImage());
			descartes.render(g);
			player.render(g, resguardo.getSlots(), resguardo.isActivated());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		Baraja baraja = player.getFullMano().getBaraja();
		
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
			if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
				buttons[1].setIndex(1);
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
			if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
				buttons[1].setIndex(1);
			}
			if (radio.getHitbox().contains(e.getX(), e.getY())) {
				if (radio.isMuted()) {
					radio.setRadioState(3);
				} else { radio.setRadioState(1); }
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		Baraja baraja = player.getFullMano().getBaraja();
		
		int i;
		
		for (i = 0; i < player.getFullMano().getSlots().size(); i++) {
			Carta c = player.getFullMano().getSlots().get(i).getCarta();
			if (c != null && c.isSeleccionada()) {
				c.setSeleccionada(false);
				c.setX(player.getFullMano().getSlots().get(i).getX());
				c.setY(player.getFullMano().getSlots().get(i).getY());
			}
		}
		
		for (i = 0; i < player.getFullMano().getSlots().size(); i++) {
			Slot s = player.getFullMano().getSlots().get(i);
			if (s.getHitbox().contains(e.getX(), e.getY()) && player.getFullMano().getSelection() != null) {
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
				buttons[1].setIndex(0);
			}
			if (resguardo.getButton().getHitbox().contains(e.getX(), e.getY())) {
				resguardo.getButton().setIndex(0);
				buttons[1].setIndex(0);
				resguardo.setActivated(false);
			}
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
			}
			if (buttons[1].getHitbox().contains(e.getX(), e.getY())) {
				resguardo.setActivated(true);
				buttons[1].setIndex(0);
			}
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
		
		if (baraja.isSelected()) {
			player.setBaraja(true);
			numActions = 1;
			baraja.setSelected(false);
		}
		
		if (descartes.isSelected()) {
			player.setDescartes(true);
			numActions = 1;
    		descartes.setSelected(false);
		}
		if (descartes.getHitbox().contains(e.getX(), e.getY()) && player.getFullMano().getSelection() != null) {
			if (slot.isIn()) {
				resguardo.getSlots().get(posI)[posJ].remove();
			}
			player.getFullMano().discard(slot.isIn());
			descartes.take(player.getUltimaCarta());
		} else {
			player.deselect();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
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
	}
}
