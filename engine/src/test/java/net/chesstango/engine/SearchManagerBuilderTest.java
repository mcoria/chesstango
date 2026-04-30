package net.chesstango.engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class SearchManagerBuilderTest {

    private SearchManagerBuilder builder;

    private Config config;

    @Mock
    private TangoFactory tangoFactory;

    @Mock
    private ExecutorService executorService;

    @Mock
    private ScheduledExecutorService scheduledExecutorService;

    @Mock
    private SearchByAggregator searchByAggregator;

    @Mock
    private SearchInvoker searchInvokerSync;

    @Mock
    private SearchInvoker searchInvokerAsync;

    @Mock
    private SearchManager searchManager;

    @Mock
    private SearchByTree searchByTree;

    @Mock
    private TimeMgmt timeMgmt;


    @BeforeEach
    public void setup() {
        builder = new SearchManagerBuilder(tangoFactory);
        config = Config.create();
    }

    @Test
    public void testBuildSearchInvokerSync() {
        when(tangoFactory.createSearchByTree(any(Config.class))).thenReturn(searchByTree);
        when(tangoFactory.createSearchByAggregator(any(Config.class), any(SearchByTree.class))).thenReturn(searchByAggregator);
        when(tangoFactory.createSearchInvokerSync(any(SearchByChain.class))).thenReturn(searchInvokerSync);
        when(tangoFactory.createTimeMgmt()).thenReturn(timeMgmt);
        when(tangoFactory.createScheduledExecutorService()).thenReturn(scheduledExecutorService);
        when(tangoFactory.createSearchManager(anyInt(), any(SearchByTree.class), any(SearchByAggregator.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);

        SearchManager searchManager = builder
                .withConfig(config
                        .setAsyncSearch(false)
                        .setInfiniteDepth(100)
                )
                .build();

        assertNotNull(searchManager);

        verify(tangoFactory).createSearchByTree(eq(config));
        verify(tangoFactory).createSearchByAggregator(eq(config), eq(searchByTree));
        verify(tangoFactory).createSearchInvokerSync(eq(searchByAggregator));
        verify(tangoFactory).createTimeMgmt();
        verify(tangoFactory).createScheduledExecutorService();
        verify(tangoFactory).createSearchManager(eq(100), eq(searchByTree), eq(searchByAggregator), eq(timeMgmt), eq(searchInvokerSync), eq(scheduledExecutorService));
    }

    @Test
    public void testBuildSearchInvokerAsync() {
        when(tangoFactory.createSearchByTree(any(Config.class))).thenReturn(searchByTree);
        when(tangoFactory.createSearchByAggregator(any(Config.class), any(SearchByTree.class))).thenReturn(searchByAggregator);
        when(tangoFactory.createExecutorService()).thenReturn(executorService);
        when(tangoFactory.createSearchInvokerAsync(any(SearchByChain.class), any(ExecutorService.class))).thenReturn(searchInvokerAsync);
        when(tangoFactory.createTimeMgmt()).thenReturn(timeMgmt);
        when(tangoFactory.createScheduledExecutorService()).thenReturn(scheduledExecutorService);
        when(tangoFactory.createSearchManager(anyInt(), any(SearchByTree.class), any(SearchByAggregator.class), any(TimeMgmt.class), any(SearchInvoker.class), any(ScheduledExecutorService.class))).thenReturn(searchManager);

        SearchManager searchManager = builder
                .withConfig(config
                        .setAsyncSearch(true)
                        .setInfiniteDepth(100)
                )
                .build();

        assertNotNull(searchManager);

        verify(tangoFactory).createSearchByTree(eq(config));
        verify(tangoFactory).createSearchByAggregator(eq(config), eq(searchByTree));
        verify(tangoFactory).createExecutorService();
        verify(tangoFactory).createSearchInvokerAsync(eq(searchByAggregator), eq(executorService));
        verify(tangoFactory).createTimeMgmt();
        verify(tangoFactory).createScheduledExecutorService();
        verify(tangoFactory).createSearchManager(eq(100), eq(searchByTree), eq(searchByAggregator), eq(timeMgmt), eq(searchInvokerAsync), eq(scheduledExecutorService));
    }

}
