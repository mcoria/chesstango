package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchListener;
import net.chesstango.search.smart.SearchContext;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 * <p>
 * DRAW (por fold repetition)
 * DEPTH = 6
 * INITIAL_FEN
 * Time taken: 139447 ms
 * <p>
 * Nodes visited per search level
 * ______________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Total Nodes |
 * |      GameEvaluatorSEandImp02 white|       39 |      931 |     6172 |    56943 |   339652 |  2746252 | 13643979 |    16793929 |
 * ------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * Nodes visited per search level AVG
 * ______________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | AVG Nodes/S |
 * |      GameEvaluatorSEandImp02 white|       39 |       23 |      158 |     1460 |     8709 |    70416 |   349845 |      430613 |
 * ------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * Max distinct moves per search level
 * _____________________________________________________________________________________________________
 * |ENGINE NAME                        | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 |
 * |      GameEvaluatorSEandImp02 white|       47 |       54 |      287 |      406 |     1095 |     1955 |
 * -----------------------------------------------------------------------------------------------------
 * <p>
 * Cutoff per search level (higher is better)
 * _____________________________________________________________________________________________________
 * |ENGINE NAME                        | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 |
 * |      GameEvaluatorSEandImp02 white|      0 % |     80 % |     64 % |     81 % |     70 % |     84 % |
 * -----------------------------------------------------------------------------------------------------
 * <p>
 * ==================================================================================================================
 * WHITE WON GameEvaluatorSEandImp02
 * DEPTH = 7
 * INITIAL_FEN
 * Time taken: 504738 ms
 * <p>
 * Nodes visited per search level
 * _________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Level  7 | Total Nodes |
 * |      GameEvaluatorSEandImp02 white|       14 |      434 |     2362 |    28909 |   125905 |  1320137 |  5655912 | 44790961 |    51924620 |
 * -----------------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * Nodes visited per search level AVG
 * _________________________________________________________________________________________________________________________________________
 * |ENGINE NAME                        | SEARCHES | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Level  7 | AVG Nodes/S |
 * |      GameEvaluatorSEandImp02 white|       14 |       31 |      168 |     2064 |     8993 |    94295 |   403993 |  3199354 |     3708901 |
 * -----------------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * Max distinct moves per search level
 * ________________________________________________________________________________________________________________
 * |ENGINE NAME                        | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Level  7 |
 * |      GameEvaluatorSEandImp02 white|       45 |       64 |      339 |      613 |     1345 |     2444 |     4095 |
 * ----------------------------------------------------------------------------------------------------------------
 * <p>
 * Cutoff per search level (higher is better)
 * ________________________________________________________________________________________________________________
 * |ENGINE NAME                        | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Level  7 |
 * |      GameEvaluatorSEandImp02 white|     16 % |     84 % |     65 % |     87 % |     71 % |     86 % |     78 % |
 * ----------------------------------------------------------------------------------------------------------------
 * <p>
 * [Event "GameEvaluatorSEandImp02 vs Spike 1.4 - Match"]
 * [Site "KANO-LENOVO"]
 * [Date "2023.03.16"]
 * [Round "?"]
 * [White "GameEvaluatorSEandImp02"]
 * [Black "Spike 1.4"]
 * [Result "1-0"]
 * <p>
 * 1. e4 d5 2. exd5 Qxd5 3. Qf3 e6 4. Qg3 Qe4+ 5. Be2 Qxc2
 * 6. Nc3 c6 7. Bd3 Bd6 8. Qxd6 Qxd3 9. Qxd3 Nf6 10. Qd6 Nbd7
 * 11. d3 Nd5 12. Bg5 h6 13. Nxd5 hxg5 14. Qe7# 1-0
 * <p>
 * Cutoff per search level (higher is better)
 * _____________________________________________________________________________________
 * | Move   | Level  1 | Level  2 | Level  3 | Level  4 | Level  5 | Level  6 | Level  7 |
 * |   e2e4 |      0 % |     73 % |     59 % |     73 % |     71 % |     79 % |     78 % |
 * |   e4d5 |      0 % |     92 % |     45 % |     90 % |     54 % |     89 % |     67 % |
 * |   d1f3 |      0 % |     81 % |     67 % |     85 % |     76 % |     83 % |     84 % |
 * |   f3g3 |      0 % |     86 % |     68 % |     87 % |     75 % |     85 % |     83 % |
 * |   f1e2 |      0 % |     43 % |     84 % |     71 % |     85 % |     78 % |     88 % |
 * |   b1c3 |      0 % |     82 % |     80 % |     83 % |     81 % |     84 % |     87 % |
 * |   e2d3 |      0 % |     78 % |     70 % |     87 % |     76 % |     88 % |     81 % |
 * |   g3d6 |      0 % |     95 % |     65 % |     96 % |     65 % |     95 % |     71 % |
 * |   d6d3 |      0 % |     96 % |     34 % |     94 % |     62 % |     90 % |     71 % |
 * |   d3d6 |      0 % |     77 % |     57 % |     89 % |     59 % |     88 % |     69 % |
 * |   d2d3 |      0 % |     77 % |     46 % |     87 % |     60 % |     86 % |     74 % |
 * |   c1g5 |      0 % |     91 % |     56 % |     92 % |     53 % |     92 % |     56 % |
 * |   c3d5 |     99 % |      0 % |     90 % |     33 % |     84 % |     79 % |     74 % |
 * |   d6e7 |     68 % |     58 % |     83 % |     75 % |     75 % |     82 % |     80 % |
 * -------------------------------------------------------------------------------------
 */
public class DefaultMoveSorter implements SearchListener, MoveSorter {

    private static final MoveComparator moveComparator = new MoveComparator();
    private Game game;

    @Override
    public List<Move> getSortedMoves() {
        return getSortedMoves(game.getPossibleMoves());
    }

    protected List<Move> getSortedMoves(Iterable<Move> possibleMoves) {

        List<Move> moveList = new LinkedList<>();

        possibleMoves.forEach(moveList::add);

        Collections.sort(moveList, moveComparator.reversed());

        return moveList;
    }

    @Override
    public void init(SearchContext context) {
        this.game = context.getGame();
    }

    @Override
    public void close(SearchMoveResult result) {

    }

}
