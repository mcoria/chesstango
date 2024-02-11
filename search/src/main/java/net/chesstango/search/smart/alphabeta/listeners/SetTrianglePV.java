package net.chesstango.search.smart.alphabeta.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.smart.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetTrianglePV implements SearchByCycleListener, SearchByDepthListener {

    @Setter
    private GameEvaluator gameEvaluator;

    private final short[][] trianglePV;
    private Game game;

    public SetTrianglePV() {
        trianglePV = new short[40][40];
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }


    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        context.setTrianglePV(trianglePV);
    }

    @Override
    public void afterSearchByDepth(SearchByDepthResult result) {
        List<Move> principalVariation = calculatePrincipalVariation(result.getBestEvaluation());
        result.setPrincipalVariation(principalVariation);
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

        int pvEvaluation = gameEvaluator.evaluate();

        // En caso que se llegó a loop
        if (game.getState().getRepetitionCounter() > 1) {
            pvEvaluation = 0;
        }

        if (bestEvaluation != pvEvaluation) {
            SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();
            throw new RuntimeException(String.format("bestEvaluation (%d) no coincide con la evaluacion PV (%d): %s", bestEvaluation, pvEvaluation, simpleMoveEncoder.encodeMoves(principalVariation)));
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
