package net.chesstango.search.smart.features.transposition;

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

    public long hash;
    public int draft; // Distance to the horizon
    public short move;
    public int value;

    public TranspositionBound transpositionBound;


    public void reset() {
        hash = 0;
        draft = 0;
        move = 0;
        value = 0;
        transpositionBound = null;
    }

    @Override
    public TranspositionEntry clone() {
        return new TranspositionEntry()
                .setHash(hash)
                .setDraft(draft)
                .setMove(move)
                .setValue(value)
                .setTranspositionBound(transpositionBound);
    }

    @Override
    public int compareTo(TranspositionEntry other) {
        int result = Integer.compare(value, other.value);

        if (result == 0) {
            if (TranspositionBound.LOWER_BOUND.equals(transpositionBound) && !TranspositionBound.LOWER_BOUND.equals(other.transpositionBound)) {
                result = 1;
            } else if (TranspositionBound.EXACT.equals(transpositionBound)) {
                if (TranspositionBound.UPPER_BOUND.equals(other.transpositionBound)) {
                    result = 1;
                } else if (TranspositionBound.LOWER_BOUND.equals(other.transpositionBound)) {
                    result = -1;
                }
            } else if (TranspositionBound.UPPER_BOUND.equals(transpositionBound) && !TranspositionBound.UPPER_BOUND.equals(other.transpositionBound)) {
                result = -1;
            }
        }

        return result;
    }

    public boolean isStored(long hash) {
        return this.hash == hash;
    }


}
