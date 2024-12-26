package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LoadSave {
	
	public static final String CARD_ATLAS = "All_medium.png";
	public static final String TABLERO = "FondoPartida.png";
	public static final String RADIO = "radio.png";
	public static final String MARCO = "medium_marco.png";

	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
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
		return img;
	}
	
}
