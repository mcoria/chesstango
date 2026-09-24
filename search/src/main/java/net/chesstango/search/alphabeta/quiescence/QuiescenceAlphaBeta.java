package net.chesstango.search.alphabeta.quiescence;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveCaptureEnPassant;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.sorters.MoveSorter;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
@Setter
public class QuiescenceAlphaBeta implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    @Getter
    private MoveSorter moveSorter;

    private Move[] bestMoves;

    private Game game;

    @Getter
    private Evaluator evaluator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int alphaBeta(final int currentPly, final int alpha, final int beta) {
        boolean search = true;
        int standingPat = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? evaluator.evaluate() : -evaluator.evaluate();

        bestMoves[currentPly] = null;
        int bestValue = Evaluator.INFINITE_NEGATIVE;

        Iterable<Move> sortedMoves = moveSorter.getOrderedMoves(currentPly);
        Iterator<Move> moveIterator = sortedMoves.iterator();
        while (moveIterator.hasNext() && search) {
            Move move = moveIterator.next();

            if (standingPat + calculateDelta(move) < Math.max(bestValue, alpha)) {
                continue;
            }

            move.executeMove();
            int currentValue = next.alphaBeta(currentPly, Math.max(bestValue, alpha), beta);
            if (currentValue > bestValue) {
                bestValue = currentValue;
                bestMoves[currentPly] = move;
                if (bestValue >= beta || bestValue == Evaluator.WON) {
                    search = false;
                }
            }
            move.undoMove();

        }

        return bestValue;
    }

    private int calculateDelta(Move move) {
        int delta = 0;

        if (move instanceof MovePromotion) {
            delta = 900_000;
        } else if (move instanceof MoveCaptureEnPassant) {
            delta = 200_000;
        } else if (move.getTo().piece() != null) {
            if (move.getTo().piece().isPawn()) {
                delta = 300_000;
            } else if (move.getTo().piece().isKnight()) {
                delta = 450_000;
            } else if (move.getTo().piece().isBishop()) {
                delta = 450_000;
            } else if (move.getTo().piece().isRook()) {
                delta = 600_000;
            } else if (move.getTo().piece().isQueen()) {
                delta = 900_000;
            }
        } else {
            throw new RuntimeException("Invalid QS move");
        }

        return delta;
    }

}
