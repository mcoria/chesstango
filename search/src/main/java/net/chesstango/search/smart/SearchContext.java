package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Map;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class SearchContext {

    private final int maxPly;

    private Map<Long, TranspositionEntry> maxMap;

    private Map<Long, TranspositionEntry> minMap;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }

}
