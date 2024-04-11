package net.chesstango.board.movesgenerators.pseudo.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.bypiece.KnightPositionsSquareIterator;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.KnightMoveFactory;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 */
public class KnightMoveGenerator extends AbstractJumpMoveGenerator {

    @Setter
    private KnightMoveFactory moveFactory;

    public KnightMoveGenerator(Color color) {
        super(color);
    }

    @Override
    protected Move createSimpleMove(PiecePositioned from, PiecePositioned to) {
        return this.moveFactory.createSimpleKnightMove(from, to);
    }


    @Override
    protected Move createCaptureMove(PiecePositioned from, PiecePositioned to) {
        return this.moveFactory.createCaptureKnightMove(from, to);
    }

    @Override
    protected Iterator<Square> getSquareIterator(Square fromSquare) {
        return new KnightPositionsSquareIterator(fromSquare);
    }

}
