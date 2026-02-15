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
@Setter
public class TranspositionTableTerminal implements AlphaBetaFilter {

    private Game game;

    private TTable maxMap;
    private TTable minMap;
    private TTable maxQMap;
    private TTable minQMap;

    @Getter
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
        entryWorkspace.setDraft(0);
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
