package chess;

/**
 * @author Mauricio Coria
 *
 */
public class CachePosiciones {
	
	public static final PosicionPieza TORRE_BLACK_REYNA = new PosicionPieza(Square.a8, Pieza.TORRE_BLACK);
	public static final PosicionPieza REY_BLACK = new PosicionPieza(Square.e8, Pieza.KING_BLACK);
	public static final PosicionPieza TORRE_BLACK_REY = new PosicionPieza(Square.h8, Pieza.TORRE_BLACK);
	
	public static final PosicionPieza TORRE_BLANCA_REYNA = new PosicionPieza(Square.a1, Pieza.TORRE_WHITE);
	public static final PosicionPieza REY_WHITE = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
	public static final PosicionPieza TORRE_BLANCA_REY = new PosicionPieza(Square.h1, Pieza.TORRE_WHITE);	
	

	private final PosicionPieza[][] tablero = new PosicionPieza[64][13];
	
	
	public CachePosiciones () {
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				for(Pieza pieza: Pieza.values()){
					tablero[Square.getSquare(file, rank).toIdx()][pieza.ordinal()] = new PosicionPieza(Square.getSquare(file, rank), pieza);
				}
				tablero[Square.getSquare(file, rank).toIdx()][12] = new PosicionPieza(Square.getSquare(file, rank), null);
			}
		}		
	}
	
	public PosicionPieza getPosicion(Square square, Pieza pieza) {
		PosicionPieza returnValue = null;
		if (pieza == null) {
			returnValue = tablero[square.toIdx()][12];
		} else {
			returnValue = tablero[square.toIdx()][pieza.ordinal()];
		}
		return returnValue;
	}

}
