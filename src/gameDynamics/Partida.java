package gameDynamics;

import java.util.List;
import java.util.Scanner;

import cardTreatment.Baraja;
import cardTreatment.Carta;
import cardTreatment.Descartes;
import ui.ResguardoOverlay;

import java.awt.Graphics;
import java.util.ArrayList;

public class Partida {

	private Round round;
	private List<Player> jugadores;
	private int numJugadores;
	private Descartes descartes;
	private boolean endRound, taken;
	private Scanner teclat;
	private Player playerLadron;
	public static Baraja baraja;
	
	public Partida(int nJ) {
		round = new Round();
		jugadores = new ArrayList<>();
		baraja = new Baraja();
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
	
	public List<Player> getJugadores() {
		return jugadores;
	}
	
	public Descartes getDescartes() { return descartes; }
	
	public void render(Graphics g, ResguardoOverlay resguardo) {
		baraja.render(g);
		descartes.render(g);
		resguardo.draw(g);
		drawPlayers(g);
	}

	private void drawPlayers(Graphics g) {
		for (Player p : jugadores) {
			p.render(g);
		}
	}

	public void run() {
		for (int i = 0; i < numJugadores; i++) {
			for (int j = 0; j < round.getNumCartas(); j++) {
				jugadores.get(i).give();
			}
		}
		printObjectives();
		while (!endRound) {
			//Sistema de turnos
			for (int i = 0; i < jugadores.size(); i++) {
				System.out.println("==============");
				System.out.println("  Jugador: " + jugadores.get(i).getTurno());
				System.out.println("==============\n");
				newTurn(jugadores.get(i)); //nuevo turno
				jugadores.get(i).roundWin(); //comprobación de si ha ganado
				if (jugadores.get(i).getRoundWinner()) {
					for (Player p : jugadores) {
						if (p.getRoundWinner()) {
							System.out.println("==========================");
							System.out.println("  Jugador " + p.getTurno() + ", has ganado!");
							System.out.println("==========================\n");
						} else {
							System.out.println("=============================");
							System.out.println("  Jugador " + p.getTurno() + ", has perdido :(");
							System.out.println("=============================\n");
						}
						p.setCiclo(1);
					}
					endRound = true;
				}
			}
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
		String res = round.sacarObjetivos();
		System.out.println(String.format("*  %-33s *", res));
		System.out.println(border + "\n");
	}

	private void updatePlayers() {
		for (int i = 0; i < jugadores.size(); i++) {
			jugadores.get(i).clearMano();
		}
	}

	public void newTurn(Player player) {
		int select = 0;
		int numTrios = round.getRoundTrios();
		int numEscaleras = round.getRoundEscaleras();
		System.out.println(player.getFullMano().toString());		
		//un turno tiene 3 fases. 
		//1. Robar o de la pila de descartes o de la baraja
		if (playerLadron != null && player.equals(playerLadron)) {
    		System.out.println("No puedes robar este turno porque has preseleccionado el último descarte\n");
    	} else {
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
    	    	if (!player.equals(jugadores.get(0))) {
    	    		if (playerLadron != null || jugadores.get(player.getTurno() - 2).getFullMano().getRetake()) {
        	    		System.out.println("Ya se ha adjudicado el descarte, por lo que se robará de la baraja\n");
        	    		player.give();
        	    		break;
        	    	}
    	    	} else {
    	    		if (playerLadron != null || jugadores.get(jugadores.size() - 1).getFullMano().getRetake()) {
        	    		System.out.println("Ya se ha adjudicado el descarte, por lo que se robará de la baraja\n");
        	    		player.give();
        	    		break;
        	    	}
    	    	}
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
    	}
		for (Player p : jugadores) {
			if (p.getFullMano().getRetake()) {
				if (!taken) {
					p.take(descartes.getCarta());
    		    	descartes.remove();
				}
				p.getFullMano().setRetake(false);
			} else {
				if (playerLadron != null && p.equals(playerLadron)) {
					p.take(descartes.getCarta());
    		    	descartes.remove();
    		    	playerLadron = null;
				}
			}
		}
		//2. Intentar Bajarte y/o jugar cartas
	    boolean endPlay = false;
    	while (!endPlay) {
    		System.out.println(player.getFullMano().toString());
    		System.out.println("1. Jugar cartas");
	    	System.out.println("2. Manejar cartas");
	    	System.out.println("3. Pasar");
	    	System.out.print("\nSeleccione una opción: ");
	    	select = teclat.nextInt();
	    	System.out.println("");
	    	switch (select) {
	    	case 1:
	    		if (player.getCiclo() > 1 && (!player.getBajadaTrios().isEmpty() || !player.getBajadaEscaleras().isEmpty())) {
	    			System.out.print("Seleccione la carta que quieres colocar: ");
	    			select = teclat.nextInt();
	    			System.out.println("");
	    			if (select > player.getMano().size()) {
		 	    		System.out.println("Valor no aceptado, escoja otro\n");
		 	    	} else {
		 	    		if (!player.getMano().get(select - 1).isSeleccionada()) {
		 	    			if (!player.getMano().get(select - 1).isResguardada()) {
		 	    				player.select(select - 1);
					 	    	System.out.println(player.getFullMano().selectionToString());
		 	    			} else { System.out.println("No puede seleccionar una carta resguardada\n"); }						
				 	    } else { System.out.println("Carta ya seleccionada, seleccione otra\n"); }
		 	    	}
	    			for (int i = 0; i < jugadores.size(); i++) {
	    				if (!jugadores.get(i).getBajadaEscaleras().isEmpty() || !jugadores.get(i).getBajadaTrios().isEmpty()) {
	    					System.out.println(jugadores.get(i).bajadaToString());
	    				}
	    			}
	    			System.out.print("Seleccione la bajada del jugador: ");
	    			select = teclat.nextInt();
	    			System.out.println("");
	    			if (select > numJugadores) { System.out.println("Ese jugador no existe\n"); }
	    			else {
	    				int jug = select;
	    				System.out.print("Desea añadir la carta a los tríos o las escaleras? Tríos(1) | Escaleras(2) : ");
	    				select = teclat.nextInt();
	    				System.out.println("");
	    				int i = 0;
	    				boolean added = false;
	    				if (select == 1) {
	    					while (i < jugadores.get(jug - 1).getBajadaTrios().size() && !added) {
	    						jugadores.get(jug - 1).getFullMano().comprobarTrio(jugadores.get(jug - 1).getSelection(), jugadores.get(jug - 1).getBajadaTrios().get(i));
	    						added = jugadores.get(jug - 1).getFullMano().getAdded();
	    						i++;
	    					}
	    					if (added) {
	    						System.out.println("Carta añadida!\n");
	    						System.out.println(jugadores.get(jug - 1).bajadaToString());
	    						player.getMano().remove(player.getSelection());
	    					} else { System.out.println("No se pudo añadir la carta\n"); }
	    				} else {
	    					while (i < jugadores.get(jug - 1).getBajadaEscaleras().size() && !added) {
	    						jugadores.get(jug - 1).getFullMano().comprobarEscalera(jugadores.get(jug - 1).getSelection(), jugadores.get(jug - 1).getBajadaEscaleras().get(i));
	    						added = jugadores.get(jug - 1).getFullMano().getAdded();
	    						i++;
	    					}
	    					if (added) {
	    						System.out.println("Carta añadida!\n");
	    						System.out.println(jugadores.get(jug - 1).bajadaToString());
	    						player.getMano().remove(player.getSelection());
	    					} else { System.out.println("No se pudo añadir la carta\n"); }
	    				}
	    			}
	    			player.getSelection().setSeleccionada(false);
	    			player.getFullMano().setSelection(null);
	    		}
	    		else { System.out.println("No puedes jugar cartas aún\n"); }
	    		break;
	    	case 2:
	    		if (!player.getBajadaEscaleras().isEmpty() || !player.getBajadaTrios().isEmpty()) {
	    			System.out.println("No puedes manejar tus cartas porque ya te has bajado\n");
	    			break;
	    		}
	    		boolean endSelection = false;
	    		while (!endSelection) {
	    			System.out.println(player.getFullMano().toString());
		    		System.out.println("1. Seleccionar cartas");
			    	System.out.println("2. Eliminar carta de la seleción");
			    	System.out.println("3. Verificar");
			    	System.out.println("4. Salir");
			    	System.out.print("\nSeleccione una opción: ");
		    		select = teclat.nextInt();
		    		System.out.println("");
		    		switch (select) {
				    case 1:
				 	    boolean done = false;
				 	    while (!done) {
				 	    	System.out.print("Seleccione una carta o pulse 0 para salir: ");
				 	    	select = teclat.nextInt();
				 	    	System.out.println("");
				 	    	if (select > player.getMano().size()) {
				 	    		System.out.println("Valor no aceptado, escoja otro\n");
				 	    	} else {
				 	    		if (select == 0) {
					 	    		System.out.println("Fin de la selección\n");
					 	    		done = true;
					 	    	}
					 	    	else {
					 	    		if (!player.getMano().get(select - 1).isSeleccionada()) {
					 	    			if (!player.getMano().get(select - 1).isResguardada()) {
					 	    				player.select(select - 1);
								 	    	System.out.println(player.getFullMano().toString());
					 	    			} else { System.out.println("No puede seleccionar una carta resguardada\n"); }						
							 	    } else { System.out.println("Carta ya seleccionada, seleccione otra\n"); }
					 	    	}
				 	    	}
				 	    }
				    	break;
				    case 2:
				 	    System.out.println("selecciona una carta: ");
				 	    select = teclat.nextInt();
				 	    player.deselect();
				    	break;
				    case 3:
				    	System.out.println("Combinaciones para verificar: ");
				    	System.out.println("1. Escalera");
				    	System.out.println("2. Trío");
				    	System.out.println("3. Cancelar verificación\n");
				    	System.out.print("selecciona qué quieres verificar: ");
				    	select = teclat.nextInt();
				    	System.out.println("");
				    	switch (select) {
				    	case 1:
				    		if (numEscaleras == 0) { System.out.println("No hay Escaleras que verificar\n"); }
				    		else if (player.getFullMano().getResguardoEscaleras().size() == numEscaleras) {
				    			System.out.println("Ya has verificado todas las escaleras\n"); 
				    		}
				    		else {
				    			player.getFullMano().sacarEscaleras(null);
				    			if (player.getFullMano().getCanEscalera()) {
				    				player.getFullMano().resguardarEscaleras();
				    				System.out.println("Escalera verificada!\n");
				    			} else { System.out.println("No se pudo verificar la escalera\n"); }
				    		}
				    		break;
				    	case 2:
				    		if (numTrios == 0) { System.out.println("No hay Tríos que verificar\n"); }
				    		else if (player.getFullMano().getResguardoTrios().size() == numTrios) {
				    			System.out.println("Ya has verificado todas los Tríos\n"); 
				    		}
				    		else {
				    			player.getFullMano().sacarTrios(null);
				    			if (player.getFullMano().getCanTrio()) {
				    				player.getFullMano().resguardarTrios();
				    				System.out.println("Trío verificado!\n");
				    			} else { System.out.println("No se pudo verificar el trío\n"); }
				    		}
				    		break;
				    	default:
				    		break;
				    	}
				    	if (player.canBajarse(numTrios, numEscaleras)) {
				    		if (player.getCiclo() == 1) { System.out.println("No puedes bajar cartas porque es tu primera jugada\n"); }
				    		else {
				    			System.out.print("Ya puedes bajarte! Deseas hacerlo ahora? Si(1) | No(2) : ");
					    		select = teclat.nextInt();
					    		System.out.println("");
					    		if (select == 1) { player.bajarse(); }
					    		endSelection = true;
				    		}
				    	} else {
				    		player.getFullMano().getSelection().setSeleccionada(false);
				    		player.getFullMano().setSelection(null);
				    		System.out.println("No te puedes bajar aún \n");
				    		endSelection = true;
				    	}
				    	break;
				    default:
				    	player.getFullMano().setSelection(null);
				    	System.out.println("No juegas cartas este turno\n");
				    	endSelection = true;
		    		}
			    }
	    		break;
	    	case 3:
	    		endPlay = true;
		    	break;
	    	default:
		    	System.out.println("número incorrecto");
		    	break;
	    	}
    	}
		//3. Descartar carta
	    if (player.getCiclo() > 1 && player.getBajadaEscaleras().isEmpty() && player.getBajadaTrios().isEmpty()) {
	    	System.out.println("No juegas cartas este turno\n");
	    }
	    boolean discarded = false;
	    while (!discarded) {
	    	System.out.print("Selecciona una carta para descartar: ");
		    select = teclat.nextInt();
		    System.out.println("");
		    if (select - 1 <= player.getMano().size()) {
		    	if (!player.getMano().get(select - 1).isResguardada()) {
		    		Carta temp = player.getMano().get(select - 1);
		    		System.out.print("Quieres intentar recuperar la carta después de descartarla? Sí(1) | No(2) : ");
		    		select = teclat.nextInt();
				    System.out.println("");
				    if (select == 1) {
				    	player.getFullMano().setRetake(true);
				    }
				    int i = 0;
			    	taken = false;
			    	System.out.println("Carta en juego: " + temp.toString() + "\n");
			    	while (i < jugadores.size() && !taken) {
			    		if (jugadores.get(i).equals(player)) { i++; }
			    		else {
			    			System.out.print("Jugador " + (i + 1) + ", Vas a querer esta carta? Sí(1) | No(2) : ");
				    		select = teclat.nextInt();
						    System.out.println("");
						    if (select == 1) {
						    	System.out.println("Jugador " + (i + 1) + " se llevará la carta\n");
						    	playerLadron = jugadores.get(i);
						    	taken = true;
						    }
						    i++;
			    		}
			    	}
			    	if (!taken) { System.out.println("Se le devolverá la carta al jugador original\n"); }
				    player.discard(temp);
			    	descartes.take(player.getUltimaCarta());
				    discarded = true;
			    } else {
			    	System.out.println("No puedes descartar una carta de tu resguardo\n");
			    }
		    } else { System.out.println("Valor no aceptado, escoja otra vez\n"); }    
	    }
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
