package mainGame;

import gameGraphics.GamePanel;
import gameGraphics.GameWindow;

public class Game {
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;

	public Game() {
		gamePanel = new GamePanel();
		gameWindow = new GameWindow(gamePanel);
	}
	
}
