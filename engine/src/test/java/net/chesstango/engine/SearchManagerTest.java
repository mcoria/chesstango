package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.*;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class SearchManagerTest {

    private SearchManager searchManager;

    @Mock
    private SearchChain searchChain;

    @Mock
    private TimeMgmt timeMgmt;

    @Mock
    private SearchInvoker searchInvoker;

    @Mock
    private SearchListener listener;

    @Captor
    private ArgumentCaptor<Integer> depthCaptor;

    private ScheduledExecutorService timeOutExecutor;

    private SearchResultByDepth expectedResultByDepth;

    private SearchResponse expectedResult;

    private Game game;


    @BeforeEach
    public void setup() {
        timeOutExecutor = Executors.newSingleThreadScheduledExecutor();
        expectedResultByDepth = new SearchResultByDepth(1);
        expectedResult = () -> null;

        game = Game.from(FEN.of(FENParser.INITIAL_FEN));

        when(searchInvoker.searchImp(any(Game.class), any(Integer.class), any(Predicate.class), any(SearchListener.class)))
                .thenAnswer(invocation -> {
                    SearchListener listener = invocation.getArgument(3);
                    listener.searchStarted();
                    Thread.sleep(1000);
                    listener.searchInfo(expectedResultByDepth);
                    Thread.sleep(1000);
                    listener.searchFinished(expectedResult);
                    return CompletableFuture.completedFuture(expectedResult);
                });

        searchManager = new SearchManager(10, searchChain, timeMgmt, searchInvoker, timeOutExecutor);
    }

    @AfterEach
    public void tearDown() throws Exception {
        searchManager.close();
        timeOutExecutor.close();
    }

    @Test
    public void testStartSearchInfinite() {
        Future<SearchResponse> searchResultFuture = searchManager.searchInfinite(game, listener);

        verify(searchInvoker).searchImp(eq(game), eq(10), any(Predicate.class), any(SearchListener.class));
        verify(searchChain, never()).stopSearching();

        assertSearchListener();
        assertResult(searchResultFuture);
    }


    @Test
    public void testStartSearchDepth() {
        Future<SearchResponse> searchResultFuture = searchManager.searchDepth(game, 3, listener);

        verify(searchInvoker).searchImp(eq(game), eq(3), any(Predicate.class), any(SearchListener.class));
        verify(searchChain, never()).stopSearching();

        assertResult(searchResultFuture);
        assertSearchListener();
    }

    @Test
    public void testStartSearchTime() {
        Future<SearchResponse> searchResultFuture = searchManager.searchTime(game, 10000, listener);

        verify(searchInvoker).searchImp(eq(game), eq(10), any(Predicate.class), any(SearchListener.class));
        verify(searchChain, never()).stopSearching();

        assertResult(searchResultFuture);
        assertSearchListener();
    }

    @Test
    public void testStartSearchTime_WithTimeOut() {
        Future<SearchResponse> searchResultFuture = searchManager.searchTime(game, 100, listener);

        verify(searchInvoker).searchImp(eq(game), eq(10), any(Predicate.class), any(SearchListener.class));
        verify(searchChain).stopSearching();

        assertResult(searchResultFuture);
        assertSearchListener();
    }

    private void assertResult(Future<SearchResponse> searchResultFuture) {
        try {
            SearchResponse searchResult = searchResultFuture.get();
            assertSame(expectedResult, searchResult);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertSearchListener() {
        verify(listener).searchStarted();
        verify(listener).searchInfo(expectedResultByDepth);
        verify(listener).searchFinished(expectedResult);
    }
}
