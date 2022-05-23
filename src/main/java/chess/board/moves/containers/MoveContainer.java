package chess.board.moves.containers;

import chess.board.moves.Move;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class MoveContainer implements MoveContainerReader {

    private int size = 0;

    private List<MoveList> moveLists = new LinkedList<MoveList>();

    private List<Move> moveList = new LinkedList<Move>();


    public void add(MoveList moveList) {
        size += moveList.size();
        moveLists.add(moveList);
    }

    public void add(Move move) {
        size ++;
        moveList.add(move);
    }

    @Override
    public int size() { return size; }


    @Override
    public boolean isEmpty() { return size == 0;}

    @Override
    public boolean contains(Move move) {
        if(moveList.contains(move)){
            return true;
        }
        for (MoveList movelist:
            moveLists) {
            if(movelist.contains(move)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Move> iterator() {
        return new Iterator<Move>() {
            private Iterator<Move> currentIterator = moveList.iterator();

            private Iterator<MoveList> currentMoveListIterator = moveLists.iterator();

            private Move next = null;

            @Override
            public boolean hasNext() {
                if(next == null){
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

            private void computeNext(){
                while(next == null && currentIterator != null ) {
                    if(currentIterator.hasNext()){
                        next = currentIterator.next();
                    } else {
                        computeNextIterator();
                    }
                }
            }

            private void computeNextIterator() {
                currentIterator = null;
                if(currentMoveListIterator.hasNext()){
                    MoveList moveList = currentMoveListIterator.next();
                    currentIterator = moveList.iterator();
                }
            }
        };
    }
}
