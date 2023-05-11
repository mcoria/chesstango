package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.SANEncoder;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchListener;
import net.chesstango.search.smart.SearchContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class SetPrincipalVariation implements SearchListener {
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;
    private Map<Long, SearchContext.TableEntry> qMaxMap;
    private Map<Long, SearchContext.TableEntry> qMinMap;
    private Game game;

    @Override
    public void init(Game game, SearchContext context) {
        this.game = game;
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
        this.qMaxMap = context.getQMaxMap();
        this.qMinMap = context.getQMinMap();
    }

    @Override
    public void close(SearchMoveResult result) {
        List<String> principalVariation = calculatePrincipalVariation(game, result.getBestMove(), result.getDepth(), maxMap, minMap, qMaxMap, qMinMap);
        result.setPrincipalVariation(principalVariation);

    }

    public List<String> calculatePrincipalVariation(Game game,
                                                    Move bestMove,
                                                    int depth,
                                                    Map<Long, SearchContext.TableEntry> maxMap,
                                                    Map<Long, SearchContext.TableEntry> minMap,
                                                    Map<Long, SearchContext.TableEntry> qMaxMap,
                                                    Map<Long, SearchContext.TableEntry> qMinMap) {

        List<String> principalVariation = new ArrayList<>();

        Move move = bestMove;

        SANEncoder sanEncoder = new SANEncoder();
        int pvMoveCounter = 0;
        do {

            principalVariation.add(sanEncoder.encode(move, game.getPossibleMoves()));

            game.executeMove(move);
            pvMoveCounter++;

            move = principalVariation.size() < depth
                    ? readMoveFromTT(game, maxMap, minMap)
                    : readMoveFromQTT(game, qMaxMap, qMinMap);

        } while (move != null);

        for (int i = 0; i < pvMoveCounter; i++) {
            game.undoMove();
        }

        return principalVariation;
    }

    private Move readMoveFromTT(Game game, Map<Long, SearchContext.TableEntry> maxMap, Map<Long, SearchContext.TableEntry> minMap) {
        Move result = null;

        long hash = game.getChessPosition().getPositionHash();

        SearchContext.TableEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);

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

    private Move readMoveFromQTT(Game game, Map<Long, SearchContext.TableEntry> qMaxMap, Map<Long, SearchContext.TableEntry> qMinMap) {
        Move result = null;

        long hash = game.getChessPosition().getPositionHash();

        SearchContext.TableEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? qMaxMap.get(hash) : qMinMap.get(hash);

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
