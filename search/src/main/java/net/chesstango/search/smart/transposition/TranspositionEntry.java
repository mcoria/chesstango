package net.chesstango.search.smart.transposition;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntry implements Serializable {

    public long hash;
    public int searchDepth;

    public long bestMoveAndValue;

    public int value;
    public TranspositionType transpositionType;
}
