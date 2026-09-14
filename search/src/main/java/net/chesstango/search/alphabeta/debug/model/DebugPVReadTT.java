package net.chesstango.search.alphabeta.debug.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.alphabeta.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class DebugPVReadTT {

    /**
     * Este field es clonado del TranspositionEntry original
     */
    private TranspositionEntry entry;

    /**
     * Hash solicitado
     */
    private long hashRequested;
}
