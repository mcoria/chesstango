package chess.board.movesgenerators.pseudo.strategies;

import chess.board.Color;
import chess.board.PiecePositioned;
import chess.board.iterators.Cardinal;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class QueenMoveGenerator extends AbstractCardinalMoveGenerator{
	
	public final static Cardinal[] QUEEN_CARDINAL = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste, Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};

	public QueenMoveGenerator(Color color) {
		super(color, QUEEN_CARDINAL);
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
