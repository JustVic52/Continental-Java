package gameDynamics;

import java.io.Serializable;

public class Round implements Serializable {
	
	private static final long serialVersionUID = 7979117387072463113L;
	
	private int numRound, trios, escaleras, numCartas, cartasMin;
	
	public Round() {
		numRound = 10;
		setRound(numRound);
	}
	
	public void updateRound() {
		if (numRound < 10) {
			numRound++;
			setRound(numRound);
		}
	}
	
	public int getNumRound() {
		return numRound;
	}
	
	public int getMinimum() {
		return cartasMin;
	}
	
	public String sacarObjetivos() {
		String res = "Objetivo: \n";
		int esc = escaleras;
		int tri = trios;
		if (esc > 0) {
			res += (esc == 1) ? "1 Escalera" : esc + " Escaleras";
			if (tri > 0) { res += " y \n"; }
			else { res += "."; }
		}
		if (tri > 0) {
			res += (tri == 1) ? "1 Trío." : tri + " Tríos.";
		}
		System.out.println(res);
		return res;
	}
	
	public String sacarObjetivosEscaleras() {
		String res = "";
		int esc = escaleras;
		int tri = trios;
		if (esc > 0) {
			res += (esc == 1) ? "1 Escalera" : esc + " Escaleras";
			if (tri > 0) { res += " y \n"; }
			else { res += "."; }
		}
		return res;
	}
	
	public String sacarObjetivosTrios() {
		String res = "";
		int tri = trios;
		if (tri > 0) {
			res += (tri == 1) ? "1 Trío." : tri + " Tríos.";
		}
		return res;
	}
	
	public void setNumRound(int num) {
		numRound = num;
		setRound(num);
	}

	private void setRound(int num) {
		//1.  1 escalera            - 7  || Cartas Mínimas: 4
		//2.  2 tríos               - 8  || Cartas Mínimas: 6
		//3.  1 trío 1 escalera     - 8  || Cartas Mínimas: 7
		//4.  2 escaleras 		    - 12 || Cartas Mínimas: 8
		//5.  3 tríos 			    - 12 || Cartas Mínimas: 9
		//6.  2 tríos 1 escalera    - 12 || Cartas Mínimas: 10
		//7.  2 escaleras 1 trío    - 12 || Cartas Mínimas: 11
		//8.  4 tríos 			    - 12 || Cartas Mínimas: 12
		//9.  3 escaleras		    - 12 || Cartas Mínimas: 12
		//10. 2 escaleras y 2 tríos - 14 || Cartas Mínimas: 14
		switch (num) {
		case 1:
			trios = 0;
			escaleras = 1;
			numCartas = 7;
			cartasMin = 4;
			break;
		case 2:
			trios = 2;
			escaleras = 0;
			numCartas = 8;
			cartasMin = 6;
			break;
		case 3:
			trios = 1;
			escaleras = 1;
			numCartas = 8;
			cartasMin = 7;
			break;
		case 4:
			trios = 0;
			escaleras = 2;
			numCartas = 12;
			cartasMin = 8;
			break;
		case 5:
			trios = 3;
			escaleras = 0;
			numCartas = 12;
			cartasMin = 9;
			break;
		case 6:
			trios = 2;
			escaleras = 1;
			numCartas = 12;
			cartasMin = 10;
			break;
		case 7:
			trios = 1;
			escaleras = 2;
			numCartas = 12;
			cartasMin = 11;
			break;
		case 8:
			trios = 4;
			escaleras = 0;
			numCartas = 12;
			cartasMin = 12;
			break;
		case 9:
			trios = 0;
			escaleras = 3;
			numCartas = 12;
			cartasMin = 12;
			break;
		case 10:
			trios = 2;
			escaleras = 2;
			numCartas = 14;
			cartasMin = 14;
			break;
		default:
			break;
		}
	}
	
	public int getRoundTrios() {
		return trios;
	}
	
	public int getNumCartas() {
		return numCartas;
	}
	
	public int getRoundEscaleras() {
		return escaleras;
	}	
}
