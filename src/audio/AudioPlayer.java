package audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

	public static int MENU_THEME = 0;
	public static int START_PLAYING = 1;
	
	public static int LVL_COMPLETED = 0;
	public static int CARDINSLOT = 1;
	public static int ROBAR = 2;
	public static int FLICK = 3;
	public static int FLACK = 4;
	public static int DESCARTAR = 5;
	public static int TURN = 6;
	public static int BAJAR = 7;
	public static int AÑADIR = 8;
	public static int LVL_LOST = 9;
	public static int BEEP = 10;
	
	private Clip[] songs, effects;
	private int currentSongId;
	private float volume = 0.75f;
	private boolean songMute, effectMute;
	
	public AudioPlayer() {
		loadSongs();
		loadEffects();
		playSong(MENU_THEME);
	}
	
	public void setVolume(Float volume) {
		this.volume = volume;
		updateSongVolume();
		updateEffectVolume();
	}
	
	public void stopSong() {
		if (songs[currentSongId].isActive()) {
			songs[currentSongId].stop();
		}
	}
	
	private void loadSongs() {
		String[] names = {"Menu theme", "Card Faces On My Mind", "About that ace", "to be and just to be", 
				"CardShark Dreams", "Ace of Pixels", "In a blank world", "Pixelated Love", "Pixel Showdown", "Pixel Hearts", "In a pixel world"};
		songs = new Clip[names.length];
		for (int i = 0; i < songs.length; i++) {
			songs[i] = getClip(names[i]);
		}
		
		updateSongVolume();
	}
	
	private void playNextSong() {
	    int nextSong = currentSongId + 1;
	    if (nextSong >= songs.length) {
	        nextSong = 0;
	    }
	    playSong(nextSong);
	}
	
	public void startLoop(int song) {
	    stopSong();
	    currentSongId = song;
	    
	    Clip currentClip = songs[currentSongId];
	    if (currentClip != null) {
	        currentClip.setMicrosecondPosition(0);
	        updateSongVolume();
	        
	        currentClip.addLineListener(event -> {
	            if (event.getType() == LineEvent.Type.STOP) {
	                currentClip.close();
	                playNextSong();
	            }
	        });
	        
	        currentClip.start();
	    }
	}
	
	private void loadEffects() {
		String[] names = {"lvlcompleted", "cardInSlot", "robar", "flick", "flack", "descartar", "turn", "bajar", "añadir", "lvllost", "beep"};
		effects = new Clip[names.length];
		for (int i = 0; i < effects.length; i++) {
			effects[i] = getClip(names[i]);
		}
		
		updateEffectVolume();
	}
	
	public void playEffect(int effect) {
		Clip clip = effects[effect];
		clip.setMicrosecondPosition(0);
		clip.start();
	}
	
	public void playSong(int song) {
		stopSong();
		currentSongId = song;
		updateSongVolume();
		songs[currentSongId].setMicrosecondPosition(0);
		songs[currentSongId].start();
		songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	private Clip getClip(String name) {
		URL url = getClass().getResource("/audio/" + name + ".wav");
		AudioInputStream audio;
		try {
			audio = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			return clip;
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {}
		
		return null;
	}
	
	private void updateSongVolume() {
		FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();
		gainControl.setValue(gain);
	}
	
	private void updateEffectVolume() {
		for (Clip c : effects) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}
	
	public void toggleSongMute() {
		songMute = !songMute;
		for (Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);
		}
	}
	
	public void toggelEffectMute() {
		effectMute = !effectMute;
		for (Clip c : effects) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(effectMute);
		}
	}
}
