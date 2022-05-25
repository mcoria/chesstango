package chess.board.movesgenerators.pseudo.strategies;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.iterators.Cardinal;
import chess.board.moves.Move;


/**
 * @author Mauricio Coria
 *
 */
public class BishopMoveGenerator extends AbstractCardinalMoveGenerator {
	
	public final static Cardinal[] BISHOP_CARDINAL = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};

	public BishopMoveGenerator(Color color) {
		super(color, BISHOP_CARDINAL);
	}

	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		return moveFactory.createSimpleMove(origen, destino, cardinal);
	}


	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		return moveFactory.createCaptureMove(origen, destino, cardinal);
	}
}
