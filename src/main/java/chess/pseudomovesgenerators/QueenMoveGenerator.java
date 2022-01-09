package chess.pseudomovesgenerators;

import chess.Color;
import chess.PiecePositioned;
import chess.iterators.Cardinal;
import chess.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class QueenMoveGenerator extends AbstractCardinalMoveGenerator{

	public QueenMoveGenerator(Color color) {
		super(color, new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste, Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur});
	}

	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return moveFactory.createSimpleMove(origen, destino);
	}
	
	
	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino) {
		return moveFactory.createCaptureMove(origen, destino); 
	}	

}
