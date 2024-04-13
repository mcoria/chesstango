package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EPDEntry;
import net.chesstango.board.representations.EPDReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class FitnessBySearch implements FitnessFunction {
    private static final Logger logger = LoggerFactory.getLogger(FitnessBySearch.class);
    private static final int MAX_DEPTH = 3;
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
    private final List<EPDEntry> edpEntries;


    public FitnessBySearch() {
        this(EPD_FILES, MAX_DEPTH);
    }

    public FitnessBySearch(List<String> epdFiles, int depth) {
        this.epdFiles = epdFiles;
        this.edpEntries = new LinkedList<>();
        this.depth = depth;
    }

    @Override
    public long fitness(GameEvaluator gameEvaluator) {
        SearchMove searchMove = AlphaBetaBuilder.createDefaultBuilderInstance(gameEvaluator)
                .build();

        return run(searchMove);
    }

    @Override
    public void start() {
        EPDReader reader = new EPDReader();

        epdFiles.stream().map(reader::readEdpFile).forEach(edpEntries::addAll);
    }

    @Override
    public void stop() {
    }

    protected long run(SearchMove searchMove) {
        long points = 0;

        final int printProgress = edpEntries.size() / 4;
        int processedEntries = 0;
        for (EPDEntry EPDEntry : edpEntries) {
            points += run(EPDEntry, searchMove);
            processedEntries++;
            if (processedEntries % printProgress == 0) {
                logger.info("Processed {} / {}", processedEntries, edpEntries.size());
            }
        }

        return points;
    }

    protected long run(EPDEntry epdEntry, SearchMove searchMove) {

        Game game = FENDecoder.loadGame(epdEntry.fen);

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, depth);

        SearchMoveResult searchResult = searchMove.search(game);

        searchResult.setId(epdEntry.id);

        searchMove.reset();

        return getPoints(epdEntry, searchResult);
    }

    protected long getPoints(EPDEntry epdEntry, SearchMoveResult searchMoveResult) {
        List<Move> bestMoveList = searchMoveResult.getSearchByDepthResultList()
                .stream()
                .map(SearchByDepthResult::getBestMove)
                .toList();

        long points = 0;
        for (int i = 0; i < bestMoveList.size(); i++) {
            Move bestMove = bestMoveList.get(i);
            if (epdEntry.isMoveSuccess(bestMove)) {
                points += (long) (i + 1) * (i + 1);
            }
        }

        return points;
    }

}
