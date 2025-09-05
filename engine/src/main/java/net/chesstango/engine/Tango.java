package net.chesstango.engine;

import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Search;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mauricio Corial
 */
public class Tango implements AutoCloseable {
    public static final Properties PROPERTIES = loadProperties();
    public static final String ENGINE_VERSION = PROPERTIES.getProperty("version");
    public static final String ENGINE_NAME = PROPERTIES.getProperty("engine_name");
    public static final String ENGINE_AUTHOR = PROPERTIES.getProperty("engine_author");
    public static final String INFINITE_DEPTH = PROPERTIES.getProperty("infinite_depth");

    private static final AtomicInteger executorCounter = new AtomicInteger(0);
    private static final ExecutorService searchExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new SearchManagerThreadFactory("search"));
    private static final ScheduledExecutorService timeOutExecutor = Executors.newSingleThreadScheduledExecutor(new SearchManagerThreadFactory("timeout"));

    private volatile SearchManager searchManager;

    private Tango() {
    }

    public static Tango open(Config config) {
        executorCounter.incrementAndGet();

        Tango tango = new Tango();

        return tango.reload(config);
    }

    @Override
    public void close() throws Exception {
        int currentValue = executorCounter.decrementAndGet();
        if (currentValue == 0) {
            stopExecutors();
        } else if (currentValue < 0) {
            throw new RuntimeException("Closed too many times");
        }
        searchManager.close();
    }

    public Tango reload(Config config) {
        if (searchManager != null) {
            try {
                searchManager.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        SearchManagerBuilder searchManagerBuilder = new SearchManagerBuilder()
                .withSearch(config.getSearch())
                .withEvaluator(config.getEvaluator())
                .withPolyglotFile(config.getPolyglotFile())
                .withSyzygyDirectory(config.getSyzygyDirectory())
                .withInfiniteDepth(Integer.parseInt(INFINITE_DEPTH))
                .withScheduledExecutorService(timeOutExecutor);


        // Configure search execution mode:
        // - Async mode: Executes search by ExecutorService
        // - Sync mode: Executes search in the calling thread
        // - Default: Async mode
        if (!config.isSyncSearch()) {
            searchManagerBuilder
                    .withAsyncInvoker()
                    .withExecutorService(searchExecutor);
        }

        searchManager = searchManagerBuilder.build();

        return this;
    }

    public Session newSession(FEN fen) {
        searchManager.reset();
        return new Session(fen, searchManager);
    }

    private static Properties loadProperties() {
        Properties properties;
        try (InputStream inputStream = Tango.class.getResourceAsStream("/chesstango.properties")) {
            // create Properties class object
            properties = new Properties();
            // load properties file into it
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
        return properties;
    }

    private synchronized static void stopExecutors() {
        searchExecutor.shutdownNow();
        timeOutExecutor.shutdownNow();
    }

    private static class SearchManagerThreadFactory implements ThreadFactory {
        private final AtomicInteger threadCounter = new AtomicInteger(1);
        private String threadNamePrefix = "";

        public SearchManagerThreadFactory(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, String.format("%s-%d", threadNamePrefix, threadCounter.getAndIncrement()));
        }
    }
}
