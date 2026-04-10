package net.chesstango.search.smart.alphabeta.pv.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;

/**
 * @author Mauricio Coria
 */
@Setter
public class TriangularPV implements AlphaBetaFilter, SearchByCycleListener, SearchByDepthListener {

    @Getter
    private AlphaBetaFilter next;

    private short[][] trianglePV;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        cleanWorkingArray();
    }

    @Override
    public void beforeSearchByDepth() {
        cleanWorkingArray();
    }

    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        cleanNextWorkingArray(currentPly);

        long moveAndValue = next.maximize(currentPly, alpha, beta);

        return process(currentPly, alpha, beta, moveAndValue);
    }


    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        cleanNextWorkingArray(currentPly);

        long moveAndValue = next.minimize(currentPly, alpha, beta);

        return process(currentPly, alpha, beta, moveAndValue);
    }


    /**
     * Decodes move/value; reads principal variation if within alpha-beta window
     */
    protected long process(int currentPly, int alpha, int beta, long moveAndValue) {
        final short currentMove = AlphaBetaHelper.decodeMove(moveAndValue);
        final int currentValue = AlphaBetaHelper.decodeValue(moveAndValue);

        if (alpha < currentValue && currentValue < beta) {
            updatePVTable(currentPly);
        }

        return moveAndValue;
    }

    void updatePVTable(int currentPly) {
        short bestMove = game.getHistory().peekLastRecord().playedMove().binaryEncoding();

        final short[] workingArray = trianglePV[currentPly - 1];
        final short[] nextWorkingArray = trianglePV[currentPly];

        workingArray[0] = bestMove;
        System.arraycopy(nextWorkingArray, 0, workingArray, 1, 39);
    }


    void cleanWorkingArray() {
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                trianglePV[i][j] = 0;
            }
        }
    }

    void cleanNextWorkingArray(int currentPly) {
        final short[] nextWorkingArray = trianglePV[currentPly];
        for (int i = 0; i < 40; i++) {
            nextWorkingArray[i] = 0;
        }
    }
}
