package gameGraphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel {
	
	private MouseInputs mouseInputs;
	private int xDelta = 0, yDelta = 0;
	private BufferedImage img;

	public GamePanel() {
		
		mouseInputs = new MouseInputs(this);
		importImg();
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}
	
	private void importImg() {
		InputStream is = getClass().getResourceAsStream("/All_medium.png");
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setPanelSize() {
		Dimension size = new Dimension(1280,720);
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
	
	public void updateGame() {
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, xDelta, yDelta, null);
	}
}
