package chess.board;

/**
 * @author Mauricio Coria
 *
 */
public enum Piece {
	PAWN_WHITE(Color.WHITE, 100, 1),
	PAWN_BLACK(Color.BLACK, -100, 1),
	
	KNIGHT_WHITE(Color.WHITE, 300, 4),
	KNIGHT_BLACK(Color.BLACK, -300, 4),
	
	BISHOP_WHITE(Color.WHITE, 300, 3),
	BISHOP_BLACK(Color.BLACK, -300, 3),

	ROOK_WHITE(Color.WHITE, 500, 2),
	ROOK_BLACK(Color.BLACK, -500, 2),
	
	QUEEN_WHITE(Color.WHITE, 900, 4),
	QUEEN_BLACK(Color.BLACK, -900, 4),
	
	KING_WHITE(Color.WHITE, 1000, 0),
	KING_BLACK(Color.BLACK, -1000, 0);
	
	private final Color color;
	private final int pieceValue;

	private final int moveValue;
	
	Piece(Color color, int pieceValue, int moveValue) {
		this.color = color;
		this.pieceValue = pieceValue;
		this.moveValue = moveValue;
	}

	public Color getColor() {
		return color;
	}
	
	public int getPieceValue() {
		return pieceValue;
	}

	public int getMoveValue() {
		return moveValue;
	}
	
	public static Piece getKing(Color color){
		switch (color) {
		case  WHITE:
			return KING_WHITE;
		case  BLACK:
			return KING_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Piece getQueen(Color color){
		switch (color) {
		case  WHITE:
			return QUEEN_WHITE;
		case  BLACK:
			return QUEEN_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Piece getBishop(Color color){
		switch (color) {
		case  WHITE:
			return BISHOP_WHITE;
		case  BLACK:
			return BISHOP_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Piece getRook(Color color){
		switch (color) {
		case  WHITE:
			return ROOK_WHITE;
		case  BLACK:
			return ROOK_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

	public static Piece getKnight(Color color) {
		switch (color) {
		case  WHITE:
			return KNIGHT_WHITE;
		case  BLACK:
			return KNIGHT_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

	public static Piece getPawn(Color color) {
		switch (color) {
		case  WHITE:
			return PAWN_WHITE;
		case  BLACK:
			return PAWN_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

}
