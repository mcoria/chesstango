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
    @Setter
    private long hash;

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

    @Override
    public void loadValues(TranspositionEntry theEntry) {
        QTransposition refEntry = (QTransposition) theEntry;
        this.hash = refEntry.hash;
        this.bestMoveAndValue = refEntry.bestMoveAndValue;
        this.type = refEntry.type;
    }
}
