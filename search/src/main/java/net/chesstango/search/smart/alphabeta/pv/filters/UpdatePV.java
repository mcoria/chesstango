package net.chesstango.search.smart.alphabeta.pv.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;

/**
 * @author Mauricio Coria
 */
@Setter
public class UpdatePV implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    private TriangularPVTable trianglePV;

    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(int currentPly, int alpha, int beta) {
        int value = next.alphaBeta(currentPly, alpha, beta);

        if (alpha < value) {
            short bestMove = game.getHistory().peekLastRecord().playedMove().binaryEncoding();
            trianglePV.updatePV(currentPly - 1, bestMove);
        }

        return value;
    }

}
