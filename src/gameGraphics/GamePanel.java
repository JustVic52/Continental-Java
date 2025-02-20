package gameGraphics;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import mainGame.Game;

public class GamePanel extends JPanel {
	
	private MouseInputs mouseInputs;
	private Game game;

	public GamePanel(Game g) {
		mouseInputs = new MouseInputs(this);
		game = g;
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	private void setPanelSize() {
		Dimension size = new Dimension(game.GAME_WIDTH, game.GAME_HEIGHT);
		setPreferredSize(size);
	}
	
	public void update() {
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	public Game getGame() {
		return game;
	}
}
