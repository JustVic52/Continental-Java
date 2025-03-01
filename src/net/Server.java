package net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import gameDynamics.Partida;
import gameDynamics.Player;
import gamestates.Gamestate;

public class Server implements Runnable {
	
	private ServerSocket server;
	private Partida partida;
	private Client client;
	private int numPlayers;
	private List<Socket> listaPlayers;

	public Server(int np) {
		numPlayers = np;
		listaPlayers = new ArrayList<>();
		try {
			this.server = new ServerSocket(6020);
			System.out.println("Servidor iniciado");
			server.setReuseAddress(true);
		} catch (final IOException e) {
			
		}
	}
	
	@Override
	public void run() {
		ArrayList<ObjectOutputStream> listaOut = new ArrayList<>();
		Socket s;
		try {
			s = new Socket(InetAddress.getLocalHost(), 6020);
			Socket s2 = server.accept();
			listaPlayers.add(s2);
			client = new Client(s2);
			client.start();
			listaOut.add(new ObjectOutputStream(s.getOutputStream()));
			listaOut.get(0).writeInt(0);
			listaOut.get(0).flush();
			if (numPlayers > 1) {
				int i = 1;
				while (i < numPlayers) {
					System.out.println("cliente " + (i + 1) + " esperando conexión...");
					s = server.accept();
					System.out.println("cliente " + (i + 1) + " conectado");
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
		System.out.println("empieza la partida");
		partida = new Partida(listaPlayers, listaOut, client);
		System.out.println(Gamestate.state);
		partida.playGame();
		closeServer();
	}

    public void closeServer() {
        try {
            server.close();
        } catch (final IOException e) {
        	
        }
        Gamestate.state = Gamestate.HOST;
    }

    public ServerSocket getServerSocket() {
        return server;
    }
    
    public Partida getPartida() { return partida; }
    
    public Client getClient() { return client; }
}
