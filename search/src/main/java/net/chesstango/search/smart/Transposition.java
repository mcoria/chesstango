package net.chesstango.search.smart;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class Transposition implements Serializable {
    public int searchDepth;

    public long bestMoveAndValue;

    private int value;

    public Type type;

    public long qBestMoveAndValue;

    private int qValue;

    public Type qType;

    public void setValue(int value) {
        this.value = value;
    }

    public void setQValue(int qValue) {
        this.qValue = qValue;
    }

    public int getValue() {
        return value;
    }

    public int getQValue() {
        return qValue;
    }

    public enum Type {
        EXACT((byte) 0b00000001), LOWER_BOUND((byte) 0b00000010), UPPER_BOUND((byte) 0b00000011);
        private final byte byteValue;

        Type(byte byteValue) {
            this.byteValue = byteValue;
        }


        public static Type valueOf(byte byteValue) {
            for (Type type :
                    Type.values()) {
                if (type.byteValue == byteValue) {
                    return type;
                }
            }
            if (byteValue == 0) {
                return null;
            }
            throw new RuntimeException("Unable to convert from byte to EntryType");
        }

        public static byte encode(Type type) {
            if (type == null) {
                return 0;
            }
            return type.byteValue;
        }

    }
}
