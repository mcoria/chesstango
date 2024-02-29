package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.factories.MoveFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheFactoryTest {

    private MoveFactoryCache moveFactoryCache;
    private MoveFactory moveFactoryImp;

    @BeforeEach
    public void setUp() {
        moveFactoryImp = SingletonMoveFactories.getDefaultMoveFactoryWhite();
        moveFactoryCache = new MoveFactoryCache(moveFactoryImp);
    }

    @Test
    public void testComputeKey(){
        PiecePositioned fromPosition = PiecePositioned.getPiecePositioned(Square.b2, Piece.KNIGHT_WHITE);
        PiecePositioned toPosition = PiecePositioned.getPosition(Square.c3);
        Integer key = moveFactoryCache.computeKey(fromPosition, toPosition);
    }
}
