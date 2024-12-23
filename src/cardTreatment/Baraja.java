package cardTreatment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import utilz.Constants;
import utilz.LoadSave;

public class Baraja {
	
	private List<Carta> baraja;
	private BufferedImage img;
	private Rectangle hitbox;
	
	public Baraja() { //Crea una baraja con 52 + 6 = 58 cartas para aleatorizar cuál extrae. Es una baraja con comodín
		baraja = new ArrayList<>();
		newBaraja();
		initHitbox();
		loadImg();
	}
	
	private void initHitbox() {
		hitbox = new Rectangle(28, 43, 148, 208);
	}
	
	private void loadImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.CARD_ATLAS);
	}
	
	public void render(Graphics g) {
		g.drawImage(img.getSubimage(0, 4 * Carta.CARD_HEIGHT, Carta.CARD_WIDTH, Carta.CARD_HEIGHT), 24, 35, 2 * Carta.CARD_WIDTH, 2 * Carta.CARD_HEIGHT, null);
//		g.setColor(Color.PINK);
//		g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}

	public void newBaraja() {
		for (int i = 0; i < Carta.DIAMANTES; i++) {
			for (int j = 0; j < Carta.KING; j++) {
				baraja.add(new Carta(j + 1, i + 1, 24, 285));
			}
		}
		for (int i = 0; i < 6; i++) {
			baraja.add(new Carta(24, 285));
		}
	}
	
	public Carta getCard(int num) {
		Carta carta = baraja.get(num);
		baraja.remove(num);
		if (baraja.size() == 0) {
			newBaraja();
		}
		return carta;
		
	}
	
	public int getSize() {
		return baraja.size();
	}
}
