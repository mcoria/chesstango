package net.chesstango.search;

import net.chesstango.board.moves.Move;

import java.io.Serializable;
import java.util.HexFormat;

/**
 * Represents an entry in the Principal Variation (PV) line of a chess game.
 * A Principal Variation is the sequence of best moves from a given position,
 * typically extracted from the transposition table after a search.
 *
 * @author Mauricio Coria
 * @param hash the Zobrist hash of the chess position
 * @param move the best move found for this position during search
 */
public record PrincipalVariation(long hash,
                                 Move move) implements Serializable {

    @Override
    public String toString() {
        return String.format("PrincipalVariation[hash=0x%sL, move=%s]", Long.toHexString(hash).toUpperCase(), move);
    }

}
