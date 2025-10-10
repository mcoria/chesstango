package net.chesstango.search.smart.features.pv.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Setter
public class TriangularPV implements AlphaBetaFilter {

    @Getter
    private AlphaBetaFilter next;

    private short[][] trianglePV;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
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
        short bestMove = game.getHistory().peekLastRecord().playedMove().binaryEncoding();

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
