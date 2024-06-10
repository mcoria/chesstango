package net.chesstango.board.moves.containers;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.imp.MoveImp;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
public class MovePair {
    private MoveImp first;
    private MoveImp second;

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

    public boolean contains(Move move) {
        return move.equals(first) || move.equals(second);
    }
}
