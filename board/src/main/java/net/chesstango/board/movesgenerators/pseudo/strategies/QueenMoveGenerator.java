package net.chesstango.board.movesgenerators.pseudo.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.QueenMoveFactory;

/**
 * @author Mauricio Coria
 */
public class QueenMoveGenerator extends AbstractCardinalMoveGenerator {

    @Setter
    private QueenMoveFactory moveFactory;

    public final static Cardinal[] QUEEN_CARDINAL = new Cardinal[]{Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste, Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur};

    public QueenMoveGenerator(Color color) {
        super(color, QUEEN_CARDINAL);
    }

    @Override
    protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return moveFactory.createSimpleQueenMove(origen, destino, cardinal);
    }


    @Override
    protected Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal) {
        return moveFactory.createCaptureQueenMove(origen, destino, cardinal);
    }

}
