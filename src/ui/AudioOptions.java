package ui;

import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import audioClasses.AudioPlayer;
import gamestates.Gamestate;
import mainGame.Game;

public class AudioOptions {

	private VolumeButton volumeButton;
	private SoundButton musicButton, sfxButton;
	private Game game;
	int vBX, vBY, sX, mY, sfxY;

	public AudioOptions(Game game) {
		this.game = game;
		createSoundButtons();
		createVolumeButton();
	}

	private void createVolumeButton() {
		vBX = 640 - SLIDER_WIDTH / 2;
		vBY = 380;
		volumeButton = new VolumeButton(vBX, vBY, SLIDER_WIDTH, VOLUME_HEIGHT);
	}

	private void createSoundButtons() {
		sX = 677;
		mY = 240;
		sfxY = 286;
		musicButton = new SoundButton(sX, mY, SOUND_SIZE, SOUND_SIZE);
		sfxButton = new SoundButton(sX, sfxY, SOUND_SIZE, SOUND_SIZE);
	}

	public void draw(Graphics g) {
		if (Gamestate.state == Gamestate.OPTIONS) {
			if (vBY != 415) {
				vBY = 415;
				mY = 276;
				sfxY = 322;
			}
		} else {
			if (vBY != 380) {
				vBY = 380;
				mY = 240;
				sfxY = 286;
			}
		}
		// Sound buttons
		musicButton.draw(g, mY);
		sfxButton.draw(g, sfxY);

		// Volume Button
		volumeButton.draw(g, vBY);
	}

	public void mouseDragged(MouseEvent e) {
		if (volumeButton.isMousePressed()) {
			float valueBefore = volumeButton.getFloatValue();
			volumeButton.changeX(e.getX());
			float valueAfter = volumeButton.getFloatValue();
			if (valueBefore != valueAfter)
				game.getAudioPlayer().setVolume(valueAfter);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (musicButton.getHitbox().contains(e.getX(), e.getY())) {
			musicButton.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
		}
		if (sfxButton.getHitbox().contains(e.getX(), e.getY())) {
			sfxButton.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
		}
		if (volumeButton.getHitbox().contains(e.getX(), e.getY())) {
			volumeButton.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.FLICK);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (musicButton.getHitbox().contains(e.getX(), e.getY())) {
			if (musicButton.isMousePressed()) {
				musicButton.setMuted(!musicButton.isMuted());
				game.getAudioPlayer().toggleSongMute();
				game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
			}

		} else if (sfxButton.getHitbox().contains(e.getX(), e.getY())) {
			if (sfxButton.isMousePressed()) {
				sfxButton.setMuted(!sfxButton.isMuted());
				game.getAudioPlayer().toggelEffectMute();
				game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
			}
		} else if (volumeButton.getHitbox().contains(e.getX(), e.getY())) {
			game.getAudioPlayer().playEffect(AudioPlayer.FLACK);
		}

		musicButton.resetBools();
		sfxButton.resetBools();
		volumeButton.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		volumeButton.setMouseOver(false);

		if (musicButton.getHitbox().contains(e.getX(), e.getY()))
			musicButton.setMouseOver(true);
		else if (sfxButton.getHitbox().contains(e.getX(), e.getY()))
			sfxButton.setMouseOver(true);
		else if (volumeButton.getHitbox().contains(e.getX(), e.getY()))
			volumeButton.setMouseOver(true);
	}

}