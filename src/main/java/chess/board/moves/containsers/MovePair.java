package chess.board.moves.containsers;

import chess.board.moves.Move;

import java.util.Iterator;

public class MovePair implements Iterable<Move> {
    private Move first;
    private Move second;

    public Move getFirst() {
        return first;
    }

    public void setFirst(Move first) {
        this.first = first;
    }

    public Move getSecond() {
        return second;
    }

    public void setSecond(Move second) {
        this.second = second;
    }

    public int size() {
        int result = 0;
        if(first!=null){
            result++;
        }
        if(second!=null){
            result++;
        }
        return result;
    }

    public boolean contains(Move move) {
        return move.equals(first) || move.equals(second);
    }

    @Override
    public Iterator<Move> iterator() {
        return new Iterator<Move>() {
            private Move currentElement = first != null ? first : second;


            @Override
            public boolean hasNext() {
                return currentElement != null;
            }

            @Override
            public Move next() {
                Move result = currentElement;
                if(first != null && currentElement == first){
                    currentElement = second;
                } else if(second!=null && currentElement == second){
                    currentElement = null;
                }
                return result;
            }
        };
    }
}
