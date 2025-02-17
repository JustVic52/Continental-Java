package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import cardTreatment.Carta;
import cardTreatment.Slot;
import gameDynamics.Partida;
import gameDynamics.Player;
import mainGame.Game;

public class Playing extends State implements Statemethods {
	
	//Playing necesita el player, los botones de retake y bajar, el botón de pausa, y la radio

	private Partida partida;
	private Player player;
	
	public Playing(Game g) {
		super(g);
		partida = new Partida(1);
		player = partida.getJugadores().get(0);
	}
	
	public Partida getPartida() {
		return partida;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void draw(Graphics g) {
		partida.render(g);
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
				c.setY(c.getY() - 20);
				player.getFullMano().select(c);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int i;
		for (i = 0; i < player.getFullMano().getSlots().size(); i++) {
			Carta c = player.getFullMano().getSlots().get(i).getCarta();;
			if (c != null && c.isSeleccionada()) {
				c.setSeleccionada(false);
				c.setX(player.getFullMano().getSlots().get(i).getX());
				c.setY(player.getFullMano().getSlots().get(i).getY());
			}
		}
		for (i = 0; i < player.getFullMano().getSlots().size(); i++) {
			Slot s = player.getFullMano().getSlots().get(i);
			if (s.getHitbox().contains(e.getX(), e.getY()) && player.getFullMano().getSelection() != null) {
				player.getFullMano().moveBetweenSlots(i);
				player.deselect();
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
			player.getFullMano().discard();
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
				int x = e.getX() - c.getX();
				c.setX(e.getX());
				int y = e.getY() - c.getY();
				c.setY(e.getY());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}
