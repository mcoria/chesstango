package net.chesstango.board.moves.containers;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveContainer<M extends Move> implements MoveContainerReader<M> {
    private int size = 0;
    private final List<MoveList<? extends M>> moveLists;
    private final List<M> moveList;
    private boolean hasQuietMoves = true;

    public MoveContainer(int moveListCount) {
        this.moveLists = new ArrayList<>(moveListCount);
        this.moveList = new LinkedList<>();
    }

    public MoveContainer() {
        this.moveLists = new LinkedList<>();
        this.moveList = new LinkedList<>();
    }

    public <ME extends M>void add(MoveList<ME> moveList) {
        if (!moveList.hasQuietMoves()) {
            hasQuietMoves = false;
        }
        size += moveList.size();
        moveLists.add(moveList);
    }

    public void add(M move) {
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
    public boolean contains(Object object) {
        if (moveList.contains(object)) {
            return true;
        }
        for (MoveList<? extends M> movelist :
                moveLists) {
            if (movelist.contains(object)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public M getMove(Square from, Square to) {
        for (M move : this) {
            if (from.equals(move.getFrom().getSquare()) &&
                    to.equals(move.getTo().getSquare())) {
                if (move instanceof MovePromotion) {
                    return null;
                }
                return move;
            }
        }
        return null;
    }

    @Override
    public M getMove(Square from, Square to, Piece promotionPiece) {
        for (M move : this) {
            if (from.equals(move.getFrom().getSquare()) &&
                    to.equals(move.getTo().getSquare()) &&
                    (move instanceof MovePromotion movePromotion)) {
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
    public Iterator<M> iterator() {
        return new Iterator<>() {
            private Iterator<? extends M> currentIterator = moveList.iterator();

            private final Iterator<MoveList<? extends M>> currentMoveListIterator = moveLists.iterator();

            private M next = null;

            @Override
            public boolean hasNext() {
                if (next == null) {
                    computeNext();
                }
                return next != null;
            }

            @Override
            public M next() {
                M nextResult = next;
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
                    MoveList<? extends M> moveList = currentMoveListIterator.next();
                    currentIterator = moveList.iterator();
                }
            }
        };
    }
}
