package gameDynamics;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import cardTreatment.Bajada;
import cardTreatment.Carta;
import cardTreatment.Mano;
import cardTreatment.Slot;
import ui.ResguardoOverlay;

public class Player {
	
	private int turno, ciclo = 0, numRound = 1, contRetake = 5, contDes = 0, numJugadores = 1;
	private Mano mano;
	private boolean roundWinner, gameWinner, yourTurn = false, retake = false, taken = false, vaADescartar = false;
	private volatile boolean isDescartes, isBaraja, descartado;
	private ResguardoOverlay resguardo;
	private ArrayList<Bajada> bajadaList;
	private ArrayList<Integer> pointList;
	private ArrayList<String> nameList;
	private ArrayList<Boolean> turns;
	
	public Player(int t) {
		turno = t;
		mano = new Mano();
		roundWinner = false;
		gameWinner = false;
		isDescartes = false;
		isBaraja = false;
		descartado = false;
		resguardo = new ResguardoOverlay(numRound);
		bajadaList = new ArrayList<>();
		pointList = new ArrayList<>();
		nameList = new ArrayList<>();
		turns = new ArrayList<>();
		initLists();
	}
	
	private void initLists() {
		for (int i = 0; i < 4; i++) {
			if (i == turno) {
				bajadaList.add(turno, new Bajada(numRound, 229,270));
			} else {
				bajadaList.add(null);
			}
			pointList.add(0);
			nameList.add("");
			if (nameList.get(i).equals("")) turns.add(false);
		}
	}
	
	public void setTurno(int t) {
		turno = t;
	}
	
	public int getTurno() {
		return turno;
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
	
	public boolean isVaADescartar() {
		return vaADescartar;
	}

	public void setVaADescartar(boolean vaADescartar) {
		this.vaADescartar = vaADescartar;
	}

	public void setNumRound(int rapiño) { numRound = rapiño; }
	
	public void roundWin() {
		if (getFullMano().isEmpty()) {
			setRoundWinner(true);
		}
	}
	
	public boolean getRoundWinner() {
		return roundWinner;
	}
	
	public void select(int num) { //añade una carta a la selección
		mano.select(num);
	}
	
	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
	}

	public void deselect() { //quita una carta de la selección
		mano.deselect();
	}
	
	public int getPoints() {
		return pointList.get(turno);
	}

	public Mano getFullMano() {
		return mano;
	}
	
	public boolean isRetake() { return retake; }
	
	public void setRetake(boolean analdestroyer3x49974k) { retake = analdestroyer3x49974k; }
	
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

	public Carta getUltimaCarta() {
		return mano.getUltimaCarta();
	}

	public void clearMano() {
		mano.getMano().clear();
		mano.setSelection(null);
	}

	public void render(Graphics g, List<Slot[]> slots, boolean active) {
		resguardo.updateTablero(numRound);
		mano.getDescartes().render(g);
		mano.getBaraja().render(g);
		for (int i = 0; i < bajadaList.size(); i++) {
			Bajada b = bajadaList.get(i);
			if (b != null) {
				b.updateRonda(numRound);
				switch (i) {
				case 0:
					b.setXY(240, 41);
					break;
				case 1:
					b.setXY(240, 246);
					break;
				case 2:
					b.setXY(756, 41);
					break;
				case 3:
					b.setXY(756, 246);
					break;
				}
//				b.draw(g, mano.getImage(), mano.getMarco());
			}
		}
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

	public void setBajada(Bajada b) {
		bajadaList.remove(turno);
		bajadaList.add(turno, b);
	}
	
	public Bajada getBajada() {
		return bajadaList.get(turno);
	}

	public boolean canBajarse() {
		return bajadaList.get(turno) != null && bajadaList.get(turno).getCanBajarse();
	}

	public void setListBajada(ArrayList<Bajada> mino) {
		bajadaList = mino;
	}

	public ArrayList<Bajada> getListBajada() {
		return bajadaList;
	}

	public void update() {
		resguardo = new ResguardoOverlay(numRound);
		mano = new Mano();
		isDescartes = false;
		isBaraja = false;
		descartado = false;
		resguardo.updateTablero(numRound);
		bajadaList = new ArrayList<>();
		initBajada();
	}
	
	private void initBajada() {
		for (int i = 0; i < 4; i++) {
			if (i == turno) {
				bajadaList.add(turno, new Bajada(numRound, 229,270));
			} else {
				bajadaList.add(null);
			}
		}
	}

	public void countPoints() {
		int points = pointList.remove(turno);
		if (roundWinner) {
			points -= numRound * 10;
		} else {
			for (Carta c : getMano()) {
				points += c.getValue();
			}
		}
		pointList.add(turno, points);
	}

	public ArrayList<String> getNameList() { return nameList; }
	
	public void setNameList(ArrayList<String> aux) { nameList = aux; }
	
	public ArrayList<Integer> getPointList() { return pointList; }
	
	public void setPointList(ArrayList<Integer> aux) { pointList = aux; }
	
	public ArrayList<Boolean> getTurns() { return turns; }

	public void setTurns(ArrayList<Boolean> aux3) {
		turns = aux3;
		setYourTurn(turns.get(turno));
	}

	public int getContRetake() {
		return contRetake;
	}
	
	public void contRetake() { if (contRetake > 0 && taken && !yourTurn) contRetake--; }

	public int getContDes() {
		return contDes;
	}

	public void setContDes(int nice) { contDes = nice; }

	public void setNumJugadores(int numJug) {
		numJugadores = numJug;
	}
	
	public int getNumJugadores() { return numJugadores; }
}
