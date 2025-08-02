package net.chesstango.engine;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.engine.manager.SearchManager;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author Mauricio Corial
 */
public class Tango {
    public static final Properties PROPERTIES = loadProperties();
    public static final String ENGINE_VERSION = PROPERTIES.getProperty("version");
    public static final String ENGINE_NAME = PROPERTIES.getProperty("engine_name");
    public static final String ENGINE_AUTHOR = PROPERTIES.getProperty("engine_author");
    public static final String INFINITE_DEPTH = PROPERTIES.getProperty("infinite_depth");

    private final SearchManager searchManager;

    private Session currentSession;

    @Setter
    private SearchListener searchListener;


    public Tango() {
        SearchListener myListener = new SearchListener() {
            @Override
            public void searchStarted() {
                if (searchListener != null) {
                    searchListener.searchStarted();
                }
            }

            @Override
            public void searchInfo(SearchResultByDepth searchByDepthResult) {
                if (searchListener != null) {
                    searchListener.searchInfo(searchByDepthResult);
                }
            }

            @Override
            public void searchFinished(SearchResult searchMoveResult) {
                currentSession.addResult(searchMoveResult);

                if (searchListener != null) {
                    searchListener.searchFinished(searchMoveResult);
                }
            }
        };

        this.searchManager = new SearchManager(myListener);
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

    public void setSyzygyDirectory(String path) {
        searchManager.setPolyglotBook(path);
    }

    public void setStartPosition(FEN fen) {
        searchManager.reset();
        currentSession = new Session(fen);
    }

    public FEN getStartPosition() {
        return currentSession.getStartPosition();
    }

    public void setMoves(List<String> moves) {
        currentSession.setMoves(moves);
    }

    public void goInfinite() {
        searchManager.searchInfinite(getGame());
    }

    public void goDepth(int depth) {
        searchManager.searchDepth(getGame(), depth);
    }

    public void goTime(int timeOut) {
        searchManager.searchTime(getGame(), timeOut);
    }

    public void goFast(int wTime, int bTime, int wInc, int bInc) {
        searchManager.searchFast(getGame(), wTime, bTime, wInc, bInc);
    }

    public void stopSearching() {
        searchManager.stopSearching();
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

    Game getGame(){
        return currentSession.getGame();
    }
}
