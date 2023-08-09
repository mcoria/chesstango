package net.chesstango.search;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mauricio Coria
 */
public class StopSearchingException extends RuntimeException {
    @Getter
    @Setter
    private SearchMoveResult searchMoveResult;
}
