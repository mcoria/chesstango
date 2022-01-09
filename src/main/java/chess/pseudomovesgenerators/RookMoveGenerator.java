package chess.pseudomovesgenerators;

import chess.Color;
import chess.PiecePositioned;
import chess.iterators.Cardinal;
import chess.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class RookMoveGenerator extends AbstractCardinalMoveGenerator {

	public RookMoveGenerator(Color color) {
		super(color, new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur});
	}

	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return moveFactory.createSimpleRookMove(origen, destino);
	}
	
	
	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return moveFactory.createCaptureRookMove(origen, destino); 
	}	
	
	
}
