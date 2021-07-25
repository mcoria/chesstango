package movecalculators;

import java.util.Collection;

import chess.BoardState;
import chess.Color;
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
	public Collection<Move> getLegalMoves() {
		Collection<Move> moves = createContainer();
		
		getLegalMovesNotKing(moves);
		
		getLegalMovesKing(moves);
		
		return moves;
	}
	

	protected Collection<Move> getLegalMovesNotKing(Collection<Move> moves) {
		final Color turnoActual = boardState.getTurnoActual();
		final Square kingSquare = getCurrentKingSquare();

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
	
	protected Collection<Move> getLegalMovesKing(Collection<Move> moves) {		
		Square 	kingSquare = getCurrentKingSquare();
		
		Collection<Move> pseudoMovesKing = getPseudoMoves(kingSquare);			

		for (Move move : pseudoMovesKing) {
			if(move.filter(filter)){
				moves.add(move);
			}
		}
		return moves;
	}	

	
	@Override
	public boolean existsLegalMove() {		
		return existsLegalMovesNotKing() || existsLegalMovesKing() ;
	}	

	protected boolean existsLegalMovesNotKing() {
		final Color turnoActual = boardState.getTurnoActual();
		final Square kingSquare = getCurrentKingSquare();

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
	
	protected boolean existsLegalMovesKing() {
		Square 	kingSquare = getCurrentKingSquare();
		Collection<Move> pseudoMovesKing = getPseudoMoves(kingSquare);			

		for (Move move : pseudoMovesKing) {
			if(move.filter(filter)){
				return true;
			}
		}
		return false;
	}

}
