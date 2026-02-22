package net.chesstango.search.smart.features.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

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
            if (entryWorkspace.getBound() == TranspositionBound.EXACT) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            } else if (entryWorkspace.getBound() == TranspositionBound.LOWER_BOUND && beta <= entryWorkspace.getValue()) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            } else if (entryWorkspace.getBound() == TranspositionBound.UPPER_BOUND && entryWorkspace.getValue() <= alpha) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            }
        }

        long moveAndValue = next.maximize(currentPly, alpha, beta);

        /**
         * Aca deberiamos llamar a la estrategia para deterimanr si reemplazamos o no
         */

        writeTransposition(maxMap, hash, draft, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        int draft = depth - currentPly;

        long hash = game.getPosition().getZobristHash();

        boolean load = minMap.load(hash, entryWorkspace);

        if (load && isTranspositionEntryValid(draft)) {
            // Es un valor exacto
            if (entryWorkspace.getBound() == TranspositionBound.EXACT) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            } else if (entryWorkspace.getBound() == TranspositionBound.LOWER_BOUND && beta <= entryWorkspace.getValue()) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            } else if (entryWorkspace.getBound() == TranspositionBound.UPPER_BOUND && entryWorkspace.getValue() <= alpha) {
                return AlphaBetaHelper.encode(entryWorkspace.getMove(), entryWorkspace.getValue());
            }
        }

        long moveAndValue = next.minimize(currentPly, alpha, beta);

        /**
         * Aca deberiamos llamar a la estrategia para deterimanr si reemplazamos o no
         */

        writeTransposition(minMap, hash, draft, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    protected void writeTransposition(TTable table, long hash, int draft, int alpha, int beta, long moveAndValue) {
        short move = AlphaBetaHelper.decodeMove(moveAndValue);
        int value = AlphaBetaHelper.decodeValue(moveAndValue);

        entryWorkspace.setHash(hash);
        entryWorkspace.setDraft(draft);
        entryWorkspace.setMove(move);
        entryWorkspace.setValue(value);

        if (beta <= value) {
            entryWorkspace.setBound(TranspositionBound.LOWER_BOUND);
        } else if (value <= alpha) {
            entryWorkspace.setBound(TranspositionBound.UPPER_BOUND);
        } else {
            entryWorkspace.setBound(TranspositionBound.EXACT);
        }

        table.save(entryWorkspace);
    }
}
