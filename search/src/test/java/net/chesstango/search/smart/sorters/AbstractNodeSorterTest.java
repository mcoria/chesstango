package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.builders.alphabeta.MoveSorterBuilder;
import net.chesstango.search.smart.features.killermoves.KillerMovesTable;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.transposition.TTableMap;
import net.chesstango.search.smart.features.transposition.TTable;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractNodeSorterTest {
    protected final MoveFactory moveFactoryWhite = SingletonMoveFactories.getDefaultMoveFactoryWhite();
    protected final MoveFactory moveFactoryBlack = SingletonMoveFactories.getDefaultMoveFactoryBlack();
    protected final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
    protected SearchByCycleContext cycleContext;
    protected SearchByDepthContext depthContext;

    protected SearchListenerMediator searchListenerMediator;
    protected MoveSorterBuilder moveSorterBuilder;
    protected TTable maxMap;
    protected TTable minMap;
    protected TTable qMaxMap;
    protected TTable qMinMap;
    protected KillerMovesTable killerMovesTable;

    @BeforeEach
    public void setup() {
        Game game = createGame();

        searchListenerMediator = new SearchListenerMediator();

        cycleContext = new SearchByCycleContext(game);

        maxMap = new TTableMap();
        minMap = new TTableMap();
        qMaxMap = new TTableMap();
        qMinMap = new TTableMap();
        cycleContext.setMaxMap(maxMap);
        cycleContext.setMinMap(minMap);
        cycleContext.setQMaxMap(qMaxMap);
        cycleContext.setQMinMap(qMinMap);

        killerMovesTable = new KillerMovesTable();
        cycleContext.setKillerMoves(killerMovesTable);

        depthContext = new SearchByDepthContext(getMaxSearchPly());

        moveSorterBuilder = new MoveSorterBuilder()
                .withSmartListenerMediator(searchListenerMediator);
    }

    protected abstract Game createGame();

    protected abstract int getMaxSearchPly();

    protected List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(simpleMoveEncoder.encode(move));
        }
        return sortedMovesStr;
    }
}
