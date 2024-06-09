package net.chesstango.board.moves.generators.pseudo.strategies;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.bypiece.KnightPositionsSquareIterator;
import net.chesstango.board.moves.factories.KnightMoveFactory;
import net.chesstango.board.moves.imp.MoveImp;

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
    protected MoveImp createSimpleMove(PiecePositioned from, PiecePositioned to) {
        return this.moveFactory.createSimpleKnightMove(from, to);
    }


    @Override
    protected MoveImp createCaptureMove(PiecePositioned from, PiecePositioned to) {
        return this.moveFactory.createCaptureKnightMove(from, to);
    }

    @Override
    protected Iterator<Square> getSquareIterator(Square fromSquare) {
        return new KnightPositionsSquareIterator(fromSquare);
    }

}
