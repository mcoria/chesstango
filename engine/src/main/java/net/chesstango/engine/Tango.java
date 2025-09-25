package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;
import net.chesstango.gardel.fen.FEN;

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
@Slf4j
public class Tango implements AutoCloseable {
    public static final Properties PROPERTIES = loadProperties();
    public static final String ENGINE_VERSION = PROPERTIES.getProperty("version");
    public static final String ENGINE_NAME = PROPERTIES.getProperty("engine_name");
    public static final String ENGINE_AUTHOR = PROPERTIES.getProperty("engine_author");
    public static final String INFINITE_DEPTH = PROPERTIES.getProperty("infinite_depth");


    public static Tango open(Config config) {
        log.info("Opening Tango engine");

        ExecutorService searchExecutor = Executors.newSingleThreadExecutor(new SearchManagerThreadFactory("search"));

        ScheduledExecutorService timeOutExecutor = Executors.newSingleThreadScheduledExecutor(new SearchManagerThreadFactory("timeout"));

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

        SearchManager searchManager = searchManagerBuilder.build();

        return new Tango(searchManager, searchExecutor, timeOutExecutor);
    }

    private final ExecutorService searchExecutor;

    private final ScheduledExecutorService timeOutExecutor;

    private final SearchManager searchManager;

    private Tango(SearchManager searchManager, ExecutorService searchExecutor, ScheduledExecutorService timeOutExecutor) {
        this.searchExecutor = searchExecutor;
        this.timeOutExecutor = timeOutExecutor;
        this.searchManager = searchManager;
    }

    @Override
    public void close() throws Exception {
        searchExecutor.shutdown();
        timeOutExecutor.shutdown();
        searchManager.close();
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
