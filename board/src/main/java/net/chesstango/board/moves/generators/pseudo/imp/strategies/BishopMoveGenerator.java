package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.BishopMoveFactory;
import net.chesstango.board.moves.PseudoMove;


/**
 * @author Mauricio Coria
 *
 */
@Setter
public class BishopMoveGenerator extends AbstractCardinalMoveGenerator {

	private BishopMoveFactory moveFactory;
	
	public final static Cardinal[] BISHOP_CARDINAL = new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste};

	public BishopMoveGenerator(Color color) {
		super(color, BISHOP_CARDINAL);
	}

	@Override
	protected PseudoMove createSimpleMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
		return moveFactory.createSimpleBishopMove(from, to, cardinal);
	}


	@Override
	protected PseudoMove createCaptureMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
		return moveFactory.createCaptureBishopMove(from, to, cardinal);
	}
}
