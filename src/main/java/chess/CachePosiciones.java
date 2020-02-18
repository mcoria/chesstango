package chess;

import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;

public class CachePosiciones {
	
	@SuppressWarnings("unchecked")
	private final Map.Entry<Square, Pieza>[][][] tablero = new Map.Entry[8][8][13];
	
	
	public CachePosiciones () {
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				for(Pieza pieza: Pieza.values()){
					tablero[file][rank][pieza.ordinal()] = new SimpleImmutableEntry<Square, Pieza>(Square.getSquare(file, rank), pieza);
				}
				tablero[file][rank][12] = new SimpleImmutableEntry<Square, Pieza>(Square.getSquare(file, rank), null);
			}
		}		
	}
	
	public Map.Entry<Square, Pieza> getPosicion(Square square, Pieza pieza) {
		Map.Entry<Square, Pieza> returnValue = null;
		if (pieza == null) {
			returnValue = tablero[square.getFile()][square.getRank()][12];
		} else {
			returnValue = tablero[square.getFile()][square.getRank()][pieza.ordinal()];
		}
		return returnValue;
	}

}
