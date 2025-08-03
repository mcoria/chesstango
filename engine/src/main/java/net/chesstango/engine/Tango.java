package net.chesstango.engine;

import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.DefaultSearch;

import java.io.IOException;
import java.io.InputStream;
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

    private Tango(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    public static Tango open(Config config) {
        SearchManagerBuilder searchManagerBuilder = new SearchManagerBuilder();

        searchManagerBuilder.withSearch(new DefaultSearch());

        searchManagerBuilder.withInfiniteDepth(Integer.parseInt(INFINITE_DEPTH));

        if (config.getPolyglotFile() != null) {
            searchManagerBuilder.withPolyglotFile(config.getPolyglotFile());
        }

        if (config.getSyzygyDirectory() != null) {
            searchManagerBuilder.withSyzygyDirectory(config.getPolyglotFile());
        }

        SearchManager searchManager = searchManagerBuilder.build();

        searchManager.init();

        return new Tango(searchManager);
    }

    @Override
    public void close() throws Exception {
        searchManager.close();
    }

    public Session newSession(FEN fen) {
        searchManager.reset();
        return new Session(fen, searchManager);
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
