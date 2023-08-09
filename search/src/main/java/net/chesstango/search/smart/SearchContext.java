package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;

import java.util.Map;
import java.util.Set;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class SearchContext {

    private final int maxPly;

    private Map<Long, Transposition> maxMap;

    private Map<Long, Transposition> minMap;

    public SearchContext(int maxPly) {
        this.maxPly = maxPly;
    }

}
