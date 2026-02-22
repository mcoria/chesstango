package net.chesstango.search.smart.alphabeta.transposition;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class TranspositionEntry implements Serializable, Cloneable, Comparable<TranspositionEntry> {

    long hash;
    int draft; // Distance to the horizon
    short move;
    int value;

    TranspositionBound bound;


    public void reset() {
        hash = 0;
        draft = 0;
        move = 0;
        value = 0;
        bound = null;
    }

    @Override
    public TranspositionEntry clone() {
        return new TranspositionEntry()
                .setHash(hash)
                .setDraft(draft)
                .setMove(move)
                .setValue(value)
                .setBound(bound);
    }

    @Override
    public int compareTo(TranspositionEntry other) {
        int result = Integer.compare(value, other.value);

        if (result == 0) {
            if (TranspositionBound.LOWER_BOUND.equals(bound) && !TranspositionBound.LOWER_BOUND.equals(other.bound)) {
                result = 1;
            } else if (TranspositionBound.EXACT.equals(bound)) {
                if (TranspositionBound.UPPER_BOUND.equals(other.bound)) {
                    result = 1;
                } else if (TranspositionBound.LOWER_BOUND.equals(other.bound)) {
                    result = -1;
                }
            } else if (TranspositionBound.UPPER_BOUND.equals(bound) && !TranspositionBound.UPPER_BOUND.equals(other.bound)) {
                result = -1;
            }
        }

        return result;
    }

}
