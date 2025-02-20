package mainGame;

import java.awt.Graphics;

import gameDynamics.Partida;
import gameGraphics.GamePanel;
import gameGraphics.GameWindow;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
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
	private Playing playing;
	private Menu menu;

	public Game() {
		initStates();
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();
		startGameLoop();
	}
	
	private void initStates() {
		playing = new Playing(this);
		menu = new Menu(this);
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
		playing.getPartida().playGame();
	}
	
	public void update() {
		switch (Gamestate.state) {
		case MENU:
			playing.update();
			break;
		case PLAYING:
			playing.update();
			break;
		default:
			break;
		}
	}
	
	public void render(Graphics g) {	
		switch (Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		default:
			break;
		}
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
	
	public Playing getPlaying() {
		return playing;
	}
	
	public Menu getMenu() {
		return menu;
	}
}
