package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.board.Game;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.builders.BottomMoveCounterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class FitnessByEpdBottomMoveCounter extends FitnessByEpdAbstract {
    private static final Logger logger = LoggerFactory.getLogger(FitnessByEpdBottomMoveCounter.class);
    private static final int MAX_DEPTH = 1;
    private static final List<String> EPD_FILES = List.of(
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\players\\Kasparov-only-nobook-1k.epd"
    );


    public FitnessByEpdBottomMoveCounter() {
        super(EPD_FILES, MAX_DEPTH);
    }

    public FitnessByEpdBottomMoveCounter(List<String> epdFiles, int depth) {
        super(epdFiles, depth);
    }

    @Override
    protected Supplier<Search> createSearchSupplier(Supplier<Evaluator> gameEvaluatorSupplier) {
        return () ->
                new BottomMoveCounterBuilder()
                        .withGameEvaluator(gameEvaluatorSupplier.get())
                        .withGameEvaluatorCache()

                        .withQuiescence()

                        .withTranspositionTable()

                        .withTranspositionMoveSorter()
                        .withKillerMoveSorter()
                        .withRecaptureSorter()
                        .withMvvLvaSorter()

                        .build();
    }

    @Override
    protected long getPoints(EPD epd, SearchResult searchResult) {
        Game game = FENDecoder.loadGame(epd.getFenWithoutClocks());

        int possibleMoves = game.getPossibleMoves().size();

        if (possibleMoves == 1) {
            return 100;
        }

        return 100L * searchResult.getBottomMoveCounter() / (possibleMoves - 1);
    }
}
