package net.chesstango.engine;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.search.Search;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Future;

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
            tango.newSession(FEN.of(FENParser.INITIAL_FEN));
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
            Session session = tango.newSession(FEN.of("8/8/2P5/8/8/pP6/K2k4/3r4 b - - 0 1"));
            session.setMoves(List.of());
            Future<SearchResponse> searchResponseFuture =  session.goDepth(1);
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
            Session session = tango.newSession(FEN.of("8/8/3P4/8/5k2/p2K1p2/P7/8 b - - 0 1"));
            session.setMoves(List.of());
            Future<SearchResponse> searchResponseFuture =  session.goDepth(5);
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
            Session session = tango.newSession(FEN.of("8/8/8/8/8/2k2KNp/5P1r/2R5 b - - 6 69"));
            session.setMoves(List.of());
            Future<SearchResponse> searchResponseFuture =  session.goDepth(4);
            SearchResponse searchResponse = searchResponseFuture.get();
            System.out.println(searchResponse.move());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
