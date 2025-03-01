package gameDynamics;

import java.util.List;
import java.util.Scanner;

import cardTreatment.Baraja;
import cardTreatment.Carta;
import cardTreatment.Descartes;
import cardTreatment.Mano;
import cardTreatment.Slot;
import gamestates.Gamestate;
import net.Client;
import utilz.Pair;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Partida implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private Round round;
	private List<Socket> socketPlayers;
	private int numJugadores;
	private Descartes descartes;
	private boolean endRound, taken;
	private Player playerLadron;
	private Baraja baraja;
	List<ObjectOutputStream> out;
	List <ObjectInputStream> in;
	
	public Partida(List<Socket> sp, ArrayList<ObjectOutputStream> listaOut, Client client) {
		round = new Round();
		socketPlayers = sp;
		baraja = new Baraja();
		descartes = new Descartes();
		numJugadores = sp.size();
		endRound = false;
		crearOutIn(listaOut, client);
	}
	
	private void crearOutIn(ArrayList<ObjectOutputStream> listaOut, Client client) {
		out = listaOut;
		in = new ArrayList<>();
		for (int i = 0; i < numJugadores; i++) {
			try {
				if (i == 0) {
					in.add(client.getInputStream());
				} else {
					in.add(new ObjectInputStream(socketPlayers.get(i).getInputStream()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Descartes getDescartes() { return descartes; }

	public void run() {
		while (!endRound) {
			for (int i = 0; i < socketPlayers.size(); i++) {
				Socket s = socketPlayers.get(i);
				ObjectOutputStream outS = out.get(i);
				ObjectInputStream inS = in.get(i);
				try {
					s.setKeepAlive(true);
					//decir que es tu turno
					sendTurns(s);
					//fase 1: robar
					int action = -1;
					while (action == -1) {
						action = inS.readInt();
					}
					if (action == 1) {
						sendCard(baraja.give(), outS);
					}
					if (action == 2) {
						sendCard(descartes.getCarta(), outS);
					}
					//fase 2: bajarse (opcional)
					
					//fase 3: descartar
					descartes.take((Carta) inS.readObject());
					
					endRound = inS.readBoolean();
				} catch (IOException e) {
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		countPoints();
		round.updateRound();
		descartes.clear();
		endRound = false;
	}
	
	private void sendTurns(Socket actual) {
		for (int i = 0; i < socketPlayers.size(); i++) {
			Socket s = socketPlayers.get(i);
			ObjectOutputStream outS = out.get(i);
			try {
				if (s != actual) {
					outS.writeBoolean(false);
				} else {
					outS.writeBoolean(true);
				}
				outS.flush();
			} catch (IOException e) {
				e.printStackTrace();
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
		for (ObjectOutputStream out : out) {
//			for (int j = 0; j < round.getNumCartas(); j++) {
//				cartas.add(baraja.give());
//			}
			try {
				out.writeInt(round.getNumCartas());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			cartas.clear();
		}
	}

	public void updateRun() { round.updateRound(); }

	public void playGame() {
		for (int i = 0; i < 10; i++) {
			giveCards();
			run();
		}
		setGameWinner();
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
