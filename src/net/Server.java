package net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import gameDynamics.Partida;

public class Server extends Thread {
	
	private ServerSocket server;
	private Partida partida;
	private Client client;
	private int numPlayers;
	private String nombre = "";
	private List<Socket> listaPlayers;
	private boolean starting = false;

	public Server(ServerSocket ss, int np, String name) {
		numPlayers = np;
		nombre = name;
		listaPlayers = new ArrayList<>();
		server = ss;
		try {
			server.setReuseAddress(true);
		} catch (SocketException e) {}
	}
	
	@Override
	public void run() {
		ArrayList<ObjectOutputStream> listaOut = new ArrayList<>();
		Socket s;
		try {
			Socket sC = new Socket(InetAddress.getLocalHost(), 6020);
			sC.setKeepAlive(true);
			s = server.accept();
			listaPlayers.add(s);
			client = new Client(sC, nombre);
			//Mención especial a mi novia por ver en directo cómo modificaba esto y funcionaba.
			client.setName("client");
			client.start();
			listaOut.add(new ObjectOutputStream(s.getOutputStream()));
			listaOut.get(0).flush();
			listaOut.get(0).writeInt(0);
			listaOut.get(0).flush();
			if (numPlayers > 1) {
				int i = 1;
				while (i < numPlayers) {
					s = server.accept();
					listaPlayers.add(s);
					listaOut.add(new ObjectOutputStream(s.getOutputStream()));
					listaOut.get(i).flush();
					listaOut.get(i).writeInt(i);
					listaOut.get(i).flush();
					i++;
				}
			}
			starting = true;
		} catch (IOException e) {}
		partida = new Partida(listaPlayers, listaOut);
		partida.playGame();
		closeServer();
	}

	public void closeServer() {
    	try {
    		partida.closeGates();
			server.close();
			client.closeClient();
			client.interrupt();
		}
    	catch (IOException e) {}
	}

	public ServerSocket getServerSocket() {
        return server;
    }
    
    public Partida getPartida() { return partida; }
    
    public Client getClient() { return client; }

	public boolean getStarting() {
		return starting;
	}
}
