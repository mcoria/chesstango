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
public class SetPrincipalVariation implements SearchLifeCycle {
    private TTable maxMap;
    private TTable minMap;
    private Game game;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        if(result != null) {
            List<Move> principalVariation = calculatePrincipalVariation(game, result.getBestMove(), result.getDepth(), maxMap, minMap);
            result.setPrincipalVariation(principalVariation);
        }
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    public List<Move> calculatePrincipalVariation(Game game,
                                                    Move bestMove,
                                                    int depth,
                                                    TTable maxMap,
                                                    TTable minMap) {

        List<Move> principalVariation = new ArrayList<>();

        if(maxMap != null && minMap != null) {
            Move move = bestMove;
            int pvMoveCounter = 0;
            do {

                principalVariation.add(move);

                game.executeMove(move);
                pvMoveCounter++;

                move = principalVariation.size() < depth
                        ? readMoveFromTT(game, maxMap, minMap)
                        : readMoveFromQTT(game, maxMap, maxMap);

            } while (move != null);

            for (int i = 0; i < pvMoveCounter; i++) {
                game.undoMove();
            }
        } else {
            principalVariation.add(bestMove);
        }

        return principalVariation;
    }

    private Move readMoveFromTT(Game game, TTable maxMap, TTable minMap) {
        Move result = null;

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);

        if (entry != null) {
            short bestMoveEncoded = TranspositionEntry.decodeMove(entry.moveAndValue);
            for (Move posibleMove : game.getPossibleMoves()) {
                if (posibleMove.binaryEncoding() == bestMoveEncoded) {
                    result = posibleMove;
                    break;
                }
            }
            if (result == null) {
                throw new RuntimeException("BestMove not found");
            }
        }

        return result;
    }

    private Move readMoveFromQTT(Game game, TTable qMaxMap, TTable qMinMap) {
        Move result = null;

        long hash = game.getChessPosition().getZobristHash();

        TranspositionEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? qMaxMap.get(hash) : qMinMap.get(hash);

        if (entry != null) {
            short bestMoveEncoded = TranspositionEntry.decodeMove(entry.moveAndValue);
            if (bestMoveEncoded != 0) {
                for (Move posibleMove : game.getPossibleMoves()) {
                    if (posibleMove.binaryEncoding() == bestMoveEncoded) {
                        result = posibleMove;
                        break;
                    }
                }
                if (result == null) {
                    throw new RuntimeException("BestMove not found");
                }
            }
        }

        return result;
    }
}
