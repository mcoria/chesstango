package net.chesstango.search.smart.alphabeta.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetPVStorage implements SearchLifeCycle {

    @Setter
    private GameEvaluator gameEvaluator;

    private final short[][] trianglePV;
    private Game game;

    public SetPVStorage() {
        trianglePV = new short[40][40];
    }

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }


    @Override
    public void beforeSearchByDepth(SearchContext context) {
        context.setTrianglePV(trianglePV);
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        validatePrincipalVariation(result.getEvaluation());
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
    }

    public void validatePrincipalVariation(int bestEvaluation) {

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
