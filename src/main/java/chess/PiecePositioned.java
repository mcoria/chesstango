package chess;

import java.util.AbstractMap.SimpleImmutableEntry;

/**
 * @author Mauricio Coria
 *
 */
public class PiecePositioned extends SimpleImmutableEntry<Square, Piece> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final PiecePositioned ROOK_BLACK_QUEEN = new PiecePositioned(Square.a8, Piece.ROOK_BLACK);
	public static final PiecePositioned KING_BLACK = new PiecePositioned(Square.e8, Piece.KING_BLACK);
	public static final PiecePositioned ROOK_BLACK_KING = new PiecePositioned(Square.h8, Piece.ROOK_BLACK);

	public static final PiecePositioned ROOK_WHITE_QUEEN = new PiecePositioned(Square.a1, Piece.ROOK_WHITE);
	public static final PiecePositioned KING_WHITE = new PiecePositioned(Square.e1, Piece.KING_WHITE);
	public static final PiecePositioned ROOK_WHITE_KING = new PiecePositioned(Square.h1, Piece.ROOK_WHITE);	

	private PiecePositioned(Square key, Piece value) {
		super(key, value);
	}

	public static PiecePositioned getPiecePositioned(Square key, Piece value){
		return CachePosicioness.getInstance().getPosicion(key, value);
	}


	private static class CachePosicioness {

		private final PiecePositioned[][] tablero = new PiecePositioned[64][13];

		private CachePosicioness() {
			for (int file = 0; file < 8; file++) {
				for (int rank = 0; rank < 8; rank++) {
					for (Piece piece : Piece.values()) {
						tablero[Square.getSquare(file, rank).toIdx()][piece.ordinal()] = new PiecePositioned(
								Square.getSquare(file, rank), piece);
					}
					tablero[Square.getSquare(file, rank).toIdx()][12] = new PiecePositioned(
							Square.getSquare(file, rank), null);
				}
			}
		}

		PiecePositioned getPosicion(Square square, Piece piece) {
			PiecePositioned returnValue = null;
			if (piece == null) {
				returnValue = tablero[square.toIdx()][12];
			} else {
				returnValue = tablero[square.toIdx()][piece.ordinal()];
			}
			return returnValue;
		}
		
		private static CachePosicioness theInstance  = new CachePosicioness();
		
		static CachePosicioness getInstance(){
			return theInstance;
		}

	}

}
