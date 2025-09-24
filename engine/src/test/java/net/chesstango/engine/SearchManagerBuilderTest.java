package net.chesstango.engine;

import net.chesstango.search.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @author Mauricio Coria
 */
public class SearchManagerBuilderTest {

    private SearchManagerBuilder builder;
    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;

    @BeforeEach
    public void setup() {
        builder = new SearchManagerBuilder();
        executorService = mock(ExecutorService.class);
        scheduledExecutorService = mock(ScheduledExecutorService.class);
    }

    @Test
    public void testBuildMinimalConfiguration() {
        SearchManager searchManager = builder
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .build();

        assertNotNull(searchManager);
    }

    @Test
    public void testBuildAsyncConfiguration() {
        SearchManager searchManager = builder
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withAsyncInvoker()
                .withExecutorService(executorService)
                .withSearch(Search.newSearchBuilder().build())
                .build();

        assertNotNull(searchManager);
    }

    @Test
    public void testBuildWithBookAndTablebase() {
        SearchManager searchManager = builder
                .withScheduledExecutorService(scheduledExecutorService)
                .withInfiniteDepth(100)
                .withPolyglotFile("test.bin")
                .withSyzygyDirectory("test/path")
                .build();

        assertNotNull(searchManager);
    }
}
