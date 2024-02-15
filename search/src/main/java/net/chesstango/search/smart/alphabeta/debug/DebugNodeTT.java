package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.transposition.TranspositionBound;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class DebugNodeTT{
    private long hash_requested;
    private String tableName;
    private long hash;
    private int depth;
    private long movesAndValue;
    private TranspositionBound bound;
}
