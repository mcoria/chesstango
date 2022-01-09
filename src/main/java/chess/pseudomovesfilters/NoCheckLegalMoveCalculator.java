package chess.pseudomovesfilters;

import java.util.Collection;

import chess.Color;
import chess.Square;
import chess.iterators.SquareIterator;
import chess.moves.Move;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.pseudomovesgenerators.MoveGeneratorResult;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;
import chess.pseudomovesgenerators.KingAbstractMoveGenerator;

//TODO: deberiamos contabilizar aquellas piezas que se exploraron en busca de movimientos validos y no producieron resultados validos.
//      de esta forma cuendo se busca en getLegalMovesNotKing() no volver a filtrar los mismos movimientos

/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveCalculator extends AbstractLegalMoveCalculator {

	public NoCheckLegalMoveCalculator(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard,
			ColorBoard colorBoard, MoveCacheBoard moveCache, PositionState positionState, MoveGeneratorStrategy strategy, MoveFilter filter) {
		super(dummyBoard, kingCacheBoard, colorBoard, moveCache, positionState, strategy, filter);
	}

	@Override
	public Collection<Move> getLegalMoves() {
		Collection<Move> moves = createContainer();
		
		getLegalMovesNotKing(moves);
		
		getLegalMovesKing(moves);
		
		getLegalMovesSpecial(moves);		
		
		return moves;
	}

	protected Collection<Move> getLegalMovesNotKing(Collection<Move> moves) {
		final Color turnoActual = positionState.getTurnoActual();
		final Square kingSquare = getCurrentKingSquare();

		KingAbstractMoveGenerator kingMoveGenerator = strategy.getKingMoveGenerator(turnoActual);

		// Casilleros donde se encuentran piezas propias que de moverse pueden
		// poner en jaque al King.
		long pinnedSquares = kingMoveGenerator.getPinnedSquare(kingSquare);

		for (SquareIterator iterator = colorBoard.iteratorSquareWhitoutKing(turnoActual, kingSquare); iterator.hasNext();) {

			Square origenSquare = iterator.next();
			
			MoveGeneratorResult generatorResult = getPseudoMovesResult(origenSquare);

			Collection<Move> pseudoMoves = generatorResult.getPseudoMoves();

			if ( (pinnedSquares & origenSquare.getPosicion()) != 0 ) {
				for (Move move : pseudoMoves) {
					if(move.filter(filter)){
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
		
		MoveGeneratorResult generatorResult = getPseudoMovesResult(kingSquare);
		
		Collection<Move> pseudoMovesKing = generatorResult.getPseudoMoves();

		for (Move move : pseudoMovesKing) {
			if(move.filter(filter)){
				moves.add(move);
			}
		}
		return moves;
	}

	/*
	@Override
	public boolean existsLegalMove() {	
		count++;
		return existsLegalMovesNotKing() || existsLegalMovesKing() ;
	}	
	
	//TODO: los pinned deberian ser los ultimos en buscar movimientos
	protected boolean existsLegalMovesNotKing() {
		final Color turnoActual = boardState.getTurnoActual();
		final Square kingSquare = getCurrentKingSquare();

		KingAbstractMoveGenerator kingMoveGenerator = strategy.getKingMoveGenerator(turnoActual);

		// Casilleros donde se encuentran piezas propias que de moverse pueden
		// poner en jaque al King.
		Collection<Square> pinnedSquares = kingMoveGenerator.getPinnedSquare(kingSquare);

		for (SquareIterator iterator = colorBoard.iteratorSquareWhitoutKing(turnoActual, kingSquare); iterator.hasNext();) {

			Square origenSquare = iterator.next();

			MoveGeneratorResult generatorResult = getPseudoMovesResult(origenSquare);

			Collection<Move> pseudoMoves = generatorResult.getPseudoMoves();
			
			if (pinnedSquares.contains(origenSquare) || generatorResult.hasCapturePawnPasante()) {
				for (Move move : pseudoMoves) {

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
		
	*/
	

}
