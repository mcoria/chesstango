package chess.board;

/**
 * @author Mauricio Coria
 *
 */
public enum Piece {
	PAWN_WHITE(Color.WHITE, 10),
	PAWN_BLACK(Color.BLACK, -10),
	
	ROOK_WHITE(Color.WHITE, 50),
	ROOK_BLACK(Color.BLACK, -50),
	
	KNIGHT_WHITE(Color.WHITE, 30),
	KNIGHT_BLACK(Color.BLACK, -30),
	
	BISHOP_WHITE(Color.WHITE, 30),
	BISHOP_BLACK(Color.BLACK, -30),
	
	QUEEN_WHITE(Color.WHITE, 100),
	QUEEN_BLACK(Color.BLACK, -100),
	
	KING_WHITE(Color.WHITE, 1000),
	KING_BLACK(Color.BLACK, -1000);
	
	private final Color color;
	private final int value;
	
	private Piece(Color color, int value) {
		this.color = color;
		this.value = value;
	}

	public Color getColor() {
		return color;
	}
	
	public int getValue() {
		return value;
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
