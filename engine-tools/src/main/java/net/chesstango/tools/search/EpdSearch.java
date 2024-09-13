package net.chesstango.tools.search;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SANEncoder;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.Search;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
@Setter
public class EpdSearch {
    private static final Logger logger = LoggerFactory.getLogger(EpdSearch.class);

    private static final SANEncoder sanEncoder = new SANEncoder();

    @Accessors(chain = true)
    private Supplier<Search> searchMoveSupplier;

    @Accessors(chain = true)
    private int depth;

    @Accessors(chain = true)
    private Integer timeOut;


    public List<EpdSearchResult> run(Stream<EPD> edpEntries) {
        final int availableCores = Runtime.getRuntime().availableProcessors();

        AtomicInteger pendingJobsCounter = new AtomicInteger(0);

        List<SearchJob> activeJobs = Collections.synchronizedList(new LinkedList<>());

        List<EpdSearchResult> epdSearchResults = Collections.synchronizedList(new LinkedList<>());

        BlockingQueue<Search> searchPool = new LinkedBlockingDeque<>(availableCores);
        for (int i = 0; i < availableCores; i++) {
            searchPool.add(searchMoveSupplier.get());
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(availableCores)) {
            edpEntries.forEach(epd -> {
                pendingJobsCounter.incrementAndGet();
                executorService.submit(() -> {
                    SearchJob searchJob = null;
                    try {
                        Instant startInstant = Instant.now();

                        Search search = searchPool.take();

                        searchJob = new SearchJob(startInstant, search);

                        activeJobs.add(searchJob);

                        EpdSearchResult epdSearchResult = run(search, epd);

                        epdSearchResults.add(epdSearchResult);

                        /*
                        if (!epdSearchResult.isSearchSuccess()) {
                            String failedTest = String.format("Fail [%s] - best move found %s", epd.getText(), epdSearchResult.bestMoveFoundAlgNot());
                            logger.info(failedTest);
                        }
                         */

                        searchPool.put(searchJob.search);

                    } catch (RuntimeException e) {
                        e.printStackTrace(System.err);
                        logger.error(String.format("Error processing: %s", epd.getText()));
                        throw e;
                    } catch (InterruptedException e) {
                        logger.error(String.format("Thread interrupted while processing: %s", epd.getText()));
                        e.printStackTrace(System.err);
                        throw new RuntimeException(e);
                    } finally {
                        assert searchJob != null;

                        activeJobs.remove(searchJob);

                        pendingJobsCounter.decrementAndGet();
                    }
                });
            });

            try {
                if (timeOut != null) {
                    while (pendingJobsCounter.get() > 0) {
                        Thread.sleep(500);
                        activeJobs.forEach(searchJob -> {
                            if (searchJob.elapsedMillis() >= timeOut) {
                                searchJob.search.stopSearching();
                            }
                        });
                    }
                }
            } catch (InterruptedException e) {
                logger.error("Stopping executorService....");
                executorService.shutdownNow();
            }
        }


        if (epdSearchResults.isEmpty()) {
            throw new RuntimeException("No edp entry was processed");
        }

        if (pendingJobsCounter.get() > 0) {
            throw new RuntimeException(String.format("Todavia siguen pendiente %d busquedas", pendingJobsCounter.get()));
        }

        epdSearchResults.sort(Comparator.comparing(o -> o.epd().getId()));

        return epdSearchResults;
    }


    public EpdSearchResult run(Search search, EPD epd) {
        Game game = FENDecoder.loadGame(epd.getFenWithoutClocks());

        search.setSearchParameter(SearchParameter.MAX_DEPTH, depth);
        search.setSearchParameter(SearchParameter.EPD_PARAMS, epd);

        SearchMoveResult searchResult = search.search(game);

        searchResult.setId(epd.getId());

        search.reset();

        Move bestMove = searchResult.getBestMove();

        String bestMoveAlgNotation = sanEncoder.encodeAlgebraicNotation(bestMove, game.getPossibleMoves());

        return new EpdSearchResult(epd, searchResult,
                bestMoveAlgNotation,
                epd.isMoveSuccess(bestMove),
                calculateAccuracy(epd, searchResult.getSearchByDepthResults())
        );
    }


    private int calculateAccuracy(EPD epd, List<SearchByDepthResult> searchByDepthResults) {
        if (!searchByDepthResults.isEmpty()) {
            long successCounter = searchByDepthResults
                    .stream()
                    .map(SearchByDepthResult::getBestMove)
                    .filter(epd::isMoveSuccess)
                    .count();
            return (int) (successCounter * 100 / searchByDepthResults.size());
        }
        return 0;
    }

    private record SearchJob(Instant startInstant, Search search) {
        public long elapsedMillis() {
            return Duration.between(startInstant, Instant.now()).toMillis();
        }
    }
}
