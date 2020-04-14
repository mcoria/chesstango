package chess;

import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;

public class CachePosiciones {
	
	public static final Map.Entry<Square, Pieza> TORRE_NEGRO_REYNA = new SimpleImmutableEntry<Square, Pieza>(Square.a8, Pieza.TORRE_NEGRO);
	public static final Map.Entry<Square, Pieza> REY_NEGRO = new SimpleImmutableEntry<Square, Pieza>(Square.e8, Pieza.REY_NEGRO);
	public static final Map.Entry<Square, Pieza> TORRE_NEGRO_REY = new SimpleImmutableEntry<Square, Pieza>(Square.h8, Pieza.TORRE_NEGRO);
	
	public static final Map.Entry<Square, Pieza> TORRE_BLANCA_REYNA = new SimpleImmutableEntry<Square, Pieza>(Square.a1, Pieza.TORRE_BLANCO);
	public static final Map.Entry<Square, Pieza> REY_BLANCO = new SimpleImmutableEntry<Square, Pieza>(Square.e1, Pieza.REY_BLANCO);
	public static final Map.Entry<Square, Pieza> TORRE_BLANCA_REY = new SimpleImmutableEntry<Square, Pieza>(Square.h1, Pieza.TORRE_BLANCO);	
	
	@SuppressWarnings("unchecked")
	private final Map.Entry<Square, Pieza>[][] tablero = new Map.Entry[64][13];
	
	
	public CachePosiciones () {
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				for(Pieza pieza: Pieza.values()){
					tablero[Square.getSquare(file, rank).toIdx()][pieza.ordinal()] = new SimpleImmutableEntry<Square, Pieza>(Square.getSquare(file, rank), pieza);
				}
				tablero[Square.getSquare(file, rank).toIdx()][12] = new SimpleImmutableEntry<Square, Pieza>(Square.getSquare(file, rank), null);
			}
		}		
	}
	
	public Map.Entry<Square, Pieza> getPosicion(Square square, Pieza pieza) {
		Map.Entry<Square, Pieza> returnValue = null;
		if (pieza == null) {
			returnValue = tablero[square.toIdx()][12];
		} else {
			returnValue = tablero[square.toIdx()][pieza.ordinal()];
		}
		return returnValue;
	}

}
