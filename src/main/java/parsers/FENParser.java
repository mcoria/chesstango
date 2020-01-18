package parsers;

import chess.Board;
import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class FENParser {
	public static final String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	public Board parse(String input) {
		String fields[] = input.split(" ");
		
		String piecePlacement = fields[0];
		String activeColor= fields[1];
		String enroquesPermitidos = fields[2];
		String peonPasante = fields[3];
		
		DummyBoard tablero = parsePiecePlacement(piecePlacement);
		Color color = parseColor(activeColor);
		
		Square peonPasanteSquare = parsePeonPasanteSquare(peonPasante);
		
		BoardState boardState = new BoardState();
		boardState.setPeonPasanteSquare(peonPasanteSquare);
		boardState.setEnroqueBlancoReinaPermitido(isEnroqueBlancoReinaPermitido(enroquesPermitidos));
		boardState.setEnroqueBlancoReyPermitido(isEnroqueBlancoReyPermitido(enroquesPermitidos));
		boardState.setEnroqueNegroReinaPermitido(isEnroqueNegroReinaPermitido(enroquesPermitidos));
		boardState.setEnroqueNegroReyPermitido(isEnroqueNegroReyPermitido(enroquesPermitidos));		
		
		return new Board(tablero, color, boardState);
	}
	
	protected boolean isEnroqueBlancoReinaPermitido(String enroquesPermitidos){
		if(enroquesPermitidos.contains("Q")){
			return true;
		}
		return false;
	}
	
	protected boolean isEnroqueBlancoReyPermitido(String enroquesPermitidos){
		if(enroquesPermitidos.contains("K")){
			return true;
		}
		return false;
	}	
	
	protected boolean isEnroqueNegroReinaPermitido(String enroquesPermitidos){
		if(enroquesPermitidos.contains("q")){
			return true;
		}
		return false;
	}
	
	protected boolean isEnroqueNegroReyPermitido(String enroquesPermitidos){
		if(enroquesPermitidos.contains("k")){
			return true;
		}
		return false;
	}	
	
	protected Square parsePeonPasanteSquare(String peonPasante) {
		Square result = null;
		if( ! "-".equals(peonPasante)){
			char file = peonPasante.charAt(0);
			char rank = peonPasante.charAt(1);
			int fileNumber = -1;
			int rankNumber = Integer.parseInt(String.valueOf(rank)) - 1;
			switch (file) {
			case 'a':
				fileNumber = 0;
				break;
			case 'b':
				fileNumber = 1;
				break;
			case 'c':
				fileNumber = 2;
				break;
			case 'd':
				fileNumber = 3;
				break;
			case 'e':
				fileNumber = 4;
				break;
			case 'f':
				fileNumber = 5;
				break;
			case 'g':
				fileNumber = 6;
				break;
			case 'h':
				fileNumber = 7;
				break;				
			default:
				throw new RuntimeException("Invalid FEV code");
			}
			result = Square.getSquare(fileNumber, rankNumber);
		}; 
		return result;
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
