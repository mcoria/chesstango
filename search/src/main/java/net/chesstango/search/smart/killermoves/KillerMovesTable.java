package net.chesstango.search.smart.killermoves;

import net.chesstango.board.moves.Move;

import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class KillerMovesTable implements KillerMoves {
    private Move[] killerMovesTableA;
    private Move[] killerMovesTableB;

    public KillerMovesTable() {
        this.killerMovesTableA = new Move[50];
        this.killerMovesTableB = new Move[50];
    }

    @Override
    public boolean trackKillerMove(Move killerMove, int currentPly) {
        if (killerMove.isQuiet()) {
            if (!Objects.equals(killerMove, killerMovesTableA[currentPly - 2]) && !Objects.equals(killerMove, killerMovesTableB[currentPly - 2])) {
                killerMovesTableB[currentPly - 2] = killerMovesTableA[currentPly];
                killerMovesTableA[currentPly - 2] = killerMove;
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
        for (int i = 0; i < 50; i++) {
            killerMovesTableA[i] = null;
            killerMovesTableB[i] = null;
        }
    }
}
