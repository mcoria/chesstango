package net.chesstango.uci.arena;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.listeners.MatchListener;
import net.chesstango.uci.arena.matchtypes.MatchType;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class Tournament {
    private static final Logger logger = LoggerFactory.getLogger(Tournament.class);

    private final MatchType matchType;

    private final List<Supplier<EngineController>> engineSupplierList;

    @Setter
    @Accessors(chain = true)
    private MatchListener matchListener;

    public Tournament(List<Supplier<EngineController>> engineSupplierList, MatchType matchType) {
        this.engineSupplierList = engineSupplierList;
        this.matchType = matchType;
    }

    public List<MatchResult> play(Stream<FEN> fenList) {

        List<MatchResult> matchResults = Collections.synchronizedList(new LinkedList<>());

        Supplier<EngineController> mainEngineSupplier = engineSupplierList.getFirst();

        try (ObjectPool<EngineController> mainPool = new GenericObjectPool<>(new EngineControllerPoolFactory(mainEngineSupplier))) {
            for (Supplier<EngineController> opponentEngineSupplier : engineSupplierList) {
                try (ObjectPool<EngineController> opponentPool = new GenericObjectPool<>(new EngineControllerPoolFactory(opponentEngineSupplier))) {
                    if (mainEngineSupplier != opponentEngineSupplier) {
                        MatchMultiple matchMultiple = new MatchMultiple(mainPool, opponentPool, matchType)
                                .setSwitchChairs(true)
                                .setMatchListener(matchListener);
                        matchResults.addAll(matchMultiple.play(fenList));
                    }
                }
            }
        }

        return matchResults;
    }


}
