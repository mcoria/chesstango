package net.chesstango.search;

import net.chesstango.board.moves.Move;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public record PrincipalVariation(long hash, Move move) implements Serializable {
}
