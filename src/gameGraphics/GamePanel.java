package gameGraphics;

import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel {
	
	private MouseInputs mouseInputs;
	private int xDelta = 100, yDelta = 100;
	private int frames = 0;
	private long lastCheck = 0;

	public GamePanel() {
		
		mouseInputs = new MouseInputs(this);
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.fillRect(xDelta, yDelta, 200, 50);
		
		frames++;
		if (System.currentTimeMillis() - lastCheck >= 1000) {
			lastCheck = System.currentTimeMillis();
			System.out.println("FPS: " + frames);
			frames = 0;
		}
	}
}
