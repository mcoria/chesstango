package net.chesstango.search.smart.alphabeta.killermoves;

import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.Constants;

import java.util.Objects;

import static net.chesstango.search.smart.Constants.KILLER_MOVES_TABLE_SIZE;

/**
 * @author Mauricio Coria
 */
public class KillerMovesTable implements KillerMoves {
    private final Move[] killerMovesTableA;
    private final Move[] killerMovesTableB;

    public KillerMovesTable() {
        this.killerMovesTableA = new Move[KILLER_MOVES_TABLE_SIZE];
        this.killerMovesTableB = new Move[KILLER_MOVES_TABLE_SIZE];
    }

    @Override
    public boolean trackKillerMove(Move move, int currentPly) {
        if (move.isQuiet()) {
            if (!Objects.equals(move, killerMovesTableA[currentPly - 2]) && !Objects.equals(move, killerMovesTableB[currentPly - 2])) {
                killerMovesTableB[currentPly - 2] = killerMovesTableA[currentPly - 2];
                killerMovesTableA[currentPly - 2] = move;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isKiller(Move move, int currentPly) {
        return Objects.equals(move, killerMovesTableA[currentPly - 1]) || Objects.equals(move, killerMovesTableB[currentPly - 1]);
    }

    @Override
    public void reset() {
        for (int i = 0; i < KILLER_MOVES_TABLE_SIZE; i++) {
            killerMovesTableA[i] = null;
            killerMovesTableB[i] = null;
        }
    }
}
