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

    private enum IteratorSate{READING_MOVELIST, READING_CONTAINERLIST, READING_END};

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
            private IteratorSate state = IteratorSate.READING_MOVELIST;

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
                while(next == null && state != IteratorSate.READING_END) {
                    if(state==IteratorSate.READING_MOVELIST){
                        if(currentIterator.hasNext()){
                            this.next = currentIterator.next();
                        } else {
                            state = IteratorSate.READING_CONTAINERLIST;
                            computeNextIteratorFromContainers();
                        }
                    } else if(state==IteratorSate.READING_CONTAINERLIST){
                        if(currentIterator.hasNext()){
                            this.next = currentIterator.next();
                        } else {
                            computeNextIteratorFromContainers();
                        }
                    }
                    if(currentIterator == null) {
                        state = IteratorSate.READING_END;
                    }
                }
            }

            private Iterator<Move> computeNextIteratorFromContainers() {
                currentIterator = null;
                if(currentMoveListIterator.hasNext()){
                    MoveList moveList = currentMoveListIterator.next();
                    currentIterator = moveList.iterator();
                }
                return currentIterator;
            }
        };
    }
}
