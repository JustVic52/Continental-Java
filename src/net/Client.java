package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cardTreatment.Bajada;
import cardTreatment.Carta;
import gameDynamics.Player;

public class Client extends Thread {

	private Player player;
	private final Socket socket;
	private String nombre = "";
	private boolean endRound = false, end = false, roundOver = false;
	private boolean yourTurn = false;
	private int numRound = 1;
	ObjectOutputStream out;
	ObjectInputStream in;
	
	public Client(Socket s, String name) {
		socket = s;
		nombre = name;
	}

	@Override
	public void run() {
		try {
			//creación y obtención del player.
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			player = new Player(in.readInt());
			//número de jugadores
			recievePlayers();
			//Nombres de los jugadores
			recieveNames();
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
	
	private void recieveNames() {
		ArrayList<String> aux = new ArrayList<>();
		try {
			out.writeObject(nombre);
			out.flush();
			aux = (ArrayList<String>) in.readObject();
			player.setNameList(aux);
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
	}

	private void recieveRound() {
		try {
			numRound = in.readInt();
			player.setNumRound(numRound);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void runGame() {
		roundOver = false;
		int action = 1;
		ArrayList<Carta> aux = new ArrayList<>();
		ArrayList<Bajada> aux2 = new ArrayList<>();
		ArrayList<Boolean> aux3 = new ArrayList<>();
		endRound = false;
		try { aux2 = (ArrayList<Bajada>) in.readObject(); } catch (ClassNotFoundException | IOException e) {}
		player.setListBajada(aux2);
		recieveRound();
		try {
			while (!endRound) {
				end = false;
				aux3 = (ArrayList<Boolean>) in.readObject();
				player.setTurns(aux3);
				yourTurn = player.isYourTurn();
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
					boolean bajarse = false, descartado = false, isBajado = false;
					while (!descartado && !bajarse) {
						bajarse = player.canBajarse();
						descartado = player.isDescartado();
					}
					out.writeBoolean(bajarse);
					out.flush();
					bajarse = in.readBoolean();
					boolean temp = false;
					if (bajarse) {
						while(!isBajado && !descartado) {
							isBajado = player.getBajada().isBajado();
							descartado = player.isDescartado();
						}
					} else {
						isBajado = player.getBajada() != null && player.getBajada().isBajado();
						out.writeBoolean(isBajado);
						out.flush();
						isBajado = in.readBoolean();
						if (isBajado) {
							while(!descartado && !temp) {
								descartado = player.isDescartado();
								temp = player.getFullMano().isEmpty();
							}
						}
					}
					temp = player.getFullMano().isEmpty();
					out.writeBoolean(temp);
					out.flush();
					if (temp) {
						end = true;
						out.writeBoolean(true);
						out.flush();
					} else {
						out.writeObject(player.getListBajada());
						out.flush();
						aux2 = (ArrayList<Bajada>) in.readObject();
						player.setListBajada(aux2);
						//fase 3: descartar
						while (!descartado) { descartado = player.isDescartado(); }
						out.writeObject(player.getFullMano().getDescartes().getDescartes());
						out.flush();
						aux = (ArrayList<Carta>) in.readObject();
						player.setFullDescartes(aux);
						end = player.getFullMano().isEmpty();
						out.writeBoolean(end);
						out.flush();
					}
				} else {
					aux = (ArrayList<Carta>) in.readObject();
					player.setFullDescartes(aux);
					boolean temp = in.readBoolean();
					if (temp) {
						end = in.readBoolean();
					} else {
						aux2 = (ArrayList<Bajada>) in.readObject();
						player.setListBajada(aux2);
						aux = (ArrayList<Carta>) in.readObject();
						player.setFullDescartes(aux);
						end = in.readBoolean();
					}
				}
				yourTurn = false;
				endRound = end;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		player.setRoundWinner(player.getFullMano().isEmpty());
		exchangePoints();
		roundOver = true;
		try { Thread.sleep(5000); } catch (InterruptedException e) {}
		player.update();
	}

	private void recievePlayers() {
		try {
			int numJugadores = in.readInt();
			ArrayList<Integer> aux = new ArrayList<>();
			for (int i = 0; i < numJugadores; i++) {
				aux.add(0);
			}
			player.setPointList(aux);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void exchangePoints() {
		ArrayList<Integer> aux = new ArrayList<>();
		try {
			player.countPoints();
			out.writeInt(player.getPoints());
			out.flush();
			aux = (ArrayList<Integer>) in.readObject();
			player.setPointList(aux);
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
	}

	private void recieveCard() {
		try {
			Carta carta = (Carta) in.readObject();
			System.out.println(carta);
			player.getFullMano().take(carta);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void recieveMano() {
		try {
			ArrayList<Carta> aux = (ArrayList<Carta>) in.readObject();
			player.setMano(aux);
		} 
		catch (ClassNotFoundException e) {}
		catch (IOException e) {}		
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
	
	public boolean isRoundOver() { return roundOver; }
}
