package parsers;

import chess.Pieza;

public class FENTableroParser {
	
	private Pieza[][] tablero;
			

	protected Pieza[][] parsePiecePlacement(String piecePlacement){
		Pieza[][] tablero = new Pieza[8][8];
		String ranks[] = piecePlacement.split("/");
		int currentRank = 7;
		for (int i = 0; i < 8; i++) {
			Pieza[] rankPiezas = parseRank(ranks[i]);
			for (int j = 0; j < 8; j++) {
				tablero[j][currentRank] = rankPiezas[j];
			}
			currentRank--;
		}
		this.tablero =  tablero;
		return this.tablero;
	}
	
	protected Pieza[] parseRank(String rank) {
		Pieza piezas[] = new Pieza[8];
		int position = 0;
		for (int i = 0; i < rank.length(); i++) {
			char theCharCode = rank.charAt(i);
			Pieza currentPieza =  getCode(theCharCode);
			if(currentPieza != null){
				piezas[position] = currentPieza;
				position++;
			} else {
				int offset = Integer.parseInt(String.valueOf(theCharCode)); 
				position += offset;
			}
		}
		if(position != 8) {
			throw new RuntimeException("FEN: Malformed rank: " + rank);
		}
		return piezas;
	}

	private Pieza getCode(char t) {
		Pieza pieza = null;
		switch (t) {
			case 'r':
				pieza = Pieza.TORRE_NEGRO;
				break;
			case 'n':
				pieza = Pieza.CABALLO_NEGRO;
				break;
			case 'q':
				pieza = Pieza.REINA_NEGRO;
				break;
			case 'k':
				pieza = Pieza.REY_NEGRO;
				break;
			case 'p':
				pieza = Pieza.PEON_NEGRO;
				break;
			case 'b':
				pieza = Pieza.ALFIL_NEGRO;
				break;
			case 'R':
				pieza = Pieza.TORRE_BLANCO;
				break;
			case 'N':
				pieza = Pieza.CABALLO_BLANCO;
				break;
			case 'Q':
				pieza = Pieza.REINA_BLANCO;
				break;
			case 'K':
				pieza = Pieza.REY_BLANCO;
				break;
			case 'P':
				pieza = Pieza.PEON_BLANCO;
				break;
			case 'B':
				pieza = Pieza.ALFIL_BLANCO;
				break;
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
				break;				
			default:
				throw new RuntimeException("Unknown FEN code " + t);
		}

		return pieza;
	}
	

	
	public Pieza[][] getTablero() {
		return tablero;
	}

}
