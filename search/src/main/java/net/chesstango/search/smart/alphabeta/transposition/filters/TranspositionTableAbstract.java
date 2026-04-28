package net.chesstango.search.smart.alphabeta.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

import static net.chesstango.search.Bound.*;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public abstract class TranspositionTableAbstract implements AlphaBetaFilter {

    private AlphaBetaFilter next;

    private Game game;

    private TTable tTable;

    private int depth;

    private Move[] bestMoves;

    private TranspositionTablePVUpdate transpositionTablePVUpdate;

    protected final TranspositionEntry entryWorkspace;

    public TranspositionTableAbstract() {
        entryWorkspace = new TranspositionEntry();
    }

    protected abstract boolean isDraftAcceptable(int draft);

    @Override
    public int alphaBeta(final int currentPly, final int alpha, final int beta) {
        int draft = depth - currentPly;

        long hash = game.getPosition().getZobristHash();

        boolean load = tTable.load(hash, entryWorkspace);

        if (load && hash == entryWorkspace.getHash() && isDraftAcceptable(draft)) {
            // Es un valor exacto
            if (entryWorkspace.getBound() == EXACT) {
                int value = entryWorkspace.getValue();
                transpositionTablePVUpdate.walkPrincipalVariation(currentPly, value);
                return entryWorkspace.getValue();
            } else if (entryWorkspace.getBound() == LOWER_BOUND && beta <= entryWorkspace.getValue()) {
                return entryWorkspace.getValue();
            } else if (entryWorkspace.getBound() == UPPER_BOUND && entryWorkspace.getValue() <= alpha) {
                return entryWorkspace.getValue();
            }
        }

        int value = next.alphaBeta(currentPly, alpha, beta);

        // Simple replacement schema
        if (load) {
            if (draft >= entryWorkspace.getDraft() && entryWorkspace.getBound() != EXACT) {
                writeTransposition(hash, currentPly, draft, alpha, beta, value);
            } else if (alpha < value && value < beta) {
                writeTransposition(hash, currentPly, draft, alpha, beta, value);
            }
        } else {
            writeTransposition(hash, currentPly, draft, alpha, beta, value);
        }

        return value;
    }

    private void writeTransposition(long hash, int currentPly, int draft, int alpha, int beta, int value) {
        short move = bestMoves[currentPly] != null ? bestMoves[currentPly].binaryEncoding() : 0;

        entryWorkspace.setHash(hash);
        entryWorkspace.setDraft((byte) draft);
        entryWorkspace.setMove(move);
        entryWorkspace.setValue(value);

        if (beta <= value) {
            entryWorkspace.setBound(LOWER_BOUND);
        } else if (value <= alpha) {
            entryWorkspace.setBound(UPPER_BOUND);
        } else {
            entryWorkspace.setBound(EXACT);
        }

        tTable.save(entryWorkspace);
    }
}
