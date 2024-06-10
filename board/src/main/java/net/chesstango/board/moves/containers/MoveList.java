package net.chesstango.board.moves.containers;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.imp.MoveImp;

import java.io.Serial;
import java.util.ArrayList;

/**
 * @author Mauricio Coria
 */
public class MoveList extends ArrayList<MoveImp> {

    @Serial
    private static final long serialVersionUID = 1L;
    private boolean hasQuietMoves = true;

    @Override
    public boolean add(MoveImp move) {
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
