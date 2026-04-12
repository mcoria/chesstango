package net.chesstango.search.smart.alphabeta.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

import static net.chesstango.search.Bound.EXACT;

/**
 * @author Mauricio Coria
 */
@Setter
public class TranspositionTableRoot implements AlphaBetaFilter, Acceptor {

    @Getter
    private AlphaBetaFilter next;

    @Getter
    protected TTable maxMap;

    @Getter
    protected TTable minMap;

    protected Game game;

    protected int depth;

    private Move[] bestMoves;

    private final TranspositionEntry entryWorkspace;

    public TranspositionTableRoot() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public int maximize(final int currentPly, final int alpha, final int beta) {
        int value = next.maximize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();

        saveEntry(maxMap, hash, alpha, beta, value);

        return value;
    }

    @Override
    public int minimize(final int currentPly, final int alpha, final int beta) {
        int value = next.minimize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();

        saveEntry(minMap, hash, alpha, beta, value);

        return value;
    }

    protected void saveEntry(TTable table, long hash, int alpha, int beta, int value) {
        short move = bestMoves[0] != null ? bestMoves[0].binaryEncoding() : 0;
        //TranspositionBound bound;
        if (beta <= value) {
            //bound = LOWER_BOUND;
        } else if (value <= alpha) {
            //bound = UPPER_BOUND;
        } else {
            entryWorkspace.setHash(hash);
            entryWorkspace.setBound(EXACT);
            entryWorkspace.setDraft((byte) depth);
            entryWorkspace.setMove(move);
            entryWorkspace.setValue(value);

            table.save(entryWorkspace);
        }
    }
}
