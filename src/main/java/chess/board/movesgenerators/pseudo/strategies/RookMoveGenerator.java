package chess.board.movesgenerators.pseudo.strategies;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.iterators.Cardinal;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class RookMoveGenerator extends AbstractCardinalMoveGenerator {
	
	public final static Cardinal[] ROOK_CARDINAL = new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};

	public RookMoveGenerator(Color color) {
		super(color, ROOK_CARDINAL);
	}

	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		return moveFactory.createSimpleRookMove(origen, destino);
	}
	
	
	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		return moveFactory.createCaptureRookMove(origen, destino); 
	}	
	
	
}
