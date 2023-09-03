package net.chesstango.search.smart.transposition;

import net.chesstango.board.moves.Move;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntry implements Serializable {

    public long hash;
    public int searchDepth;

    public long bestMoveAndValue;

    private static final long VALUE_MASK = 0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L;

    public static long encodedMoveAndValue(TranspositionType transpositionType, Move move, int value) {
        short encodedMove = move != null ? move.binaryEncoding() : (short) 0;

        return encodedMoveAndValue(transpositionType, encodedMove, value);
    }

    public static long encodedMoveAndValue(TranspositionType transpositionType, short encodedMove, int value) {
        long encodedMoveLng = ((long) encodedMove) << 32;

        long encodedValueLng = VALUE_MASK & value;

        long encodedTranspositionTypeLng = ((long) transpositionType.binaryEncoding()) << 48;

        return encodedTranspositionTypeLng | encodedValueLng | encodedMoveLng;
    }

    public static int decodeValue(long encodedMoveAndValue) {
        return (int) (VALUE_MASK & encodedMoveAndValue);
    }

    public static short decodeMove(long encodedMoveAndValue) {
        return (short) (encodedMoveAndValue >> 32);
    }

    public static TranspositionType decodeTransposition(long encodedMoveAndValue) {
        return TranspositionType.valueOf((byte) (encodedMoveAndValue >> 48));
    }


}
