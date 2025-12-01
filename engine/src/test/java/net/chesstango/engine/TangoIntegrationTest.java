package net.chesstango.engine;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.search.Search;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Mauricio Coria
 */
public class TangoIntegrationTest {

    private Config config;

    @BeforeEach
    public void setup() {
        config = new Config();
    }

    @Test
    @Disabled
    public void testOpenTango() {
        config.setPolyglotFile("C:/java/projects/chess/chess-utils/books/openings/polyglot-collection/komodo.bin");
        //config.setSyzygyDirectory("C:/java/projects/chess/chess-utils/books/syzygy/3-4-5");
        //config.setSyncSearch(true);

        try (Tango tango = Tango.open(config);) {
            tango.newSession();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Disabled
    public void testSearch01() {
        Search search = AlphaBetaBuilder.createDefaultBuilderInstance()
                .withGameEvaluator(Evaluator.getInstance())
                .withDebugSearchTree(false, false, false)
                .build();

        //config.setPolyglotFile("C:/java/projects/chess/chess-utils/books/openings/polyglot-collection/komodo.bin");
        config.setSyzygyDirectory("C:/java/projects/chess/chess-utils/books/syzygy/3-4-5");
        config.setSearch(search);
        config.setSyncSearch(true);

        try (Tango tango = Tango.open(config)) {
            //new ChainPrinterVisitor().print(search, System.out);
            Session session = tango.newSession();
            session.setFen(FEN.of("8/8/2P5/8/8/pP6/K2k4/3r4 b - - 0 1"));
            session.setMoves(List.of());
            Future<SearchResponse> searchResponseFuture = session.goDepth(1);
            SearchResponse searchResponse = searchResponseFuture.get();
            System.out.println(searchResponse.move());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Disabled
    public void testSearch02() {
        Search search = AlphaBetaBuilder.createDefaultBuilderInstance()
                .withGameEvaluator(Evaluator.getInstance())
                .withDebugSearchTree(false, false, false)
                .build();

        //config.setPolyglotFile("C:/java/projects/chess/chess-utils/books/openings/polyglot-collection/komodo.bin");
        config.setSyzygyDirectory("C:/java/projects/chess/chess-utils/books/syzygy/3-4-5");
        config.setSearch(search);
        config.setSyncSearch(true);

        try (Tango tango = Tango.open(config)) {
            //new ChainPrinterVisitor().print(search, System.out);
            Session session = tango.newSession();
            session.setFen(FEN.of("8/8/3P4/8/5k2/p2K1p2/P7/8 b - - 0 1"));
            session.setMoves(List.of());
            Future<SearchResponse> searchResponseFuture = session.goDepth(5);
            SearchResponse searchResponse = searchResponseFuture.get();
            System.out.println(searchResponse.move());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Disabled
    public void testSearch03() {
        Search search = AlphaBetaBuilder.createDefaultBuilderInstance()
                .withGameEvaluator(Evaluator.getInstance())
                .withDebugSearchTree(true, false, false)
                .build();

        //config.setPolyglotFile("C:/java/projects/chess/chess-utils/books/openings/polyglot-collection/komodo.bin");
        config.setSyzygyDirectory("C:/java/projects/chess/chess-utils/books/syzygy/3-4-5");
        config.setSearch(search);
        config.setSyncSearch(true);

        try (Tango tango = Tango.open(config)) {
            //new ChainPrinterVisitor().print(search, System.out);
            Session session = tango.newSession();
            session.setFen(FEN.of("8/8/8/8/8/2k2KNp/5P1r/2R5 b - - 6 69"));
            session.setMoves(List.of());
            Future<SearchResponse> searchResponseFuture = session.goDepth(4);
            SearchResponse searchResponse = searchResponseFuture.get();
            System.out.println(searchResponse.move());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testThrowRuntimeException() {
        config.setSyncSearch(true);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            String moves = "e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e5 d4b5 d7d6 c1g5 a7a6 b5a3 b7b5 c3d5 f8e7 g5f6 e7f6 c2c3 e8g8 a3c2 f8e8 f1e2 f6g5 e1g1 c8e6 a2a4 a8b8 a4b5 a6b5 a1a6 d8c8 d5b4 c6b4 c2b4 g5e7 e2f3 c8b7 d1c2 b8d8 f1a1 d8c8 a6a7 b7b6 b4d5 e6d5 e4d5 b5b4 c2b3 e7h4 g2g3 h4g5 a1a6 b6c5 b3b4 c5b4 c3b4 e8f8 a6d6 c8c1 g1g2 c1c2 d6c6 c2b2 a7c7 b2b4 d5d6 b4d4 h2h4 g5f6 f3h5 f8d8 h5f7 g8h8 h4h5 d4d6 c6d6 d8d6 c7c8 d6d8 c8d8 f6d8 g2f3 g7g6 h5g6 h7g6 f7g6 h8g7 g6d3 d8b6 d3f5 g7f6 f5h7 f6e6 h7g6 e6d5 g6f5 d5d4 f5e4 b6c5 e4h7 c5e7 h7f5 e7f6 f5g6 f6e7 g6h7 e7b4 h7f5 b4d2 f5g6 d2b4 g3g4 b4d2 g6b1 d2g5 b1f5 g5f6 f5e4 f6g5 e4b1 g5f6 b1h7 f6e7 h7g6 e7f6 g6f5 f6g5 f5e4 g5h4 e4b7 h4f6 b7a8 f6h4 a8c6 h4g5 c6b5 e5e4 f3g3 d4e5 b5c4 g5d2 g3g2 d2g5 c4b5 e5f4 b5e2 g5e7 g2h3 e7c5 h3g2 c5e7 g2h2 e7c5 h2g1 c5d6 g1g2 d6e7";
            String[] movesArray = moves.split(" ");

            try (Tango tango = Tango.open(config)) {
                Session session = tango.newSession();
                session.setFen(FEN.of(FENParser.INITIAL_FEN));
                session.setMoves(Arrays.asList(movesArray));
                session.goDepth(1);
            }
        });
        assertEquals("java.lang.RuntimeException: Game is already finished", runtimeException.getMessage());
    }
}
