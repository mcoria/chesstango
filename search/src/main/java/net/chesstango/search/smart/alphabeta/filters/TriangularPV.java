package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TriangularPV implements AlphaBetaFilter {
    @Setter
    private AlphaBetaFilter next;
    private short[][] trianglePV;
    private Game game;

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.trianglePV = context.getTrianglePV();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
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

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        cleanNextWorkingArray(currentPly);

        long bestMoveAndValue = next.maximize(currentPly, alpha, beta);
        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

        if (currentValue < beta) {
            updatePVTable(currentPly);
        }
        return bestMoveAndValue;
    }


    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        cleanNextWorkingArray(currentPly);

        long bestMoveAndValue = next.minimize(currentPly, alpha, beta);
        int currentValue = TranspositionEntry.decodeValue(bestMoveAndValue);

        if (currentValue > alpha) {
            updatePVTable(currentPly);
        }
        return bestMoveAndValue;
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
