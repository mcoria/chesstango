package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.smart.transposition.TTable;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class SearchContext {

    private final int maxPly;

    private TTable tTable;

    private TTable qTTable;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }

}
