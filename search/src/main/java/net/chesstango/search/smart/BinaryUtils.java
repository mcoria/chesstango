package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
public class BinaryUtils {
    public static long encodedMoveAndValue(short move, int value){
        long encodedMoveLng = ((long) move) << 32;

        long encodedValueLng = 0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & value;

        return encodedValueLng | encodedMoveLng;
    }

    public static int decodeValue(long encodedMoveAndValue){
        return (int) (0b00000000_00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L & encodedMoveAndValue);
    }


}
