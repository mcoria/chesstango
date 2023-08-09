package net.chesstango.mbeans;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record GameDescriptionInitial(String gameId, String initialFEN, String white, String black,
                                     String turn) implements Serializable {
}
