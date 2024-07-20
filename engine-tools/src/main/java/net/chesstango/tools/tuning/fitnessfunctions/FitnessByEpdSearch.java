package net.chesstango.tools.tuning.fitnessfunctions;


import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.epd.EPDDecoder;
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
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd",
            "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\sbd.epd",
            "C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Nolot.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS1.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS2.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS3.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS4.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS5.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS6.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS7.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS8.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS9.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS10.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS11.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS12.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS13.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS14.epd",
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS15.epd"
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
        epdSearch.setSearchMoveSupplier(() -> AlphaBetaBuilder.createDefaultBuilderInstance(gameEvaluatorSupplier.get()).build());

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


    protected long getPoints(EPD epd, SearchMoveResult searchMoveResult) {
        List<Move> bestMoveList = searchMoveResult
                .getSearchByDepthResultList()
                .stream()
                .map(SearchByDepthResult::getBestMove)
                .toList();

        long points = 0;
        for (int i = 0; i < bestMoveList.size(); i++) {
            Move bestMove = bestMoveList.get(i);
            if (epd.isMoveSuccess(bestMove)) {
                points += (long) (i + 1) * (i + 1);
            }
        }

        return points;
    }

}
