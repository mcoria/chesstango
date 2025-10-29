package net.chesstango.engine;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.search.Search;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.visitors.ChainPrinterVisitor;
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
    public void testSearch() {
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
            Session session = tango.newSession(FEN.of("8/8/2P5/8/8/pP6/K2k4/3r4 b - - 0 6 "));
            session.setMoves(List.of());
            Future<SearchResponse> searchResponseFuture =  session.goDepth(1);
            SearchResponse searchResponse = searchResponseFuture.get();
            System.out.println(searchResponse.getMove());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
