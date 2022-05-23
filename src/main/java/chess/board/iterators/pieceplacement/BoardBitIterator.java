package chess.board.iterators.pieceplacement;

import chess.board.PiecePositioned;

//Esta implementacion es muy especifica a como se representa un tablero y como se representa un listado de posiciones

/**
 * @author Mauricio Coria
 *
 */
public class BoardBitIterator implements PiecePlacementIterator {
	private long posiciones;

	private final PiecePositioned[] piecePositioned;

	public BoardBitIterator(PiecePositioned[] piecePositioned, long posiciones) {
		this.posiciones = posiciones;
		this.piecePositioned = piecePositioned;
	}

	@Override
	public boolean hasNext() {
		return posiciones != 0;
	}

	@Override
	public PiecePositioned next() {
		PiecePositioned result = null;
		if (posiciones != 0) {
			long posicionLng = Long.lowestOneBit(posiciones);
			result  = piecePositioned[Long.numberOfTrailingZeros(posicionLng)];
			posiciones &= ~posicionLng;
		}
		return result;
	}


}
