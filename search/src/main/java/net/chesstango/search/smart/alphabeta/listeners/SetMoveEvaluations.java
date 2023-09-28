package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

/**
 * ESTA JOROBANDO
 * <p>
 * Captura la evaluacion de la posiciones que resultan de cada movimiento
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
            List<MoveEvaluation> moveEvaluationList = createMoveEvaluations(result.getBestMove(), result.getEvaluation());
            result.setMoveEvaluations(moveEvaluationList);
        }
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    /**
     * La lista que retorna puede no estar completa; esto se debe a que entradas en TT pueden ser sobreescritas.
     *
     * @param bestMove
     * @param bestMoveEvaluation
     * @return
     */
    public List<MoveEvaluation> createMoveEvaluations(final Move bestMove,
                                                      final int bestMoveEvaluation) {
        List<MoveEvaluation> moveEvaluationList = new ArrayList<>();

        boolean bestMovePresent = false;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            long hash = game.getChessPosition().getZobristHash();

            TranspositionEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);

            if (entry != null && entry.searchDepth == maxPly - 1) {
                MoveEvaluation moveEvaluation = new MoveEvaluation(move, TranspositionEntry.decodeValue(entry.bestMoveAndValue));
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
            MoveEvaluation moveEvaluation = new MoveEvaluation(bestMove, bestMoveEvaluation);
            moveEvaluationList.add(moveEvaluation);
        }

        if (Color.WHITE.equals(game.getChessPosition().getCurrentTurn())) {
            OptionalInt bestEvaluation = moveEvaluationList.stream().mapToInt(MoveEvaluation::evaluation).max();

            // Puede que el mejor valor sea unico y no este presente en TT
            if (bestEvaluation.isPresent()) {
                int bestEvaluationValue = bestEvaluation.getAsInt();
                // Si existe un valor, no puede ser mejor que la mejor evaluacion
                if(bestEvaluationValue > bestMoveEvaluation){
                    throw new RuntimeException("Best move is not present in game");
                }
            }
        } else {
            OptionalInt bestEvaluation = moveEvaluationList.stream().mapToInt(MoveEvaluation::evaluation).min();
            if (bestEvaluation.isPresent()) {
                int bestEvaluationValue = bestEvaluation.getAsInt();
                // Si existe un valor, no puede ser mejor que la mejor evaluacion
                if(bestEvaluationValue < bestMoveEvaluation){
                    throw new RuntimeException("Best move is not present in game");
                }
            }
        }


        return moveEvaluationList;
    }
}
