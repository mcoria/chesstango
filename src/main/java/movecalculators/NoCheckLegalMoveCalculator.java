package movecalculators;

import java.util.Collection;

import chess.BoardState;
import chess.Move;
import chess.Square;
import iterators.SquareIterator;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movegenerators.MoveGeneratorStrategy;
import movegenerators.ReyAbstractMoveGenerator;

public class NoCheckLegalMoveCalculator extends AbstractLegalMoveCalculator {

	public NoCheckLegalMoveCalculator(PosicionPiezaBoard dummyBoard, KingCacheBoard kingCacheBoard,
			ColorBoard colorBoard, MoveCacheBoard moveCache, BoardState boardState, MoveGeneratorStrategy strategy, MoveFilter filter) {
		super(dummyBoard, kingCacheBoard, colorBoard, moveCache, boardState, strategy, filter);
	}

	@Override
	protected Collection<Move> getLegalMovesNotKing(Collection<Move> moves) {
		Square kingSquare = getCurrentKingSquare();

		ReyAbstractMoveGenerator reyMoveGenerator = strategy.getReyMoveGenerator(turnoActual);

		// Casilleros donde se encuentran piezas propias que de moverse pueden
		// poner en jaque al Rey.
		Collection<Square> pinnedSquares = reyMoveGenerator.getPinnedSquare(kingSquare);

		for (SquareIterator iterator = colorBoard.iteratorSquareWhitoutKing(turnoActual, kingSquare); iterator.hasNext();) {

			Square origenSquare = iterator.next();

			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);

			if (pinnedSquares.contains(origenSquare)) {
				for (Move move : pseudoMoves) {
					/*
					 * if(! origen.equals(move.getFrom()) ){ throw new
					 * RuntimeException("Que paso?!?!?"); }
					 */

					// assert origen.equals(move.getFrom());

					if (filter.filterMove(move)) {
						moves.add(move);
					}
				}

			} else {
				moves.addAll(pseudoMoves);
			}

		}

		return moves;
	}

	@Override
	protected boolean existsLegalMovesNotKing() {
		Square kingSquare = getCurrentKingSquare();

		ReyAbstractMoveGenerator reyMoveGenerator = strategy.getReyMoveGenerator(turnoActual);

		// Casilleros donde se encuentran piezas propias que de moverse pueden
		// poner en jaque al Rey.
		Collection<Square> pinnedSquares = reyMoveGenerator.getPinnedSquare(kingSquare);

		for (SquareIterator iterator = colorBoard.iteratorSquareWhitoutKing(turnoActual, kingSquare); iterator.hasNext();) {

			Square origenSquare = iterator.next();

			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);

			if (pinnedSquares.contains(origenSquare)) {
				for (Move move : pseudoMoves) {
					/*
					 * if(! origen.equals(move.getFrom()) ){ throw new
					 * RuntimeException("Que paso?!?!?"); }
					 */

					// assert origen.equals(move.getFrom());

					if (filter.filterMove(move)) {
						return true;
					}
				}

			} else {
				return true;
			}

		}
		return false;
	}

}
