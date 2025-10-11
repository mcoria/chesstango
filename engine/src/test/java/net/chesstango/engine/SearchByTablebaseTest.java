package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
@Disabled
public class SearchByTablebaseTest {
    private SearchByTablebase searchByTablebase;

    private String zygosityTableDirectory = "C:/java/projects/chess/chess-utils/books/syzygy/3-4-5";

    @BeforeEach
    public void setup() {
        searchByTablebase = SearchByTablebase.open(zygosityTableDirectory);
    }

    @Test
    public void test01() {
        Game game = Game.from(FEN.of("8/8/8/8/8/8/2Rk4/1K6 b - - 0 1"));
        SearchContext searchContext = new SearchContext()
                .setGame(game);

        SearchResult result = searchByTablebase.search(searchContext);

        Move move = result.getBestMove();

        assertEquals(Square.d2, move.getFrom().square());
        assertEquals(Square.d3, move.getTo().square());
    }

    @Test
    public void test02() {
        Game game = Game.from(FEN.of("8/8/8/8/8/4k3/2R5/1K6 w - - 0 1"));
        SearchContext searchContext = new SearchContext()
                .setGame(game);

        SearchResult result = searchByTablebase.search(searchContext);

        Move move = result.getBestMove();

        assertEquals(Square.c2, move.getFrom().square());
        assertEquals(Square.c4, move.getTo().square());
    }
}
