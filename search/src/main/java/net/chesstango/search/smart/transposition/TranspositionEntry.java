package net.chesstango.search.smart.transposition;

import net.chesstango.board.moves.Move;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntry implements Serializable {

    public long hash;
    public int searchDepth;

    public long moveAndValue;

    public TranspositionBound transpositionBound;

    public void reset() {
        hash = 0;
    }

    private static final long VALUE_MASK = 0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L;

    public static long encode(int value) {
        return VALUE_MASK & value;
    }

    public static long encode(Move move, int value) {
        short encodedMove = move != null ? move.binaryEncoding() : (short) 0;

        return encode(encodedMove, value);
    }

    public static long encode(short encodedMove, int value) {
        long encodedValueLng = VALUE_MASK & value;

        long encodedMoveLng = ((long) encodedMove) << 32;

        return encodedValueLng | encodedMoveLng;
    }

    public static int decodeValue(long encodedMoveAndValue) {
        return (int) (VALUE_MASK & encodedMoveAndValue);
    }

    public static short decodeMove(long encodedMoveAndValue) {
        return (short) (encodedMoveAndValue >> 32);
    }


}
