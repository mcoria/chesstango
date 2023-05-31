package net.chesstango.search.smart;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class Transposition implements Serializable {
    public int searchDepth;
    public long bestMoveAndValue;
    public int value;
    public Type type;


    public long qBestMoveAndValue;
    public int qValue;
    public Type qType;

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
