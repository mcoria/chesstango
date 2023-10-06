package net.chesstango.board.moves.containers;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.moves.MovePromotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveContainer implements MoveContainerReader {
    private int size = 0;
    private final List<MoveList> moveLists;
    private final List<Move> moveList = new LinkedList<>();
    private boolean hasQuietMoves = true;

    public MoveContainer(int moveListCount) {
        moveLists = new ArrayList<>(moveListCount);
    }

    public MoveContainer() {
        this(0);
    }

    public void add(MoveList moveList) {
        if (!moveList.hasQuietMoves()) {
            hasQuietMoves = false;
        }
        size += moveList.size();
        moveLists.add(moveList);
    }

    public void add(Move move) {
        if (!move.isQuiet()) {
            hasQuietMoves = false;
        }
        size++;
        moveList.add(move);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Move move) {
        if (moveList.contains(move)) {
            return true;
        }
        for (MoveList movelist :
                moveLists) {
            if (movelist.contains(move)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Move getMove(Square from, Square to) {
        for (Move move : this) {
            if (from.equals(move.getFrom().getSquare()) && to.equals(move.getTo().getSquare())) {
                if (move instanceof MovePromotion) {
                    return null;
                }
                return move;
            }
        }
        return null;
    }

    @Override
    public Move getMove(Square from, Square to, Piece promotionPiece) {
        for (Move move : this) {
            if (from.equals(move.getFrom().getSquare()) && to.equals(move.getTo().getSquare()) && (move instanceof MovePromotion)) {
                MovePromotion movePromotion = (MovePromotion) move;
                if (movePromotion.getPromotion().equals(promotionPiece)) {
                    return move;
                }
            }
        }
        return null;
    }

    @Override
    public boolean hasQuietMoves() {
        return hasQuietMoves;
    }

    @Override
    public Iterator<Move> iterator() {
        return new Iterator<Move>() {
            private Iterator<Move> currentIterator = moveList.iterator();

            private final Iterator<MoveList> currentMoveListIterator = moveLists.iterator();

            private Move next = null;

            @Override
            public boolean hasNext() {
                if (next == null) {
                    computeNext();
                }
                return next != null;
            }

            @Override
            public Move next() {
                Move nextResult = next;
                next = null;
                return nextResult;
            }

            private void computeNext() {
                while (next == null && currentIterator != null) {
                    if (currentIterator.hasNext()) {
                        next = currentIterator.next();
                    } else {
                        computeNextIterator();
                    }
                }
            }

            private void computeNextIterator() {
                currentIterator = null;
                if (currentMoveListIterator.hasNext()) {
                    MoveList moveList = currentMoveListIterator.next();
                    currentIterator = moveList.iterator();
                }
            }
        };
    }
}
