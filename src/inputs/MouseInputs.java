package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gameGraphics.GamePanel;
import gamestates.Gamestate;

public class MouseInputs implements MouseListener, MouseMotionListener {
	
	private GamePanel gamePanel;
	
	public MouseInputs(GamePanel gp) {
		gamePanel = gp;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseDragged(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseDragged(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getOptions().mouseDragged(e);
		default:
			break;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseMoved(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseMoved(e);
			break;
		case HOST:
			gamePanel.getGame().getHost().mouseMoved(e);
			break;
		case JOIN:
			gamePanel.getGame().getJoin().mouseMoved(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getOptions().mouseMoved(e);
		default:
			break;
		}
//		System.out.println("X: " + e.getX() + " | Y: " + e.getY());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (Gamestate.state) {
		case HOST:
			gamePanel.getGame().getHost().mouseClicked(e);
			break;
		case JOIN:
			gamePanel.getGame().getJoin().mouseClicked(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mousePressed(e);
			break;
		case HOST:
			gamePanel.getGame().getHost().mousePressed(e);
			break;
		case JOIN:
			gamePanel.getGame().getJoin().mousePressed(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getOptions().mousePressed(e);
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseReleased(e);
			break;
		case HOST:
			gamePanel.getGame().getHost().mouseReleased(e);
			break;
		case JOIN:
			gamePanel.getGame().getJoin().mouseReleased(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getOptions().mouseReleased(e);
		default:
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}
