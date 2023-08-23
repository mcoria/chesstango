package net.chesstango.search.smart.transposition;

import lombok.Getter;
import net.chesstango.search.smart.BinaryUtils;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class QTransposition implements TranspositionEntry, Serializable {

    @Getter
    private long bestMoveAndValue;

    @Getter
    private TranspositionType type;

    @Override
    public int getValue() {
        return BinaryUtils.decodeValue(getBestMoveAndValue());
    }


    public void setBestMoveAndValue(long bestMoveAndValue) {
        this.bestMoveAndValue = bestMoveAndValue;
    }

    public void setType(TranspositionType type) {
        this.type = type;
    }
}
