package gameGraphics;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import mainGame.Game;

public class GamePanel extends JPanel {
	
	private static final long serialVersionUID = -6596848864696710719L;
	
	private MouseInputs mouseInputs;
	private Game game;
	private Font fuente;
	private static int textCursor = -1;

	public GamePanel(Game g) {
		mouseInputs = new MouseInputs(this);
		game = g;
		setPanelSize();
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	private void setPanelSize() {
		Dimension size = new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT);
		setPreferredSize(size);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			InputStream is = getClass().getResourceAsStream("/fuentes/Minecraftia-Regular.ttf");
			Font fuente = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 18);
			g.setFont(fuente);
        } catch (IOException | FontFormatException e) {
            fuente = new Font("Arial", Font.BOLD, 12);
        }
		g.setFont(fuente);
		game.render(g);
		if (textCursor != -1) { setCursor(new Cursor(textCursor)); }
	}

	public Game getGame() {
		return game;
	}

	public static void setIntCursor(int tc) {
		textCursor = tc;
	}
}
