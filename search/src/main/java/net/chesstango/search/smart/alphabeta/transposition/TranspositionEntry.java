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
    byte draft; // Distance to the horizon
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
        int result = bound.compareTo(other.bound);

        if (result == 0) {
            result = Integer.compare(value, other.value);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranspositionEntry that = (TranspositionEntry) o;
        return hash == that.hash &&
                draft == that.draft &&
                move == that.move &&
                value == that.value &&
                bound == that.bound;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(hash);
    }


}
