package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    public static final int INFINITE_DEPTH = 10;

    private SearchManager searchManager;

    @Mock
    private SearchByAggregator searchByAggregator;

    @Mock
    private SearchByTree searchByTree;

    @Mock
    private TimeMgmt timeMgmt;

    @Mock
    private SearchInvoker searchInvoker;

    @Mock
    private SearchListener listener;

    @Mock
    private SearchResponse expectedResult;

    private ScheduledExecutorService timeOutExecutor;

    private Game game;


    @BeforeEach
    public void setup() {
        timeOutExecutor = Executors.newSingleThreadScheduledExecutor();

        game = Game.from(FEN.START_POSITION);

        when(searchInvoker.searchImp(any(Game.class), any(Integer.class), any(Predicate.class), any(SearchListener.class)))
                .thenAnswer(invocation -> {
                    SearchListener listener = invocation.getArgument(3);
                    listener.searchStarted();
                    Thread.sleep(1000);
                    listener.searchInfo("Info");
                    Thread.sleep(1000);
                    listener.searchFinished(expectedResult);
                    return CompletableFuture.completedFuture(expectedResult);
                });

        searchManager = new SearchManager(INFINITE_DEPTH, searchByTree, searchByAggregator, timeMgmt, searchInvoker, timeOutExecutor);
    }

    @AfterEach
    public void tearDown() throws Exception {
        timeOutExecutor.close();
    }

    @Test
    public void test_SearchInfinite() {
        Future<SearchResponse> searchResultFuture = searchManager.searchInfinite(game, listener);

        verify(searchInvoker).searchImp(eq(game), eq(INFINITE_DEPTH), any(Predicate.class), any(SearchListener.class));
        verify(searchByTree, never()).stopSearching();

        assertSearchListener();
        assertResult(searchResultFuture);
    }


    @Test
    public void test_SearchDepth() {
        Future<SearchResponse> searchResultFuture = searchManager.searchDepth(game, 3, listener);

        verify(searchInvoker).searchImp(eq(game), eq(3), any(Predicate.class), any(SearchListener.class));
        verify(searchByTree, never()).stopSearching();

        assertResult(searchResultFuture);
        assertSearchListener();
    }

    @Test
    public void test_SearchTime_NoTimeOut() {
        Future<SearchResponse> searchResultFuture = searchManager.searchTime(game, 10000, listener);

        verify(searchInvoker).searchImp(eq(game), eq(INFINITE_DEPTH), any(Predicate.class), any(SearchListener.class));
        verify(searchByTree, never()).stopSearching();

        assertResult(searchResultFuture);
        assertSearchListener();
    }

    @Test
    public void test_SearchTime_TimeOut() {
        Future<SearchResponse> searchResultFuture = searchManager.searchTime(game, 100, listener);

        verify(searchInvoker).searchImp(eq(game), eq(INFINITE_DEPTH), any(Predicate.class), any(SearchListener.class));
        verify(searchByTree, times(1)).stopSearching();

        assertResult(searchResultFuture);
        assertSearchListener();
    }

    @Test
    public void test_SearchFast_NoTimeOut() {
        when(timeMgmt.getTimeOut(any(Game.class), any(Integer.class), any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(5000);

        Future<SearchResponse> searchResultFuture = searchManager.searchFast (game, 60000, 0, 60000, 0, listener);

        verify(searchInvoker).searchImp(eq(game), eq(INFINITE_DEPTH), any(Predicate.class), any(SearchListener.class));
        verify(searchByTree, never()).stopSearching();

        assertResult(searchResultFuture);
        assertSearchListener();
    }

    @Test
    public void test_SearchFast_TimeOut() {
        when(timeMgmt.getTimeOut(any(Game.class), any(Integer.class), any(Integer.class), any(Integer.class), any(Integer.class))).thenReturn(100);

        Future<SearchResponse> searchResultFuture = searchManager.searchFast (game, 60000, 0, 60000, 0, listener);

        verify(searchInvoker).searchImp(eq(game), eq(INFINITE_DEPTH), any(Predicate.class), any(SearchListener.class));
        verify(searchByTree, times(1)).stopSearching();

        assertResult(searchResultFuture);
        assertSearchListener();
    }

    @Test
    public void test_SearchFast_ZeroTime() {
        // Observar que wTime = 0, lo cual es un valor invalido
        Future<SearchResponse> searchResultFuture = searchManager.searchFast (game, 0, 0, 60000, 0, listener);

        verify(searchInvoker).searchImp(eq(game), eq(INFINITE_DEPTH), any(Predicate.class), any(SearchListener.class));
        verify(searchByTree, never()).stopSearching();

        assertResult(searchResultFuture);
        assertSearchListener();
    }

    @Test
    public void test_SearchFast_NegativeTime() {
        // Observar que wTime = 0, lo cual es un valor invalido
        Future<SearchResponse> searchResultFuture = searchManager.searchFast (game, -60000, 0, 60000, 0, listener);

        verify(searchInvoker).searchImp(eq(game), eq(INFINITE_DEPTH), any(Predicate.class), any(SearchListener.class));
        verify(searchByTree, never()).stopSearching();

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
        verify(listener).searchInfo("Info");
        verify(listener).searchFinished(expectedResult);
    }
}
