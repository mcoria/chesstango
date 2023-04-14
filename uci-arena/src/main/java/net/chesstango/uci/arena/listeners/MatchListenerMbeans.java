package net.chesstango.uci.arena.listeners;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.mbeans.Arena;
import net.chesstango.uci.arena.GameResult;
import net.chesstango.uci.arena.MatchListener;
import net.chesstango.uci.gui.EngineController;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class MatchListenerMbeans implements MatchListener {

    private Map<Game, MatchListenerToMBean> gameMatchListenerToMBeanMap = Collections.synchronizedMap(new HashMap<>());

    private ObjectPool<Arena> arenaPool = new GenericObjectPool<>(new ArenaFactory());

    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
        MatchListenerToMBean matchListenerToMBean = gameMatchListenerToMBeanMap.computeIfAbsent(game, gameKey -> new MatchListenerToMBean(borrowArena()));

        matchListenerToMBean.notifyNewGame(game, white, black);
    }

    @Override
    public void notifyMove(Game game, Move move) {
        MatchListenerToMBean matchListenerToMBean = gameMatchListenerToMBeanMap.get(game);

        matchListenerToMBean.notifyMove(game, move);
    }

    @Override
    public void notifyEndGame(Game game, GameResult gameResult) {
        MatchListenerToMBean matchListenerToMBean = gameMatchListenerToMBeanMap.remove(game);

        returnArena(matchListenerToMBean.getArena());
    }

    private Arena borrowArena() {
        Arena arena;
        try {
            arena = arenaPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return arena;
    }

    private void returnArena(Arena arena) {
        try {
            arenaPool.returnObject(arena);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static class ArenaFactory extends BasePooledObjectFactory<Arena> {
        @Override
        public Arena create() throws Exception {
            return Arena.createAndRegisterMBean();
        }

        @Override
        public PooledObject<Arena> wrap(Arena arena) {
            return new DefaultPooledObject<>(arena);
        }
    }
}