package net.chesstango.search.smart.alphabeta.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

import static net.chesstango.search.Bound.EXACT;

/**
 * Terminal filter in the alpha-beta search chain that stores terminal (leaf) positions in transposition tables.
 * <p>
 * This filter executes after the search reaches terminal positions and stores the exact evaluation values
 * in the transposition tables. It maintains separate tables for maximizing and minimizing positions,
 * as well as separate tables for regular search and quiescence search.
 * <p>
 * The stored entries are marked with:
 * - EXACT bound (since these are terminal evaluations)
 * - Draft value of 0 (terminal positions have no depth)
 * - The evaluated position's Zobrist hash
 * - The move and value returned from the terminal evaluation
 * <p>
 * This filter ensures that terminal positions can be quickly retrieved in future searches without
 * re-evaluation, improving search efficiency.
 * <p>
 * ESCRIBE DEMASIADO EN TT
 *
 * @author Mauricio Coria
 */
@Setter
@Getter
public class TranspositionTableLeaf implements AlphaBetaFilter, Acceptor {

    private Game game;

    private TTable tTable;

    private AlphaBetaFilter next;

    private final TranspositionEntry entryWorkspace;

    public TranspositionTableLeaf() {
        entryWorkspace = new TranspositionEntry();
        throw new RuntimeException("Deprecated class");
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public int maximize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.maximize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();

        if (!tTable.load(hash, entryWorkspace)) {
            entryWorkspace.setHash(hash);
            entryWorkspace.setBound(EXACT);
            entryWorkspace.setDraft((byte) 0);
            //entryWorkspace.setMove(AlphaBetaHelper.decodeMove(bestMoveAndValue));
            //entryWorkspace.setValue(AlphaBetaHelper.decodeValue(bestMoveAndValue));
            tTable.save(entryWorkspace);
        }

        //return bestMoveAndValue;
        return 0;
    }

    @Override
    public int minimize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.minimize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();


        if (!tTable.load(hash, entryWorkspace)) {
            entryWorkspace.setHash(hash);
            entryWorkspace.setBound(EXACT);
            entryWorkspace.setDraft((byte) 0);
            //entryWorkspace.setMove(AlphaBetaHelper.decodeMove(bestMoveAndValue));
            //entryWorkspace.setValue(AlphaBetaHelper.decodeValue(bestMoveAndValue));
            tTable.save(entryWorkspace);
        }

        //return bestMoveAndValue;
        return 0;
    }
}
