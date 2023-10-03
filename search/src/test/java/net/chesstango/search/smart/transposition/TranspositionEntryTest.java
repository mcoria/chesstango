package net.chesstango.search.smart.transposition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntryTest {

    @Test
    public void shift() {
        short one = 0b00000000_00000001;
        assertEquals(1, one);
    }

    @Test
    public void testLongToIntCast() {
        long a = 0b00000000_00000000_00000000_00000001_00000000_00000000_00000000_00000000L;
        int castedA = (int) a;

        assertEquals(0, castedA);

        long b = 0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        int castedB = (int) b;

        assertEquals(0, castedB);
    }

    @Test
    public void testEncodedMoveAndValue() {
        short bestMove = (short) 0b10000000_00000000;
        short secondBestMove = 0b00001110_11011111;
        int value = 0b10000000_00000000_00000000_00000010;

        long bestMoveEncodedLng = (TranspositionEntry.MOVE_MASK & bestMove) << 48;
        long secondMoveEncodedLng = (TranspositionEntry.MOVE_MASK & secondBestMove) << 32;
        long encodedValueLng = TranspositionEntry.VALUE_MASK & value;

        long encodedMoveAndValue = encodedValueLng | bestMoveEncodedLng | secondMoveEncodedLng;

        assertEquals(0b10000000_00000000_00001110_11011111_10000000_00000000_00000000_00000010L, encodedMoveAndValue);
    }

    @Test
    public void testDecodeMovesAndValue() {
        long encodedMoveAndValue = 0b10000000_00000000_00001110_11011111_10000000_00000000_00000000_00000010L;

        short bestMove = TranspositionEntry.decodeBestMove(encodedMoveAndValue);
        short secondBestMove = TranspositionEntry.decodeSecondBestMove(encodedMoveAndValue);
        int value = TranspositionEntry.decodeValue(encodedMoveAndValue);

        assertEquals((short) 0b10000000_00000000, bestMove);
        assertEquals((short) 0b00001110_11011111, secondBestMove);
        assertEquals(0b10000000_00000000_00000000_00000010, value);
    }

    @Test
    public void testValueMaxEncoding() {
        long maxEncoded = TranspositionEntry.encode(Integer.MAX_VALUE);
        int maxDecoded = TranspositionEntry.decodeValue(maxEncoded);
        assertEquals(Integer.MAX_VALUE, maxDecoded);
    }

    @Test
    public void testValueMinEncoding() {
        long minEncoded = TranspositionEntry.encode(Integer.MIN_VALUE);
        int minDecoded = TranspositionEntry.decodeValue(minEncoded);
        assertEquals(Integer.MIN_VALUE, minDecoded);
    }

    @Test
    public void testValueAllOneEncoding() {
        int value = 0b11111111_11111111_11111111_11111111;
        long valueEncoded = TranspositionEntry.encode(value);
        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    public void testValue01() {
        int value = 0b10000000_00000000_00000000_00000000;
        long valueEncoded = TranspositionEntry.encode(value);
        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    public void testValue02() {
        int value = 0b01000000_00000000_00000000_00000000;
        long valueEncoded = TranspositionEntry.encode(value);
        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }
}
