package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class SetBestMoveOptions implements SearchListener {
    private Map<Long, SearchContext.TableEntry> maxMap;
    private Map<Long, SearchContext.TableEntry> minMap;
    private Game game;
    private int maxPly;

    @Override
    public void init(SearchContext context) {
        this.game = context.getGame();
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void close(SearchMoveResult result) {
        List<Move> bestMoveOptions = findBestMoveOptions(result.getBestMove(), result.getEvaluation());
        result.setBestMoveOptions(bestMoveOptions);
        result.setEvaluationCollisions(bestMoveOptions.size() - 1);
    }

    public List<Move> findBestMoveOptions(final Move bestMove, final int bestMoveEvaluation) {
        List<Move> bestMoveOptions = new ArrayList<>();

        boolean bestMovePresent = false;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            long hash = game.getChessPosition().getPositionHash();

            SearchContext.TableEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);

            if (entry != null && entry.searchDepth == maxPly - 1 && entry.value == bestMoveEvaluation) {
                bestMoveOptions.add(move);
            }

            if (move.equals(bestMove)) {
                bestMovePresent = true;
            }

            game.undoMove();
        }

        if (!bestMovePresent) {
            throw new RuntimeException("Best move is not present in game");
        }


        return bestMoveOptions;
    }
}
