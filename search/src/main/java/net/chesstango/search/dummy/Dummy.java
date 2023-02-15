/**
 * 
 */
package net.chesstango.search.dummy;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class Dummy implements SearchMove {

	@Override
	public Move searchBestMove(Game game) {
		return searchBestMove(game, 10);
	}

	@Override
	public Move searchBestMove(Game game, int depth) {
		Iterable<Move> moves = game.getPossibleMoves();

		Map<PiecePositioned, List<Move>> moveMap = new HashMap<PiecePositioned, List<Move>>();

		moves.forEach(move ->
				moveMap.computeIfAbsent(move.getFrom(), k -> new ArrayList<Move>())
						.add(move)
		);

		PiecePositioned[] pieces = moveMap.keySet().toArray(new PiecePositioned[moveMap.keySet().size()]);
		PiecePositioned selectedPiece = pieces[ThreadLocalRandom.current().nextInt(0, pieces.length)];

		List<Move> selectedMovesCollection = moveMap.get(selectedPiece);

		return selectedMovesCollection.get(ThreadLocalRandom.current().nextInt(0, selectedMovesCollection.size()));
	}

	@Override
	public void stopSearching() {
	}

	@Override
	public void setGameEvaluator(GameEvaluator evaluator) {

	}

}
