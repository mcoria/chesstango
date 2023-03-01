package net.chesstango.search.smart;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMove;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntBinaryOperator;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractSmart implements SearchMove {
    protected boolean keepProcessing = true;

    @Override
    public void stopSearching() {
        keepProcessing = false;
    }

    protected Move selectMove(Color currentTurn, List<Move> moves) {
        if (moves.size() == 0) {
            throw new RuntimeException("There is no move to select");
        } else if (moves.size() == 1) {
            return moves.get(0);
        }

        Comparator<Integer> fromFn = Color.WHITE.equals(currentTurn) ? Integer::max : Integer::min;
        Comparator<Integer> toFn = Color.WHITE.equals(currentTurn) ? Integer::min : Integer::max;

        final int fromIdx = moves.stream().mapToInt(move -> move.getFrom().getSquare().toIdx()).reduce(fromFn::compare).getAsInt();
        final int toIdx = moves.stream().filter(move -> move.getFrom().getSquare().toIdx() == fromIdx).mapToInt(move -> move.getTo().getSquare().toIdx()).reduce(toFn::compare).getAsInt();

        return moves.stream().filter(move -> move.getFrom().getSquare().toIdx() == fromIdx && move.getTo().getSquare().toIdx() == toIdx).findAny().get();
    }
}
