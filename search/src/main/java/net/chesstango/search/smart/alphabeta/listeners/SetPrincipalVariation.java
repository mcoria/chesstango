package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.Transposition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class SetPrincipalVariation implements SearchLifeCycle {
    private Map<Long, Transposition> maxMap;
    private Map<Long, Transposition> minMap;
    private Game game;

    @Override
    public void initSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void closeSearch(SearchMoveResult result) {

    }

    @Override
    public void init(SearchContext context) {
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void close(SearchMoveResult result) {
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
                                                    Map<Long, Transposition> maxMap,
                                                    Map<Long, Transposition> minMap) {

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

    private Move readMoveFromTT(Game game, Map<Long, Transposition> maxMap, Map<Long, Transposition> minMap) {
        Move result = null;

        long hash = game.getChessPosition().getPositionHash();

        Transposition entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);

        if (entry != null) {
            short bestMoveEncoded = BinaryUtils.decodeMove(entry.bestMoveAndValue);
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

    private Move readMoveFromQTT(Game game, Map<Long, Transposition> qMaxMap, Map<Long, Transposition> qMinMap) {
        Move result = null;

        long hash = game.getChessPosition().getPositionHash();

        Transposition entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? qMaxMap.get(hash) : qMinMap.get(hash);

        if (entry != null) {
            short bestMoveEncoded = BinaryUtils.decodeMove(entry.bestMoveAndValue);
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
