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
        for (TranspositionType transpositionType :
                TranspositionType.values()) {
            if (transpositionType.byteValue == byteValue) {
                return transpositionType;
            }
        }
        if (byteValue == 0) {
            return null;
        }
        throw new RuntimeException("Unable to convert from byte to EntryType");
    }

    public static byte encode(TranspositionType transpositionType) {
        if (transpositionType == null) {
            return 0;
        }
        return transpositionType.byteValue;
    }

}
