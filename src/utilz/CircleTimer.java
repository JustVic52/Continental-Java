package utilz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import audioClasses.AudioPlayer;
import mainGame.Game;

public class CircleTimer {

    private int elapsedSeconds = 0;
    private Timer timer;
    private BufferedImage img;
    private int x, y, width, height;
    private boolean active = false;

    public CircleTimer(int xPos, int yPos, Game game) {
    	x = xPos;
    	y = yPos;
    	img = LoadSave.GetSpriteAtlas(LoadSave.CIRCLETIMER);
    	width = img.getWidth();
    	height = img.getHeight();
        timer = new Timer(1000, new ActionListener() { // Cada 1000ms

			@Override
			public void actionPerformed(ActionEvent e) {
				if (elapsedSeconds == 0) game.getAudioPlayer().playEffect(AudioPlayer.BEEP);
				elapsedSeconds++;
                if (elapsedSeconds >= 3) { // Después de 4 cortes (5 segundos en total)
                	active = false;
                	elapsedSeconds = 0;
                    timer.stop();
                }
			}
        });
    }
    
    public void start() { active = true; timer.start();}

    public void draw(Graphics g) {
    	if (active) {
    		// Fondo blanco
    		g.setColor(new Color(218, 56, 67, 255));
    		g.fillArc(x, y, 28, 28, 0, 360);
            g.setColor(Color.WHITE);

            // Dibujar el círculo
            int diameter = 27;
//            int x = (width - diameter) / 2;
//            int y = (height - diameter) / 2;

            // El ángulo que queda:
            int remainingAngle = 360 - (elapsedSeconds * 120);

            // Solo dibujar si queda algo
            if (remainingAngle > 0) {
                g.fillArc(x, y, diameter, diameter, -270, remainingAngle);
            }
            
            g.drawImage(img, x, y, width, height, null);
    	}
    }
}
