package chess.ai.imp.smart;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import chess.board.Color;
import chess.board.Game;
import chess.board.PiecePositioned;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;

/**
 * @author Mauricio Coria
 *
 */
public class MinMax extends AbstractSmart {

	private static final int DEFAULT_MAX_PLIES = 4;

	// Beyond level 4, the performance is really bad
	private final int totalPlies;

	private final GameEvaluator evaluator = new GameEvaluator();

	private Game game = null;

	public MinMax() {
		this(DEFAULT_MAX_PLIES);
	}

	public MinMax(int level) {
		this.totalPlies = level;
	}

	@Override
	public Move findBestMove(Game game) {
		this.keepProcessing = true;
		this.game = game;

		final boolean minOrMax = Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn()) ? false : true;
		final List<Move> possibleMoves = new ArrayList<Move>();

		int betterEvaluation = minOrMax ? GameEvaluator.INFINITE_POSITIVE: GameEvaluator.INFINITE_NEGATIVE;

		for (Move move : game.getPossibleMoves()) {
			game.executeMove(move);

			int currentEvaluation = minMax(!minOrMax, totalPlies - 1);

			if (currentEvaluation == betterEvaluation) {
				possibleMoves.add(move);
			} else {
				if (minOrMax) {
					if (currentEvaluation < betterEvaluation) {
						betterEvaluation = currentEvaluation;
						possibleMoves.clear();
						possibleMoves.add(move);
					}
				} else {
					if (currentEvaluation > betterEvaluation) {
						betterEvaluation = currentEvaluation;
						possibleMoves.clear();
						possibleMoves.add(move);
					}
				}
			}

			game.undoMove();
		}
		evaluation = betterEvaluation;

		return selectMove(possibleMoves);
	}

	private int minMax(final boolean minOrMax, final int currentPly) {
		int betterEvaluation = minOrMax ? GameEvaluator.INFINITE_POSITIVE: GameEvaluator.INFINITE_NEGATIVE;
		if (currentPly == 0 || game.getPossibleMoves().size() == 0) {
			betterEvaluation = evaluator.evaluate(game);
		} else {
			for (Move move : game.getPossibleMoves()) {

				game.executeMove(move);

				int currentEvaluation = minMax( !minOrMax, currentPly - 1);
				if (minOrMax) {
					if (currentEvaluation < betterEvaluation) {
						betterEvaluation = currentEvaluation;
					}
				} else {
					if (currentEvaluation > betterEvaluation) {
						betterEvaluation = currentEvaluation;
					}
				}

				game.undoMove();
			}
		}
		return betterEvaluation;
	}

	protected Move selectMove(Collection<Move> moves) {
		if(moves.size() == 0){
			throw new RuntimeException("There is no move to select");
		}
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
}
