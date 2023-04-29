package net.chesstango.board.moves;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class BinaryOperationTest {

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
        short encodedMove = 0b00001110_11011111;
        int value = 0b10000000_00000000_00000000_00000010;

        long encodedMoveLng = ((long) encodedMove) << 32;
        long encodedValueLng = 0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & value;

        long encodedMoveAndValue = encodedValueLng | encodedMoveLng;

        assertEquals(0b00000000_00000000_00001110_11011111_10000000_00000000_00000000_00000010L, encodedMoveAndValue);
    }

    @Test
    public void testDecodeMoveAndValue() {
        long encodedMoveAndValue = 0b00000000_00000000_00001110_11011111_10000000_00000000_00000000_00000010L;

        short move = (short) (encodedMoveAndValue >>> 32);
        int value = (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & encodedMoveAndValue);

        assertEquals(0b00001110_11011111, move);
        assertEquals(0b10000000_00000000_00000000_00000010, value);
    }
}
