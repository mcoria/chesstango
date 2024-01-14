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

    public static final long VALUE_MASK = 0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L;
    public static final long MOVE_MASK = 0b00000000_00000000_00000000_00000000_00000000_00000000_11111111_11111111L;

    public static long encode(int value) {
        return VALUE_MASK & value;
    }

    public static long encode(Move bestMove, int value) {
        short bestMoveEncoded = bestMove != null ? bestMove.binaryEncoding() : (short) 0;

        long bestMoveEncodedLng = (MOVE_MASK & bestMoveEncoded) << 48;

        long valueEncodedLng = VALUE_MASK & value;

        return bestMoveEncodedLng | valueEncodedLng;
    }

    public static int decodeValue(long encodedMovesAndValue) {
        return (int) encodedMovesAndValue;
    }

    public static short decodeBestMove(long encodedMovesAndValue) {
        return (short) (encodedMovesAndValue >> 48);
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
