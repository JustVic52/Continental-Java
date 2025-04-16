package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import cardTreatment.Bajada;
import cardTreatment.Carta;
import gameDynamics.Player;

public class Client extends Thread {

	private Player player;
	private final Socket socket;
	private String nombre = "";
	private boolean endRound = false;
	private boolean yourTurn = false;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	public Client(Socket s, String name) {
		socket = s;
		nombre = name;
	}

	@Override
	public void run() {
		try {
			socket.setKeepAlive(true);
			//creación y obtención del player.
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			player = new Player(in.readInt());
			
			//lógica del juego
			for (int i = 0; i < 10; i++) {
				//Recibir cartas
				recieveMano();
				//jugar ronda
				runGame();
			}			
			
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void runGame() {
		int action = 0;
		ArrayList<Carta> aux = new ArrayList<>();
		ArrayList<Bajada> aux2 = new ArrayList<>();
		try {
			while (!endRound) {
				yourTurn = in.readBoolean();	
				player.setYourTurn(yourTurn);
				if (yourTurn) {
					//fase 1: robar
					boolean baraja = false, descartes = false;
					while (!baraja && !descartes) {
						baraja = player.isBaraja();
						descartes = player.isDescartes();
					}
					if (player.isBaraja()) { action = 1; }
					if (player.isDescartes()) { action = 2; }
					out.writeInt(action);
					out.flush();
					player.setBaraja(false);
					player.setDescartes(false);
					recieveCard();
					out.writeObject(player.getFullMano().getDescartes().getDescartes());
					out.flush();
					aux = (ArrayList<Carta>) in.readObject();
					player.setFullDescartes(aux);
					//fase 2: bajarse (opcional)
					boolean bajarse = false, descartado = false;
					while (!descartado && !bajarse) {
						bajarse = player.canBajarse();
						descartado = player.isDescartado();
					}
					out.writeBoolean(bajarse);
					out.flush();
					bajarse = in.readBoolean();
					if (bajarse) {
						boolean isBajado = false;
						while(!isBajado && !descartado) {
							isBajado = player.getBajada().isBajado();
							descartado = player.isDescartado();
						}
						out.writeObject(player.getListBajada());
						out.flush();
					}
					aux2 = (ArrayList<Bajada>) in.readObject();
					player.setListBajada(aux2);
					//fase 3: descartar
//					boolean descartado = false;
					while (!descartado) { descartado = player.isDescartado(); }
					out.writeObject(player.getFullMano().getDescartes().getDescartes());
					out.flush();
					aux = (ArrayList<Carta>) in.readObject();
					player.setFullDescartes(aux);
					out.writeBoolean(player.getMano().size() == 0);
					out.flush();
				} else {
					aux = (ArrayList<Carta>) in.readObject();
					player.setFullDescartes(aux);
					aux2 = (ArrayList<Bajada>) in.readObject();
					player.setListBajada(aux2);
					aux = (ArrayList<Carta>) in.readObject();
					player.setFullDescartes(aux);
				}
				yourTurn = false;
				endRound = player.getMano().size() == 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void recieveCard() {
		try {
			Carta carta = (Carta) in.readObject();
			player.getFullMano().take(carta);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void recieveMano() {
		try {
			player.setMano((ArrayList<Carta>) in.readObject());
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Player getPlayer() {
		return player;
	}

	public ObjectInputStream getInputStream() {
		return in;
	}

	public ObjectOutputStream getOutputStream() {
		return out;
	}
	
	public String getNombre() { return nombre; }
}
