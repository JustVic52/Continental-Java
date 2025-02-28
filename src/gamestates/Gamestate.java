package gamestates;

public enum Gamestate {
	
	PLAYING, MENU, OPTIONS, QUIT, HOST, JOIN;
	
	public static Gamestate state = MENU;
}
