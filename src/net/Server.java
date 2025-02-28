package net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import gameDynamics.Partida;
import gameDynamics.Player;

public class Server implements Runnable {
	
	protected ServerSocket server;
	protected Partida partida;
	protected int numPlayers;
	protected List<Client> listaPlayers;

	public Server(int np) {
		numPlayers = np;
		listaPlayers = new ArrayList<>();
		try {
			this.server = new ServerSocket(6020);
			System.out.println("Servidor iniciado");
			server.setReuseAddress(true);
			Socket s = new Socket("127.0.0.1", 6020);
			server.accept();
			Client clienteHost = new Client(s, 0);
			listaPlayers.add(clienteHost);
			System.out.println("Servidor conectado como jugador");
			listaPlayers.get(0).start();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		int i = 1;
		while (i < numPlayers) {
			try {
				System.out.println("cliente " + (i + 1) + " esperando conexión...");
				Socket s = server.accept();
				System.out.println("cliente " + (i + 1) + " conectado");
				Client cliente = new Client(s, i);
				listaPlayers.add(cliente);
				listaPlayers.get(listaPlayers.size() - 1).start();
				DataOutputStream out = new DataOutputStream(s.getOutputStream());
				out.writeInt(i);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		}
		System.out.println("empieza la partida");
		partida = new Partida(listaPlayers);
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void processPacket() {
       
    }

    // server to all client
    private void sendUpdatesToAll() {
    	
    }

    // server to one client
    public void sendUpdates() {
    	
    }

    public void closeServer() {
        try {
            server.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return server;
    }
}
