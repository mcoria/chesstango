package chess.board.movesgenerators.pseudo.strategies;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.bysquare.bypiece.KnightPositionsSquareIterator;
import chess.board.moves.Move;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 *
 */
public class KnightMoveGenerator extends AbstractJumpMoveGenerator {
	
	public KnightMoveGenerator(Color color) {
		super(color);
	}
	
	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimpleMove(origen, destino, null);
	}


	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCaptureMove(origen, destino, null);
	}

	@Override
	protected Iterator<Square> getSquareIterator(Square fromSquare) {
		return new KnightPositionsSquareIterator(fromSquare);
	}

}
