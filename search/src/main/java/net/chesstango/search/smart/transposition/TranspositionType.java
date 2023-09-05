package net.chesstango.search.smart.transposition;

/**
 * @author Mauricio Coria
 */
public enum TranspositionType {
    EXACT((byte) 0b00000001), LOWER_BOUND((byte) 0b00000010), UPPER_BOUND((byte) 0b00000011);
    private final byte byteValue;

    TranspositionType(byte byteValue) {
        this.byteValue = byteValue;
    }


    public static TranspositionType valueOf(byte byteValue) {
        if (byteValue == 0) {
            return null;
        }
        for (TranspositionType transpositionType :
                TranspositionType.values()) {
            if (transpositionType.byteValue == byteValue) {
                return transpositionType;
            }
        }
        throw new RuntimeException("Unable to convert from byte to EntryType");
    }

    public byte binaryEncoding() {
        return byteValue;
    }

}
