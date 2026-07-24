package net.chesstango.search.smart.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
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
    public int alphaBeta(int currentPly, int alpha, int beta) {
        final long startHash = game.getPosition().getZobristHash();

        try {
            return next.alphaBeta(currentPly, alpha, beta);
        } catch (StopSearchingException re) {
            undoMoves(startHash);
        }

        // Se busca el mejor movimiento encontrado hasta el momento para la profundidad actual
        RootMoveEvaluation bestRootMoveEvaluation = rootMoveEvaluationCollection.getBestRootMoveEvaluation();

        // Si no existe mejor movimiento hasta ahora, devolvemos el de la profundidad anterior
        if (bestRootMoveEvaluation == null) {
            throw new RuntimeException("Stopped too early");
        }

        return bestRootMoveEvaluation.evaluation();
    }

    private void undoMoves(long startHash) {
        long currentHash = game.getPosition().getZobristHash();
        while (currentHash != startHash) {
            game.undoMove();
            currentHash = game.getPosition().getZobristHash();
        }
    }
}
