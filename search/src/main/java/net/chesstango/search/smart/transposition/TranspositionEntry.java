package net.chesstango.search.smart.transposition;

import net.chesstango.board.moves.Move;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntry implements Serializable {

    public long hash;
    public int searchDepth;
    public long movesAndValue;
    public TranspositionBound transpositionBound;

    public static final long INTEGER_MASK = 0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L;
    public static final long SHORT_MASK = 0b00000000_00000000_00000000_00000000_00000000_00000000_11111111_11111111L;
    public static final long BYTE_MASK = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_11111111L;
    public static final int VALUE_DEPTH_SHIFT = 32;
    public static final int MOVE_SHIFT = 40;

    public static long encode(int valueDepth, int value) {
        return encode((short) 0, (byte) valueDepth, value);
    }

    public static long encode(Move bestMove, int valueDepth, int value) {
        short bestMoveEncoded = bestMove != null ? bestMove.binaryEncoding() : (short) 0;

        return encode(bestMoveEncoded, (byte) valueDepth, value);
    }

    protected static long encode(short bestMoveEncoded, byte valueDepth, int value) {
        long valueEncodedLng = INTEGER_MASK & value;

        long bestMoveEncodedLng = (SHORT_MASK & bestMoveEncoded) << MOVE_SHIFT;

        long valueDepthEncodedLng = (BYTE_MASK & valueDepth) << VALUE_DEPTH_SHIFT;

        return bestMoveEncodedLng | valueDepthEncodedLng | valueEncodedLng;
    }


    public static int decodeValue(long encodedMovesAndValue) {
        return (int) encodedMovesAndValue;
    }

    public static short decodeBestMove(long encodedMovesAndValue) {
        return (short) ((encodedMovesAndValue >> MOVE_SHIFT) & SHORT_MASK);
    }

    public static int decodeValueDepth(long encodedMovesAndValue) {
        return (int) ((encodedMovesAndValue >> VALUE_DEPTH_SHIFT) & BYTE_MASK);
    }

    public static boolean greaterThan(int currentValueDepth, int currentValue, int maxValueDepth, int maxValue) {
        if (currentValue > maxValue) {
            return true;
        } else if (currentValue == maxValue) {
            return currentValueDepth < maxValueDepth;
        }
        return false;
    }

    public static boolean lowerThan(int currentValueDepth, int currentValue, int minValueDepth, int minValue) {
        if (currentValue < minValue) {
            return true;
        } else if (currentValue == minValue) {
            return currentValueDepth < minValueDepth;
        }
        return false;
    }


    public void reset() {
        hash = 0;
        searchDepth = 0;
        movesAndValue = 0;
        transpositionBound = null;
    }

    boolean isStored(long hash) {
        return this.hash == hash;
    }
}
