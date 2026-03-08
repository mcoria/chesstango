package net.chesstango.search.smart.alphabeta.transposition;

import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntryTest {

    @Test
    void testShortOne() {
        short one = 0b00000000_00000001;
        assertEquals(1, one);
    }

    @Test
    void testLongToIntCast() {
        long a = 0b00000000_00000000_00000000_00000001_00000000_00000000_00000000_00000000L;
        int castedA = (int) a;

        assertEquals(0, castedA);

        long b = 0b10000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        int castedB = (int) b;

        assertEquals(0, castedB);
    }

    @Test
    void testEncodedMoveAndValue() {
        short bestMove = (short) 0b10000000_00000001;
        int value = 0b10000000_00000000_00000000_00000001;

        long encodedMoveAndValue = AlphaBetaHelper.encode(bestMove, value);

        assertEquals(0b00000000_10000000_00000001_00000000_10000000_00000000_00000000_00000001L, encodedMoveAndValue);
    }

    @Test
    void testDecodeMovesAndValue() {
        long encodedMoveAndValue = 0b00000000_10101010_10101010_00000000_11111111_11111111_11111111_11111111L;

        short bestMove = AlphaBetaHelper.decodeMove(encodedMoveAndValue);
        int value = AlphaBetaHelper.decodeValue(encodedMoveAndValue);

        assertEquals((short) 0b10101010_10101010, bestMove);
        assertEquals(0b11111111_11111111_11111111_11111111, value);
    }

    @Test
    void testEncodeDecodeValueMax() {
        long maxEncoded = AlphaBetaHelper.encode(Integer.MAX_VALUE);

        int maxDecoded = AlphaBetaHelper.decodeValue(maxEncoded);
        assertEquals(Integer.MAX_VALUE, maxDecoded);
    }

    @Test
    void testEncodeDecodeValueMin() {
        long minEncoded = AlphaBetaHelper.encode(Integer.MIN_VALUE);

        int minDecoded = AlphaBetaHelper.decodeValue(minEncoded);
        assertEquals(Integer.MIN_VALUE, minDecoded);
    }

    @Test
    void testEncodeDecodeAllOneEncoding() {
        int value = 0b11111111_11111111_11111111_11111111;
        long valueEncoded = AlphaBetaHelper.encode(value);
        assertEquals(0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L, valueEncoded);

        int valueDecoded = AlphaBetaHelper.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    void testEncodeDecodeValue01() {
        int value = 0b10000000_00000000_00000000_00000000;
        long valueEncoded = AlphaBetaHelper.encode(value);
        assertEquals(0b00000000_00000000_00000000_00000000_10000000_00000000_00000000_00000000L, valueEncoded);

        int valueDecoded = AlphaBetaHelper.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    void testEncodeDecodeValue02() {
        int value = 0b01000000_00000000_00000000_00000000;
        long valueEncoded = AlphaBetaHelper.encode(value);
        assertEquals(0b00000000_00000000_00000000_00000000_01000000_00000000_00000000_00000000L, valueEncoded);

        int valueDecoded = AlphaBetaHelper.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    void testEncodeDecodeValue03() {
        int value = 0b00100000_00000000_00000000_00000000;
        long valueEncoded = AlphaBetaHelper.encode(value);
        assertEquals(0b00000000_00000000_00000000_00000000_00100000_00000000_00000000_00000000L, valueEncoded);

        int valueDecoded = AlphaBetaHelper.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);

    }

    @Test
    void testEncodeDecodeValue04() {
        int value = 0b00000000_10000000_00000000_00000000;
        long valueEncoded = AlphaBetaHelper.encode(value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_10000000_00000000_00000000L, valueEncoded);

        int valueDecoded = AlphaBetaHelper.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    void testEncodeDecodeValue05() {
        int value = 0b00000000_00000001_00000000_00000000;
        long valueEncoded = AlphaBetaHelper.encode(value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000001_00000000_00000000L, valueEncoded);

        int valueDecoded = AlphaBetaHelper.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    void testEncodeDecodeValue06() {
        int value = 0b00000000_00000000_10000000_00000000;
        long valueEncoded = AlphaBetaHelper.encode(value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_10000000_00000000L, valueEncoded);

        int valueDecoded = AlphaBetaHelper.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    void testEncodeDecodeValue07() {
        int value = 0b00000000_00000000_00000001_00000000;
        long valueEncoded = AlphaBetaHelper.encode(value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000001_00000000L, valueEncoded);

        int valueDecoded = AlphaBetaHelper.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }


    @Test
    void testEncodeDecodeValue08() {
        int value = 0b00000000_00000000_00000000_10000000;
        long valueEncoded = AlphaBetaHelper.encode(value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_10000000L, valueEncoded);

        int valueDecoded = AlphaBetaHelper.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    void testEncodeDecodeValue09() {
        int value = 0b00000000_00000000_00000000_00000001;
        long valueEncoded = AlphaBetaHelper.encode(value);
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000001L, valueEncoded);

        int valueDecoded = AlphaBetaHelper.decodeValue(valueEncoded);
        assertEquals(value, valueDecoded);
    }

    @Test
    void testCompare01() {
        TranspositionEntry t1 = new TranspositionEntry();
        t1.setBound(TranspositionBound.EXACT);

        TranspositionEntry t2 = new TranspositionEntry();
        t2.setBound(TranspositionBound.EXACT);

        assertEquals(0, t1.compareTo(t2));
        assertEquals(0, t2.compareTo(t1));
    }

    @Test
    void testCompareSameValue() {
        TranspositionEntry t1 = new TranspositionEntry();
        t1.setValue(0);
        t1.setBound(TranspositionBound.LOWER_BOUND);

        TranspositionEntry t2 = new TranspositionEntry();
        t2.setValue(0);
        t2.setBound(TranspositionBound.EXACT);

        TranspositionEntry t3 = new TranspositionEntry();
        t3.setValue(0);
        t3.setBound(TranspositionBound.UPPER_BOUND);

        assertTrue(t1.compareTo(t2) > 0);
        assertTrue(t2.compareTo(t1) < 0);

        assertTrue(t1.compareTo(t3) > 0);
        assertTrue(t3.compareTo(t1) < 0);

        assertTrue(t2.compareTo(t3) > 0);
        assertTrue(t3.compareTo(t2) < 0);
    }

    @Test
    void testCompareDifferentValue01() {
        TranspositionEntry t1 = new TranspositionEntry();
        t1.setValue(1);
        t1.setBound(TranspositionBound.LOWER_BOUND);

        TranspositionEntry t2 = new TranspositionEntry();
        t2.setValue(0);
        t2.setBound(TranspositionBound.EXACT);

        TranspositionEntry t3 = new TranspositionEntry();
        t3.setValue(-1);
        t3.setBound(TranspositionBound.UPPER_BOUND);

        assertTrue(t1.compareTo(t2) > 0);
        assertTrue(t2.compareTo(t1) < 0);

        assertTrue(t1.compareTo(t3) > 0);
        assertTrue(t3.compareTo(t1) < 0);

        assertTrue(t2.compareTo(t3) > 0);
        assertTrue(t3.compareTo(t2) < 0);
    }

    @Test
    void testCompareDifferentValue02() {
        TranspositionEntry t1 = new TranspositionEntry();
        t1.setValue(-1);
        t1.setBound(TranspositionBound.LOWER_BOUND);

        TranspositionEntry t2 = new TranspositionEntry();
        t2.setValue(0);
        t2.setBound(TranspositionBound.EXACT);

        TranspositionEntry t3 = new TranspositionEntry();
        t3.setValue(1);
        t3.setBound(TranspositionBound.UPPER_BOUND);

        assertTrue(t1.compareTo(t2) > 0);
        assertTrue(t2.compareTo(t1) < 0);

        assertTrue(t1.compareTo(t3) > 0);
        assertTrue(t3.compareTo(t1) < 0);

        assertTrue(t2.compareTo(t3) > 0);
        assertTrue(t3.compareTo(t2) < 0);
    }
}