/**
 * 
 */
package chess.ai.imp.smart;

import java.util.*;

import chess.board.Color;
import chess.board.Game;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;

/**
 * @author Mauricio Coria
 *
 */
public class MinMaxPrunning extends AbstractSmart {

	private final int maxLevel = 3;

	private final GameEvaluator evaluator = new GameEvaluator();

	@Override
	public Move findBestMove(Game game) {
		Move bestMove = null;
		if (Color.WHITE.equals(game.getChessPositionReader().getCurrentTurn())) {
			bestMove = findBestMoveWhite(game);
		} else {
			bestMove = findBestMoveBlack(game);
		}
		return bestMove;
	}

	public Move findBestMoveWhite(Game game) {
		int bestAlpha = Integer.MIN_VALUE;
		final int beta = Integer.MAX_VALUE;
		int currentValue = bestAlpha;

		List<Move> posibleMoves = null;

		MoveContainerReader movimientosPosible = game.getPossibleMoves();
		boolean breakLoop = false;
		for (Move move : movimientosPosible) {
			game.executeMove(move);

			currentValue = minimize(game, maxLevel - 1, bestAlpha, beta);

			if (currentValue > bestAlpha) {
				bestAlpha = currentValue;
				posibleMoves = new ArrayList<Move>();
				posibleMoves.add(move);
				if (currentValue == Integer.MAX_VALUE - 100) {
					breakLoop = true;
				}				
			} else if (currentValue == bestAlpha) {
				posibleMoves.add(move);
			}			

			game.undoMove();
			

			if (breakLoop) {
				break;
			}			
		}

		return selectMove(posibleMoves);
	}

	public Move findBestMoveBlack(Game game) {
		final int alpha = Integer.MIN_VALUE;
		int bestBeta = Integer.MAX_VALUE;
		int currentValue = bestBeta;

		List<Move> posibleMoves = null;

		MoveContainerReader movimientosPosible = game.getPossibleMoves();
		boolean breakLoop = false;
		for (Move move : movimientosPosible) {
			game.executeMove(move);

			currentValue = maximize(game, maxLevel - 1, alpha, bestBeta);

			if (currentValue < bestBeta) {
				bestBeta = currentValue;
				posibleMoves = new ArrayList<Move>();
				posibleMoves.add(move);

				if (currentValue == Integer.MIN_VALUE + 100) {
					breakLoop = true;
				}
			} else if (currentValue == bestBeta) {
				posibleMoves.add(move);
			}

			game.undoMove();

			if (breakLoop) {
				break;
			}
		}

		return selectMove(posibleMoves);
	}

	private int minimize(Game game, int currentLevel, final int alpha, final int beta) {
		int bestBeta = Integer.MAX_VALUE;
		MoveContainerReader movimientosPosible = game.getPossibleMoves();
		if (currentLevel == 0 || movimientosPosible.size() == 0) {
			bestBeta = evaluator.evaluate(game, maxLevel - currentLevel);
		} else {
			int currentValue = bestBeta;
			boolean breakLoop = false;
			for (Move move : movimientosPosible) {
				game.executeMove(move);

				currentValue = maximize(game, currentLevel - 1, alpha, bestBeta);

				if (currentValue < bestBeta) {
					bestBeta = currentValue;
					if (alpha >= bestBeta) {
						breakLoop = true;
					}
				}

				game.undoMove();

				if (breakLoop) {
					break;
				}
			}
		}
		return bestBeta;
	}

	private int maximize(Game game, int currentLevel, final int alpha, final int beta) {
		int bestAlpha = Integer.MIN_VALUE;
		MoveContainerReader movimientosPosible = game.getPossibleMoves();
		if (currentLevel == 0 || movimientosPosible.size() == 0) {
			bestAlpha = evaluator.evaluate(game, maxLevel - currentLevel);
		} else {
			int currentValue = bestAlpha;
			boolean breakLoop = false;
			for (Move move : movimientosPosible) {
				game.executeMove(move);

				currentValue = minimize(game, currentLevel - 1, bestAlpha, beta);

				if (currentValue > bestAlpha) {
					bestAlpha = currentValue;
					if (bestAlpha >= beta) {
						breakLoop = true;
					}
				}

				game.undoMove();

				if (breakLoop) {
					break;
				}
			}
		}
		return bestAlpha;
	}


}
