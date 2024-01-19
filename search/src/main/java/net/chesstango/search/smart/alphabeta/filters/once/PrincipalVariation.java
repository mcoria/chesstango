package net.chesstango.search.smart.alphabeta.filters.once;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariation implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private SmartListenerMediator smartListenerMediator;

    @Setter
    private GameEvaluator gameEvaluator;

    private short[][] trianglePV;
    private Game game;

    private List<Move> principalVariation;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public void afterSearch() {

    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        trianglePV = context.getTrianglePV();
        principalVariation = null;
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        result.setPrincipalVariation(principalVariation);
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.maximize(currentPly, alpha, beta);

        process(currentPly, bestMoveAndValue, next::maximize);

        return bestMoveAndValue;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.minimize(currentPly, alpha, beta);

        process(currentPly, bestMoveAndValue, next::minimize);

        return bestMoveAndValue;
    }


    private void process(final int currentPly, final long bestMoveAndValue, final AlphaBetaFunction fn) {
        int bestValue = TranspositionEntry.decodeValue(bestMoveAndValue);

        smartListenerMediator.triggerBeforePVSearch(bestValue);

        long bestMoveAndValueSearched = fn.search(currentPly, bestValue - 1, bestValue + 1);
        int bestValueSearched = TranspositionEntry.decodeValue(bestMoveAndValueSearched);

        if (bestMoveAndValue == bestMoveAndValueSearched) {
            principalVariation = calculatePrincipalVariation(bestValue);

        } else {
            principalVariation = List.of();
            throw new RuntimeException("El calculo de PV no retorna la misma evaluacion obtenida durante la busqueda");
        }

        smartListenerMediator.triggerAfterPVSearch(principalVariation);
    }


    public List<Move> calculatePrincipalVariation(int bestEvaluation) {

        List<Move> principalVariation = new ArrayList<>();

        Move move = null;
        int pvMoveCounter = 0;
        short[] pvMoves = trianglePV[0];
        do {
            move = readMove(pvMoves[pvMoveCounter]);

            principalVariation.add(move);

            game.executeMove(move);

            pvMoveCounter++;

        } while (pvMoves[pvMoveCounter] != 0);

        if (gameEvaluator.evaluate() != bestEvaluation) {
            throw new RuntimeException("La evaluacion no coincide");
        }

        for (int i = 0; i < pvMoveCounter; i++) {
            game.undoMove();
        }

        return principalVariation;
    }

    private Move readMove(short bestMoveEncoded) {
        for (Move posibleMove : game.getPossibleMoves()) {
            if (posibleMove.binaryEncoding() == bestMoveEncoded) {
                return posibleMove;
            }
        }
        throw new RuntimeException("Move not found");
    }

}
