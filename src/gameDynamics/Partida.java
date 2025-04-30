package gameDynamics;

import java.util.List;
import java.util.Map;

import cardTreatment.Bajada;
import cardTreatment.Baraja;
import cardTreatment.Carta;
import cardTreatment.Descartes;
import gamestates.Gamestate;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Partida {
	
	private Round round;
	private List<Socket> socketPlayers;
	private int numJugadores, numRetake = 0;
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

	@SuppressWarnings("unchecked")
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
					if (descartes.getDescartes().size() > 5) {
						Carta last = descartes.getCarta();
						descartes.clear();
						descartes.take(last);
					}
					updateDescartes(false);
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
						updateDescartes(true);
						numRetake = i;
						setAndSendRetakes(numRetake);
						updateDescartes(true);
						end = inS.readBoolean();
						updateEnd(end, outS);
						if (end) break;
					}
				} catch (IOException | ClassNotFoundException e) { Gamestate.state = Gamestate.QUIT; }
			}
			endRound = end;
		}
		countPoints();
		try { Thread.sleep(3000); } catch (InterruptedException e) {}
		round.updateRound();
		descartes = new Descartes();
		baraja = new Baraja();
		bajadas = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	private void setAndSendRetakes(int numR) {
		ArrayList<Boolean> retakes = new ArrayList<>();
		Map<Boolean, Integer> prioridades = new HashMap<>();
		for (ObjectInputStream inS : in) {
			try {
				boolean temp = inS.readBoolean();
				retakes.add(temp);
			}
			catch (IOException e) {}
		}
		for (int i = 0; i < numJugadores; i++) {
	        int diff = (i - numR + numJugadores) % numJugadores;
	        prioridades.put(retakes.get(i), diff == 0 ? 0 : numJugadores - diff);
	    }
		int highest = -1;
		for (int i = 0; i < numJugadores; i++) {
			if (retakes.get(i) && prioridades.get(retakes.get(i)) > highest) highest = i;
		}
		for (int i = 0; i < numJugadores; i++) {
			ObjectOutputStream outS = out.get(i);
			try {
				outS.writeBoolean(highest == i);
				outS.flush();
			}
			catch (IOException e) {}
		}
		if (highest != -1) {
			sendCard(descartes.getCarta(), out.get(highest));
			descartes.remove();
			try {
				ArrayList<Carta> aux = (ArrayList<Carta>) in.get(highest).readObject();
				descartes.setDescartes(aux);
			}
			catch (ClassNotFoundException | IOException e) {}
		}
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

	private void updateDescartes(boolean vaADescartar) {
		for (ObjectOutputStream outS : out) {
			try {
				outS.writeBoolean(vaADescartar);
				outS.flush();
				outS.writeObject(descartes.getDescartes());
				outS.flush();
			} catch (IOException e) {
				Gamestate.state = Gamestate.QUIT;
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

//	private List<Carta> makePersonalizedCards() {
//		List<Carta> res = new ArrayList<>();
//		res.add(new Carta(9,1,0,0));
//		res.add(new Carta(9,2,0,0));
//		res.add(new Carta(9,3,0,0));
//		res.add(new Carta(9,4,0,0));
//		res.add(new Carta(10,1,0,0));
//		res.add(new Carta(10,2,0,0));
//		res.add(new Carta(10,3,0,0));
//		res.add(new Carta(10,4,0,0));
//		res.add(new Carta(10,4,0,0));
//		
//		return res;
//	}

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
		setWinner();
		//Decimos lo del winner y tal
	}

	private void setWinner() {
		int lowest = 0;
		for (int i = 0; i < numJugadores; i++) {
			if (pointList.get(i) < pointList.get(lowest)) lowest = i;
		}
		for (int i = 0; i < numJugadores; i++) {
			try {
				out.get(i).writeBoolean(lowest == i);
				out.get(i).flush();
			} catch (IOException e) {}
		}
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

	public void closeGates() {
		try {
			for (int i = 0; i < numJugadores; i++) {
				out.get(i).close();
				in.get(i).close();
				socketPlayers.get(i).close();
			}
		} catch (IOException e) {}
	}
}
