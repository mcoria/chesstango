package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.Map;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class SearchContext {

    private final int maxPly;

    private TTable maxMap;

    private TTable minMap;

    private TTable qMaxMap;

    private TTable qMinMap;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }

}
