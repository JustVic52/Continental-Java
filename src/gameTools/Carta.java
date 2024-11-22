package gameTools;

public class Carta {
	
	private int value, palo, number;
	public boolean comodin, seleccionada;
	public static final int PICAS = 1, CORAZONES = 2, TREVOLES = 3, DIAMANTES = 4;
	public static final int ACE = 1, JACK = 11, QUEEN = 12, KING = 13;
	
	public Carta(int n, int p) { //Crea una carta de un valor X y un palo Y
		number = n;
		adjustValue(number);
		palo = p;
		seleccionada = false;
	}
	
	public Carta() { //Crea un comodín
		number = 14;
		adjustValue(number);
		comodin = true;
		palo = 5;
		seleccionada = false;
	}
	
	private void adjustValue(int n) {
		switch (n) {
		case ACE:
		case JACK:
		case QUEEN:
		case KING:
			value = 10;
			break;
		case 14:
			value = 20;
			break;
		default:
			value = n;
			break;
		}
	}
	
	public boolean isSeleccionada() { return seleccionada; }
	
	public void setSeleccionada(boolean s) { seleccionada = s; }
	
	public int getValue() {
		return value;
	}
	
	public boolean isComodin() {
		return comodin;
	}
	
	public int getPalo() {
		return palo;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber (int n) { value = n; }
	
	public void setValue(int v) { value = v; }
	
	public void setPalo(int p) { palo = p; }
	
	public String getNombrePalo() {
		switch (palo) {
		case PICAS:
			return "♠";
		case CORAZONES:
			return "♥";
		case TREVOLES:
			return "♣";
		case DIAMANTES:
			return "♦";
		default:
			return "";
		}
	}
	
	public boolean equals(Object o) {
		Carta carta = (Carta) o;
		return this.number == carta.number && this.palo == carta.palo;
	}
	
	public String toString() {
		
		if (isComodin()) {
			return "C";
		}
		
		String res = "";
		switch (number) {
		case ACE:
			res += "A";
			break;
		case JACK:
			res += "J";
			break;
		case QUEEN:
			res += "Q";
			break;
		case KING:
			res += "K";
			break;
		default:
			res += number;
		}
		return res += getNombrePalo();
	}
}
