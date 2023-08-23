package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.smart.transposition.QTransposition;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.Transposition;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class SearchContext {

    private final int maxPly;

    private TTable<Transposition> tTable;

    private TTable<QTransposition> qTTable;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }

}
