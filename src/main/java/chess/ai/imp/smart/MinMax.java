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

	private static final int DEFAULT_MAXLEVEL = 4;

	// Beyond level 4, the performance is really bad
	private final int maxLevel;

	private final GameEvaluator evaluator = new GameEvaluator();

	public MinMax() {
		this(DEFAULT_MAXLEVEL);
	}

	public MinMax(int level) {
		this.maxLevel = level;
	}

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
			betterEvaluation = evaluator.evaluate(game);
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
