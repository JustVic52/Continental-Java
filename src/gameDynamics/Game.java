package gameDynamics;

import java.util.List;

import gameTools.Descartes;
import java.util.Scanner;
import java.util.ArrayList;

public class Game {

	private Round round;
	private List<Player> jugadores;
	private int numJugadores;
	private Descartes descartes;
	private boolean endRound;
	private Scanner teclat;
	
	public Game(int nJ) {
		round = new Round();
		jugadores = new ArrayList<>();
		createPlayers(nJ);
		descartes = new Descartes();
		numJugadores = nJ;
		endRound = false;
		teclat = new Scanner(System.in);
	}
	
	private void createPlayers(int nJ) {
		for (int i = 0; i < nJ; i++) {
			jugadores.add(new Player(i + 1));
		}
	}

	public void run() {
		for (int i = 0; i < numJugadores; i++) {
			for (int j = 0; j < round.getNumCartas(); j++) {
				jugadores.get(i).give();
			}
		}
		printObjectives();
		int i = 0;
		while (!endRound) {
			//Sistema de turnos
			System.out.println("==============");
			System.out.println("  Jugador: " + jugadores.get(i).getTurno());
			System.out.println("==============\n");
			newTurn(jugadores.get(i)); //nuevo turno
			jugadores.get(i).roundWin(); //comprobación de si ha ganado
			if (jugadores.get(i).getRoundWinner()) {
				System.out.println("===============");
				System.out.println("  has ganado!");
				System.out.println("===============\n");
				jugadores.get(i).setCiclo(1);
				endRound = true; 
			} /*else {
				System.out.println("==================");
				System.out.println("  has perdido :(");
				System.out.println("==================\n");
			}*/
			i = (i + 1) % numJugadores;
		}
		
		System.out.println("- Ronda terminada -");
		countPoints();
		round.updateRound();
		descartes.clear();
		endRound = false;
		updatePlayers();
	}
	
	public void updateRun() { round.updateRound(); }
	
	public void printObjectives() {
		String border = "*".repeat(38);
		System.out.println(border);
		//Número de ronda
		System.out.println(String.format("*  %-33s *", "Ronda: " + round.getNumRound()));
		//Cartas repartidas
		System.out.println(String.format("*  %-33s *", "Cartas repartidas: " + round.getNumCartas()));
		//Cartas mínimas
		System.out.println(String.format("*  %-33s *", "Cartas mínimas para bajarse: " + round.getMinimum()));
		//Objetivos de la ronda
		String res = "Objetivo: ";
		int esc = round.getRoundEscaleras();
		int tri = round.getRoundTrios();
		if (esc > 0) {
			res += (esc == 1) ? "1 Escalera" : esc + " Escaleras";
			if (tri > 0) { res += " y "; }
			else { res += "."; }
		}
		if (tri > 0) {
			res += (tri == 1) ? "1 Trío." : tri + " Tríos.";
		}
		System.out.println(String.format("*  %-33s *", res));
		System.out.println(border + "\n");
	}

	private void updatePlayers() {
		for (int i = 0; i < jugadores.size(); i++) {
			jugadores.get(i).clearMano();
		}
	}

