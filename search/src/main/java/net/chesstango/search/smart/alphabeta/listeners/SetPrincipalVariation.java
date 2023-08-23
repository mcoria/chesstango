package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.Transposition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class SetPrincipalVariation implements SearchLifeCycle {
    private TTable<Transposition> tTable;
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
        this.tTable = context.getTTable();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        if(result != null) {
            List<Move> principalVariation = calculatePrincipalVariation(game, result.getBestMove(), result.getDepth());
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
                                                    int depth) {

        List<Move> principalVariation = new ArrayList<>();

        if(tTable != null) {
            Move move = bestMove;
            int pvMoveCounter = 0;
            do {

                principalVariation.add(move);

                game.executeMove(move);
                pvMoveCounter++;

                move = principalVariation.size() < depth
                        ? readMoveFromTT(game)
                        : null; //readMoveFromQTT(game, maxMap, maxMap);

            } while (move != null);

            for (int i = 0; i < pvMoveCounter; i++) {
                game.undoMove();
            }
        } else {
            principalVariation.add(bestMove);
        }

        return principalVariation;
    }

    private Move readMoveFromTT(Game game) {
        Move result = null;

        long hash = game.getChessPosition().getZobristHash();

        Transposition entry = tTable.get(hash);

        if (entry != null) {
            short bestMoveEncoded = BinaryUtils.decodeMove(entry.getBestMoveAndValue());
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

        long hash = game.getChessPosition().getZobristHash();

        Transposition entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? qMaxMap.get(hash) : qMinMap.get(hash);

        if (entry != null) {
            short bestMoveEncoded = BinaryUtils.decodeMove(entry.getBestMoveAndValue());
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
