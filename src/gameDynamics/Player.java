package gameDynamics;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import cardTreatment.Baraja;
import cardTreatment.Carta;
import cardTreatment.Descartes;
import cardTreatment.Escalera;
import cardTreatment.Mano;
import cardTreatment.Slot;
import cardTreatment.Trio;
import ui.ResguardoOverlay;

public class Player extends Round {
	
	private int points, turno, ciclo;
	private Mano mano;
	private boolean roundWinner, gameWinner, yourTurn = false;
	private volatile boolean isDescartes, isBaraja, descartado;
	private ResguardoOverlay resguardo;
	
	public Player(int t) {
		points = 0;
		turno = t;
		ciclo = 1;
		mano = new Mano();
		roundWinner = false;
		gameWinner = false;
		isDescartes = false;
		isBaraja = false;
		descartado = false;
		resguardo = new ResguardoOverlay();
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
		return true;
	}
	
	public void bajarse() {
		
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
	}

	public void render(Graphics g, List<Slot[]> slots, boolean active) {
		mano.getDescartes().render(g);
		mano.getBaraja().render(g);
		resguardo.setSlots(slots);
		mano.renderSlots(g, slots, active);
	}

	public void setMano(ArrayList<Carta> m) {
		mano.setMano(m);
	}

	public boolean isDescartes() {
		return isDescartes;
	}

	public void setDescartes(boolean isDescartes) {
		this.isDescartes = isDescartes;
	}

	public boolean isBaraja() {
		return isBaraja;
	}

	public void setBaraja(boolean isBaraja) {
		this.isBaraja = isBaraja;
	}

	public boolean isDescartado() {
		return descartado;
	}

	public void setDescartado(boolean descartado) {
		this.descartado = descartado;
	}

	public void setYourTurn(boolean alejandroChan) {yourTurn = alejandroChan; }
	
	public boolean isYourTurn() {
		return yourTurn;
	}

	public void setFullDescartes(ArrayList<Carta> albaChan) {
		mano.setDescartes(albaChan);
	}

	public ResguardoOverlay getResguardo() {
		return resguardo;
	}

	public void setResguardo(ResguardoOverlay resguardo) {
		this.resguardo = resguardo;
	}
}
