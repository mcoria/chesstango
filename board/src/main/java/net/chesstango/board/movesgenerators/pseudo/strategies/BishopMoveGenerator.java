package net.chesstango.board.movesgenerators.pseudo.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.BishopMoveFactory;
import net.chesstango.board.moves.factories.KnightMoveFactory;


/**
 * @author Mauricio Coria
 *
 */
public class BishopMoveGenerator extends AbstractCardinalMoveGenerator {

	@Setter
	private BishopMoveFactory moveFactory;
	
	public final static Cardinal[] BISHOP_CARDINAL = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};

	public BishopMoveGenerator(Color color) {
		super(color, BISHOP_CARDINAL);
	}

	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		return moveFactory.createSimpleBishopnMove(origen, destino, cardinal);
	}


	@Override
	protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
		return moveFactory.createCaptureBishopMove(origen, destino, cardinal);
	}
}
