package chess.ai.imp.smart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import chess.ai.BestMoveFinder;
import chess.board.Game;
import chess.board.PiecePositioned;
import chess.board.GameState.GameStatus;
import chess.board.iterators.square.SquareIterator;
import chess.board.moves.Move;
import chess.board.position.ChessPositionReader;

/**
 * @author Mauricio Coria
 *
 */
public class Smart implements BestMoveFinder {
	
	private final int maxLevel = 3;


	@Override
	public Move findBestMove(Game game) {
		int maxEvaluationValue = Integer.MIN_VALUE;
		int currentEvaluationValue = maxEvaluationValue;
		List<Move> posibleMoves = null; 
		//Move selectedMove = null;

		Collection<Move> movimientosPosible = game.getPossibleMoves();
		for (Move move : movimientosPosible) {
			game.executeMove(move);

			currentEvaluationValue = minMax(game, maxLevel - 1, true);

			if (currentEvaluationValue > maxEvaluationValue) {
				maxEvaluationValue = currentEvaluationValue;
				posibleMoves = new ArrayList<Move>();
				posibleMoves.add(move);
			} else if (currentEvaluationValue == maxEvaluationValue) {
				posibleMoves.add(move);
			}

			game.undoMove();
		}

		return selectedMove(posibleMoves);
	}

	private int minMax(Game game, int currentLevel, boolean minOrMax) {
		int minMaxEvaluationValue = minOrMax ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		int evaluation = 0;
		Collection<Move> movimientosPosible = game.getPossibleMoves();
		if (currentLevel == 0 || movimientosPosible.size() == 0) {
			evaluation = evaluate(game);

			minMaxEvaluationValue = minOrMax ? (evaluation * -1) : evaluation;
		} else {
			int currentEvaluationValue = minMaxEvaluationValue;

			for (Move move : movimientosPosible) {

				game.executeMove(move);

				currentEvaluationValue = minMax(game, currentLevel - 1, !minOrMax);

				if (minOrMax) {
					if (currentEvaluationValue < minMaxEvaluationValue) {
						minMaxEvaluationValue = currentEvaluationValue;
					}
				} else {
					if (currentEvaluationValue > minMaxEvaluationValue) {
						minMaxEvaluationValue = currentEvaluationValue;
					}
				}

				game.undoMove();
			}
		}
		return minMaxEvaluationValue;
	}


	private int evaluate(Game game) {
		int evaluation = 0;
		if(GameStatus.JAQUE_MATE.equals(game.getGameStatus())){
			// Nos quedamos sin movimiento, perdimos
			evaluation = (Integer.MIN_VALUE + 1);
		//} else if (GameStatus.JAQUE.equals(game.getGameStatus())){
		//	// Tenemos en jaque a nuestro rey, warning
		//	evaluation = (Integer.MIN_VALUE + 1) / 2;
		} else if (GameStatus.TABLAS.equals(game.getGameStatus())){
			//Evitar entrar en tablas
			evaluation = (Integer.MIN_VALUE + 1) / 2;
		} else {
			ChessPositionReader reader = game.getChessPositionReader();
			
			SquareIterator iterator = reader.iteratorSquareWhitoutKing(reader.getTurnoActual());
			while(iterator.hasNext()){
				iterator.next();
				//Square square = iterator.next();
				//Piece piece = reader.getPieza(square);
				evaluation++;
			}
			
			iterator = reader.iteratorSquareWhitoutKing(reader.getTurnoActual().opositeColor());
			while(iterator.hasNext()){
				iterator.next();
				//Square square = iterator.next();
				//Piece piece = reader.getPieza(square);
				evaluation--;
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
