package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchListener;
import net.chesstango.search.smart.SearchContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

/**
 * @author Mauricio Coria
 */
public class MoveEvaluations implements SearchListener {
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;
    private Game game;
    private int maxPly;

    @Override
    public void init(SearchContext context) {
        this.game = context.getGame();
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void close(SearchMoveResult result) {
        List<SearchMoveResult.MoveEvaluation> moveEvaluationList = createMoveEvaluations(result.getBestMove(), result.getEvaluation());
        result.setMoveEvaluations(moveEvaluationList);
    }

    public List<SearchMoveResult.MoveEvaluation> createMoveEvaluations(final Move bestMove,
                                                                       final int bestMoveEvaluation) {
        List<SearchMoveResult.MoveEvaluation> moveEvaluationList = new ArrayList<>();

        boolean bestMovePresent = false;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            long hash = game.getChessPosition().getPositionHash();

            SearchContext.TableEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);

            if (entry != null && entry.searchDepth == maxPly) {
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
        } else {
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
                throw new RuntimeException("bestEvaluation in moveEvaluationList");
            }
        }

        return moveEvaluationList;
    }
}
