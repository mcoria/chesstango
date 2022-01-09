package chess.parsers;

import chess.Color;
import chess.Piece;
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
		String enroquesAlloweds = fields[2];
		String peonPasante = fields[3];
		
		parsePiecePlacement(piecePlacement);
		
		chessBuilder.withPawnPasanteSquare(parsePawnPasanteSquare(peonPasante));
		
		chessBuilder.withTurno(parseTurno(activeColor));
		
		if(isCastlingWhiteQueenAllowed(enroquesAlloweds)){
			chessBuilder.withCastlingWhiteQueenAllowed(true);
		}
		
		if(isCastlingWhiteKingAllowed(enroquesAlloweds)){
			chessBuilder.withCastlingWhiteKingAllowed(true);
		}
		
		if(isCastlingBlackQueenAllowed(enroquesAlloweds)){
			chessBuilder.withCastlingBlackQueenAllowed(true);
		}
		
		if(isCastlingBlackKingAllowed(enroquesAlloweds)){
			chessBuilder.withCastlingBlackKingAllowed(true);
		}
		
	}
	
	public void parsePiecePlacement(String piecePlacement){
		Piece[][] piezas = parsePieces(piecePlacement);
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				Square square = Square.getSquare(file, rank);
				Piece piece = piezas[rank][file];
				if(piece != null){
					chessBuilder.withPieza(square, piece);
				}
			}
		}
	}
	
	protected Piece[][] parsePieces(String piecePlacement){
		Piece[][] tablero = new Piece[8][8];
		String ranks[] = piecePlacement.split("/");
		int currentRank = 7;
		for (int i = 0; i < 8; i++) {
			Piece[] rankPiezas = parseRank(ranks[i]);
			for (int j = 0; j < 8; j++) {
				tablero[currentRank][j] = rankPiezas[j];
			}
			currentRank--;
		}
		return tablero;
	}	
	
	protected Piece[] parseRank(String rank) {
		Piece piezas[] = new Piece[8];
		int position = 0;
		for (int i = 0; i < rank.length(); i++) {
			char theCharCode = rank.charAt(i);
			Piece currentPieza =  getCode(theCharCode);
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

	private Piece getCode(char t) {
		Piece piece = null;
		switch (t) {
			case 'r':
				piece = Piece.ROOK_BLACK;
				break;
			case 'n':
				piece = Piece.KNIGHT_BLACK;
				break;
			case 'q':
				piece = Piece.QUEEN_BLACK;
				break;
			case 'k':
				piece = Piece.KING_BLACK;
				break;
			case 'p':
				piece = Piece.PAWN_BLACK;
				break;
			case 'b':
				piece = Piece.BISHOP_BLACK;
				break;
			case 'R':
				piece = Piece.ROOK_WHITE;
				break;
			case 'N':
				piece = Piece.KNIGHT_WHITE;
				break;
			case 'Q':
				piece = Piece.QUEEN_WHITE;
				break;
			case 'K':
				piece = Piece.KING_WHITE;
				break;
			case 'P':
				piece = Piece.PAWN_WHITE;
				break;
			case 'B':
				piece = Piece.BISHOP_WHITE;
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

		return piece;
	}	
	
	protected Square parsePawnPasanteSquare(String peonPasante) {
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

	protected boolean isCastlingWhiteQueenAllowed(String enroquesAlloweds){
		if(enroquesAlloweds.contains("Q")){
			return true;
		}
		return false;
	}
	
	protected boolean isCastlingWhiteKingAllowed(String enroquesAlloweds){
		if(enroquesAlloweds.contains("K")){
			return true;
		}
		return false;
	}	
	
	protected boolean isCastlingBlackQueenAllowed(String enroquesAlloweds){
		if(enroquesAlloweds.contains("q")){
			return true;
		}
		return false;
	}
	
	protected boolean isCastlingBlackKingAllowed(String enroquesAlloweds){
		if(enroquesAlloweds.contains("k")){
			return true;
		}
		return false;
	}


}
