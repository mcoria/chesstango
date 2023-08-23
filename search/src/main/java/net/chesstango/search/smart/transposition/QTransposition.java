package net.chesstango.search.smart.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.smart.BinaryUtils;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class QTransposition implements TranspositionEntry, Serializable {

    @Getter
    private final long hash;

    @Getter
    @Setter
    private long bestMoveAndValue;

    @Getter
    @Setter
    private TranspositionType type;

    public QTransposition(long hash) {
        this.hash = hash;
    }

    @Override
    public int getValue() {
        return BinaryUtils.decodeValue(getBestMoveAndValue());
    }
}
