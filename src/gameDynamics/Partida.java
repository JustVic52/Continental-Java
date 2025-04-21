package gameDynamics;

import java.util.List;

import cardTreatment.Bajada;
import cardTreatment.Baraja;
import cardTreatment.Carta;
import cardTreatment.Descartes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Partida {
	
	private Round round;
	private List<Socket> socketPlayers;
	private int numJugadores;
	private Descartes descartes;
	private boolean endRound, taken;
	private Baraja baraja;
	private ArrayList<Bajada> bajadas = new ArrayList<>();
	List<ObjectOutputStream> out;
	List<ObjectInputStream> in;
	
	public Partida(List<Socket> sp, ArrayList<ObjectOutputStream> listaOut) {
		round = new Round();
		socketPlayers = sp;
		baraja = new Baraja();
		descartes = new Descartes();
		numJugadores = sp.size();
		endRound = false;
		crearOutIn(listaOut);
	}
	
	private void crearOutIn(ArrayList<ObjectOutputStream> listaOut) {
		out = listaOut;
		in = new ArrayList<>();
		for (int i = 0; i < numJugadores; i++) {
			try {
				in.add(new ObjectInputStream(socketPlayers.get(i).getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Descartes getDescartes() { return descartes; }

	public void run() {
		for (int i = 0; i < numJugadores; i++) {
			bajadas.add(new Bajada(round.getNumRound(), 0, 0));
		}
		endRound = false;
		boolean end = false;
		ArrayList<Carta> aux = new ArrayList<>();
		giveRound();
		while (!endRound) {
			for (int i = 0; i < numJugadores; i++) {
				Socket s = socketPlayers.get(i);
				ObjectOutputStream outS = out.get(i);
				ObjectInputStream inS = in.get(i);
				try {
					//decir que es tu turno
					sendTurns(s);
					//fase 1: robar
					int action = inS.readInt();
					if (action == 1) {
						sendCard(baraja.give(), outS);
					}
					if (action == 2) {
						sendCard(descartes.getCarta(), outS);
						descartes.remove();
					}
					aux = (ArrayList<Carta>) inS.readObject();
					descartes.setDescartes(aux);
					updateDescartes();
					//fase 2: bajarse (opcional)
					boolean descartado = inS.readBoolean();
					if (descartado) {
						outS.writeBoolean(true);
						outS.flush();
						bajadas = (ArrayList<Bajada>) inS.readObject();
					} else {
						outS.writeBoolean(false);
						outS.flush();
						boolean isBajado = inS.readBoolean();
						if (isBajado) {
							outS.writeBoolean(true);
							outS.flush();
							bajadas = (ArrayList<Bajada>) inS.readObject();
						} else {
							outS.writeBoolean(false);
							outS.flush();
						}
					}
					updateBajada();
					//fase 3: descartar
					aux = (ArrayList<Carta>) inS.readObject();
					descartes.setDescartes(aux);
					updateDescartes();
					end = inS.readBoolean();
					updateEnd(end, outS);
					if (end) break;
				} catch (IOException e) {
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			endRound = end;
		}
//		countPoints();
		round.updateRound();
		descartes = new Descartes();
		baraja = new Baraja();
		bajadas = new ArrayList<>();
	}

	private void updateEnd(boolean end, ObjectOutputStream outS2) {
		for (ObjectOutputStream outS : out) {
			if (outS != outS2) {
				try {
					outS.writeBoolean(end);
					outS.flush();
				}
				catch (IOException e) {}
			}
		}
	}

	private void updateBajada() {
		for (ObjectOutputStream outS : out) {
			try {
				outS.writeObject(bajadas);
				outS.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateDescartes() {
		for (ObjectOutputStream outS : out) {
			try {
				outS.writeObject(descartes.getDescartes());
				outS.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendTurns(Socket actual) {
		for (int i = 0; i < socketPlayers.size(); i++) {
			Socket s = socketPlayers.get(i);
			ObjectOutputStream outS = out.get(i);
			try {
				outS.writeBoolean(s == actual);
				outS.flush();
			} catch (IOException e) {
			}
		}
	}

	private void sendCard(Carta carta, ObjectOutputStream out) {
		try {
			out.writeObject(carta);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void giveCards() {
		List<Carta> cartas = new ArrayList<>();
		for (ObjectOutputStream outS : out) {
//			cartas = makePersonalizedCards();
			for (int j = 0; j < round.getNumCartas(); j++) {
				cartas.add(baraja.give());
			}
			try {
				outS.writeObject(cartas);
				outS.flush();
			}
			catch (IOException e) {}
			cartas.clear();
		}
	}

	private List<Carta> makePersonalizedCards() {
		List<Carta> res = new ArrayList<>();
		for (int j = 0; j < round.getNumCartas(); j++) {
			Carta carta = baraja.give();
		}
		res.add(new Carta(9,1,0,0));
		res.add(new Carta(9,2,0,0));
		res.add(new Carta(9,3,0,0));
		res.add(new Carta(9,4,0,0));
		res.add(new Carta(10,1,0,0));
		res.add(new Carta(10,2,0,0));
		res.add(new Carta(10,3,0,0));
		res.add(new Carta(10,4,0,0));
		
		return res;
	}

	public void playGame() {
		for (int i = 0; i < 10; i++) {
			giveCards();
			run();
		}
		setGameWinner();
	}

	private void giveRound() {
		int i = 0;
		for (ObjectOutputStream outS : out) {
			try {
				outS.writeInt(round.getNumRound());
				outS.flush();
			} catch (IOException e) {
				
			}
			i++;
		}
	}

	private void setGameWinner() {
		
	}

	private void countPoints() {
		
	}

	public Baraja getBaraja() {
		return baraja;
	}

	public Round getRound() {
		return round;
	}
}
