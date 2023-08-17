package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Mauricio Coria
 */
public class Tango {
    private final SearchManager searchManager;

    @Getter
    private Session currentSession;

    @Setter
    SearchListener listenerClient;

    public Tango() {
        this(new DefaultSearchMove());
    }

    public Tango(SearchMove searchMove) {
        SearchListener myListener = new SearchListener() {
            @Override
            public void searchStarted() {
                if (listenerClient != null) {
                    listenerClient.searchStarted();
                }
            }

            @Override
            public void searchInfo(SearchInfo info) {
                if (listenerClient != null) {
                    listenerClient.searchInfo(info);
                }
            }

            @Override
            public void searchStopped() {
                if (listenerClient != null) {
                    listenerClient.searchStopped();
                }
            }

            @Override
            public void searchFinished(SearchMoveResult searchResult) {
                currentSession.addResult(searchResult);

                if (listenerClient != null) {
                    listenerClient.searchFinished(searchResult);
                }
            }
        };

        this.searchManager = new SearchManager(searchMove, myListener);
    }

    public void open() {
        searchManager.open();
    }

    public void close() {
        searchManager.close();
    }

    public void newGame() {
        searchManager.reset();
        currentSession = new Session();
    }

    public void setPosition(String fen, List<String> moves) {
        if (currentSession == null ||
                currentSession != null &&
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
        searchManager.searchUpToDepth(currentSession.getGame(), depth);
    }

    public void goMoveTime(int timeOut) {
        searchManager.searchUpToTime(currentSession.getGame(), timeOut);
    }

    public void stopSearching() {
        searchManager.stopSearching();
    }

    public static final Properties properties = loadProperties();

    private static Properties loadProperties() {
        Properties properties;
        InputStream inputStream = null;
        try {
            inputStream = Tango.class.getClassLoader().getResourceAsStream("chesstango.properties");
            // create Properties class object
            properties = new Properties();
            // load properties file into it
            properties.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties;
    }

}
