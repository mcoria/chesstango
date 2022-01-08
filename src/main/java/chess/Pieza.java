package chess;

import java.util.function.Function;

import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;

/**
 * @author Mauricio Coria
 *
 */
public enum Pieza {
	PAWN_WHITE(Color.WHITE, strategy -> strategy.getPeonBlancoMoveGenerator()),
	PAWN_BLACK(Color.BLACK,  strategy -> strategy.getPeonNegroMoveGenerator()),
	
	ROOK_WHITE(Color.WHITE, strategy -> strategy.getTorreBlancaMoveGenerator()),
	ROOK_BLACK(Color.BLACK, strategy -> strategy.getTorreNegraMoveGenerator()),
	
	KNIGHT_WHITE(Color.WHITE, strategy -> strategy.getCaballoBlancoMoveGenerator()),
	KNIGHT_BLACK(Color.BLACK, strategy -> strategy.getCaballoNegroMoveGenerator()),
	
	BISHOP_WHITE(Color.WHITE, strategy -> strategy.getAlfilBlancoMoveGenerator()),
	BISHOP_BLACK(Color.BLACK, strategy -> strategy.getAlfilNegroMoveGenerator()),
	
	QUEEN_WHITE(Color.WHITE, strategy -> strategy.getReinaBlancaMoveGenerator()),
	QUEEN_BLACK(Color.BLACK, strategy -> strategy.getReinaNegraMoveGenerator()),
	
	KING_WHITE(Color.WHITE, strategy -> strategy.getKingBlancoMoveGenerator()),
	KING_BLACK(Color.BLACK, strategy -> strategy.getKingNegroMoveGenerator());
	
	private final Color color;
	private final Function<MoveGeneratorStrategy, MoveGenerator> selector;
	
	private Pieza(Color color, Function<MoveGeneratorStrategy, MoveGenerator> selector) {
		this.color = color;
		this.selector = selector;
	}

	public Color getColor() {
		return color;
	}

	public MoveGenerator getMoveGenerator(MoveGeneratorStrategy strategy) {
		return selector.apply(strategy);
	}	
	
	public static Pieza getKing(Color color){
		switch (color) {
		case  WHITE:
			return KING_WHITE;
		case  BLACK:
			return KING_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Pieza getReina(Color color){
		switch (color) {
		case  WHITE:
			return QUEEN_WHITE;
		case  BLACK:
			return QUEEN_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Pieza getAlfil(Color color){
		switch (color) {
		case  WHITE:
			return BISHOP_WHITE;
		case  BLACK:
			return BISHOP_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Pieza getTorre(Color color){
		switch (color) {
		case  WHITE:
			return ROOK_WHITE;
		case  BLACK:
			return ROOK_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

	public static Pieza getCaballo(Color color) {
		switch (color) {
		case  WHITE:
			return KNIGHT_WHITE;
		case  BLACK:
			return KNIGHT_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

	public static Pieza getPeon(Color color) {
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
