package net.chesstango.engine;

import net.chesstango.engine.timemgmt.TimeMgmt;
import net.chesstango.search.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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
    private SearchByAlgorithm searchByAlgorithm;

    @Mock
    private SearchInvoker searchInvokerSync;

    @Mock
    private SearchInvoker searchInvokerAsync;

    @Mock
    private SearchManager searchManager;

    @Mock
    private SearchManagerBuilder.SearchManagerFactory searchManagerFactory;


    @BeforeEach
    public void setup() {
        builder = new SearchManagerBuilder(searchManagerFactory);
    }

    @Test
    public void testBuildSearchInvokerSync() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByAlgorithm);
        when(searchManagerFactory.createSearchInvokerSync(any(SearchChain.class))).thenReturn(searchInvokerSync);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory).createSearchByAlgorithm(any(Search.class));
        verify(searchManagerFactory).createSearchInvokerSync(any(SearchChain.class));
        verify(searchManagerFactory).createSearchManager(eq(100), any(SearchChain.class), any(TimeMgmt.class), eq(searchInvokerSync), eq(scheduledExecutorService));
    }

    @Test
    public void testBuildSearchInvokerAsync() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByAlgorithm);
        when(searchManagerFactory.createSearchInvokerAsync(any(SearchChain.class), any(ExecutorService.class))).thenReturn(searchInvokerAsync);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withAsyncInvoker()
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory).createSearchByAlgorithm(any(Search.class));
        verify(searchManagerFactory).createSearchInvokerAsync(any(SearchChain.class), eq(executorService));
        verify(searchManagerFactory).createSearchManager(eq(100), any(SearchChain.class), any(TimeMgmt.class), eq(searchInvokerAsync), eq(scheduledExecutorService));
    }

    @Test
    public void testBuildWithBook() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByAlgorithm);
        when(searchManagerFactory.createSearchInvokerAsync(any(SearchChain.class), any(ExecutorService.class))).thenReturn(searchInvokerAsync);
        when(searchManagerFactory.createSearchByOpenBook(anyString())).thenReturn(searchByOpenBook);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withPolyglotFile("test.bin")
                .withAsyncInvoker()
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory).createSearchByAlgorithm(any(Search.class));
        verify(searchManagerFactory).createSearchByOpenBook(eq("test.bin"));
        verify(searchManagerFactory).createSearchInvokerAsync(any(SearchChain.class), eq(executorService));
        verify(searchManagerFactory).createSearchManager(eq(100), any(SearchChain.class), any(TimeMgmt.class), eq(searchInvokerAsync), eq(scheduledExecutorService));
    }

    @Test
    public void testBuildWithSyzygyDirectory() {
        when(searchManagerFactory.createSearchManager(anyInt(), any(SearchChain.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);
        when(searchManagerFactory.createSearchByAlgorithm(any(Search.class))).thenReturn(searchByAlgorithm);
        when(searchManagerFactory.createSearchInvokerAsync(any(SearchChain.class), any(ExecutorService.class))).thenReturn(searchInvokerAsync);
        when(searchManagerFactory.createSearchByTablebase(anyString())).thenReturn(searchByTablebase);

        SearchManager searchManager = builder
                .withExecutorService(executorService)
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withSyzygyDirectory("/tmp/syzygy")
                .withAsyncInvoker()
                .build();

        assertNotNull(searchManager);

        verify(searchManagerFactory).createSearchByAlgorithm(any(Search.class));
        verify(searchManagerFactory).createSearchByTablebase(eq("/tmp/syzygy"));
        verify(searchManagerFactory).createSearchInvokerAsync(any(SearchChain.class), eq(executorService));
        verify(searchManagerFactory).createSearchManager(eq(100), any(SearchChain.class), any(TimeMgmt.class), eq(searchInvokerAsync), eq(scheduledExecutorService));
    }
}
