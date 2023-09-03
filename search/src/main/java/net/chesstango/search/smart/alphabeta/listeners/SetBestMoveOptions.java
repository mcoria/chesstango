package net.chesstango.search.smart.alphabeta.listeners;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.BinaryUtils;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SetBestMoveOptions implements SearchLifeCycle {
    private TTable maxMap;
    private TTable minMap;
    private Game game;
    private int maxPly;

    @Override
    public void beforeSearch(Game game, int maxDepth) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {

    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
        this.maxPly = context.getMaxPly();
        this.maxMap = context.getMaxMap();
        this.minMap = context.getMinMap();
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
        if (result != null) {
            List<Move> bestMoveOptions = findBestMoveOptions(result.getBestMove(), result.getEvaluation());
            result.setBestMoveOptions(bestMoveOptions);
            result.setEvaluationCollisions(bestMoveOptions.size() - 1);
        }
    }

    @Override
    public void stopSearching() {

    }

    @Override
    public void reset() {

    }

    public List<Move> findBestMoveOptions(final Move bestMove, final int bestMoveEvaluation) {
        List<Move> bestMoveOptions = new ArrayList<>();

        boolean bestMovePresent = false;
        for (Move move : game.getPossibleMoves()) {
            game.executeMove(move);

            long hash = game.getChessPosition().getZobristHash();

            TranspositionEntry entry = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? maxMap.get(hash) : minMap.get(hash);

            if (entry != null) {
                int value = BinaryUtils.decodeValue(entry.bestMoveAndValue);
                if (entry.searchDepth == maxPly - 1 && value == bestMoveEvaluation) {
                    bestMoveOptions.add(move);
                }
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
