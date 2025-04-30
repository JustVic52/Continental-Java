package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import cardTreatment.Bajada;
import cardTreatment.Carta;
import gameDynamics.Player;
import gamestates.Gamestate;

public class Client extends Thread {

	private Player player;
	private final Socket socket;
	private String nombre = "";
	private boolean endRound = false, end = false, roundOver = false, starting = false, gameOver = false;
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
			starting = true;
			//lógica del juego
			for (int i = 0; i < 10; i++) {
				//Recibir cartas
				recieveMano();
				//jugar ronda
				runGame();
			}			
			boolean winner = in.readBoolean();
			player.setGameWinner(winner);
			gameOver = true;
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	private void runGame() {
		roundOver = false;
		int action = 1;
		boolean vaADescartar = false;
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
				player.contRetake();
				if (yourTurn) {
					//fase 1: robar
					boolean baraja = false, descartes = false;
					while (!baraja && !descartes && !player.isTaken()) {
						baraja = player.isBaraja();
						descartes = player.isDescartes();
					}
					if (player.isTaken()) { action = 0; }
					if (player.isBaraja()) { action = 1; }
					if (player.isDescartes()) { action = 2; }
					out.writeInt(action);
					out.flush();
					if (action != 0) recieveCard();
					player.setBaraja(false);
					player.setDescartes(false);
					out.writeObject(player.getFullMano().getDescartes().getDescartes());
					out.flush();
					vaADescartar = in.readBoolean();
					player.setVaADescartar(vaADescartar);
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
						vaADescartar = in.readBoolean();
						player.setVaADescartar(vaADescartar);
						player.setContDes(1);
						aux = (ArrayList<Carta>) in.readObject();
						player.setFullDescartes(aux);
						player.setCiclo(1);
						if (player.getNumJugadores() != 1) try { Thread.sleep(3000); } catch (InterruptedException e) {}
						player.setCiclo(0);
						out.writeBoolean(player.isRetake());
						out.flush();
						boolean tempRetake = in.readBoolean();
						if (tempRetake) {
							Carta c = (Carta) in.readObject();
							player.take(c);
							player.setTaken(true);
							player.getFullMano().getDescartes().remove();
							out.writeObject(player.getFullMano().getDescartes().getDescartes());
							out.flush();
						} else { player.setTaken(false); }
						vaADescartar = in.readBoolean();
						player.setVaADescartar(vaADescartar);
						aux = (ArrayList<Carta>) in.readObject();
						player.setFullDescartes(aux);
						end = player.getFullMano().isEmpty();
						out.writeBoolean(end);
						out.flush();
					}
				} else {
					vaADescartar = in.readBoolean();
					player.setVaADescartar(vaADescartar);
					aux = (ArrayList<Carta>) in.readObject();
					player.setFullDescartes(aux);
					boolean temp = in.readBoolean();
					if (temp) {
						end = in.readBoolean();
					} else {
						aux2 = (ArrayList<Bajada>) in.readObject();
						player.setListBajada(aux2);
						vaADescartar = in.readBoolean();
						player.setVaADescartar(vaADescartar);
						player.setContDes(1);
						aux = (ArrayList<Carta>) in.readObject();
						player.setFullDescartes(aux);
						try { Thread.sleep(3000); } catch (InterruptedException e) {}
						boolean tempRetake = player.isRetake();
						out.writeBoolean(tempRetake);
						out.flush();
						tempRetake = in.readBoolean();
						if (tempRetake) {
							Carta c = (Carta) in.readObject();
							player.take(c);
							player.setTaken(true);
							player.getFullMano().getDescartes().remove();
							out.writeObject(player.getFullMano().getDescartes().getDescartes());
							out.flush();
						} else { player.setTaken(false); }
						vaADescartar = in.readBoolean();
						player.setVaADescartar(vaADescartar);
						aux = (ArrayList<Carta>) in.readObject();
						player.setFullDescartes(aux);
						end = in.readBoolean();
					}
				}
				yourTurn = false;
				endRound = end;
			}
		} catch (IOException e) {
			Gamestate.state = Gamestate.QUIT;
		} catch (ClassNotFoundException e) {
			Gamestate.state = Gamestate.QUIT;
		}
		player.setRoundWinner(player.getFullMano().isEmpty());
		exchangePoints();
		roundOver = true;
		try { Thread.sleep(3000); } catch (InterruptedException e) {}
		player.update();
	}

	private void recievePlayers() {
		try {
			int numJugadores = in.readInt();
			ArrayList<Integer> aux = new ArrayList<>();
			for (int i = 0; i < numJugadores; i++) {
				aux.add(0);
			}
			player.setNumJugadores(numJugadores);
			player.setPointList(aux);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void exchangePoints() {
		ArrayList<Integer> aux = new ArrayList<>();
		try {
			player.countPoints();
			out.writeInt(player.getPoints());
			out.flush();
			aux = (ArrayList<Integer>) in.readObject();
			player.setPointList(aux);
		} catch (IOException | ClassNotFoundException e) { Gamestate.state = Gamestate.QUIT; }
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

	@SuppressWarnings("unchecked")
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

	public boolean getStarting() {
		return starting;
	}

	public void closeClient() {
		try {
			out.close();
			in.close();
			socket.close();
		}
		catch (IOException e) {}
	}

	public boolean isGameOver() {
		return gameOver;
	}
}
