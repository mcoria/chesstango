package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.factories.imp.MoveFactoryCache;
import net.chesstango.board.moves.factories.imp.MoveFactoryWhite;
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
        moveFactoryImp = new MoveFactoryWhite();
        moveFactoryCache = new MoveFactoryCache(moveFactoryImp);
    }

    @Test
    public void testComputeKey(){
        PiecePositioned fromPosition = PiecePositioned.of(Square.b2, Piece.KNIGHT_WHITE);
        PiecePositioned toPosition = PiecePositioned.getPosition(Square.c3);
        Integer key = moveFactoryCache.computeKey(fromPosition, toPosition);
    }
}
