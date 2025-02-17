package gameGraphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import mainGame.Game;
import utilz.Constants;
import utilz.LoadSave;
import widgets.Radio;

public class GamePanel extends JPanel {
	
	private MouseInputs mouseInputs;
	private int xDelta = 24, yDelta = 35;
	private BufferedImage img;
	private Game game;
	private Radio radio;

	public GamePanel(Game g) {
		mouseInputs = new MouseInputs(this);
		game = g;
		radio = new Radio();
		setPanelSize();
		importTablero();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	private void setPanelSize() {
		Dimension size = new Dimension(game.GAME_WIDTH, game.GAME_HEIGHT);
		setPreferredSize(size);
	}

	public void changeXDelta(int xDelta) {
		this.xDelta += xDelta;
	}

	public void changeYDelta(int yDelta) {
		this.yDelta += yDelta;
	}
	
	public void setRectPos(int x, int y) {
		xDelta = x;
		yDelta = y;
	}
	
	private void importTablero() {
		img = LoadSave.GetSpriteAtlas(LoadSave.TABLERO);
	}
	
	public void update() {
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, Constants.TableroConstants.TABLERO_WIDTH, Constants.TableroConstants.TABLERO_HEIGHT, null);
		radio.render(g);
		game.render(g);
	}

	public Game getGame() {
		return game;
	}
}
