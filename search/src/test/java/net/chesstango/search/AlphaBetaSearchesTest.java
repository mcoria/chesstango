package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
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
        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));

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
        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));

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
        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));

        Search search = alphaBetaBuilder
                .withStatistics()
                .withDebugSearchTree(null, false, true, true)
                .build();

        search.accept(new SetMaxDepthVisitor(6));
        SearchResult searchResult = search.startSearch(game);

        Move bm = searchResult.getBestMove();

        assertNotNull(bm);

        assertEquals(Piece.KNIGHT_WHITE, bm.getFrom().piece());
        assertEquals(Square.g1, bm.getFrom().square());
        assertEquals(Square.h3, bm.getTo().square());
    }

}
