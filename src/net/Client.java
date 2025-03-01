package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
		try {
			while (!endRound) {
				while (!yourTurn) {
					yourTurn = in.readBoolean();
				}
				//fase 1: robar
				while (!player.isBaraja() || !player.isDescartes()) { }
				if (player.isBaraja()) { action = 1; }
				if (player.isDescartes()) { action = 2; }
				player.setBaraja(false);
				player.setDescartes(false);
				out.writeInt(action);
				out.flush();
				recieveCard();
				//fase 2: bajarse (opcional)
				
				//fase 3: descartar
				while (!player.isDescartado()) { }
				out.writeObject(player.getUltimaCarta());
				out.flush();
				if (player.getMano().size() == 0) {
					out.writeBoolean(true);
				} else {
					out.writeBoolean(false);
				}
				out.flush();
				yourTurn = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void recieveCard() {
		try {
			player.getFullMano().take((Carta) in.readObject());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void recieveMano() {
		try {
			int num = in.readInt();
			for (int i = 0; i < num; i++) {
				player.give(null);
			}
//			ArrayList<Carta> aux = null;
//			Object o = in.readObject();
//			if (o instanceof ArrayList<?>) { 
//				player.setMano((ArrayList<Carta>) o);
//			}
			System.out.println(player.getFullMano().toString());
		} 
//			catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
			catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
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
