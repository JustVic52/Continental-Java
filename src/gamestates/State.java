package gamestates;

import audio.AudioPlayer;
import mainGame.Game;

public class State {

	protected Game game;
	
	public State(Game g) {
		this.game = g;
	}
	
	public Game getGame() {
		return game;
	}
	
}
