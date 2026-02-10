package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaHelper {
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
