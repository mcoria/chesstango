package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class FitnessByEpdBestMove extends FitnessByEpdAbstract {
    private static final Logger logger = LoggerFactory.getLogger(FitnessByEpdBestMove.class);
    private static final int MAX_DEPTH = 1;
    private static final List<String> EPD_FILES = List.of(
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\players\\Kasparov-only-nobook-1k.epd"
    );

    public FitnessByEpdBestMove() {
        super(EPD_FILES, MAX_DEPTH);
    }

    public FitnessByEpdBestMove(List<String> epdFiles, int depth) {
        super(EPD_FILES, MAX_DEPTH);
    }

    @Override
    protected Supplier<Search> createSearchSupplier(Supplier<Evaluator> gameEvaluatorSupplier) {
        return () -> new AlphaBetaBuilder()
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


    /**
     * La unica verdad, es la realidad..... o lo que queremos predecir
     */
    @Override
    protected long getPoints(EPD epd, SearchResult searchResult) {
        if (epd.isMoveSuccess(searchResult.getBestMove())) {
            return 1;
        }
        return 0;
    }

    /**
     * Esta funcion optimiza la mitad del juego donde existe mayor cantiaad de movimientos posibles
     */
    protected static long getPointsV1(EPD epd, SearchResult searchResult) {
        Game game = FENDecoder.loadGame(epd.getFenWithoutClocks());
        int possibleMoves = game.getPossibleMoves().size();
        if (epd.isMoveSuccess(searchResult.getBestMove())) {
            return (possibleMoves - 1);
        }
        return 0;
    }


    /**
     * SearchByDepthResult es la lista de resultados de busqueda por profundidad
     * Mientras más profundo el resultado de esta busqueda sea exitoso implica que mayor numero de comparaciones
     * se realizó.
     *
     * @param epd
     * @param searchResult
     * @return
     */
    protected static long getPointsDepthV2(EPD epd, SearchResult searchResult) {
        List<Move> bestMoveList = searchResult
                .getSearchResultByDepths()
                .stream()
                .map(SearchResultByDepth::getBestMove)
                .toList();

        Game game = FENDecoder.loadGame(epd.getFenWithoutClocks());
        int possibleMoves = game.getPossibleMoves().size();

        int points = 0;
        int i = 1;
        for (Move bestMove : bestMoveList) {
            if (epd.isMoveSuccess(bestMove)) {
                points += i * i * (possibleMoves - 1);
            }
            i++;
        }
        return points;
    }

}
