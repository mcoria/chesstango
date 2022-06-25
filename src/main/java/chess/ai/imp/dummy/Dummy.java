/**
 * 
 */
package chess.ai.imp.dummy;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import chess.ai.BestMoveFinder;
import chess.board.Game;
import chess.board.PiecePositioned;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class Dummy implements BestMoveFinder {

	@Override
	public Move searchBestMove(Game game) {
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

}
