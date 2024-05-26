package net.chesstango.board.moves.containers;

import net.chesstango.board.moves.Move;

import java.util.ArrayList;

/**
 * @author Mauricio Coria
 */
public class MoveList extends ArrayList<Move> {

    private static final long serialVersionUID = 1L;
    private boolean hasQuietMoves = true;

    @Override
    public boolean add(Move move) {
        if (!move.isQuiet()) {
            hasQuietMoves = false;
        }
        return super.add(move);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (Move move : this) {
            buffer.append(move).append("\n");
        }
        return buffer.toString();
    }

    public boolean hasQuietMoves() {
        return hasQuietMoves;
    }
}
