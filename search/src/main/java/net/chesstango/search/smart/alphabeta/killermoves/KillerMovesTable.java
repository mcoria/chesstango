package net.chesstango.search.smart.alphabeta.killermoves;

import net.chesstango.board.moves.Move;

import java.util.Objects;

import static net.chesstango.search.smart.Constants.MAX_DEPTH;

/**
 * @author Mauricio Coria
 */
public class KillerMovesTable implements KillerMoves {
    private final Move[] killerMovesTableA;
    private final Move[] killerMovesTableB;

    public KillerMovesTable() {
        this.killerMovesTableA = new Move[MAX_DEPTH];
        this.killerMovesTableB = new Move[MAX_DEPTH];
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
        for (int i = 0; i < MAX_DEPTH; i++) {
            killerMovesTableA[i] = null;
            killerMovesTableB[i] = null;
        }
    }
}
