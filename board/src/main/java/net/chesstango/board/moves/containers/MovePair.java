package net.chesstango.board.moves.containers;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class MovePair implements Iterable<Move> {
    private Move first;
    private Move second;

    public int size() {
        int result = 0;
        if (first != null) {
            result++;
        }
        if (second != null) {
            result++;
        }
        return result;
    }

    public boolean contains(Move move) {
        return move.equals(first) || move.equals(second);
    }

    @Override
    public Iterator<Move> iterator() {
        return new Iterator<>() {
            private Move currentElement = first != null ? first : second;


            @Override
            public boolean hasNext() {
                return currentElement != null;
            }

            @Override
            public Move next() {
                Move result = currentElement;
                if (first != null && currentElement == first) {
                    currentElement = second;
                } else if (second != null && currentElement == second) {
                    currentElement = null;
                }
                return result;
            }
        };
    }
}
