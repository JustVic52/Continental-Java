package gameDynamics;

import java.util.ArrayList;
import java.util.List;

import gameTools.Carta;
import gameTools.Escalera;
import gameTools.Mano;
import gameTools.Trio;

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
		mano.discard(carta);
	}
	
	public void take(Carta carta) {
		mano.take(carta);
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
	
	public boolean canBajarse(int numTrios, int numEscaleras, List<Carta> selection) {
		List<Carta> temp = new ArrayList<>(selection);
		if (temp.size() >= getMinimum()) {
			return mano.canBajarse(numTrios, numEscaleras, temp);
		}
		else { return false; }
	}
	
	public void bajarse() {
		int numTrios = getRoundTrios();
		int numEscaleras = getRoundEscaleras();
		if (mano.getSelection().size() >= getMinimum()) {
			if (canBajarse(numTrios, numEscaleras, mano.getSelection())) {
				if (numEscaleras > 0) {
					mano.bajarEscaleras(mano.getListaEscaleras());	
				}
				if (numTrios > 0) {
					mano.bajarTrios(mano.getListaTrios());
				}
			}
		}
	}
	
	public void updateBajarse() {
		List<Carta> selection = mano.getSelection();
		
	}
	
	public void select(int num) { //añade una carta a la selección
		mano.select(num);
	}
	
	public void deselect(int num) { //quita una carta de la selección
		mano.deselect(num);
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
	
	public List<Carta> getSelection() {
		return mano.getSelection();
	}
	
	public void setPoints(int p) {
		points = p;
	}
	
//	public void put(int num, Carta carta) { //añade una carta a las cartas bajadas
//		mano.put(num, carta);
//	}

	public Carta getUltimaCarta() {
		return mano.getUltimaCarta();
	}

	public void clearMano() {
		mano.getMano().clear();
		mano.getSelection().clear();
		mano.getBajadaTrios().clear();
		mano.getBajadaEscaleras().clear();
	}
}
