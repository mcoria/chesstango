package net.chesstango.engine;

import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.DefaultSearch;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Mauricio Corial
 */
public class Tango implements AutoCloseable {
    public static final Properties PROPERTIES = loadProperties();
    public static final String ENGINE_VERSION = PROPERTIES.getProperty("version");
    public static final String ENGINE_NAME = PROPERTIES.getProperty("engine_name");
    public static final String ENGINE_AUTHOR = PROPERTIES.getProperty("engine_author");
    public static final String INFINITE_DEPTH = PROPERTIES.getProperty("infinite_depth");

    private final SearchManager searchManager;

    Session currentSession;

    private Tango(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    public static Tango open(Config config) {
        SearchManagerBuilder searchManagerBuilder = new SearchManagerBuilder();

        searchManagerBuilder.withSearchMove(new DefaultSearch());

        searchManagerBuilder.withInfiniteDepth(Integer.parseInt(INFINITE_DEPTH));

        SearchManager searchManager = searchManagerBuilder.build();

        Tango tango = new Tango(searchManager);

        searchManager.open();

        return tango;
    }

    @Override
    public void close() {
        searchManager.close();
    }

    public Session newSession(FEN fen) {
        searchManager.reset();
        currentSession = new Session(fen, searchManager);
        return currentSession;
    }

    static Properties loadProperties() {
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
