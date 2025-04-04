package net.chesstango.board.internal.moves.generators.pseudo.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.factories.QueenMoveFactory;
import net.chesstango.board.moves.PseudoMove;

/**
 * @author Mauricio Coria
 */
@Setter
public class QueenMoveGenerator extends AbstractCardinalMoveGenerator {

    private QueenMoveFactory moveFactory;

    public final static Cardinal[] QUEEN_CARDINAL = new Cardinal[]{Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste, Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};

    public QueenMoveGenerator(Color color) {
        super(color, QUEEN_CARDINAL);
    }

    @Override
    protected PseudoMove createSimpleMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return moveFactory.createSimpleQueenMove(from, to, cardinal);
    }


    @Override
    protected PseudoMove createCaptureMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal) {
        return moveFactory.createCaptureQueenMove(from, to, cardinal);
    }

}
