package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gameGraphics.GamePanel;
import gamestates.Gamestate;

public class KeyboardInputs implements KeyListener {

	private GamePanel gamePanel;

	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		switch (Gamestate.state) {
		case HOST:
			gamePanel.getGame().getHost().keyTyped(e);
			break;
		case JOIN:
			gamePanel.getGame().getJoin().keyTyped(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (Gamestate.state) {
		case PLAYING:
			gamePanel.getGame().getPlaying().keyPressed(e);
			break;
		case HOST:
			gamePanel.getGame().getHost().keyPressed(e);
			break;
		case JOIN:
			gamePanel.getGame().getJoin().keyPressed(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getOptions().keyPressed(e);
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
