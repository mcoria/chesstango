package net.chesstango.board.moves.containers;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.imp.MoveCommandImp;
import net.chesstango.board.moves.imp.MoveImp;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class MovePair<M extends Move> {
    private M first;
    private M second;

    public int size() {
        int result = 0;
        if (first != null) {
            result++;
        }
        if (second != null) {
            result++;
        }
        return result;
    }

    public boolean contains(M move) {
        return move.equals(first) || move.equals(second);
    }
}
