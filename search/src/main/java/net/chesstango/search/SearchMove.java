package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.GameEvaluator;

/**
 * @author Mauricio Coria
 *
 * Positions: Balsa_Top50.pgn
 * Depth: 5
 * Time taken: 3078154 ms ~ 51min
 *  ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |            GameEvaluatorSEandImp02|       0 |       0 |       10 |        9 |        0 |        1 |        0.0 |        0.5 |   0.5 / 20 |    2.5 |
 * |                          Spike 1.4|       9 |      10 |        0 |        0 |        1 |        0 |        9.5 |       10.0 |  19.5 / 20 |   97.5 |
 *  ---------------------------------------------------------------------------------------------------------------------------------------------------
 *  ______________________________________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | COLLISIONS | Level  1  | Level  2  | Level  3  | Level  4  | Level  5  |Total Nodes|AVG Nodes/S|
 * |            GameEvaluatorSEandImp02|      696 |           567 |          129 |        306 |     86111 |    500594 |   3510722 |  15450499 |  55968747 |  75516673 |    108500 |
 *  ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 *
 *  EL OBJETIVO SERIA DISMINUIR 'AVG Nodes/S' (promedio de nodos visitados por busqueda)
 */
public interface SearchMove {
    SearchMoveResult searchBestMove(Game game);

    SearchMoveResult searchBestMove(Game game, int depth);

    void stopSearching();

    void setGameEvaluator(GameEvaluator evaluator);
}
