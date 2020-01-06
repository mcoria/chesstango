package parsers;

import chess.Board;
import chess.Color;
import chess.DummyBoard;
import chess.Pieza;

public class FENParser {
	public static final String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	protected Board parse(String input) {
		String fields[] = input.split(" ");
		
		String piecePlacement = fields[0];
		String activeColor= fields[1];
		
		DummyBoard tablero = parsePiecePlacement(piecePlacement);
		Color color = parseColor(activeColor);
		
		return new Board(tablero, color);
	}
	
	public DummyBoard  parsePiecePlacement(String piecePlacement){
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
		return new DummyBoard(tablero);
	}
	
	protected Pieza[] parseRank(String rank) {
		Pieza piezas[] = new Pieza[8];
		int position = 0;
		for (int i = 0; i < rank.length(); i++) {
			Pieza currentPieza =  getCode(rank.charAt(i));
			if(currentPieza != null){
				piezas[i] = currentPieza;
				position++;
			} else {
				position = position + Integer.parseUnsignedInt(rank.substring(i, i+1));
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
	
	public Color parseColor(String activeColor) {
		char colorChar = activeColor.charAt(0);
		Color color = null;
		switch (colorChar) {
		case 'w':
			color = Color.BLANCO;
			break;	
		case 'b':
			color = Color.NEGRO;
			break;				
		default:
			throw new RuntimeException("Unknown FEN code " + activeColor);			
		}
		return color;
	}	

	private static FENParser fenParser = new FENParser();
	
	public static Board parseFEN(String input){
		return fenParser.parse(input);
	}
}
