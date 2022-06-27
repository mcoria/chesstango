package net.chesstango.ai.imp.smart;

import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.Move;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Mauricio Coria
 *
 */
public class MinMax extends AbstractSmart {

	private static final int DEFAULT_MAX_PLIES = 4;

	// Beyond level 4, the performance is really bad
	private final int totalPlies;

	private final GameEvaluator evaluator;

	public MinMax() {
		this(DEFAULT_MAX_PLIES, new GameEvaluator());
	}

	public MinMax(int level) {
		this(level, new GameEvaluator());
	}

	public MinMax(int level, GameEvaluator evaluator) {
		this.totalPlies = level;
		this.evaluator = evaluator;
	}

	@Override
	public Move searchBestMove(Game game) {
		return searchBestMove(game, 10);
	}

	@Override
	public Move searchBestMove(Game game, int depth) {
		this.keepProcessing = true;

		final boolean minOrMax = Color.WHITE.equals(game.getChessPosition().getCurrentTurn()) ? false : true;
		final List<Move> possibleMoves = new ArrayList<Move>();

		int betterEvaluation = minOrMax ? GameEvaluator.INFINITE_POSITIVE: GameEvaluator.INFINITE_NEGATIVE;

		for (Move move : game.getPossibleMoves()) {
			game = game.executeMove(move);

			int currentEvaluation = minMax(game, !minOrMax, totalPlies - 1);

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

			game = game.undoMove();
		}
		evaluation = betterEvaluation;

		return selectMove(possibleMoves);
	}

	protected int minMax(Game game, final boolean minOrMax, final int currentPly) {
		int betterEvaluation = minOrMax ? GameEvaluator.INFINITE_POSITIVE: GameEvaluator.INFINITE_NEGATIVE;
		if (currentPly == 0 || !game.getStatus().isInProgress()) {
			betterEvaluation = evaluator.evaluate(game);
		} else {
			for (Move move : game.getPossibleMoves()) {
				game = game.executeMove(move);

				int currentEvaluation = minMax(game, !minOrMax, currentPly - 1);
				if (minOrMax) {
					if (currentEvaluation < betterEvaluation) {
						betterEvaluation = currentEvaluation;
					}
				} else {
					if (currentEvaluation > betterEvaluation) {
						betterEvaluation = currentEvaluation;
					}
				}

				game = game.undoMove();
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
