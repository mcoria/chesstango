package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TriangularPV implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener, SearchPvListener {
    @Setter
    @Getter
    private AlphaBetaFilter next;
    private short[][] trianglePV;
    private Game game;

    private boolean trackPV;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.game = context.getGame();
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        this.trianglePV = context.getTrianglePV();
        this.trackPV = false;
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void beforePVSearch(int bestValue) {
        trackPV = true;
    }

    @Override
    public void afterPVSearch(List<Move> principalVariation) {
        trackPV = false;
    }

    @Override
    public void afterSearch() {
    }


    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        if (trackPV) {
            cleanNextWorkingArray(currentPly);

            long bestMoveAndValue = next.maximize(currentPly, alpha, beta);
            int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

            if (currentValue < beta) {
                updatePVTable(currentPly);
            }
            return bestMoveAndValue;
        } else {
            return next.maximize(currentPly, alpha, beta);
        }
    }


    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        if (trackPV) {
            cleanNextWorkingArray(currentPly);

            long bestMoveAndValue = next.minimize(currentPly, alpha, beta);
            int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

            if (currentValue > alpha) {
                updatePVTable(currentPly);
            }
            return bestMoveAndValue;
        } else {
            return next.minimize(currentPly, alpha, beta);
        }
    }

    private void updatePVTable(int currentPly) {
        short bestMove = game.getState().getPreviousState().getSelectedMove().binaryEncoding();

        final short[] workingArray = trianglePV[currentPly - 1];
        final short[] nextWorkingArray = trianglePV[currentPly];

        workingArray[0] = bestMove;
        System.arraycopy(nextWorkingArray, 0, workingArray, 1, 39);
    }


    private void cleanNextWorkingArray(int currentPly) {
        final short[] nextWorkingArray = trianglePV[currentPly];
        for (int i = 0; i < 40; i++) {
            nextWorkingArray[i] = 0;
        }
    }
}
