
package net.chesstango.search.dummy;

import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.Move;
import net.chesstango.search.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Mauricio Coria
 */
public class Dummy implements Search {

    @Override
    public SearchResult search(Game game) {
        Iterable<? extends Move> moves = game.getPossibleMoves();

        Map<PiecePositioned, List<Move>> moveMap = new HashMap<>();

        moves.forEach(move ->
                moveMap.computeIfAbsent(move.getFrom(), k -> new ArrayList<>()).add(move)
        );

        Set<PiecePositioned> fromPieces = moveMap.keySet();

        PiecePositioned[] pieces = fromPieces.toArray(new PiecePositioned[moveMap.keySet().size()]);

        PiecePositioned selectedPiece = pieces[ThreadLocalRandom.current().nextInt(0, pieces.length)];

        List<Move> selectedMovesCollection = moveMap.get(selectedPiece);

        MoveEvaluation bestMove = new MoveEvaluation(selectedMovesCollection.get(ThreadLocalRandom.current().nextInt(0, selectedMovesCollection.size())), 0, MoveEvaluationType.EXACT);

        return new SearchResult().addSearchResultByDepth(new SearchResultByDepth(1).setBestMoveEvaluation(bestMove));
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
    public void setSearchResultByDepthListener(SearchResultByDepthListener searchResultByDepthListener) {

    }

}
