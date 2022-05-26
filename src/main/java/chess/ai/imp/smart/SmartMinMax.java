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
public class SmartMinMax extends AbstractSmart {
	
	private final int maxLevel = 3;

	private final GameEvaluator evaluator = new GameEvaluator();


	@Override
	public Move findBestMove(Game game) {
		boolean minOrMax = Color.BLACK.equals(game.getChessPositionReader().getCurrentTurn());
		int betterEvaluation = minOrMax ? Integer.MAX_VALUE: Integer.MIN_VALUE;
		int currentEvaluation = betterEvaluation;
		List<Move> posibleMoves = new ArrayList<Move>();
		//Move selectedMove = null;

		MoveContainerReader movimientosPosible = game.getPossibleMoves();
		for (Move move : movimientosPosible) {
			game.executeMove(move);

			currentEvaluation = minMax(game, maxLevel - 1, !minOrMax);


			if (currentEvaluation == betterEvaluation) {
				posibleMoves.add(move);
			} else {
				if (minOrMax) {
					if (currentEvaluation < betterEvaluation) {
						betterEvaluation = currentEvaluation;
						posibleMoves = new ArrayList<Move>();
						posibleMoves.add(move);
					}
				} else {
					if (currentEvaluation > betterEvaluation) {
						betterEvaluation = currentEvaluation;
						posibleMoves = new ArrayList<Move>();
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

			int currentEvaluation = betterEvaluation;
			for (Move move : movimientosPosible) {

				game.executeMove(move);

				currentEvaluation = minMax(game, currentLevel - 1, !minOrMax);

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

}
