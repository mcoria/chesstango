package net.chesstango.search.smart.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.smart.BinaryUtils;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class Transposition implements TranspositionEntry, Serializable {

    @Getter
    @Setter
    private long hash;

    @Getter
    @Setter
    private int searchDepth;

    @Getter
    @Setter
    private long bestMoveAndValue;

    @Getter
    @Setter
    private TranspositionType type;

    @Override
    public int getValue() {
        return BinaryUtils.decodeValue(getBestMoveAndValue());
    }
}
