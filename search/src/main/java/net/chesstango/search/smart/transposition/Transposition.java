package net.chesstango.search.smart.transposition;

import net.chesstango.search.smart.BinaryUtils;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class Transposition implements TranspositionEntry, Serializable {
    public int searchDepth;

    public long bestMoveAndValue;

    public TranspositionType type;

    @Override
    public int getValue() {
        return BinaryUtils.decodeValue(bestMoveAndValue);
    }

}
