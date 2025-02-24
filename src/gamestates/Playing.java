package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import cardTreatment.Carta;
import cardTreatment.Slot;
import gameDynamics.Partida;
import gameDynamics.Player;
import mainGame.Game;
import ui.GameButton;
import ui.ResguardoOverlay;
import utilz.Constants;
import utilz.LoadSave;
import widgets.Radio;

public class Playing extends State implements Statemethods {
	
	//Playing necesita el player, los botones de retake y bajar, el botón de pausa, y la radio

	private Partida partida;
	private BufferedImage tablero;
	private Player player;
	private GameButton[] buttons = new GameButton[2];
	private Radio radio;
	private ResguardoOverlay resguardo;
	private Slot slot = null;
	private int posI = 0, posJ = 0;
	
	public Playing(Game g) {
		super(g);
		tablero = LoadSave.GetSpriteAtlas(LoadSave.TABLERO);
		partida = new Partida(1);
		player = partida.getJugadores().get(0);
		radio = new Radio();
		resguardo = new ResguardoOverlay();
		iniButtons();
	}
	
	private void iniButtons() {
		buttons[0] = new GameButton(1182, 481, 1);
		buttons[1] = new GameButton(1182, 629, 0);
	}

	public Partida getPartida() {
		return partida;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(tablero, 0, 0, Constants.TableroConstants.TABLERO_WIDTH, Constants.TableroConstants.TABLERO_HEIGHT, null);
		for (GameButton gb : buttons) {
			gb.draw(g);
		}
		radio.render(g);
		resguardo.draw(g, player.getFullMano().getImage());
		partida.render(g, resguardo.getSlots(), resguardo.isActivated());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		if (partida.baraja.getHitbox().contains(e.getX(), e.getY())) { partida.baraja.setSelected(true); }
		
		if (partida.getDescartes().getHitbox().contains(e.getX(), e.getY()) && !partida.getDescartes().isEmpty()) { partida.getDescartes().setSelected(true); }
		
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
		
		if (partida.baraja.isSelected()) {
			player.give();
			partida.baraja.setSelected(false);
		}
		
		if (partida.getDescartes().isSelected()) {
			player.take(partida.getDescartes().getCarta());
    		partida.getDescartes().remove();
    		partida.getDescartes().setSelected(false);
		}
		if (partida.getDescartes().getHitbox().contains(e.getX(), e.getY()) && player.getFullMano().getSelection() != null) {
			if (slot.isIn()) {
				resguardo.getSlots().get(posI)[posJ].remove();
			}
			player.getFullMano().discard(slot.isIn());
			partida.getDescartes().take(player.getUltimaCarta());
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
	public void keyReleased(KeyEvent e) {
		
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

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
