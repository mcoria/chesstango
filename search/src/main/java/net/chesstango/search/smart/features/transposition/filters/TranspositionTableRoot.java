package net.chesstango.search.smart.features.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaHelper;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableRoot implements AlphaBetaFilter {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    protected TTable maxMap;

    @Setter
    protected TTable minMap;

    @Setter
    protected Game game;

    @Setter
    protected int depth;

    private final TranspositionEntry entryWorkspace;

    public TranspositionTableRoot() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public long maximize(final int currentPly, final int alpha, final int beta) {
        long moveAndValue = next.maximize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();

        saveEntry(maxMap, hash, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    @Override
    public long minimize(final int currentPly, final int alpha, final int beta) {
        long moveAndValue = next.minimize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();

        saveEntry(minMap, hash, alpha, beta, moveAndValue);

        return moveAndValue;
    }

    protected void saveEntry(TTable table, long hash, int alpha, int beta, long moveAndValue) {
        short move = AlphaBetaHelper.decodeMove(moveAndValue);
        int value = AlphaBetaHelper.decodeValue(moveAndValue);
        //TranspositionBound bound;
        if (beta <= value) {
            //bound = TranspositionBound.LOWER_BOUND;
        } else if (value <= alpha) {
            //bound = TranspositionBound.UPPER_BOUND;
        } else {
            entryWorkspace.setHash(hash);
            entryWorkspace.setBound(TranspositionBound.EXACT);
            entryWorkspace.setDraft(depth);
            entryWorkspace.setMove(move);
            entryWorkspace.setValue(value);

            table.save(entryWorkspace);
        }
    }
}