	private void newTurn(Player player) {
		int select = 0;
		System.out.println(player.getFullMano().toString());
		//un turno tiene 3 fases. 
		//1. Robar o de la pila de descartes o de la baraja
		System.out.println("1. Robar de la baraja");
		System.out.println("2. Robar de los descartes");
		System.out.print("\nSeleccione una opción: ");
		select = teclat.nextInt();
		System.out.println("");
	    switch (select) {
	    case 1:
	    	player.give();
	    	break;
	    case 2:
	    	if (!descartes.isEmpty()) {
	    		player.take(descartes.getCarta());
		    	descartes.remove();
	    	} else {
	    		System.out.println("No hay descartes, se robará de la baraja\n");
	    		player.give();
	    	}
	    	break;
	    default:
	    	System.out.println("número incorrecto");
	    	break;
	    }
	    System.out.println(player.getFullMano().toString());
		//2. Intentar Bajarte y/o jugar cartas
//	    System.out.println("Suponemos que juegas una carta");
//	    player.getMano().remove(0);
//	    System.out.println("1. Jugar Carta");
	    if (!player.getBajadaEscaleras().isEmpty() || !player.getBajadaTrios().isEmpty()) {
	    	System.out.println("Supongamos que juegas una carta\n");
	 	    player.getMano().remove(0);
	 	    System.out.println(player.getFullMano().toString());
	    }
	    if (player.getCiclo() > 1) {
	    	System.out.println("1. Jugar cartas");
	    	System.out.println("2. Bajarse");
	    	System.out.println("3. Pasar");
	    	System.out.print("\nSeleccione una opción: ");
	    	select = teclat.nextInt();
	    	System.out.println("");
	    	switch (select) {
	    	case 1:
	    		System.out.println("WORK IN PROGRESS");
	    		break;
	    	case 2:
	    		boolean endSelection = false;
	    		while (!endSelection) {
	    			System.out.println(player.getFullMano().toString());
		    		System.out.println("1. Seleccionar cartas");
			    	System.out.println("2. Eliminar carta de la seleción");
			    	System.out.println("3. Bajarse");
			    	System.out.println("4. Salir");
			    	System.out.print("\nSeleccione una opción: ");
		    		select = teclat.nextInt();
		    		System.out.println("");
		    		switch (select) {
				    case 1:
				 	    boolean done = false;
				 	    while (!done) {
				 	    	System.out.print("Seleccione una carta: ");
				 	    	select = teclat.nextInt();
				 	    	if (select == 0) {
				 	    		System.out.println("Fin de la selección");
				 	    		done = true;
				 	    	}
				 	    	else {
				 	    		if (!player.getMano().get(select - 1).isSeleccionada()) {
						 	    	player.select(select - 1);
						 	    	System.out.println(player.getFullMano().toString());
						 	    } else { System.out.println("Carta ya seleccionada, seleccione otra\n"); }
				 	    	}
				 	    }
				    	break;
				    case 2:
				 	    System.out.println("selecciona una carta: ");
				 	    select = teclat.nextInt();
				 	    player.deselect(select - 1);
				    	break;
				    case 3:
				    	if (player.canBajarse(player.getRoundTrios(), player.getRoundEscaleras(), player.getSelection())) {
				    		player.bajarse();
				    		System.out.println(player.getFullMano().toString());
				    		endSelection = true;
				    	} else {
				    		for (int i = 0; i < player.getSelection().size(); i++) {
				    			player.getSelection().get(i).setSeleccionada(false);
				    		}
				    		player.getSelection().clear();
				    		System.out.println("No te puedes bajar aún \n");
				    		endSelection = true;
				    	}
				    	break;
				    default:
				    	player.getSelection().clear();
				    	System.out.println("No juegas cartas este turno\n");
				    	endSelection = true;
		    		}
			    }
	    		break;
	    	case 3:
		    	break;
	    	default:
		    	System.out.println("número incorrecto");
		    	break;
	    	}
    	} else { System.out.println("No puedes jugar cartas porque es tu primera jugada\n"); }
		//3. Descartar carta
	    if (player.getCiclo() > 1 && player.getBajadaEscaleras().isEmpty() && player.getBajadaTrios().isEmpty()) {
	    	System.out.println("No juegas cartas este turno\n");
	    }
	    System.out.print("Selecciona una carta para descartar: \n");
	    select = teclat.nextInt();
	    player.discard(player.getMano().get(select - 1));
	    descartes.take(player.getUltimaCarta());
	    System.out.println(player.getFullMano().toString());
	    System.out.println(descartes.toString());
	    player.updateCiclo();	    
	}

	public void playGame() {
		for (int i = 0; i < 10; i++) {
			run();
		}
		setGameWinner();
	}

	private void setGameWinner() {
		int lowest = 0;
		int pl = 0;
		for (int i = 0; i < numJugadores; i++) {
			if (jugadores.get(i).getPoints() < lowest) {
				lowest = jugadores.get(i).getPoints();
				pl = i;
			}
			jugadores.get(i).setPoints(0);
		}
		jugadores.get(pl).setGameWinner(true);
	}

	private void countPoints() {
		for (int i = 0; i < numJugadores; i++) {
			if (jugadores.get(i).getRoundWinner()) {
				jugadores.get(i).setPoints(jugadores.get(i).getPoints() + -10 * round.getNumRound());
				jugadores.get(i).setRoundWinner(false);
			} else {
				jugadores.get(i).count();
			}
			System.out.println("Jugador: " + jugadores.get(i).getTurno() + " -> Puntuación: " + jugadores.get(i).getPoints() + ".");
		}
		System.out.println("");
	}
}
