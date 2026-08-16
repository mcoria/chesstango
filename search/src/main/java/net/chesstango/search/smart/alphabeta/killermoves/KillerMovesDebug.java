package net.chesstango.search.smart.alphabeta.killermoves;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTracker;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;

import java.util.List;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class KillerMovesDebug implements KillerMoves, Acceptor {

    private DebugNodeTracker debugNodeTracker;

    private KillerMoves imp;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean trackKillerMove(Move move, int currentPly) {
        if (imp.trackKillerMove(move, currentPly)) {
            DebugNode currentNode = debugNodeTracker.getCurrentNode();
            currentNode.setKillerMove(move);
            return true;
        }
        return false;
    }

    @Override
    public boolean isKiller(Move move, int currentPly) {
        boolean result = imp.isKiller(move, currentPly);
        if (result) {
            DebugNode currentNode = debugNodeTracker.getCurrentNode();
            if (currentNode.getKillerMovesTableA() == null || move.equals(currentNode.getKillerMovesTableA())) {
                currentNode.setKillerMovesTableA(move);
            } else if (currentNode.getKillerMovesTableB() == null|| move.equals(currentNode.getKillerMovesTableB())) {
                currentNode.setKillerMovesTableB(move);
            } else if (!move.equals(currentNode.getKillerMovesTableA()) && !move.equals(currentNode.getKillerMovesTableB())) {
                throw new RuntimeException("Ya se encuentran 2 movimientos presentes en la tabla killermoves");
            }
        }
        return result;
    }

    @Override
    public void reset() {
        imp.reset();
    }
}
