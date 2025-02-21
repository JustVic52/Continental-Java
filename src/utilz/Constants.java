package utilz;

import mainGame.Game;

public class Constants {
	
	public static class UI {
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 139;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int BUTTON_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int BUTTON_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
		}
	}

	public static class CardConstants {
		public static final int CARD_WIDTH = 78;
		public static final int CARD_HEIGHT = 112;
		public static final int MARCO_WIDTH = 78;
		public static final int MARCO_HEIGHT = 108;
		
	}
	
	public static class TableroConstants {
		public static final int TABLERO_WIDTH = 1280;
		public static final int TABLERO_HEIGHT = 720;
	}
	
	public static class RadioConstants {
		public static final int RADIO_WIDTH = 98;
		public static final int RADIO_HEIGHT = 125;
		public static final int RADIO_PLAY = 0;
		public static final int RADIO_SELECTED = 1;
		public static final int RADIO_MUTED = 2;
		public static final int RADIO_MUTED_SELECTED = 3;
	}
	
}
