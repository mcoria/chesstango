package net.chesstango.search.smart.transposition;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntry implements Serializable {

    public long hash;
    public int searchDepth;

    public long bestMoveAndValue;

    public TranspositionType transpositionType;


    private static final long VALUE_MASK = 0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L;

    public static long encodedMoveAndValue(short move, int value) {
        long encodedMoveLng = ((long) move) << 32;

        long encodedValueLng = VALUE_MASK & value;

        return encodedValueLng | encodedMoveLng;
    }

    public static int decodeValue(long encodedMoveAndValue) {
        return (int) (VALUE_MASK & encodedMoveAndValue);
    }

    public static short decodeMove(long encodedMoveAndValue) {
        return (short) (encodedMoveAndValue >> 32);
    }
}
