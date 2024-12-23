package mainGame;

import java.awt.Graphics;

import gameDynamics.Partida;
import gameGraphics.GamePanel;
import gameGraphics.GameWindow;
import utilz.Constants;

public class Game implements Runnable {
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;
	public static final float SCALE = 1.0f;
	public static final int GAME_WIDTH = (int) (Constants.TableroConstants.TABLERO_WIDTH * SCALE);
	public static final int GAME_HEIGHT = (int) (Constants.TableroConstants.TABLERO_HEIGHT * SCALE);
	private Partida partida;

	public Game() {
		gamePanel = new GamePanel(this);
		partida = new Partida(1);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		startGameLoop();
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
		partida.run();
	}
	
	public void update() {
		
	}
	
	public void render(Graphics g) {	
		partida.render(g);
	}
	
	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		long previousTime = System.nanoTime();
		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();
		double deltaU = 0;
		double deltaF = 0;
		
		while (true) {
			long currentTime = System.nanoTime();
			
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			
			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}
			
			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}
			
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
//				System.out.println("FPS: " + frames + " | " + "UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
}
