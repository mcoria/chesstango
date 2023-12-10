
package net.chesstango.search.dummy;

import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.ProgressListener;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Mauricio Coria
 */
public class Dummy implements SearchMove {

    @Override
    public SearchMoveResult search(Game game) {
        Iterable<Move> moves = game.getPossibleMoves();

        Map<PiecePositioned, List<Move>> moveMap = new HashMap<PiecePositioned, List<Move>>();

        moves.forEach(move ->
                moveMap.computeIfAbsent(move.getFrom(), k -> new ArrayList<>()).add(move)
        );

        Set<PiecePositioned> fromPieces = moveMap.keySet();

        PiecePositioned[] pieces = fromPieces.toArray(new PiecePositioned[moveMap.keySet().size()]);

        PiecePositioned selectedPiece = pieces[ThreadLocalRandom.current().nextInt(0, pieces.length)];

        List<Move> selectedMovesCollection = moveMap.get(selectedPiece);

        return new SearchMoveResult(1, 0, selectedMovesCollection.get(ThreadLocalRandom.current().nextInt(0, selectedMovesCollection.size())), null);
    }

    @Override
    public void stopSearching() {
    }

    @Override
    public void reset() {
    }

    @Override
    public void setSearchParameter(SearchParameter parameter, Object value) {
    }

    @Override
    public void setProgressListener(ProgressListener progressListener) {

    }

}
