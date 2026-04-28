package net.chesstango.search.smart.alphabeta.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

import static net.chesstango.search.Bound.EXACT;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public class TranspositionTablePVUpdate implements Acceptor {

    private final TranspositionEntry entryWorkspace;

    private TriangularPVTable trianglePV;

    private Game game;

    private TTable tTable;

    public TranspositionTablePVUpdate() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    void walkPrincipalVariation(int currentPly, int eval) {
        long currentHash = game.getPosition().getZobristHash();
        Move currentMove = readMoveFromTT(currentHash, eval);

        boolean keepSign = false;
        int executeMoves = 0;
        while (currentMove != null) {

            trianglePV.writePV(currentPly, currentMove.binaryEncoding());

            currentMove.executeMove();
            executeMoves++;

            currentHash = game.getPosition().getZobristHash();
            currentMove = readMoveFromTT(currentHash, keepSign ? eval : -eval);
            keepSign = !keepSign;
        }

        for (int i = 0; i < executeMoves; i++) {
            game.undoMove();
        }
    }

    Move readMoveFromTT(long hash, int eval) {
        Move result = null;
        boolean load = tTable.load(hash, entryWorkspace);
        if (load && hash == entryWorkspace.getHash() && EXACT.equals(entryWorkspace.getBound()) && entryWorkspace.getValue() == eval) {
            short bestMoveEncoded = entryWorkspace.getMove();
            result = bestMoveEncoded != 0 ? getMove(bestMoveEncoded) : null;
        }
        return result;
    }

    Move getMove(short moveEncoded) {
        for (Move posibleMove : game.getPossibleMoves()) {
            if (posibleMove.binaryEncoding() == moveEncoded) {
                return posibleMove;
            }
        }
        return null;
    }
}
