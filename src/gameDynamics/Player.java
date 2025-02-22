package gameDynamics;

import java.awt.Graphics;
import java.util.List;

import cardTreatment.Baraja;
import cardTreatment.Carta;
import cardTreatment.Escalera;
import cardTreatment.Mano;
import cardTreatment.Trio;

public class Player extends Round {
	
	private int points, turno, ciclo;
	private Mano mano;
	private boolean roundWinner, gameWinner;
	
	public Player(int t) {
		points = 0;
		turno = t;
		ciclo = 1;
		mano = new Mano();
		roundWinner = false;
		gameWinner = false;
	}
	
	public void give() {
		mano.give();
	}
	
	public void setTurno(int t) {
		turno = t;
	}
	
	public int getTurno() {
		return turno;
	}
	
	public void discard(Carta carta) {
		boolean res = false;
		mano.discard(res);
	}
	
	public void take(Carta carta) {
		mano.take(carta);
	}
	
	public String bajadaToString() {
		String res = "Jugador " + turno + ": \n";
		res += mano.bajadaToString();
		return res;
	}
	
	public void setRoundWinner(boolean w) {
		roundWinner = w;
	}
	
	public void setGameWinner(boolean w) {
		gameWinner = w;
	}
	
	public boolean getGameWinner() {
		return gameWinner;
	}
	
	public void roundWin() {
		if (mano.getSize() == 0) {
			setRoundWinner(true);
		}
	}
	
	public void count() {
		if (!roundWinner) {
			for (int i = 0; i < mano.getSize(); i++) {
				points += mano.getMano().get(i).getValue();
			}
		}
	}
	
	public boolean getRoundWinner() {
		return roundWinner;
	}
	
	public void retake() {
		if (mano.getRetake()) { mano.retake(); }
	}
	
	public boolean canBajarse(int numTrios, int numEscaleras) {
		return mano.canBajarse(numTrios, numEscaleras);
	}
	
	public void bajarse() {
		mano.bajarEscaleras();
		mano.setEscaleras(0);
		mano.bajarTrios();
		mano.setTrios(0);
	}
	
	public String getStringCombos(List<Escalera> escaleras, List<Trio> trios) {
		String res = "";
		if (!escaleras.isEmpty()) {
			for (int i = 0; i < escaleras.size(); i++) {
				res += (escaleras.size() == 1) ? "Escalera: " : "Escalera " + (i + 1) + ": ";
				res += escaleras.get(i) + " ";
			}	
		}
		if (!trios.isEmpty()) {
			for (int i = 0; i < trios.size(); i++) {
				res += (trios.size() == 1) ? "Trío: " : "Trío " + (i + 1) + ": ";
				res += trios.get(i) + " ";
			}
		}
		return res;
	}
	
	public void select(int num) { //añade una carta a la selección
		mano.select(num);
	}
	
	public void deselect() { //quita una carta de la selección
		mano.deselect();
	}
	
	public int getPoints() {
		return points;
	}
	
	public String toString() {
		String res = "Jugador: " + turno + " || Puntuación: " + points;
		res += "\n" + mano.toString();
		return res;
	}

	public Mano getFullMano() {
		return mano;
	}
	
	public int getCiclo() {
		return ciclo;
	}
	
	public void setCiclo(int c) {
		ciclo = c;
	}
	
	public void updateCiclo() {
		ciclo++;
	}
	
	public List<Carta> getMano() {
		return mano.getMano();
	}
	
	public List<Trio> getBajadaTrios() {
		return mano.getBajadaTrios();
	}
	
	public List<Escalera> getBajadaEscaleras() {
		return mano.getBajadaEscaleras();
	}
	
	public Carta getSelection() {
		return mano.getSelection();
	}
	
	public void setPoints(int p) {
		points = p;
	}

	public Carta getUltimaCarta() {
		return mano.getUltimaCarta();
	}

	public void clearMano() {
		mano.getMano().clear();
		mano.setSelection(null);
		mano.getBajadaTrios().clear();
		mano.getBajadaEscaleras().clear();
	}

	public void render(Graphics g) {
		mano.renderSlots(g);
	}
}
