package chess.board.pseudomovesgenerators.strategies;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.square.statics.KnightBitSquareIterator;
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
		return this.moveFactory.createSimpleMove(origen, destino);
	}


	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCaptureMove(origen, destino);
	}

	@Override
	protected Iterator<Square> getSquareIterator(Square fromSquare) {
		return new KnightBitSquareIterator(fromSquare);
	}

}
