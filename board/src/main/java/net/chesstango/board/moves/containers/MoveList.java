package net.chesstango.board.moves.containers;

import net.chesstango.board.moves.Move;

import java.io.Serial;
import java.util.ArrayList;

/**
 * @author Mauricio Coria
 */
public class MoveList<M extends Move> extends ArrayList<M> {

    @Serial
    private static final long serialVersionUID = 1L;
    private boolean hasQuietMoves = true;

    @Override
    public boolean add(M move) {
        if (!move.isQuiet()) {
            hasQuietMoves = false;
        }
        return super.add(move);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (M move : this) {
            buffer.append(move).append("\n");
        }
        return buffer.toString();
    }

    public boolean hasQuietMoves() {
        return hasQuietMoves;
    }
}
