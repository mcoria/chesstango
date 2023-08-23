package net.chesstango.search.smart.transposition;

import net.chesstango.search.smart.BinaryUtils;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class QTransposition implements TranspositionEntry, Serializable {
    public long bestMoveAndValue;

    public TranspositionType qType;

    @Override
    public int getValue() {
        return BinaryUtils.decodeValue(bestMoveAndValue);
    }
}
