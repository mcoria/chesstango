package net.chesstango.search.smart.alphabeta.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
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

    private TTable maxMap;
    private TTable minMap;

    private int depth;

    private Move[] bestMoves;

    protected final TranspositionEntry entryWorkspace;

    public TranspositionTableAbstract() {
        entryWorkspace = new TranspositionEntry();
    }

    protected abstract boolean isTranspositionEntryValid(int draft);

    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        int draft = depth - currentPly;

        long hash = game.getPosition().getZobristHash();

        boolean load = maxMap.load(hash, entryWorkspace);

        if (load && isTranspositionEntryValid(draft)) {
            // Es un valor exacto
            if (entryWorkspace.getBound() == EXACT) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            } else if (entryWorkspace.getBound() == LOWER_BOUND && beta <= entryWorkspace.getValue()) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            } else if (entryWorkspace.getBound() == UPPER_BOUND && entryWorkspace.getValue() <= alpha) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            }
        }

        long moveAndValue = next.maximize(currentPly, alpha, beta);

        /**
         * Aca deberiamos llamar a la estrategia para deterimanr si reemplazamos o no
         */

        writeTransposition(maxMap, hash, currentPly, draft, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        int draft = depth - currentPly;

        long hash = game.getPosition().getZobristHash();

        boolean load = minMap.load(hash, entryWorkspace);

        if (load && isTranspositionEntryValid(draft)) {
            // Es un valor exacto
            if (entryWorkspace.getBound() == EXACT) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            } else if (entryWorkspace.getBound() == LOWER_BOUND && beta <= entryWorkspace.getValue()) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            } else if (entryWorkspace.getBound() == UPPER_BOUND && entryWorkspace.getValue() <= alpha) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            }
        }

        long moveAndValue = next.minimize(currentPly, alpha, beta);

        /**
         * Aca deberiamos llamar a la estrategia para deterimanr si reemplazamos o no
         */

        writeTransposition(minMap, hash, currentPly, draft, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    private void writeTransposition(TTable table, long hash, int currentPly, int draft, int alpha, int beta, long moveAndValue) {
        short move = bestMoves[currentPly] != null ? bestMoves[currentPly].binaryEncoding() : 0;
        int value = AlphaBetaHelper.decodeValue(moveAndValue);

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

        table.save(entryWorkspace);
    }
}
