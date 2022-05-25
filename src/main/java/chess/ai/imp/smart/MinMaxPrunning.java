/**
 * 
 */
package chess.ai.imp.smart;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import chess.ai.BestMoveFinder;
import chess.board.Color;
import chess.board.Game;
import chess.board.GameState.GameStatus;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;
import chess.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 */
public class MinMaxPrunning implements BestMoveFinder {

	private final int maxLevel = 6;

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

		return selectedMove(posibleMoves);
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

		return selectedMove(posibleMoves);
	}

	private int minimize(Game game, int currentLevel, final int alpha, final int beta) {
		int bestBeta = Integer.MAX_VALUE;
		MoveContainerReader movimientosPosible = game.getPossibleMoves();
		if (currentLevel == 0 || movimientosPosible.size() == 0) {
			bestBeta = evaluate(game, maxLevel - currentLevel);
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
			bestAlpha = evaluate(game, maxLevel - currentLevel);
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

	private int evaluate(Game game, int depth) {
		int evaluation = 0;
		if (GameStatus.JAQUE_MATE.equals(game.getGameStatus())) {
			evaluation = Color.BLACK.equals(game.getChessPositionReader().getCurrentTurn()) ? Integer.MAX_VALUE - depth
					: Integer.MIN_VALUE + depth;
		} else if (GameStatus.JAQUE.equals(game.getGameStatus())) {
			evaluation = Color.BLACK.equals(game.getChessPositionReader().getCurrentTurn()) ? 90 - depth : -90 + depth;
		} else {
			ChessPositionReader positionReader = game.getChessPositionReader();

			for (Iterator<PiecePositioned> it = positionReader.iteratorAllPieces(); it.hasNext(); ) {
				PiecePositioned piecePlacement = it.next();
				Piece piece = piecePlacement.getValue();

				evaluation += piece.getValue();
			}


		}

		return evaluation;
	}

	private Move selectedMove(List<Move> moves) {
		Map<PiecePositioned, Collection<Move>> moveMap = new HashMap<PiecePositioned, Collection<Move>>();

		for (Move move : moves) {
			PiecePositioned key = move.getFrom();
			Collection<Move> positionMoves = moveMap.get(key);
			if (positionMoves == null) {
				positionMoves = new ArrayList<Move>();
				moveMap.put(key, positionMoves);
			}
			positionMoves.add(move);
		}

		PiecePositioned[] pieces = moveMap.keySet().toArray(new PiecePositioned[moveMap.keySet().size()]);
		PiecePositioned selectedPiece = pieces[ThreadLocalRandom.current().nextInt(0, pieces.length)];

		Collection<Move> selectedMovesCollection = moveMap.get(selectedPiece);
		Move[] selectedMovesArray = selectedMovesCollection.toArray(new Move[selectedMovesCollection.size()]);

		return selectedMovesArray[ThreadLocalRandom.current().nextInt(0, selectedMovesArray.length)];
	}

}
