/**
 * 
 */
package chess.ai.imp.dummy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import chess.ai.BestMoveFinder;
import chess.board.Game;
import chess.board.PiecePositioned;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class Dummy implements BestMoveFinder{

	@Override
	public Move findBestMove(Game game) {
		Iterable<Move> moves = game.getPossibleMoves();

		Map<PiecePositioned, Collection<Move>> moveMap = new HashMap<PiecePositioned, Collection<Move>>();

		moves.forEach(move -> {
			PiecePositioned key = move.getFrom();
			Collection<Move> positionMoves = moveMap.computeIfAbsent(key, k -> new ArrayList<Move>());
			positionMoves.add(move);
		});

		PiecePositioned[] pieces = moveMap.keySet().toArray(new PiecePositioned[moveMap.keySet().size()]);
		PiecePositioned selectedPiece = pieces[ThreadLocalRandom.current().nextInt(0, pieces.length)];

		Collection<Move> selectedMovesCollection = moveMap.get(selectedPiece);
		Move[] selectedMovesArray = selectedMovesCollection.toArray(new Move[selectedMovesCollection.size()]);

		return  selectedMovesArray[ThreadLocalRandom.current().nextInt(0, selectedMovesArray.length)];
	}

}
