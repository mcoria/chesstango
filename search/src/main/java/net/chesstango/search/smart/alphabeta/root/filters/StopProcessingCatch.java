package net.chesstango.search.smart.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.RootChildEvaluation;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.AlphaBetaFunction;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
import net.chesstango.search.smart.alphabeta.root.MoveEvaluations;

import java.util.Optional;

/**
 * @author Mauricio Coria
 */
@Setter
public class StopProcessingCatch implements AlphaBetaFilter {

    @Getter
    private AlphaBetaFilter next;

    @Getter
    private MoveEvaluations moveEvaluations;

    private Game game;

    private RootChildEvaluation lastBestMoveEvaluation;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::maximize, true);
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::minimize, false);
    }

    private long process(int currentPly, int alpha, int beta, AlphaBetaFunction fn, boolean maximize) {
        final long startHash = game.getPosition().getZobristHash();

        try {
            return fn.search(currentPly, alpha, beta);
        } catch (StopSearchingException re) {
            undoMoves(startHash);
        }

        RootChildEvaluation bestEvaluationResult = lastBestMoveEvaluation;

        // Se busca el mejor movimiento encontrado hasta el momento para la profundidad actual
        Optional<RootChildEvaluation> bestEvaluationTracked = moveEvaluations.getBestMoveEvaluation(maximize);


        // Si no existe mejor movimiento hasta ahora, devolvemos el de la profundidad anterior
        if (bestEvaluationTracked.isPresent()) {
            bestEvaluationResult = bestEvaluationTracked.get();
        }

        if (bestEvaluationResult != null) {
            Move bestMove = bestEvaluationResult.move();
            int bestValue = bestEvaluationResult.evaluation();
            return AlphaBetaHelper.encode(bestMove, bestValue);
        }

        throw new RuntimeException("Stopped too early");
    }

    private void undoMoves(long startHash) {
        long currentHash = game.getPosition().getZobristHash();
        while (currentHash != startHash) {
            game.undoMove();
            currentHash = game.getPosition().getZobristHash();
        }
    }
}
