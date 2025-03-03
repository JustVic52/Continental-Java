package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
					player.setYourTurn(yourTurn);
				}
				//fase 1: robar
				boolean baraja = false, descartes = false;
				while (!baraja && !descartes) {
					baraja = player.isBaraja();
					descartes = player.isDescartes();
				}
				if (player.isBaraja()) { action = 1; }
				if (player.isDescartes()) { action = 2; }
				System.out.println(action);
				out.writeInt(action);
				out.flush();
				player.setBaraja(false);
				player.setDescartes(false);
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
			Carta carta = (Carta) in.readObject();
			player.getFullMano().take(carta);
			System.out.println("Mi carta es: " + carta);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void recieveMano() {
		try {
//			int num = -1;
//			while (num == -1) {
//				num = in.readInt();
//			}
			ArrayList<Carta> aux = (ArrayList<Carta>) in.readObject();
			player.setMano(aux);
			System.out.println(player.getFullMano().toString());
//			for (int i = 0; i < num; i++) {
//				player.give();
//			}
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
