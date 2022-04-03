package chess.board.iterators.pieceplacement;

import chess.board.PiecePositioned;

//Esta implementacion es muy especifica a como se representa un tablero y como se representa un listado de posiciones

/**
 * @author Mauricio Coria
 *
 */
public class BoardBitIterator implements PiecePlacementIterator {

	private final long posiciones;
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
		return this.idx < 64;
	}

	@Override
	public PiecePositioned next() {
		PiecePositioned currentPoint = tablero[this.idx];
		calcularNextPoint();
		return currentPoint;
	}

	// TODO: Llevar esta implementacion a otros iteradores
	private void calcularNextPoint() {
		do {
			this.idx++;
		} while (this.idx < 64 && (posiciones & (1L << this.idx)) == 0);
	}

}
