package net.chesstango.uci.arena;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.listeners.MatchListener;
import net.chesstango.uci.arena.matchtypes.MatchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 */
public class Tournament {
    private static final Logger logger = LoggerFactory.getLogger(Tournament.class);

    private final static int THREADS_NUMBER = 5;

    private final MatchType matchType;

    private final List<EngineControllerPoolFactory> controllerFactories;

    @Setter
    @Accessors(chain = true)
    private MatchListener matchListener;

    public Tournament(List<EngineControllerPoolFactory> controllerFactories, MatchType matchType) {
        this.controllerFactories = controllerFactories;
        this.matchType = matchType;
    }

    public List<MatchResult> play(List<String> fenList) {

        List<MatchResult> matchResults = new LinkedList<>();

        EngineControllerPoolFactory mainPool = controllerFactories.getFirst();

        for (EngineControllerPoolFactory opponentPoolFactory : controllerFactories) {

            if (mainPool != opponentPoolFactory) {
                MatchMultiple matchMultiple = new MatchMultiple(mainPool, opponentPoolFactory, matchType)
                        .setSwitchChairs(true)
                        .setMatchListener(matchListener);

                matchResults.addAll(matchMultiple.play(fenList));
            }
        }

        return matchResults;
    }


}
