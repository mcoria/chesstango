package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaSearchesTest {
    private AlphaBetaBuilder alphaBetaBuilder;

    @BeforeEach
    public void setup() {
        alphaBetaBuilder = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(new EvaluatorByMaterial());
    }


    @Test
    public void testSearch_00() {
        Game game = Game.from(FEN.START_POSITION);

        Search search = alphaBetaBuilder.build();

        search.accept(new SetMaxDepthVisitor(6));
        SearchResult searchResult = search.startSearch(game);

        Move bm = searchResult.getBestMove();

        assertNotNull(bm);

        assertEquals(Piece.KNIGHT_WHITE, bm.getFrom().piece());
        assertEquals(Square.g1, bm.getFrom().square());
        assertEquals(Square.h3, bm.getTo().square());
    }

    @Test
    public void testSearch_02() {
        Game game = Game.from(FEN.START_POSITION);

        Search search = alphaBetaBuilder
                .withStatistics()
                .build();

        search.accept(new SetMaxDepthVisitor(6));
        SearchResult searchResult = search.startSearch(game);

        Move bm = searchResult.getBestMove();

        assertNotNull(bm);

        assertEquals(Piece.KNIGHT_WHITE, bm.getFrom().piece());
        assertEquals(Square.g1, bm.getFrom().square());
        assertEquals(Square.h3, bm.getTo().square());
    }

    @Test
    public void testSearch_03() {
        Game game = Game.from(FEN.START_POSITION);

        Search search = alphaBetaBuilder
                .withStatistics()
                //.withDebugSearchTree(false, true, true)
                .build();

        search.accept(new SetMaxDepthVisitor(6));
        SearchResult searchResult = search.startSearch(game);

        Move bm = searchResult.getBestMove();

        assertNotNull(bm);

        assertEquals(Piece.KNIGHT_WHITE, bm.getFrom().piece());
        assertEquals(Square.g1, bm.getFrom().square());
        assertEquals(Square.h3, bm.getTo().square());
    }

    @Test
    public void testSearch_40H_069() {
        Game game = Game.from(FEN.of("1B1Q1R2/8/qNrn3p/2p1rp2/Rn3k1K/8/5P2/bbN4B w - - 0 1"));

        Search search = alphaBetaBuilder
                //.withStatistics()
                .withDebugSearchTree(false, true, true)
                .build();

        search.accept(new SetMaxDepthVisitor(3));
        SearchResult searchResult = search.startSearch(game);

        Move bm = searchResult.getBestMove();

        assertNotNull(bm);

        assertEquals(Piece.QUEEN_WHITE, bm.getFrom().piece());
        assertEquals(Square.d8, bm.getFrom().square());
        assertEquals(Square.f6, bm.getTo().square());
    }

}
