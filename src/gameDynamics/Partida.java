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
	private boolean endRound;
	private Baraja baraja;
	private ArrayList<Bajada> bajadas = new ArrayList<>();
	List<ObjectOutputStream> out;
	List<ObjectInputStream> in;
	ArrayList<Integer> pointList;
	ArrayList<String> nameList;
	
	public Partida(List<Socket> sp, ArrayList<ObjectOutputStream> listaOut) {
		round = new Round();
		socketPlayers = sp;
		baraja = new Baraja();
		descartes = new Descartes();
		numJugadores = sp.size();
		endRound = false;
		pointList = new ArrayList<>();
		nameList = new ArrayList<>();
		crearOutInPoints(listaOut);
	}
	
	private void crearOutInPoints(ArrayList<ObjectOutputStream> listaOut) {
		out = listaOut;
		in = new ArrayList<>();
		for (int i = 0; i < numJugadores; i++) {
			pointList.add(0);
			nameList.add("");
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
		updateBajada();
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
					boolean bajarse = inS.readBoolean();
					if (bajarse) {
						outS.writeBoolean(true);
						outS.flush();
					} else {
						outS.writeBoolean(false);
						outS.flush();
						boolean isBajado = inS.readBoolean();
						if (isBajado) {
							outS.writeBoolean(true);
							outS.flush();
						} else {
							outS.writeBoolean(false);
							outS.flush();
						}
					}
					boolean empty = inS.readBoolean();
					updateEnd(empty, outS);
					if (empty) {
						end = inS.readBoolean();
						updateEnd(end, outS);
						if (end) break;
					} else {
						bajadas = (ArrayList<Bajada>) inS.readObject();
						updateBajada();
						//fase 3: descartar
						aux = (ArrayList<Carta>) inS.readObject();
						descartes.setDescartes(aux);
						updateDescartes();
						end = inS.readBoolean();
						updateEnd(end, outS);
						if (end) break;
					}
				} catch (IOException e) {
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			endRound = end;
		}
		countPoints();
		try { Thread.sleep(5000); } catch (InterruptedException e) {}
		round.updateRound();
		descartes = new Descartes();
		baraja = new Baraja();
		bajadas = new ArrayList<>();
	}

	private void givePlayers() {
		for (ObjectOutputStream outS : out) {
			try {
				outS.writeInt(numJugadores);
				outS.flush();
			} catch (IOException e) {
				
			}
		}
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
		ArrayList<Boolean> aux = new ArrayList<>();
		for (Socket s : socketPlayers) {
			aux.add(s == actual);
		}
		for (ObjectOutputStream outS : out) {
			try {
				outS.writeObject(aux);
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
			for (int j = 0; j < round.getNumCartas(); j++) {
				cartas.add(baraja.give());
			}
//			cartas = makePersonalizedCards();
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
		res.add(new Carta(9,1,0,0));
		res.add(new Carta(9,2,0,0));
		res.add(new Carta(9,3,0,0));
		res.add(new Carta(9,4,0,0));
		res.add(new Carta(10,1,0,0));
		res.add(new Carta(10,2,0,0));
		res.add(new Carta(10,3,0,0));
//		res.add(new Carta(10,4,0,0));
//		res.add(new Carta(10,4,0,0));
		
		return res;
	}

	public void playGame() {
		//número de jugadores
		givePlayers();
		//lista con los nombres de los jugadores
		giveNames();
		//lógica del juego
		for (int i = 0; i < 10; i++) {
			//damos cartas
			giveCards();
			//jugamos ronda
			run();
		}
		//Decimos lo del winner y tal
	}

	private void giveNames() {
		try {
			for (int i = 0; i < numJugadores; i++) {
				ObjectInputStream inS = in.get(i);
				nameList.remove(i);
				String name = (String) inS.readObject();
				nameList.add(i, name);
			}
			for (ObjectOutputStream outS : out) {
				outS.writeObject(nameList);
				outS.flush();
			}
		}
		catch (IOException | ClassNotFoundException e) {}
	}

	private void giveRound() {
		for (ObjectOutputStream outS : out) {
			try {
				outS.writeInt(round.getNumRound());
				outS.flush();
			} catch (IOException e) {}
		}
	}

	private void countPoints() {
		try {
			for (int i = 0; i < numJugadores; i++) {
				ObjectInputStream inS = in.get(i);
				pointList.remove(i);
				int points = inS.readInt();
				pointList.add(i, points);
			}
			for (ObjectOutputStream outS : out) {
				outS.writeObject(pointList);
				outS.flush();
			}
		}
		catch (IOException e) {}
	}

	public Baraja getBaraja() {
		return baraja;
	}

	public Round getRound() {
		return round;
	}
}
