package chess.ai.imp.smart;

import chess.board.Game;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class NegaMinMax extends AbstractSmart {

	private static final int DEFAULT_MAXLEVEL = 4;

	private boolean keepProcessing;

	private final int maxLevel;

	private final GameEvaluator evaluator = new GameEvaluator();

	public NegaMinMax() {
		this(DEFAULT_MAXLEVEL);
	}

	public NegaMinMax(int level) {
		this.maxLevel = level;
	}

	@Override
	public Move findBestMove(Game game) {
		keepProcessing = true;
		final List<Move> posibleMoves = new ArrayList<Move>();
		final MoveContainerReader movimientosPosible = game.getPossibleMoves();

		int betterEvaluation = GameEvaluator.INFINITE_NEGATIVE;

		for (Move move : movimientosPosible) {
			game.executeMove(move);

			int currentEvaluation = - negaMinMax(game, maxLevel - 1);

			if (currentEvaluation == betterEvaluation) {
				posibleMoves.add(move);
			} else {
				if (currentEvaluation > betterEvaluation) {
					betterEvaluation = currentEvaluation;
					posibleMoves.clear();
					posibleMoves.add(move);
				}
			}

			game.undoMove();
		}

		return selectMove(posibleMoves);
	}

	private int negaMinMax(Game game, int currentLevel) {
		int betterEvaluation = GameEvaluator.INFINITE_NEGATIVE;
		MoveContainerReader movimientosPosible = game.getPossibleMoves();
		if (currentLevel == 0 || movimientosPosible.size() == 0) {
			betterEvaluation = evaluator.evaluate(game);
		} else {
			for (Move move : movimientosPosible) {

				game.executeMove(move);

				int currentEvaluation = - negaMinMax(game, currentLevel - 1);

				if (currentEvaluation > betterEvaluation) {
					betterEvaluation = currentEvaluation;
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
