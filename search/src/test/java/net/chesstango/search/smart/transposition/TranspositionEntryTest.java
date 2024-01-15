package net.chesstango.search.smart.transposition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntryTest {

    @Test
    public void testShortOne() {
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
        short bestMove = (short) 0b10000000_00000001;
        int value = 0b10000000_00000000_00000000_00000001;

        long encodedMoveAndValue = TranspositionEntry.encode(bestMove, (byte) 0, value);

        assertEquals(0b00000000_10000000_00000001_00000000_10000000_00000000_00000000_00000001L, encodedMoveAndValue);
    }

    @Test
    public void testDecodeMovesAndValue() {
        long encodedMoveAndValue = 0b00000000_10101010_10101010_00000000_11111111_11111111_11111111_11111111L;

        short bestMove = TranspositionEntry.decodeBestMove(encodedMoveAndValue);
        int valueDepth = TranspositionEntry.decodeValueDepth(encodedMoveAndValue);
        int value = TranspositionEntry.decodeValue(encodedMoveAndValue);

        assertEquals((short) 0b10101010_10101010, bestMove);
        assertEquals(0b11111111_11111111_11111111_11111111, value);
        assertEquals(0, valueDepth);
    }

    @Test
    public void testEncodeDecodeValueMax() {
        long maxEncoded = TranspositionEntry.encode(1, Integer.MAX_VALUE);

        int maxDecoded = TranspositionEntry.decodeValue(maxEncoded);
        assertEquals(Integer.MAX_VALUE, maxDecoded);

        int valueDepth = TranspositionEntry.decodeValueDepth(maxEncoded);
        assertEquals(1, valueDepth);
    }

    @Test
    public void testEncodeDecodeValueMin() {
        long minEncoded = TranspositionEntry.encode(2, Integer.MIN_VALUE);

        int minDecoded = TranspositionEntry.decodeValue(minEncoded);
        assertEquals(Integer.MIN_VALUE, minDecoded);

        int valueDepth = TranspositionEntry.decodeValueDepth(minEncoded);
        assertEquals(2, valueDepth);
    }

    @Test
    public void testEncodeDecodeAllOneEncoding() {
        int value = 0b11111111_11111111_11111111_11111111;
        long valueEncoded = TranspositionEntry.encode(0b11111111, value);
        assertEquals(0b00000000_00000000_00000000_11111111_11111111_11111111_11111111_11111111L, valueEncoded);

        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);

        int valueDepth = TranspositionEntry.decodeValueDepth(valueEncoded);
        assertEquals(0b11111111, valueDepth);
    }

    @Test
    public void testEncodeDecodeValue01() {
        int value = 0b10000000_00000000_00000000_00000000;
        long valueEncoded = TranspositionEntry.encode(0, value);
        assertEquals(0b00000000_00000000_00000000_00000000_10000000_00000000_00000000_00000000L, valueEncoded);

        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    public void testEncodeDecodeValue02() {
        int value = 0b01000000_00000000_00000000_00000000;
        long valueEncoded = TranspositionEntry.encode(0, value);
        assertEquals(0b00000000_00000000_00000000_00000000_01000000_00000000_00000000_00000000L, valueEncoded);

        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    public void testEncodeDecodeValue03() {
        int value = 0b00100000_00000000_00000000_00000000;
        long valueEncoded = TranspositionEntry.encode(0, value);
        assertEquals(0b00000000_00000000_00000000_00000000_00100000_00000000_00000000_00000000L, valueEncoded);

        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);

    }

    @Test
    public void testEncodeDecodeValue04() {
        int value = 0b00000000_10000000_00000000_00000000;
        long valueEncoded = TranspositionEntry.encode(0, value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_10000000_00000000_00000000L, valueEncoded);

        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    public void testEncodeDecodeValue05() {
        int value = 0b00000000_00000001_00000000_00000000;
        long valueEncoded = TranspositionEntry.encode(0, value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000001_00000000_00000000L, valueEncoded);

        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    public void testEncodeDecodeValue06() {
        int value = 0b00000000_00000000_10000000_00000000;
        long valueEncoded = TranspositionEntry.encode(0, value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_10000000_00000000L, valueEncoded);

        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    public void testEncodeDecodeValue07() {
        int value = 0b00000000_00000000_00000001_00000000;
        long valueEncoded = TranspositionEntry.encode(0, value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000001_00000000L, valueEncoded);

        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }


    @Test
    public void testEncodeDecodeValue08() {
        int value = 0b00000000_00000000_00000000_10000000;
        long valueEncoded = TranspositionEntry.encode(0, value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_10000000L, valueEncoded);

        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    public void testEncodeDecodeValue09() {
        int value = 0b00000000_00000000_00000000_00000001;
        long valueEncoded = TranspositionEntry.encode(0, value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000001L, valueEncoded);

        int valueDecoded = TranspositionEntry.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }
}