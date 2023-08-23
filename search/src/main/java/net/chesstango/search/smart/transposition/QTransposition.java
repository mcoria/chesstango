package net.chesstango.search.smart.transposition;

import net.chesstango.search.smart.BinaryUtils;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class QTransposition implements Serializable {
    public long qBestMoveAndValue;

    public TranspositionType qType;


    public int getQValue() {
        return BinaryUtils.decodeValue(qBestMoveAndValue);
    }
}
