package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

/**
 * ESTA JOROBANDO
 *
 * @author Mauricio Coria
 */
public class SetMoveEvaluations implements SearchLifeCycle {
    private TTable maxMap;
    private TTable minMap;
    private Game game;
    private int maxPly;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        if (result != null) {
            List<SearchMoveResult.MoveEvaluation> moveEvaluationList = createMoveEvaluations(result.getBestMove(), result.getEvaluation());
            result.setMoveEvaluations(moveEvaluationList);
        }
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    public List<SearchMoveResult.MoveEvaluation> createMoveEvaluations(final Move bestMove,
                                                                       final int bestMoveEvaluation) {
        List<SearchMoveResult.MoveEvaluation> moveEvaluationList = new ArrayList<>();

        boolean bestMovePresent = false;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            long hash = game.getChessPosition().getZobristHash();

            TranspositionEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);

            if (entry != null && entry.searchDepth == maxPly - 1) {
                SearchMoveResult.MoveEvaluation moveEvaluation = new SearchMoveResult.MoveEvaluation();
                moveEvaluation.move = move;
                moveEvaluation.evaluation = BinaryUtils.decodeValue(entry.bestMoveAndValue);
                moveEvaluationList.add(moveEvaluation);
            }

            if (move.equals(bestMove)) {
                bestMovePresent = true;
            }

            game.undoMove();
        }

        if (!bestMovePresent) {
            throw new RuntimeException("Best move is not present in game");
        }

        if (moveEvaluationList.isEmpty()) {
            SearchMoveResult.MoveEvaluation moveEvaluation = new SearchMoveResult.MoveEvaluation();
            moveEvaluation.move = bestMove;
            moveEvaluation.evaluation = bestMoveEvaluation;
            moveEvaluationList.add(moveEvaluation);
        }

        OptionalInt bestEvaluation = null;
        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            bestEvaluation = moveEvaluationList.stream().mapToInt(me -> me.evaluation).max();
        } else {
            bestEvaluation = moveEvaluationList.stream().mapToInt(me -> me.evaluation).min();
        }

        if (!bestEvaluation.isPresent()) {
            throw new RuntimeException("moveEvaluationList is empty");
        }

        if (bestEvaluation.getAsInt() != bestMoveEvaluation) {
            throw new RuntimeException("bestEvaluation doesn't match");
        }

        return moveEvaluationList;
    }
}
