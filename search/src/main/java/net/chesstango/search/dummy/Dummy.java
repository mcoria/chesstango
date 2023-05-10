
package net.chesstango.search.dummy;

import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Mauricio Coria
 */
public class Dummy implements SearchMove {

    @Override
    public SearchMoveResult searchBestMove(Game game) {
        return searchBestMove(game, 10);
    }

    @Override
    public SearchMoveResult searchBestMove(Game game, int depth) {
        Iterable<Move> moves = game.getPossibleMoves();

        Map<PiecePositioned, List<Move>> moveMap = new HashMap<PiecePositioned, List<Move>>();

        moves.forEach(move ->
                moveMap.computeIfAbsent(move.getFrom(), k -> new ArrayList<Move>())
                        .add(move)
        );

        PiecePositioned[] pieces = moveMap.keySet().toArray(new PiecePositioned[moveMap.keySet().size()]);
        PiecePositioned selectedPiece = pieces[ThreadLocalRandom.current().nextInt(0, pieces.length)];

        List<Move> selectedMovesCollection = moveMap.get(selectedPiece);

        return new SearchMoveResult(depth, 0, selectedMovesCollection.get(ThreadLocalRandom.current().nextInt(0, selectedMovesCollection.size())), null);
    }

    @Override
    public void stopSearching() {
    }


}
