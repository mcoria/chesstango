package net.chesstango.tools.tuning.fitnessfunctions;

import lombok.Setter;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.epd.EPDDecoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.builders.BottomMoveCounterBuilder;
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
public class FitnessByEpdBottomMoveCounter implements FitnessFunction {
    private static final Logger logger = LoggerFactory.getLogger(FitnessByEpdBottomMoveCounter.class);
    private static final int MAX_DEPTH = 1;
    private static final List<String> EPD_FILES = List.of(
            "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\players\\Kasparov-only-nobook-1k.epd"
    );

    private final int depth;
    private final List<String> epdFiles;
    private final List<EPD> edpEntries;


    public FitnessByEpdBottomMoveCounter() {
        this(EPD_FILES, MAX_DEPTH);
    }

    public FitnessByEpdBottomMoveCounter(List<String> epdFiles, int depth) {
        this.epdFiles = epdFiles;
        this.depth = depth;
        this.edpEntries = new LinkedList<>();
    }

    @Override
    public long fitness(Supplier<Evaluator> gameEvaluatorSupplier) {
        EpdSearch epdSearch = new EpdSearch();

        epdSearch.setDepth(depth);

        epdSearch.setSearchMoveSupplier(() ->
                new BottomMoveCounterBuilder()
                        .withGameEvaluator(gameEvaluatorSupplier.get())
                        .withGameEvaluatorCache()

                        .withQuiescence()

                        .withTranspositionTable()

                        .withTranspositionMoveSorter()
                        .withKillerMoveSorter()
                        .withRecaptureSorter()
                        .withMvvLvaSorter()

                        .build()
        );

        List<EpdSearchResult> epdSearchResults = epdSearch.run(edpEntries.stream());

        return epdSearchResults
                .stream()
                .mapToLong(EpdSearchResult::getBottomMoveCounter)
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

}
