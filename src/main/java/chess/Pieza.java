package chess;

import java.util.function.Function;

import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorStrategy;

/**
 * @author Mauricio Coria
 *
 */
public enum Pieza {
	PEON_BLANCO(Color.BLANCO, strategy -> strategy.getPeonBlancoMoveGenerator()),
	PEON_NEGRO(Color.NEGRO,  strategy -> strategy.getPeonNegroMoveGenerator()),
	
	TORRE_BLANCO(Color.BLANCO, strategy -> strategy.getTorreBlancaMoveGenerator()),
	TORRE_NEGRO(Color.NEGRO, strategy -> strategy.getTorreNegraMoveGenerator()),
	
	CABALLO_BLANCO(Color.BLANCO, strategy -> strategy.getCaballoBlancoMoveGenerator()),
	CABALLO_NEGRO(Color.NEGRO, strategy -> strategy.getCaballoNegroMoveGenerator()),
	
	ALFIL_BLANCO(Color.BLANCO, strategy -> strategy.getAlfilBlancoMoveGenerator()),
	ALFIL_NEGRO(Color.NEGRO, strategy -> strategy.getAlfilNegroMoveGenerator()),
	
	REINA_BLANCO(Color.BLANCO, strategy -> strategy.getReinaBlancaMoveGenerator()),
	REINA_NEGRO(Color.NEGRO, strategy -> strategy.getReinaNegraMoveGenerator()),
	
	REY_BLANCO(Color.BLANCO, strategy -> strategy.getReyBlancoMoveGenerator()),
	REY_NEGRO(Color.NEGRO, strategy -> strategy.getReyNegroMoveGenerator());
	
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
	
	public static Pieza getRey(Color color){
		switch (color) {
		case  BLANCO:
			return REY_BLANCO;
		case  NEGRO:
			return REY_NEGRO;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Pieza getReina(Color color){
		switch (color) {
		case  BLANCO:
			return REINA_BLANCO;
		case  NEGRO:
			return REINA_NEGRO;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Pieza getAlfil(Color color){
		switch (color) {
		case  BLANCO:
			return ALFIL_BLANCO;
		case  NEGRO:
			return ALFIL_NEGRO;
		default:
			throw new RuntimeException("Invalid color");
		}
	}
	
	public static Pieza getTorre(Color color){
		switch (color) {
		case  BLANCO:
			return TORRE_BLANCO;
		case  NEGRO:
			return TORRE_NEGRO;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

	public static Pieza getCaballo(Color color) {
		switch (color) {
		case  BLANCO:
			return CABALLO_BLANCO;
		case  NEGRO:
			return CABALLO_NEGRO;
		default:
			throw new RuntimeException("Invalid color");
		}
	}

	public static Pieza getPeon(Color color) {
		switch (color) {
		case  BLANCO:
			return PEON_BLANCO;
		case  NEGRO:
			return PEON_NEGRO;
		default:
			throw new RuntimeException("Invalid color");
		}
	}	

}
