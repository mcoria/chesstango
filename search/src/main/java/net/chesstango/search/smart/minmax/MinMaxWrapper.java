package net.chesstango.search.smart.minmax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

/**
 * Positions: Balsa_Top50.pgn
 * Depth: 2
 * Time taken: 519848 ms
 *  ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |            GameEvaluatorSEandImp02|       0 |       0 |        9 |        8 |        1 |        2 |        0.5 |        1.0 |   1.5 / 20 |    7.5 |
 * |                          Spike 1.4|       8 |       9 |        0 |        0 |        2 |        1 |        9.0 |        9.5 |  18.5 / 20 |   92.5 |
 *  ---------------------------------------------------------------------------------------------------------------------------------------------------
 *  __________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | COLLISIONS | Level  1  | Level  2  |Total Nodes|AVG Nodes/S|
 * |            GameEvaluatorSEandImp02|      703 |           680 |           23 |         54 |     38650 |    700966 |    739616 |      1052 |
 *  ------------------------------------------------------------------------------------------------------------------------------------------
 *  Observar como se dispara en Level 2 comparado con MinMaxPruning
 *
 *
 * Positions: Balsa_Top50.pgn
 * Depth: 1
 * Time taken: 18750 ms ~ 18 segs
 *  ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |            GameEvaluatorSEandImp02|       1 |       0 |        5 |        4 |        4 |        6 |        3.0 |        3.0 |   6.0 / 20 |   30.0 |
 * |                          Spike 1.4|       4 |       5 |        0 |        1 |        6 |        4 |        7.0 |        7.0 |  14.0 / 20 |   70.0 |
 *  ---------------------------------------------------------------------------------------------------------------------------------------------------
 *  ______________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | COLLISIONS | Level  1  |Total Nodes|AVG Nodes/S|
 * |            GameEvaluatorSEandImp02|      660 |           642 |           18 |         19 |     19375 |     19375 |        29 |
 *  ------------------------------------------------------------------------------------------------------------------------------
 */
public class MinMaxWrapper implements SearchMove {
    private MinMax minMax = new MinMax();
    private Quiescence quiescence = new Quiescence();


    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return minMax.searchBestMove(game);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {
        return minMax.searchBestMove(game, depth);
    }

    @Override
    public void stopSearching() {
        minMax.stopSearching();
    }

    @Override
    public void setGameEvaluator(GameEvaluator evaluator) {
        quiescence.setGameEvaluator(evaluator);
        minMax.setGameEvaluator(game -> Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ?
                quiescence.maximize(game, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE) :
                quiescence.minimize(game, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE));
    }
}
