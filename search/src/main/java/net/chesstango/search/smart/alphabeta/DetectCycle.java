package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENEncoder;
import net.chesstango.search.smart.SearchContext;


/**
 * @author Mauricio Coria
 *
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
 *
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
public class DetectCycle implements AlphaBetaFilter{
    private AlphaBetaFilter next;

    private String[] whitePositions = new String[60];

    private String[] blackPositions = new String[60];

    @Override
    public void init(Game game, SearchContext context) {
        if(Color.WHITE.equals(game.getChessPosition().getCurrentTurn())){
            whitePositions[0] = getFENWithoutClocks(game);
        } else {
            blackPositions[0] = getFENWithoutClocks(game);
        }
    }

    @Override
    public int maximize(Game game, int currentPly, int alpha, int beta) {
        if(repeated(game, currentPly, whitePositions)) {
            return beta;
        }
        return next.maximize(game, currentPly, alpha, beta);
    }

    @Override
    public int minimize(Game game, int currentPly, int alpha, int beta) {
        if(repeated(game, currentPly, blackPositions)) {
            return alpha;
        }
        return next.minimize(game, currentPly, alpha, beta);
    }

    @Override
    public void stopSearching() {
    }

    public void setNext(AlphaBetaFilter next) {
        this.next = next;
    }

    private boolean repeated(Game game, int currentPly, String[] positions) {
        String fenWithoutClocks = getFENWithoutClocks(game);
        for (int i = currentPly - 2; i >= 0; i -=2) {
            if(fenWithoutClocks.equals(positions[i])){
                return true;
            }
        }

        positions[currentPly] = fenWithoutClocks;
        return false;
    }

    private String getFENWithoutClocks(Game game) {
        FENEncoder encoder = new FENEncoder();

        game.getChessPosition().constructBoardRepresentation(encoder);

        return encoder.getFENWithoutClocks();
    }

}
