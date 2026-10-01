package net.chesstango.search.alphabeta.quiescence;

import lombok.Setter;
import net.chesstango.board.Piece;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCaptureEnPassant;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.core.filters.AlphaBetaAbstract;

/**
 * @author Mauricio Coria
 */
@Setter
public class QSAlphaBeta extends AlphaBetaAbstract implements AlphaBetaFilter, Acceptor {

    private final boolean withDeltaPruning;

    private int[] standingPats;

    public QSAlphaBeta(boolean withDeltaPruning) {
        this.withDeltaPruning = withDeltaPruning;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    protected boolean pruneMove(int currentPly, int alpha, int beta, final int bestValue, Move move) {
        int standingPat = standingPats[currentPly];

        /**
         * Delta pruning
         */
        if (withDeltaPruning && standingPat + calculateDelta(move) < Math.max(bestValue, alpha)) {
            return true;
        }

        return false;
    }

    int calculateDelta(Move move) {
        int delta = 0;

        if (move instanceof MovePromotion) {
            delta = 900_000;
        } else if (move instanceof MoveCaptureEnPassant) {
            delta = 300_000;
        }

        Piece pieceTo = move.getTo().piece();
        if (pieceTo != null) {
            if (pieceTo.isPawn()) {
                delta += 300_000;
            } else if (pieceTo.isKnight()) {
                delta += 450_000;
            } else if (pieceTo.isBishop()) {
                delta += 450_000;
            } else if (pieceTo.isRook()) {
                delta += 600_000;
            } else if (pieceTo.isQueen()) {
                delta += 900_000;
            }
        }

        return delta;
    }

}
