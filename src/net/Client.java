package net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import gameDynamics.Player;

public class Client extends Thread {

	private Player player;
	private final Socket socket;
	private String nombre = "";
	
	public Client(Socket s, int i) {
		socket = s;
		player = new Player(i);
	}

	@Override
	public void run() {
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			int num = in.readInt();
			player.setTurno(num);
			System.out.println(num);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Player getPlayer() {
		return player;
	}
	
}
