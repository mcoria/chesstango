package net.chesstango.board;

/**
 * @author Mauricio Coria
 *
 */
public class PiecePositioned{
	private final Square key;
	private final Piece value;
	
	public static final PiecePositioned ROOK_BLACK_QUEEN = PiecePositionedCache.getInstance().getPiecePositioned(Square.a8, Piece.ROOK_BLACK);
	public static final PiecePositioned ROOK_BLACK_KING = PiecePositionedCache.getInstance().getPiecePositioned(Square.h8, Piece.ROOK_BLACK);
	public static final PiecePositioned KING_BLACK = PiecePositionedCache.getInstance().getPiecePositioned(Square.e8, Piece.KING_BLACK);

	public static final PiecePositioned ROOK_WHITE_QUEEN = PiecePositionedCache.getInstance().getPiecePositioned(Square.a1, Piece.ROOK_WHITE);
	public static final PiecePositioned ROOK_WHITE_KING = PiecePositionedCache.getInstance().getPiecePositioned(Square.h1, Piece.ROOK_WHITE);
	public static final PiecePositioned KING_WHITE = PiecePositionedCache.getInstance().getPiecePositioned(Square.e1, Piece.KING_WHITE);

	private PiecePositioned(Square key, Piece value) {
		this.key = key;
		this.value = value;
	}

	public static PiecePositioned getPiecePositioned(Square key, Piece value){
		return PiecePositionedCache.getInstance().getPiecePositioned(key, value);
	}

	public static PiecePositioned getPosition(Square square) {
		return PiecePositionedCache.getInstance().getPosition(square);
	}


	public Square getSquare() {
		return key;
	}


	public Piece getPiece() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("%s=%s", key, value);
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
