package net.chesstango.search.smart.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.AlphaBetaFunction;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;

/**
 * @author Mauricio Coria
 */
@Setter
public class StopProcessingCatch implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    private RootMoveEvaluationCollection rootMoveEvaluationCollection;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public int maximize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::maximize, true);
    }

    @Override
    public int minimize(int currentPly, int alpha, int beta) {
        return process(currentPly, alpha, beta, next::minimize, false);
    }

    private int process(int currentPly, int alpha, int beta, AlphaBetaFunction fn, boolean maximize) {
        final long startHash = game.getPosition().getZobristHash();

        try {
            return fn.search(currentPly, alpha, beta);
        } catch (StopSearchingException re) {
            undoMoves(startHash);
        }

        // Se busca el mejor movimiento encontrado hasta el momento para la profundidad actual
        RootMoveEvaluation bestRootMoveEvaluation = rootMoveEvaluationCollection.getBestRootMoveEvaluation();

        // Si no existe mejor movimiento hasta ahora, devolvemos el de la profundidad anterior
        if (bestRootMoveEvaluation == null) {
            throw new RuntimeException("Stopped too early");
        }

        return 0;
    }

    private void undoMoves(long startHash) {
        long currentHash = game.getPosition().getZobristHash();
        while (currentHash != startHash) {
            game.undoMove();
            currentHash = game.getPosition().getZobristHash();
        }
    }
}
