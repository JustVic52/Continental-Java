package mainGame;

import gameDynamics.Game;
import gameDynamics.Player;
import gameTools.Carta;
import gameDynamics.Combinaciones;
import java.util.Scanner;

public class Main {
	
	private static Player player;
	private static Game game;
	
	private Main() { }
	
	public static void main(String[] args) {
		
		game = new Game(1);
		player = game.getJugadores().get(0);
		
//		game.playGame();	
		
		//Carta 1
		Carta carta1 = new Carta(4, 1);
		player.getMano().add(carta1);
		player.select(0);
		
		//Carta 2
		Carta carta2 = new Carta(2, 1);
		player.getMano().add(carta2);
		player.select(1);
		
		// Carta 3
		Carta carta3 = new Carta();
		player.getMano().add(carta3);
		player.select(2);
		
		//Carta 4
		Carta carta4 = new Carta(3, 1);
		player.getMano().add(carta4);
		player.select(3);
		
		//Carta 5
		Carta carta5 = new Carta(5, 1);
		player.getMano().add(carta5);
//		player.select(4);
//		
//		//Carta 6
//		Carta carta6 = new Carta();
//		player.getMano().add(carta6);
//		player.select(5);
//		
//		//Carta 7
//		Carta carta7 = new Carta(2, 1);
//		player.getMano().add(carta7);
//		player.select(6);
//		
//		//Carta 8
//		Carta carta8 = new Carta(2, 2);
//		player.getMano().add(carta8);
//		player.select(7);
//		
//		//Carta 9
//		Carta carta9 = new Carta();
//		player.getMano().add(carta9);
//		player.select(8);
//		
//		//Carta 10
//		Carta carta10 = new Carta(4, 1);
//		player.getMano().add(carta10);
//		player.select(9);
//		
//		//Carta 11
//		Carta carta11 = new Carta(6, 4);
//		player.getMano().add(carta11);
//		player.select(10);
//		
//		//Carta 12
//		Carta carta12 = new Carta();
//		player.getMano().add(carta12);
//		player.select(11);
//		
//		//Carta 13
//		Carta carta13 = new Carta(4, 4);
//		player.getMano().add(carta13);
//		player.select(12);
//
//		
//		//Carta 14
//		Carta carta14 = new Carta();
//		player.getMano().add(carta14);
//		player.select(13);
		
		player.setCiclo(2);
		game.newTurn(player);
	}
}
