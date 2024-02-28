package net.chesstango.board.movesgenerators.pseudo.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.RookMoveFactory;

/**
 * @author Mauricio Coria
 *
 */
public class RookMoveGenerator extends AbstractCardinalMoveGenerator {

	@Setter
	private RookMoveFactory moveFactory;

	public final static Cardinal[] ROOK_CARDINAL = new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};

	public RookMoveGenerator(Color color) {
		super(color, ROOK_CARDINAL);
	}

	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		return moveFactory.createSimpleRookMove(origen, destino, cardinal);
	}
	
	
	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		return moveFactory.createCaptureRookMove(origen, destino, cardinal);
	}	
	
	
}
