package chess.board.iterators.pieceplacement;

import chess.board.PiecePositioned;

//Esta implementacion es muy especifica a como se representa un tablero y como se representa un listado de posiciones

/**
 * @author Mauricio Coria
 *
 */
public class BoardBitIterator implements PiecePlacementIterator {

	private long posiciones;
	private final PiecePositioned[] tablero;

	private int idx = -1;

	// Observar este constructor
	public BoardBitIterator(PiecePositioned[] tablero, long posiciones) {
		this.posiciones = posiciones;
		this.tablero = tablero;
		calcularNextPoint();
	}

	@Override
	public boolean hasNext() {
		return this.idx != -1;
	}

	@Override
	public PiecePositioned next() {
		PiecePositioned currentPoint = tablero[this.idx];
		calcularNextPoint();
		return currentPoint;
	}

	// TODO: Llevar esta implementacion a otros iteradores
	private void calcularNextPoint() {
		
		if (posiciones != 0) {
			long posicionLng = Long.lowestOneBit(posiciones);
			idx = Long.numberOfTrailingZeros(posicionLng);
			posiciones &= ~posicionLng;
		} else {
			idx = -1;
		}
		

	}

}
