package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetTranspositionPV implements SearchLifeCycle {
    private TTable maxMap;
    private TTable minMap;

    private TTable qMaxMap;
    private TTable qMinMap;
    private Game game;

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
        this.qMaxMap = context.getQMaxMap();
        this.qMinMap = context.getQMinMap();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        List<Move> principalVariation = calculatePrincipalVariation(result.getBestMove(), result.getDepth());
        result.setPrincipalVariation(principalVariation);
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    public List<Move> calculatePrincipalVariation(Move bestMove,
                                                  int depth) {

        List<Move> principalVariation = new ArrayList<>();

        Move move = bestMove;
        int pvMoveCounter = 0;
        do {

            principalVariation.add(move);

            game.executeMove(move);

            pvMoveCounter++;

            move = principalVariation.size() < depth
                    ? readMoveFromTT(maxMap, minMap)
                    : readMoveFromTT(qMaxMap, qMinMap);

        } while (move != null);

        for (int i = 0; i < pvMoveCounter; i++) {
            game.undoMove();
        }

        return principalVariation;
    }

    private Move readMoveFromTT(TTable maxMap, TTable minMap) {
        Move result = null;

        if (maxMap != null && minMap != null) {
            long hash = game.getChessPosition().getZobristHash();
            TranspositionEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);
            if (entry != null) {
                short bestMoveEncoded = TranspositionEntry.decodeBestMove(entry.movesAndValue);
                for (Move posibleMove : game.getPossibleMoves()) {
                    if (posibleMove.binaryEncoding() == bestMoveEncoded) {
                        result = posibleMove;
                        break;
                    }
                }
            }
        }

        return result;
    }
}