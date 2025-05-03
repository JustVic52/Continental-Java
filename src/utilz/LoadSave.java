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
	public static final String ADDMARCO = "marco_add.png";
	public static final String ADDCLIPPEDMARCO = "marco_add_clipped.png";
	public static final String BAJAR_BUTTON = "ES_actions_atlas.png";
	public static final String RESGUARDO = "resguardo_ronda_";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_OVERLAY = "menu_background.png";
	public static final String MENU_BACKGROUND = "background_menu.png";
	public static final String LOGO = "logo.png";
	public static final String HOST_OVERLAY = "host_background.png";
	public static final String JOIN_OVERLAY = "join_background.png";
	public static final String COMBO_BUTTONS = "comboBox_atlas.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String MARCO_CUADRO = "marco_cuadrotexto.png";
	public static final String TABLEROPOINTS = "tableropoints.png";
	public static final String WIN_LOSE_BANNER = "win_lose_banner.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String OPTIONS_MENU = "options_background.png";
	public static final String CIRCLETIMER = "circleTimer.png";
	public static final String WAITING = "waiting.png";
	public static final String CREDITS = "credits_list.png";

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
