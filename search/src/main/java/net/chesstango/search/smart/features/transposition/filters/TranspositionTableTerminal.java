package net.chesstango.search.smart.features.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

/**
 * Terminal filter in the alpha-beta search chain that stores terminal (leaf) positions in transposition tables.
 * <p>
 * This filter executes after the search reaches terminal positions and stores the exact evaluation values
 * in the transposition tables. It maintains separate tables for maximizing and minimizing positions,
 * as well as separate tables for regular search (maxMap, minMap) and quiescence search (maxQMap, minQMap).
 * <p>
 * The stored entries are marked with:
 * - EXACT bound (since these are terminal evaluations)
 * - Draft value of 0 (terminal positions have no depth)
 * - The evaluated position's Zobrist hash
 * - The move and value returned from the terminal evaluation
 * <p>
 * This filter ensures that terminal positions can be quickly retrieved in future searches without
 * re-evaluation, improving search efficiency.
 * 
 * @author Mauricio Coria
 */
@Setter
@Getter
public class TranspositionTableTerminal implements AlphaBetaFilter {

    private Game game;

    private TTable maxMap;
    private TTable minMap;
    private TTable maxQMap;
    private TTable minQMap;

    private AlphaBetaFilter next;

    private final TranspositionEntry entryWorkspace;

    public TranspositionTableTerminal() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.maximize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();

        entryWorkspace.setHash(hash);
        entryWorkspace.setBound(TranspositionBound.EXACT);
        entryWorkspace.setDraft(0); // No debiera ser INFINITO ???
        entryWorkspace.setMove(AlphaBetaHelper.decodeMove(bestMoveAndValue));
        entryWorkspace.setValue(AlphaBetaHelper.decodeValue(bestMoveAndValue));

        if (!maxMap.load(hash, entryWorkspace)) {
            maxMap.save(entryWorkspace);
        }
        if (!maxQMap.load(hash, entryWorkspace)) {
            maxQMap.save(entryWorkspace);
        }

        return bestMoveAndValue;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.minimize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();

        entryWorkspace.setHash(hash);
        entryWorkspace.setBound(TranspositionBound.EXACT);
        entryWorkspace.setDraft(0);
        entryWorkspace.setMove(AlphaBetaHelper.decodeMove(bestMoveAndValue));
        entryWorkspace.setValue(AlphaBetaHelper.decodeValue(bestMoveAndValue));

        if (!minMap.load(hash, entryWorkspace)) {
            minMap.save(entryWorkspace);
        }
        if (!minQMap.load(hash, entryWorkspace)) {
            minQMap.save(entryWorkspace);
        }

        return bestMoveAndValue;
    }
}
