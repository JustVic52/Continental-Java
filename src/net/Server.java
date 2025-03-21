package net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import gameDynamics.Partida;
import gamestates.Gamestate;

public class Server extends Thread {
	
	private ServerSocket server;
	private Partida partida;
	private Client client;
	private int numPlayers;
	private String nombre = "";
	private List<Socket> listaPlayers;

	public Server(ServerSocket ss, int np, String name) {
		numPlayers = np;
		nombre = name;
		listaPlayers = new ArrayList<>();
		server = ss;
		try {
			server.setReuseAddress(true);
		} catch (SocketException e) {
			
		}
	}
	
	@Override
	public void run() {
		ArrayList<ObjectOutputStream> listaOut = new ArrayList<>();
		Socket s;
		try {
			Socket sC = new Socket(InetAddress.getLocalHost(), 6020);
			s = server.accept();
			listaPlayers.add(s);
			client = new Client(sC, nombre);
			//Mención especial a mi novia por ver en directo cómo modificaba esto y funcionaba.
			client.start();
			listaOut.add(new ObjectOutputStream(s.getOutputStream()));
			listaOut.get(0).writeInt(0);
			listaOut.get(0).flush();
			if (numPlayers > 1) {
				int i = 1;
				while (i < numPlayers) {
					s = server.accept();
					listaPlayers.add(s);
					listaOut.add(new ObjectOutputStream(s.getOutputStream()));
					listaOut.get(i).writeInt(i);
					listaOut.get(i).flush();
					i++;
				}
			}
		} catch (UnknownHostException e) {
			closeServer();
		} catch (IOException e) {
			closeServer();
		}
		partida = new Partida(listaPlayers, listaOut);
		partida.playGame();
		closeServer();
	}

    public void closeServer() {
        try {
            server.close();
        } catch (final IOException e) {
        	
        }
    }

    public ServerSocket getServerSocket() {
        return server;
    }
    
    public Partida getPartida() { return partida; }
    
    public Client getClient() { return client; }
}
