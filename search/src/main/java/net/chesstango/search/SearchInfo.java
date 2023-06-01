package net.chesstango.search;

import net.chesstango.board.moves.Move;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public record SearchInfo(int depth, int selDepth, List<Move> pv) {
}
