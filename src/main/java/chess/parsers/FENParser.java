package chess.parsers;

import chess.Color;
import chess.Pieza;
import chess.Square;
import chess.builder.ChessBuilder;

/**
 * @author Mauricio Coria
 *
 */
public class FENParser {
	public static final String INITIAL_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	private ChessBuilder chessBuilder;
	
	public FENParser(ChessBuilder chessBuilder) {
		this.chessBuilder = chessBuilder; 
	}	
			
	public void parseFEN(String input) {
		String fields[] = input.split(" ");
		
		String piecePlacement = fields[0];
		String activeColor= fields[1];
		String enroquesPermitidos = fields[2];
		String peonPasante = fields[3];
		
		parsePiecePlacement(piecePlacement);
		
		chessBuilder.withPeonPasanteSquare(parsePeonPasanteSquare(peonPasante));
		
		chessBuilder.withTurno(parseTurno(activeColor));
		
		if(isCastlingWhiteReinaPermitido(enroquesPermitidos)){
			chessBuilder.withCastlingWhiteReinaPermitido(true);
		}
		
		if(isCastlingWhiteKingPermitido(enroquesPermitidos)){
			chessBuilder.withCastlingWhiteKingPermitido(true);
		}
		
		if(isCastlingBlackReinaPermitido(enroquesPermitidos)){
			chessBuilder.withCastlingBlackReinaPermitido(true);
		}
		
		if(isCastlingBlackKingPermitido(enroquesPermitidos)){
			chessBuilder.withCastlingBlackKingPermitido(true);
		}
		
	}
	
	public void parsePiecePlacement(String piecePlacement){
		Pieza[][] piezas = parsePieces(piecePlacement);
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				Square square = Square.getSquare(file, rank);
				Pieza pieza = piezas[rank][file];
				if(pieza != null){
					chessBuilder.withPieza(square, pieza);
				}
			}
		}
	}
	
	protected Pieza[][] parsePieces(String piecePlacement){
		Pieza[][] tablero = new Pieza[8][8];
		String ranks[] = piecePlacement.split("/");
		int currentRank = 7;
		for (int i = 0; i < 8; i++) {
			Pieza[] rankPiezas = parseRank(ranks[i]);
			for (int j = 0; j < 8; j++) {
				tablero[currentRank][j] = rankPiezas[j];
			}
			currentRank--;
		}
		return tablero;
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
				pieza = Pieza.ROOK_BLACK;
				break;
			case 'n':
				pieza = Pieza.KNIGHT_BLACK;
				break;
			case 'q':
				pieza = Pieza.QUEEN_BLACK;
				break;
			case 'k':
				pieza = Pieza.KING_BLACK;
				break;
			case 'p':
				pieza = Pieza.PAWN_BLACK;
				break;
			case 'b':
				pieza = Pieza.BISHOP_BLACK;
				break;
			case 'R':
				pieza = Pieza.ROOK_WHITE;
				break;
			case 'N':
				pieza = Pieza.KNIGHT_WHITE;
				break;
			case 'Q':
				pieza = Pieza.QUEEN_WHITE;
				break;
			case 'K':
				pieza = Pieza.KING_WHITE;
				break;
			case 'P':
				pieza = Pieza.PAWN_WHITE;
				break;
			case 'B':
				pieza = Pieza.BISHOP_WHITE;
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
	
	protected Color parseTurno(String activeColor) {
		char colorChar = activeColor.charAt(0);
		Color turno = null;
		switch (colorChar) {
		case 'w':
			turno = Color.WHITE;
			break;	
		case 'b':
			turno = Color.BLACK;
			break;				
		default:
			throw new RuntimeException("Unknown FEN code " + activeColor);			
		}
		return turno;
	}

	protected boolean isCastlingWhiteReinaPermitido(String enroquesPermitidos){
		if(enroquesPermitidos.contains("Q")){
			return true;
		}
		return false;
	}
	
	protected boolean isCastlingWhiteKingPermitido(String enroquesPermitidos){
		if(enroquesPermitidos.contains("K")){
			return true;
		}
		return false;
	}	
	
	protected boolean isCastlingBlackReinaPermitido(String enroquesPermitidos){
		if(enroquesPermitidos.contains("q")){
			return true;
		}
		return false;
	}
	
	protected boolean isCastlingBlackKingPermitido(String enroquesPermitidos){
		if(enroquesPermitidos.contains("k")){
			return true;
		}
		return false;
	}


}
