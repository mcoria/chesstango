package net.chesstango.search.smart.features.transposition.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaHelper;
import net.chesstango.search.smart.features.transposition.TTable;
import net.chesstango.search.smart.features.transposition.TranspositionBound;

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

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public long maximize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.maximize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();
        if (maxMap.read(hash) == null) {
            maxMap.write(hash, TranspositionBound.EXACT, 0, AlphaBetaHelper.decodeMove(bestMoveAndValue), AlphaBetaHelper.decodeValue(bestMoveAndValue));
        }
        if (maxQMap.read(hash) == null) {
            maxQMap.write(hash, TranspositionBound.EXACT, 0, AlphaBetaHelper.decodeMove(bestMoveAndValue), AlphaBetaHelper.decodeValue(bestMoveAndValue));
        }

        return bestMoveAndValue;
    }

    @Override
    public long minimize(int currentPly, int alpha, int beta) {
        long bestMoveAndValue = next.minimize(currentPly, alpha, beta);

        long hash = game.getPosition().getZobristHash();
        if (minMap.read(hash) == null) {
            minMap.write(hash, TranspositionBound.EXACT, 0, AlphaBetaHelper.decodeMove(bestMoveAndValue), AlphaBetaHelper.decodeValue(bestMoveAndValue));
        }
        if (minQMap.read(hash) == null) {
            minQMap.write(hash, TranspositionBound.EXACT, 0, AlphaBetaHelper.decodeMove(bestMoveAndValue), AlphaBetaHelper.decodeValue(bestMoveAndValue));
        }

        return bestMoveAndValue;
    }
}
