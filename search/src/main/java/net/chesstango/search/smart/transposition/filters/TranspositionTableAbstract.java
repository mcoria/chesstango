package net.chesstango.search.smart.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.pv.model.PVWalkerFromTT;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import static net.chesstango.search.Bound.*;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
public abstract class TranspositionTableAbstract implements AlphaBetaFilter {

    protected final TranspositionEntry entryWorkspace;

    private AlphaBetaFilter next;

    private Game game;

    private TTable tTable;

    private int depth;

    private Move[] bestMoves;

    private PVWalkerFromTT pvWalkerFromTT;

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
            int value = entryWorkspace.getValue();
            // Es un valor exacto
            if (entryWorkspace.getBound() == EXACT) {
                pvWalkerFromTT.walkPrincipalVariation(currentPly, value);
                return value;
            } else if (entryWorkspace.getBound() == LOWER_BOUND && beta <= value) {
                return value;
            } else if (entryWorkspace.getBound() == UPPER_BOUND && value <= alpha) {
                return value;
            }
        }

        int value = next.alphaBeta(currentPly, alpha, beta);

        /**
         * Aca deberiamos llamar a la estrategia para deterimanr si reemplazamos o no
         */

        writeTransposition(hash, currentPly, draft, alpha, beta, value);

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
