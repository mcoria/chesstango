package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.DefaultSearch;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.board.representations.fen.FEN;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Mauricio Coria
 */
public class Tango {
    public static final Properties PROPERTIES = loadProperties();
    public static final String ENGINE_VERSION = PROPERTIES.getProperty("version");
    public static final String ENGINE_NAME = PROPERTIES.getProperty("engine_name");
    public static final String ENGINE_AUTHOR = PROPERTIES.getProperty("engine_author");
    public static final String INFINITE_DEPTH = PROPERTIES.getProperty("infinite_depth");

    private final SearchManager searchManager;

    @Getter
    private Session currentSession;

    @Setter
    private SearchListener listenerClient;

    public Tango() {
        this(new DefaultSearch());
    }

    public Tango(Search search) {
        SearchListener myListener = new SearchListener() {
            @Override
            public void searchStarted() {
                if (listenerClient != null) {
                    listenerClient.searchStarted();
                }
            }

            @Override
            public void searchInfo(SearchByDepthResult searchByDepthResult) {
                if (listenerClient != null) {
                    listenerClient.searchInfo(searchByDepthResult);
                }
            }

            @Override
            public void searchFinished(SearchResult searchMoveResult) {
                currentSession.addResult(searchMoveResult);

                if (listenerClient != null) {
                    listenerClient.searchFinished(searchMoveResult);
                }
            }
        };

        this.searchManager = new SearchManager(search, myListener);
        this.searchManager.setInfiniteDepth(Integer.parseInt(INFINITE_DEPTH));
    }

    public void open() {
        searchManager.open();
    }

    public void close() {
        searchManager.close();
    }

    public void setPolyglotBook(String path) {
        searchManager.setPolyglotBook(path);
    }

    public void newGame() {
        searchManager.reset();
        currentSession = new Session();
    }

    public void setPosition(FEN fen, List<String> moves) {
        if (currentSession == null ||
                currentSession.getGame() != null &&
                        !Objects.equals(fen, currentSession.getInitialFen())) {
            newGame();
        }
        currentSession.setPosition(fen, moves);
    }


    public void goInfinite() {
        searchManager.searchInfinite(currentSession.getGame());
    }

    public void goDepth(int depth) {
        searchManager.searchDepth(currentSession.getGame(), depth);
    }

    public void goTime(int timeOut) {
        searchManager.searchTime(currentSession.getGame(), timeOut);
    }

    public void goFast(int wTime, int bTime, int wInc, int bInc) {
        searchManager.searchFast(currentSession.getGame(), wTime, bTime, wInc, bInc);
    }

    public void stopSearching() {
        searchManager.stopSearching();
    }

    private static Properties loadProperties() {
        Properties properties;
        try (InputStream inputStream = Tango.class.getClassLoader().getResourceAsStream("chesstango.properties");) {
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
