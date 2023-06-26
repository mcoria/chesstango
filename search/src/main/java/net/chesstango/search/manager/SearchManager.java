package net.chesstango.search.manager;


import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.search.*;
import net.chesstango.search.polyglot.MappedPolyglotBook;
import net.chesstango.search.polyglot.PolyglotEntry;
import net.chesstango.search.smart.IterativeDeepening;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class SearchManager {
    private ScheduledExecutorService executorService;
    private final SearchMove searchMove;
    private final SearchListener listenerClient;
    private final MappedPolyglotBook book;
    private final boolean searByBookEnabled = false;

    public SearchManager(SearchMove searchMove, SearchListener listenerClient) {
        this.searchMove = searchMove;
        this.listenerClient = listenerClient;
        this.book = new MappedPolyglotBook();

        if (searchMove instanceof DefaultSearchMove) {
            DefaultSearchMove searchMoveDefault = (DefaultSearchMove) searchMove;
            SearchMove searchImp = searchMoveDefault.getImplementation();

            if (searchImp instanceof IterativeDeepening) {
                ((IterativeDeepening) searchImp).setSearchStatusListener(listenerClient::searchInfo);
            }
        }
    }

    public void searchInfinite(Game game) {
        searchImp(game, Integer.MAX_VALUE, null);
    }

    public void searchUpToDepth(Game game, int depth) {
        searchImp(game, depth, null);
    }

    public void searchUpToTime(Game game, int timeOut) {
        searchImp(game, Integer.MAX_VALUE, timeOut);
    }

    public void reset() {
        searchMove.reset();
    }

    public void stopSearching() {
        searchMove.stopSearching();
    }

    public void open() {
        executorService = Executors.newScheduledThreadPool(2);
        if (searByBookEnabled) {
            book.load(Path.of("C:\\Java\\projects\\chess\\chess-utils\\books\\openings\\polyglot-collection\\final-book.bin"));
        }
    }

    public void close() {
        try {
            executorService.shutdown();
            executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
            if (searByBookEnabled) {
                book.close();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void searchImp(Game game, int depth, Integer timeOut) {
        executorService.execute(() -> {
            try {
                listenerClient.searchStarted();

                SearchMoveResult searchResult = null;

                if (searByBookEnabled) {
                    searchResult = searchByBook(game);
                }

                if (searchResult == null) {
                    searchResult = searchByAlgorithm(game, depth, timeOut);
                }

                listenerClient.searchFinished(searchResult);
            } catch (RuntimeException exception) {
                exception.printStackTrace(System.err);
            }
        });
    }

    protected SearchMoveResult searchByBook(Game game) {
        List<PolyglotEntry> bookSearchResult = book.search(game.getChessPosition().getZobristHash());
        if (bookSearchResult != null) {
            MoveContainerReader possibleMoves = game.getPossibleMoves();
            for (PolyglotEntry polyglotEntry : bookSearchResult) {
                Move move = possibleMoves.getMove(polyglotEntry.from(), polyglotEntry.to());
                if (move != null) {
                    return new SearchMoveResult(1, 0, move, null);
                }
            }
        }
        return null;
    }

    protected SearchMoveResult searchByAlgorithm(Game game, int depth, Integer timeOut) {
        if (timeOut != null) {
            executorService.schedule(this::stopSearching, timeOut, TimeUnit.MILLISECONDS);
        }

        SearchMoveResult searchResult = null;

        try {
            searchResult = searchMove.search(game, depth);
        } catch (StopSearchingException spe) {
            searchResult = spe.getSearchMoveResult();
            listenerClient.searchStopped();
        }

        return searchResult;
    }
}
