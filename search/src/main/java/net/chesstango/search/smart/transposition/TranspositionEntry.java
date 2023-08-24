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

    /**
     * No tiene sentido en entradas Q por eso no lo setteamos este valor cuando escribimos
     */
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
        this.type = refEntry.type;
        this.searchDepth = refEntry.searchDepth;
        this.bestMoveAndValue = refEntry.bestMoveAndValue;
    }
}
