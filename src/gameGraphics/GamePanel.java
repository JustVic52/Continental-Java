package gameGraphics;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JTextField;

import gamestates.Gamestate;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import mainGame.Game;

public class GamePanel extends JPanel {
	
	private MouseInputs mouseInputs;
	private Game game;
	private Font fuente;
	private static int textCursor = -1;

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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
            fuente = Font.createFont(Font.TRUETYPE_FONT, new File("res/fuentes/Minecraftia-Regular.ttf"));
            fuente = fuente.deriveFont(Font.PLAIN, 18); // Ajustar tamaño y estilo
        } catch (IOException e) {
            e.printStackTrace();
            fuente = new Font("Arial", Font.BOLD, 12); // Fuente por defecto si falla
        } catch (FontFormatException e) {
            e.printStackTrace();
            fuente = new Font("Arial", Font.BOLD, 12); // Fuente por defecto si falla
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
