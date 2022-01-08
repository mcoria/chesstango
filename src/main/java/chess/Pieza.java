package chess;

import java.util.function.Function;

import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;

/**
 * @author Mauricio Coria
 *
 */
public enum Pieza {
	PEON_WHITE(Color.WHITE, strategy -> strategy.getPeonBlancoMoveGenerator()),
	PEON_BLACK(Color.BLACK,  strategy -> strategy.getPeonNegroMoveGenerator()),
	
	TORRE_WHITE(Color.WHITE, strategy -> strategy.getTorreBlancaMoveGenerator()),
	TORRE_BLACK(Color.BLACK, strategy -> strategy.getTorreNegraMoveGenerator()),
	
	CABALLO_WHITE(Color.WHITE, strategy -> strategy.getCaballoBlancoMoveGenerator()),
	CABALLO_BLACK(Color.BLACK, strategy -> strategy.getCaballoNegroMoveGenerator()),
	
	ALFIL_WHITE(Color.WHITE, strategy -> strategy.getAlfilBlancoMoveGenerator()),
	ALFIL_BLACK(Color.BLACK, strategy -> strategy.getAlfilNegroMoveGenerator()),
	
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
			return ALFIL_WHITE;
		case  BLACK:
			return ALFIL_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Pieza getTorre(Color color){
		switch (color) {
		case  WHITE:
			return TORRE_WHITE;
		case  BLACK:
			return TORRE_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

	public static Pieza getCaballo(Color color) {
		switch (color) {
		case  WHITE:
			return CABALLO_WHITE;
		case  BLACK:
			return CABALLO_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

	public static Pieza getPeon(Color color) {
		switch (color) {
		case  WHITE:
			return PEON_WHITE;
		case  BLACK:
			return PEON_BLACK;
		default:
			throw new RuntimeException("Invalid color");
		}
	}	

}
