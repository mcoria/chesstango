package net.chesstango.search.smart.features.transposition;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaHelper;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class TranspositionEntry implements Serializable, Cloneable, Comparable<TranspositionEntry> {

    public long hash;
    public int searchDepth;
    public long moveAndValue;
    public TranspositionBound transpositionBound;


    public void reset() {
        hash = 0;
        searchDepth = 0;
        moveAndValue = 0;
        transpositionBound = null;
    }

    @Override
    public TranspositionEntry clone() {
        return new TranspositionEntry()
                .setHash(hash)
                .setSearchDepth(searchDepth)
                .setMoveAndValue(moveAndValue)
                .setTranspositionBound(transpositionBound);
    }

    @Override
    public int compareTo(TranspositionEntry other) {
        int moveValue1 = AlphaBetaHelper.decodeValue(moveAndValue);
        int moveValue2 = AlphaBetaHelper.decodeValue(other.moveAndValue);

        int result = Integer.compare(moveValue1, moveValue2);

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
