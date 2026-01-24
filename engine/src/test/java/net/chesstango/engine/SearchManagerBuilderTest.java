package net.chesstango.engine;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class SearchManagerBuilderTest {

    private SearchManagerBuilder builder;

    @Mock
    private ExecutorService executorService;

    @Mock
    private ScheduledExecutorService scheduledExecutorService;

    @Mock
    private SearchByOpenBook searchByOpenBook;

    @Mock
    private SearchByTablebase searchByTablebase;

    @Mock
    private SearchByTree searchByTree;

    @Mock
    private SearchInvoker searchInvokerSync;

    @Mock
    private SearchInvoker searchInvokerAsync;

    @Mock
    private SearchManager searchManager;

    @Mock
    private Search search;

    @Mock
    private Evaluator evaluator;

    @Mock
    private SearchManagerBuilder.SearchManagerFactory searchManagerFactory;

    @Captor
    private ArgumentCaptor<SearchByChain> searchChainCaptor;

    @BeforeEach
    public void setup() {
        builder = new SearchManagerBuilder(searchManagerFactory);
    }

    @Test
    public void testBuildSearchInvokerSync() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchByChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByTree);
        when(searchManagerFactory.createSearchInvokerSync(any(SearchByChain.class))).thenReturn(searchInvokerSync);
        when(searchManagerFactory.createSearch()).thenReturn(search);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory).createSearch();
        verify(searchManagerFactory).createSearchByAlgorithm(eq(search));
        verify(searchManagerFactory).createSearchInvokerSync(eq(searchByTree));
        verify(searchManagerFactory).createSearchManager(eq(100), eq(searchByTree), any(TimeMgmt.class), eq(searchInvokerSync), eq(scheduledExecutorService));
    }

    @Test
    public void testBuildSearchInvokerAsync() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchByChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByTree);
        when(searchManagerFactory.createSearchInvokerAsync(any(SearchByChain.class), any(ExecutorService.class))).thenReturn(searchInvokerAsync);
        when(searchManagerFactory.createSearch()).thenReturn(search);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withAsyncInvoker()
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory).createSearch();
        verify(searchManagerFactory).createSearchByAlgorithm(eq(search));
        verify(searchManagerFactory).createSearchInvokerAsync(eq(searchByTree), eq(executorService));
        verify(searchManagerFactory).createSearchManager(eq(100), any(SearchByChain.class), any(TimeMgmt.class), eq(searchInvokerAsync), eq(scheduledExecutorService));
    }

    @Test
    public void testBuildWithBook() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchByChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByTree);
        when(searchManagerFactory.createSearchInvokerAsync(any(SearchByChain.class), any(ExecutorService.class))).thenReturn(searchInvokerAsync);
        when(searchManagerFactory.createSearchByOpenBook(anyString())).thenReturn(searchByOpenBook);
        when(searchManagerFactory.createSearch()).thenReturn(search);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withPolyglotFile("test.bin")
                .withAsyncInvoker()
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory).createSearch();
        verify(searchManagerFactory).createSearchByAlgorithm(eq(search));
        verify(searchManagerFactory).createSearchByOpenBook(eq("test.bin"));
        verify(searchManagerFactory).createSearchInvokerAsync(eq(searchByOpenBook), eq(executorService));
        verify(searchManagerFactory).createSearchManager(eq(100), searchChainCaptor.capture(), any(TimeMgmt.class), eq(searchInvokerAsync), eq(scheduledExecutorService));

        SearchByChain searchByChain = searchChainCaptor.getValue();
        assertEquals(searchByOpenBook, searchByChain);

        verify(searchByOpenBook).setNext(eq(searchByTree));
    }

    @Test
    public void testBuildWithSyzygyPath() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchByChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByTree);
        when(searchManagerFactory.createSearchInvokerAsync(any(SearchByChain.class), any(ExecutorService.class))).thenReturn(searchInvokerAsync);
        when(searchManagerFactory.createSearchByTablebase(anyString())).thenReturn(searchByTablebase);
        when(searchManagerFactory.createSearch()).thenReturn(search);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withSyzygyPath("/tmp/syzygy")
                .withAsyncInvoker()
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory).createSearch();
        verify(searchManagerFactory).createSearchByAlgorithm(eq(search));
        verify(searchManagerFactory).createSearchByTablebase(eq("/tmp/syzygy"));
        verify(searchManagerFactory).createSearchInvokerAsync(eq(searchByTablebase), eq(executorService));
        verify(searchManagerFactory).createSearchManager(eq(100), searchChainCaptor.capture(), any(TimeMgmt.class), eq(searchInvokerAsync), eq(scheduledExecutorService));

        SearchByChain searchByChain = searchChainCaptor.getValue();
        assertEquals(searchByTablebase, searchByChain);

        verify(searchByTablebase).setNext(eq(searchByTree));
    }


    @Test
    public void testBuildWithBookAndSyzygyDirectory() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchByChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByTree);
        when(searchManagerFactory.createSearchInvokerAsync(any(SearchByChain.class), any(ExecutorService.class))).thenReturn(searchInvokerAsync);
        when(searchManagerFactory.createSearchByOpenBook(anyString())).thenReturn(searchByOpenBook);
        when(searchManagerFactory.createSearchByTablebase(anyString())).thenReturn(searchByTablebase);
        when(searchManagerFactory.createSearch()).thenReturn(search);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withPolyglotFile("test.bin")
                .withSyzygyPath("/tmp/syzygy")
                .withAsyncInvoker()
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory).createSearch();
        verify(searchManagerFactory).createSearchByAlgorithm(eq(search));
        verify(searchManagerFactory).createSearchByOpenBook(eq("test.bin"));
        verify(searchManagerFactory).createSearchByTablebase(eq("/tmp/syzygy"));
        verify(searchManagerFactory).createSearchInvokerAsync(eq(searchByOpenBook), eq(executorService));
        verify(searchManagerFactory).createSearchManager(eq(100), searchChainCaptor.capture(), any(TimeMgmt.class), eq(searchInvokerAsync), eq(scheduledExecutorService));

        SearchByChain searchByChain = searchChainCaptor.getValue();
        assertEquals(searchByOpenBook, searchByChain);

        verify(searchByOpenBook).setNext(eq(searchByTablebase));
        verify(searchByTablebase).setNext(eq(searchByTree));
    }


    @Test
    public void testBuildSearchWithSearch() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchByChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByTree);
        when(searchManagerFactory.createSearchInvokerSync(any(SearchByChain.class))).thenReturn(searchInvokerSync);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withSearch(search)
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory, never()).createSearch();
        verify(searchManagerFactory).createSearchByAlgorithm(eq(search));
        verify(searchManagerFactory).createSearchInvokerSync(eq(searchByTree));
        verify(searchManagerFactory).createSearchManager(eq(100), eq(searchByTree), any(TimeMgmt.class), eq(searchInvokerSync), eq(scheduledExecutorService));
    }


    @Test
    public void testBuildSearchWithEvaluator() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchByChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByTree);
        when(searchManagerFactory.createSearchInvokerSync(any(SearchByChain.class))).thenReturn(searchInvokerSync);
        when(searchManagerFactory.createSearch(any(Evaluator.class))).thenReturn(search);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withEvaluator(evaluator)
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory).createSearch(eq(evaluator));
        verify(searchManagerFactory).createSearchByAlgorithm(eq(search));
        verify(searchManagerFactory).createSearchInvokerSync(eq(searchByTree));
        verify(searchManagerFactory).createSearchManager(eq(100), eq(searchByTree), any(TimeMgmt.class), eq(searchInvokerSync), eq(scheduledExecutorService));
    }
}
