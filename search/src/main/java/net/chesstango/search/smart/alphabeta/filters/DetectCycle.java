package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.CycleException;
import net.chesstango.search.smart.SearchContext;


/**
 * @author Mauricio Coria
 * <p>
 * No se que pasa que no esta ganando con INITIAL_FEN y DEPTH 7
 * ---------------------------------------
 * WITHOUT CICLE DETECTION / 5segs 340ms
 * Visited Nodes Level  1 =          3
 * Visited Nodes Level  2 =          5
 * Visited Nodes Level  3 =         15
 * Visited Nodes Level  4 =         25
 * Visited Nodes Level  5 =         55
 * Visited Nodes Level  6 =         85
 * Visited Nodes Level  7 =        203
 * Visited Nodes Level  8 =        321
 * Visited Nodes Level  9 =        727
 * Visited Nodes Level 10 =       1133
 * Visited Nodes Level 11 =       2623
 * Visited Nodes Level 12 =       4113
 * Visited Nodes Level 13 =       9411
 * Visited Nodes Level 14 =      14709
 * Visited Nodes Level 15 =      33839
 * Visited Nodes Level 16 =      52969
 * Visited Nodes Level 17 =     121527
 * Visited Nodes Level 18 =     190085
 * Visited Nodes Level 19 =     531725
 * Visited Nodes Level 20 =    1024921
 * Total visited Nodes =       1988494
 * <p>
 * WITH CICLE DETECTION / 676ms
 * Visited Nodes Level  1 =          3
 * Visited Nodes Level  2 =          5
 * Visited Nodes Level  3 =         15
 * Visited Nodes Level  4 =         25
 * Visited Nodes Level  5 =         58
 * Visited Nodes Level  6 =         97
 * Visited Nodes Level  7 =        196
 * Visited Nodes Level  8 =        259
 * Visited Nodes Level  9 =        552
 * Visited Nodes Level 10 =        693
 * Visited Nodes Level 11 =       1453
 * Visited Nodes Level 12 =       1949
 * Visited Nodes Level 13 =       4004
 * Visited Nodes Level 14 =       5112
 * Visited Nodes Level 15 =      10487
 * Visited Nodes Level 16 =      14713
 * Visited Nodes Level 17 =      31276
 * Visited Nodes Level 18 =      42002
 * Visited Nodes Level 19 =      90047
 * Visited Nodes Level 20 =     128598
 * Total visited Nodes    =     331544
 */
//TODO: no esta funcionando adecuadamente este filtro
public class DetectCycle implements AlphaBetaFilter {

    public final static long CYCLE_MAX = 0b00000000_00000000_00000000_00000000_01111111_11111111_11111111_11111111L;

    public final static long CYCLE_MIN = 0b00000000_00000000_00000000_00000000_10000000_00000000_00000000_00000001L;

    private AlphaBetaFilter next;

    private long[] whitePositions = new long[60];

    private long[] blackPositions = new long[60];
    private Game game;

    @Override
    public void init(SearchContext context) {
        this.game = context.getGame();
    }

    @Override
    public void close(SearchMoveResult result) {

    }

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        if (repeated(currentPly, whitePositions)) {
            // minimize invoca a maximize, esto tiene el efecto de saltear el movimiento en minimize
            return CYCLE_MAX;
        }

        long result = next.maximize(currentPly, alpha, beta);

        if (currentPly == 0 && result == CYCLE_MAX) {
            throw new CycleException(CYCLE_MAX);
        }

        return result;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        if (repeated(currentPly, blackPositions)) {
            // maximize invoca a minimize, esto tiene el efecto de saltear el movimiento en maximize
            return CYCLE_MIN;
        }

        long result = next.minimize(currentPly, alpha, beta);

        if (currentPly == 0 && result == CYCLE_MIN) {
            throw new CycleException(CYCLE_MIN);
        }

        return result;
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    private boolean repeated(int currentPly, long[] positions) {
        long positionHash = game.getChessPosition().getPositionHash();
        for (int i = currentPly - 4; i >= 0; i -= 2) {
            if (positionHash == positions[i]) {
                return true;
            }
        }
        positions[currentPly] = positionHash;
        return false;
    }

}
