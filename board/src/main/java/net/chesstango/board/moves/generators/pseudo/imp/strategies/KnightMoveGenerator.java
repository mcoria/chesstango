package net.chesstango.board.moves.generators.pseudo.imp.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.bypiece.KnightPositionsSquareIterator;
import net.chesstango.board.moves.factories.KnightMoveFactory;
import net.chesstango.board.moves.PseudoMove;

import java.util.Iterator;


/**
 * @author Mauricio Coria
 */
@Setter
public class KnightMoveGenerator extends AbstractJumpMoveGenerator {

    private KnightMoveFactory moveFactory;

    public KnightMoveGenerator(Color color) {
        super(color);
    }

    @Override
    protected PseudoMove createSimpleMove(PiecePositioned from, PiecePositioned to) {
        return this.moveFactory.createSimpleKnightMove(from, to);
    }


    @Override
    protected PseudoMove createCaptureMove(PiecePositioned from, PiecePositioned to) {
        return this.moveFactory.createCaptureKnightMove(from, to);
    }

    @Override
    protected Iterator<Square> getSquareIterator(Square fromSquare) {
        return new KnightPositionsSquareIterator(fromSquare);
    }

}
