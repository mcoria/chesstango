package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public enum TranspositionBound {
    EXACT((byte) 0b00000001), LOWER_BOUND((byte) 0b00000010), UPPER_BOUND((byte) 0b00000011);
    private final byte byteValue;

    TranspositionBound(byte byteValue) {
        this.byteValue = byteValue;
    }


    public static TranspositionBound valueOf(byte byteValue) {
        if (byteValue == 0) {
            return null;
        }

        for (TranspositionBound transpositionBound :
                TranspositionBound.values()) {
            if (transpositionBound.byteValue == byteValue) {
                return transpositionBound;
            }
        }

        throw new RuntimeException("Unable to convert from byte to EntryType");
    }

    public byte binaryEncoding() {
        return byteValue;
    }

}
