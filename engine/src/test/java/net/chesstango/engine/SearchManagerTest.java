package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.engine.timemgmt.TimeMgmt;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private SearchListener listener;

    @Captor
    private ArgumentCaptor<SearchContext> searchContextCaptor;

    private ExecutorService searchExecutor;

    private ScheduledExecutorService timeOutExecutor;

    private SearchResult expectedResult;

    @BeforeEach
    public void setup() {
        searchExecutor = Executors.newSingleThreadExecutor();
        timeOutExecutor = Executors.newSingleThreadScheduledExecutor();

        expectedResult = new SearchResult();
        searchManager = new SearchManager(10, searchChain, timeMgmt, searchExecutor, timeOutExecutor);

        when(searchChain.search(any(SearchContext.class))).thenReturn(expectedResult);
    }

    @AfterEach
    public void tearDown() throws Exception {
        searchManager.close();
        searchExecutor.close();
        timeOutExecutor.close();
    }

    @Test
    public void testStartSearchInfinite() {
        Future<SearchResult> searchResultFuture = searchManager.searchInfinite(Game.from(FEN.of(FENParser.INITIAL_FEN)), listener);

        assertResult(searchResultFuture);
        assertStartSearchListener();

        verify(searchChain).search(searchContextCaptor.capture());

        SearchContext searchContext = searchContextCaptor.getValue();
        assertEquals(FEN.of(FENParser.INITIAL_FEN), searchContext.getGame().getCurrentFEN());
        assertEquals(10, searchContext.getDepth());

        Predicate<SearchResultByDepth> predicate = searchContext.getSearchPredicate();
        assertTrue(predicate.test(null));
    }


    @Test
    public void testStartSearchDepth() {
        Future<SearchResult> searchResultFuture = searchManager.searchDepth(Game.from(FEN.of(FENParser.INITIAL_FEN)), 3, listener);

        assertResult(searchResultFuture);
        assertStartSearchListener();

        verify(searchChain).search(searchContextCaptor.capture());

        SearchContext searchContext = searchContextCaptor.getValue();
        assertEquals(FEN.of(FENParser.INITIAL_FEN), searchContext.getGame().getCurrentFEN());
        assertEquals(3, searchContext.getDepth());

        Predicate<SearchResultByDepth> predicate = searchContext.getSearchPredicate();
        assertTrue(predicate.test(null));
    }

    private void assertResult(Future<SearchResult> searchResultFuture) {
        try {
            SearchResult searchResult = searchResultFuture.get();
            assertSame(expectedResult, searchResult);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertStartSearchListener() {
        verify(listener).searchStarted();
        verify(listener).searchFinished(expectedResult);
    }
}
