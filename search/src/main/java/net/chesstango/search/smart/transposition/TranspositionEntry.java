package net.chesstango.search.smart.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.smart.BinaryUtils;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntry implements Serializable {

    @Getter
    @Setter
    private TranspositionType type;

    @Getter
    @Setter
    private int searchDepth;

    @Getter
    @Setter
    private long bestMoveAndValue;


    public int getValue() {
        return BinaryUtils.decodeValue(getBestMoveAndValue());
    }


    public void loadValues(TranspositionEntry refEntry) {
        this.bestMoveAndValue = refEntry.bestMoveAndValue;
        this.searchDepth = refEntry.searchDepth;
        this.type = refEntry.type;
    }
}
