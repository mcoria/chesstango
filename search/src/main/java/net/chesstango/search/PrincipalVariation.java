package net.chesstango.search;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public record PrincipalVariation(List<PVMove> pvMoves,
                                 boolean pvComplete) implements Serializable {
}
