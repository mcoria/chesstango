package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.piazzolla.syzygy.SyzygyPosition;
import net.chesstango.piazzolla.syzygy.SyzygyPositionBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class SearchByTablebaseTest {
    private SearchByTablebase searchByTablebase;

    @Mock
    private Syzygy syzygy;

    @BeforeEach
    public void setup() {
        searchByTablebase = new SearchByTablebase(syzygy);
    }

    @Test
    public void test01() {
        when(syzygy.tb_largest()).thenReturn(5);
        when(syzygy.tb_probe_root(any(SyzygyPosition.class), any(int[].class))).thenReturn(0x2002D30);

        Game game = Game.from(FEN.of("8/8/8/8/8/8/2Rk4/1K6 b - - 0 1"));
        SearchContext searchContext = new SearchContext()
                .setGame(game);

        SearchResponse result = searchByTablebase.search(searchContext);

        Move move = result.move();

        assertEquals(Square.d2, move.getFrom().square());
        assertEquals(Square.d3, move.getTo().square());
    }

    @Test
    public void test02() {
        when(syzygy.tb_largest()).thenReturn(5);
        when(syzygy.tb_probe_root(any(SyzygyPosition.class), any(int[].class))).thenReturn(0x1B029A4);

        Game game = Game.from(FEN.of("8/8/8/8/8/4k3/2R5/1K6 w - - 0 1"));
        SearchContext searchContext = new SearchContext()
                .setGame(game);

        SearchResponse result = searchByTablebase.search(searchContext);

        Move move = result.move();

        assertEquals(Square.c2, move.getFrom().square());
        assertEquals(Square.c4, move.getTo().square());
    }

    @Test
    public void test_promotion() {
        when(syzygy.tb_largest()).thenReturn(5);
        when(syzygy.tb_probe_root(any(SyzygyPosition.class), any(int[].class))).thenReturn(0x1DBE2);

        Game game = Game.from(FEN.of("8/6P1/8/7K/3k4/8/8/1q6 w - - 0 1"));
        SearchContext searchContext = new SearchContext()
                .setGame(game);

        SearchResponse result = searchByTablebase.search(searchContext);

        MovePromotion move = (MovePromotion) result.move();

        assertEquals(Square.g7, move.getFrom().square());
        assertEquals(Square.g8, move.getTo().square());
        assertEquals(Piece.QUEEN_WHITE, move.getPromotion());
    }

    @Test
    public void testBindSyzygyPosition01() {
        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2R5/1K6 w - - 0 1"));

        SyzygyPositionBuilder positionBuilder = new SyzygyPositionBuilder();
        game.getPosition().export(positionBuilder);
        SyzygyPosition syzygyPositionExpected = positionBuilder.getPositionRepresentation();

        SyzygyPosition syzygyPositionActual = searchByTablebase.bindSyzygyPosition(game);

        assertEquals(syzygyPositionExpected, syzygyPositionActual);
    }

    @Test
    public void testBindSyzygyPosition02() {
        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2R5/1K6 w - - 3 10"));

        SyzygyPositionBuilder positionBuilder = new SyzygyPositionBuilder();
        game.getPosition().export(positionBuilder);
        SyzygyPosition syzygyPositionExpected = positionBuilder.getPositionRepresentation();

        SyzygyPosition syzygyPositionActual = searchByTablebase.bindSyzygyPosition(game);

        assertEquals(syzygyPositionExpected, syzygyPositionActual);
    }
}
