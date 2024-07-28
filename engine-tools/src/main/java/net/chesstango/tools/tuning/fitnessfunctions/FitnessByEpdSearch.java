package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.epd.EPDDecoder;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.tools.search.EpdSearch;
import net.chesstango.tools.search.EpdSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
public class FitnessByEpdSearch implements FitnessFunction {
    private static final Logger logger = LoggerFactory.getLogger(FitnessByEpdSearch.class);
    private static final int MAX_DEPTH = 1;
    private static final List<String> EPD_FILES = List.of(
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\players\\Kasparov-only-nobook-1k.epd"
    );

    private final List<String> epdFiles;
    private final int depth;
    private final List<EPD> edpEntries;


    public FitnessByEpdSearch() {
        this(EPD_FILES, MAX_DEPTH);
    }

    public FitnessByEpdSearch(List<String> epdFiles, int depth) {
        this.epdFiles = epdFiles;
        this.edpEntries = new LinkedList<>();
        this.depth = depth;
    }

    @Override
    public long fitness(Supplier<Evaluator> gameEvaluatorSupplier) {
        EpdSearch epdSearch = new EpdSearch();

        epdSearch.setDepth(depth);

        epdSearch.setSearchMoveSupplier(() ->
                new AlphaBetaBuilder()
                        .withGameEvaluator(gameEvaluatorSupplier.get())
                        .withGameEvaluatorCache()

                        .withQuiescence()

                        .withTranspositionTable()

                        .withTranspositionMoveSorter()
                        .withKillerMoveSorter()
                        .withRecaptureSorter()
                        .withMvvLvaSorter()

                        .withEpdHypothesisValidator()

                        .build()
        );

        List<EpdSearchResult> epdSearchResults = epdSearch.run(edpEntries.stream());

        return epdSearchResults
                .stream()
                .mapToLong(epdSearchResult -> getPoints(epdSearchResult.epd(), epdSearchResult.searchResult()))
                .sum();
    }

    @Override
    public void start() {
        EPDDecoder reader = new EPDDecoder();

        epdFiles.forEach(fileName -> reader.readEdpFile(fileName).forEach(edpEntries::add));
    }

    @Override
    public void stop() {
    }


    /**
     * La unica verdad, es la realidad..... o lo que queremos predecir
     */
    protected static long getPoints(EPD epd, SearchMoveResult searchMoveResult) {
        if (epd.isMoveSuccess(searchMoveResult.getBestMove())) {
            return 1;
        }
        return 0;
    }

    /**
     * Esta funcion optimiza la mitad del juego donde existe mayor cantiaad de movimientos posibles
     */
    protected static long getPointsV1(EPD epd, SearchMoveResult searchMoveResult) {
        Game game = FENDecoder.loadGame(epd.getFenWithoutClocks());
        int possibleMoves = game.getPossibleMoves().size();
        if (epd.isMoveSuccess(searchMoveResult.getBestMove())) {
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
     * @param searchMoveResult
     * @return
     */
    protected static long getPointsDepthV2(EPD epd, SearchMoveResult searchMoveResult) {
        List<Move> bestMoveList = searchMoveResult
                .getSearchByDepthResults()
                .stream()
                .map(SearchByDepthResult::getBestMove)
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
