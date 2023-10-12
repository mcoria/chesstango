package net.chesstango.search;

import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 * <p>
 * Positions: Balsa_Top10.pgn
 * Depth: 5
 * Time taken: 3078154 ms ~ 51min
 * ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |            GameEvaluatorSEandImp02|       0 |       0 |       10 |        9 |        0 |        1 |        0.0 |        0.5 |   0.5 / 20 |    2.5 |
 * |                          Spike 1.4|       9 |      10 |        0 |        0 |        1 |        0 |        9.5 |       10.0 |  19.5 / 20 |   97.5 |
 * ---------------------------------------------------------------------------------------------------------------------------------------------------
 * ______________________________________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | COLLISIONS | Level  1  | Level  2  | Level  3  | Level  4  | Level  5  |Total Nodes|AVG Nodes/S|
 * |            GameEvaluatorSEandImp02|      696 |           567 |          129 |        306 |     86111 |    500594 |   3510722 |  15450499 |  55968747 |  75516673 |    108500 |
 * ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * Positions: Balsa_Top10.pgn
 * Depth: 4
 * Time taken: 616170 ms ~ 10 min
 * ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |            GameEvaluatorSEandImp02|       0 |       0 |        8 |        9 |        2 |        1 |        1.0 |        0.5 |   1.5 / 20 |    7.5 |
 * |                          Spike 1.4|       9 |       8 |        0 |        0 |        1 |        2 |        9.5 |        9.0 |  18.5 / 20 |   92.5 |
 * ---------------------------------------------------------------------------------------------------------------------------------------------------
 * __________________________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | COLLISIONS | Level  1  | Level  2  | Level  3  | Level  4  |Total Nodes|AVG Nodes/S|
 * |            GameEvaluatorSEandImp02|      762 |           632 |          130 |        206 |     78724 |    420937 |   2737012 |   8192782 |  11429455 |     14999 |
 * ------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * Positions: Balsa_Top10.pgn
 * Depth: 3
 * Time taken: 66276 ms ~ 66 segs
 * ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |            GameEvaluatorSEandImp02|       0 |       0 |        7 |        9 |        3 |        1 |        1.5 |        0.5 |   2.0 / 20 |   10.0 |
 * |                          Spike 1.4|       9 |       7 |        0 |        0 |        1 |        3 |        9.5 |        8.5 |  18.0 / 20 |   90.0 |
 * ---------------------------------------------------------------------------------------------------------------------------------------------------
 * ______________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | COLLISIONS | Level  1  | Level  2  | Level  3  |Total Nodes|AVG Nodes/S|
 * |            GameEvaluatorSEandImp02|      621 |           554 |           67 |        143 |     45782 |    214399 |   1007218 |   1267399 |      2040 |
 * ------------------------------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * Positions: Balsa_Top10.pgn
 * Depth: 2
 * Time taken: 19440 ms ~20segs
 * ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |            GameEvaluatorSEandImp02|       0 |       0 |        9 |        9 |        1 |        1 |        0.5 |        0.5 |   1.0 / 20 |    5.0 |
 * |                          Spike 1.4|       9 |       9 |        0 |        0 |        1 |        1 |        9.5 |        9.5 |  19.0 / 20 |   95.0 |
 * ---------------------------------------------------------------------------------------------------------------------------------------------------
 * __________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | COLLISIONS | Level  1  | Level  2  |Total Nodes|AVG Nodes/S|
 * |            GameEvaluatorSEandImp02|      693 |           654 |           39 |         79 |     38112 |    135809 |    173921 |       250 |
 * ------------------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * Positions: Balsa_Top10.pgn
 * Depth: 1
 * Time taken: 4508 ms ~ 5 segs
 * ___________________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        |WHITE WON|BLACK WON|WHITE LOST|BLACK LOST|WHITE DRAW|BLACK DRAW|WHITE POINTS|BLACK POINTS|TOTAL POINTS|   WIN %|
 * |            GameEvaluatorSEandImp02|       1 |       0 |        5 |        4 |        4 |        6 |        3.0 |        3.0 |   6.0 / 20 |   30.0 |
 * |                          Spike 1.4|       4 |       5 |        0 |        1 |        6 |        4 |        7.0 |        7.0 |  14.0 / 20 |   70.0 |
 * ---------------------------------------------------------------------------------------------------------------------------------------------------
 * ______________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | COLLISIONS | Level  1  |Total Nodes|AVG Nodes/S|
 * |            GameEvaluatorSEandImp02|      652 |           634 |           18 |         21 |     19101 |     19101 |        29 |
 * ------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * EL OBJETIVO SERIA DISMINUIR 'AVG Nodes/S' (promedio de nodos visitados por busqueda)
 */
public interface SearchMove {

    /**
     * Search up to depth
     */
    SearchMoveResult search(Game game);

    /**
     * Stop searching. This method may be called while another thread is searching
     */
    void stopSearching();

    /**
     * Reset internal counters and buffers (for instance TT)
     */
    void reset();

    /**
     *
     */
    void setParameter(SearchParameter parameter, Object value);
}
