package net.chesstango.search.smart.features.transposition;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.moves.Move;

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
    public long movesAndValue;
    public TranspositionBound transpositionBound;


    public void reset() {
        hash = 0;
        searchDepth = 0;
        movesAndValue = 0;
        transpositionBound = null;
    }

    @Override
    public TranspositionEntry clone() {
        return new TranspositionEntry()
                .setHash(hash)
                .setSearchDepth(searchDepth)
                .setMovesAndValue(movesAndValue)
                .setTranspositionBound(transpositionBound);
    }

    @Override
    public int compareTo(TranspositionEntry other) {
        int moveValue1 = TranspositionEntry.decodeValue(movesAndValue);
        int moveValue2 = TranspositionEntry.decodeValue(other.movesAndValue);

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


    public static final long INTEGER_MASK = 0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L;
    public static final long SHORT_MASK = 0b00000000_00000000_00000000_00000000_00000000_00000000_11111111_11111111L;
    public static final long BYTE_MASK = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_11111111L;
    public static final int MOVE_SHIFT = 40;

    public static long encode(int value) {
        return encode((short) 0, value);
    }

    public static long encode(Move bestMove, int value) {
        short bestMoveEncoded = bestMove != null ? bestMove.binaryEncoding() : (short) 0;

        return encode(bestMoveEncoded, value);
    }

    public static long encode(short bestMoveEncoded, int value) {
        long valueEncodedLng = INTEGER_MASK & value;

        long bestMoveEncodedLng = (SHORT_MASK & bestMoveEncoded) << MOVE_SHIFT;

        return bestMoveEncodedLng | valueEncodedLng;
    }


    public static int decodeValue(long encodedMovesAndValue) {
        return (int) (encodedMovesAndValue & INTEGER_MASK);
    }

    public static short decodeBestMove(long encodedMovesAndValue) {
        return (short) ((encodedMovesAndValue >> MOVE_SHIFT) & SHORT_MASK);
    }

}
