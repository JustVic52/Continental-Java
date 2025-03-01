package gameGraphics;

import java.awt.image.BufferedImage;

import javax.swing.*;

import utilz.LoadSave;

public class GameWindow {
	
	private JFrame jframe;
	
	public GameWindow(GamePanel gamePanel) {
		jframe = new JFrame("Continental: Classic");
		BufferedImage icono = LoadSave.GetSpriteAtlas(LoadSave.LOGO);
        jframe.setIconImage(icono);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);
//		jframe.setLocationRelativeTo(null);
        jframe.setLocation(80, 50);
		jframe.setResizable(false);
		jframe.pack();
		jframe.setVisible(true);
	}

	public JFrame getJFrame() {
		return jframe;
	}
	
}
