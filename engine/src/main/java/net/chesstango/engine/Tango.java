package net.chesstango.engine;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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

        TangoFactory tangoFactory = new TangoFactoryImp();

        ExecutorService searchExecutor = config.getSyncSearch() ? null : Executors.newSingleThreadExecutor(tangoFactory.createThreadFactory("search"));

        ScheduledExecutorService timeOutExecutor = Executors.newSingleThreadScheduledExecutor(tangoFactory.createThreadFactory("timeout"));

        // Configure search execution mode:
        // - Async mode: Executes search asynchronously
        // - Sync mode: Executes search synchronously in the calling thread
        SearchManagerBuilder searchManagerBuilder = new SearchManagerBuilder(tangoFactory)
                .withConfig(config)
                .withInfiniteDepth(Integer.parseInt(INFINITE_DEPTH))
                .withExecutorService(searchExecutor)
                .withScheduledExecutorService(timeOutExecutor);


        SearchManager searchManager = searchManagerBuilder.build();

        return new Tango(searchManager, searchExecutor, timeOutExecutor);
    }

    private final ExecutorService searchExecutor;

    private final ScheduledExecutorService timeOutExecutor;

    private final SearchManager searchManager;

    private Tango(SearchManager searchManager,
                  ExecutorService searchExecutor,
                  ScheduledExecutorService timeOutExecutor) {
        this.searchExecutor = searchExecutor;
        this.timeOutExecutor = timeOutExecutor;
        this.searchManager = searchManager;
    }

    @Override
    public void close() {
        if (searchExecutor != null) {
            searchExecutor.shutdown();
        }
        timeOutExecutor.shutdown();
        //searchManager.close();
    }

    public Session newSession() {
        //searchManager.reset();
        return new Session(searchManager);
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
}
