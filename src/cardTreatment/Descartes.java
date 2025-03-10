package cardTreatment;

import java.util.List;

import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Descartes {
	
	private List<Carta> descartes;
	private Rectangle hitbox;
	private BufferedImage img, marco;
	private boolean selected = false;
	
	public Descartes() {
		descartes = new ArrayList<>();
		initHitbox();
		loadImg();
	}
	
	public void take(Carta carta) {
		descartes.add(carta);
		carta.setX(24);
		carta.setY(286);
	}
	
	private void initHitbox() {
		hitbox = new Rectangle(28, 293, 148, 208);
	}
	
	private void loadImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.CARD_ATLAS);
		marco = LoadSave.GetSpriteAtlas(LoadSave.MARCO);
	}
	
	public void setSelected(boolean s) { selected = s; }
	
	public boolean isSelected() { return selected; }
	
	public void render(Graphics g) {
		if (!descartes.isEmpty()) {
			getCarta().render(g, img, marco, 2); // 28, 293
		}
		if (selected) {
			g.drawImage(marco, 24, 289, 2 * Carta.MARCO_WIDTH, 2 * Carta.MARCO_HEIGHT, null);
		}
//		g.setColor(Color.PINK);
//		g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}
	
	public void remove() {
		if (!descartes.isEmpty()) {
            descartes.remove(getSize() - 1); // Elimina y devuelve el último elemento
        }
	}
	
	public Carta getCarta() {
		return descartes.get(getSize() - 1);
	}
	
	public int getSize() {
		return descartes.size();
	}
	
	public boolean isEmpty() {
		return descartes.size() == 0;
	}
	
	public String toString() {
		String res = "Carta superior de descartes: ";
		if (!descartes.isEmpty()) { res += getCarta().toString() + "\n"; }
		return res;
	}

	public void clear() {
		descartes.clear();
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public void setDescartes(ArrayList<Carta> albaChan) {
		descartes = albaChan;
	}

	public ArrayList<Carta> getDescartes() {
		return (ArrayList<Carta>) descartes;
	}
	
}
