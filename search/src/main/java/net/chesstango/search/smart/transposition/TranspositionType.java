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
        for (TranspositionType type :
                TranspositionType.values()) {
            if (type.byteValue == byteValue) {
                return type;
            }
        }
        if (byteValue == 0) {
            return null;
        }
        throw new RuntimeException("Unable to convert from byte to EntryType");
    }

    public static byte encode(TranspositionType type) {
        if (type == null) {
            return 0;
        }
        return type.byteValue;
    }

}
