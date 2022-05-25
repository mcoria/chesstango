package chess.ai.imp.smart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import chess.ai.BestMoveFinder;
import chess.board.Color;
import chess.board.Game;
import chess.board.GameState.GameStatus;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.iterators.SquareIterator;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;
import chess.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 */
public class SmartMinMax implements BestMoveFinder {
	
	private final int maxLevel = 3;


	@Override
	public Move findBestMove(Game game) {
		boolean minOrMax = Color.BLACK.equals(game.getChessPositionReader().getTurnoActual());
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

		return selectedMove(posibleMoves);
	}

	private int minMax(Game game, int currentLevel, boolean minOrMax) {
		int betterEvaluation = minOrMax ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		MoveContainerReader movimientosPosible = game.getPossibleMoves();
		if (currentLevel == 0 || movimientosPosible.size() == 0) {
			betterEvaluation = evaluate(game, maxLevel - currentLevel);
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


	private int evaluate(Game game, int depth) {
		int evaluation = 0;
		if (GameStatus.JAQUE_MATE.equals(game.getGameStatus())) {
			evaluation = Color.BLACK.equals(game.getChessPositionReader().getTurnoActual()) ? Integer.MAX_VALUE - depth
					: Integer.MIN_VALUE + depth;
		} else if (GameStatus.JAQUE.equals(game.getGameStatus())) {
			evaluation = Color.BLACK.equals(game.getChessPositionReader().getTurnoActual()) ? 90 - depth : -90 + depth;
		} else {
			ChessPositionReader reader = game.getChessPositionReader();

			SquareIterator iterator = reader.iteratorSquareWhitoutKing(Color.WHITE);
			while (iterator.hasNext()) {
				Piece pieza = reader.getPieza(iterator.next());
				evaluation += pieza.getValue();
			}

			iterator = reader.iteratorSquareWhitoutKing(Color.BLACK);
			while (iterator.hasNext()) {
				Piece pieza = reader.getPieza(iterator.next());
				evaluation += pieza.getValue();
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

		return  selectedMovesArray[ThreadLocalRandom.current().nextInt(0, selectedMovesArray.length)];
	}	

}
