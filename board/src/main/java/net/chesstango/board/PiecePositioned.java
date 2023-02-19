package net.chesstango.board;

import java.util.AbstractMap.SimpleImmutableEntry;

/**
 * @author Mauricio Coria
 *
 */
public class PiecePositioned extends SimpleImmutableEntry<Square, Piece> {

	private static final long serialVersionUID = 1L;
	
	public static final PiecePositioned ROOK_BLACK_QUEEN = new PiecePositioned(Square.a8, Piece.ROOK_BLACK);
	public static final PiecePositioned ROOK_BLACK_KING = new PiecePositioned(Square.h8, Piece.ROOK_BLACK);
	public static final PiecePositioned KING_BLACK = new PiecePositioned(Square.e8, Piece.KING_BLACK);

	public static final PiecePositioned ROOK_WHITE_QUEEN = new PiecePositioned(Square.a1, Piece.ROOK_WHITE);
	public static final PiecePositioned ROOK_WHITE_KING = new PiecePositioned(Square.h1, Piece.ROOK_WHITE);
	public static final PiecePositioned KING_WHITE = new PiecePositioned(Square.e1, Piece.KING_WHITE);

	private PiecePositioned(Square key, Piece value) {
		super(key, value);
	}

	public static PiecePositioned getPiecePositioned(Square key, Piece value){
		return PiecePositionedCache.getInstance().getPiecePositioned(key, value);
	}

	public static PiecePositioned getPosition(Square square) {
		return PiecePositionedCache.getInstance().getPosition(square);
	}


	private static class PiecePositionedCache {

		private static final PiecePositionedCache theInstance  = new PiecePositionedCache();
		private final PiecePositioned[][] board = new PiecePositioned[64][13];

		private PiecePositionedCache() {
			for (int file = 0; file < 8; file++) {
				for (int rank = 0; rank < 8; rank++) {
					for (Piece piece : Piece.values()) {
						board[Square.getSquare(file, rank).toIdx()][piece.ordinal()] = new PiecePositioned(
								Square.getSquare(file, rank), piece);
					}
					board[Square.getSquare(file, rank).toIdx()][12] = new PiecePositioned(
							Square.getSquare(file, rank), null);
				}
			}
		}

		public PiecePositioned getPiecePositioned(Square square, Piece piece) {
			PiecePositioned returnValue = null;
			if (piece == null) {
				returnValue = board[square.toIdx()][12];
			} else {
				returnValue = board[square.toIdx()][piece.ordinal()];
			}
			return returnValue;
		}

		public PiecePositioned getPosition(Square square) {
			return board[square.toIdx()][12];
		}
		

		public static PiecePositionedCache getInstance(){
			return theInstance;
		}
	}

}
