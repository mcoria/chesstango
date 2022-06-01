package chess.ai.imp.smart;

import java.util.ArrayList;
import java.util.List;

import chess.board.Color;
import chess.board.Game;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;

/**
 * @author Mauricio Coria
 *
 */
public class MinMax extends AbstractSmart {

	private boolean keepProcessing;
	// Beyond level 4, the performance is really bad
	private final int maxLevel = 4;

	private final GameEvaluator evaluator = new GameEvaluator();

	@Override
	public Move findBestMove(Game game) {
		keepProcessing = true;
		final boolean minOrMax = Color.BLACK.equals(game.getChessPositionReader().getCurrentTurn());
		final List<Move> posibleMoves = new ArrayList<Move>();
		final MoveContainerReader movimientosPosible = game.getPossibleMoves();

		int betterEvaluation = minOrMax ? Integer.MAX_VALUE: Integer.MIN_VALUE;

		for (Move move : movimientosPosible) {
			game.executeMove(move);

			int currentEvaluation = minMax(game, maxLevel - 1, !minOrMax);

			if (currentEvaluation == betterEvaluation) {
				posibleMoves.add(move);
			} else {
				if (minOrMax) {
					if (currentEvaluation < betterEvaluation) {
						betterEvaluation = currentEvaluation;
						posibleMoves.clear();
						posibleMoves.add(move);
					}
				} else {
					if (currentEvaluation > betterEvaluation) {
						betterEvaluation = currentEvaluation;
						posibleMoves.clear();
						posibleMoves.add(move);
					}
				}
			}

			game.undoMove();
		}

		return selectMove(posibleMoves);
	}

	private int minMax(Game game, int currentLevel, boolean minOrMax) {
		int betterEvaluation = minOrMax ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		MoveContainerReader movimientosPosible = game.getPossibleMoves();
		if (currentLevel == 0 || movimientosPosible.size() == 0) {
			betterEvaluation = evaluator.evaluate(game, maxLevel - currentLevel);
		} else {
			for (Move move : movimientosPosible) {

				game.executeMove(move);

				int currentEvaluation = minMax(game, currentLevel - 1, !minOrMax);
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

	@Override
	public void stopProcessing() {
		keepProcessing = false;
	}

}
