package movecalculators;

import java.util.Collection;

import chess.BoardAnalyzer;
import chess.BoardState;
import chess.Move;
import chess.Square;
import iterators.SquareIterator;
import layers.ColorBoard;
import layers.DummyBoard;
import layers.MoveCache;
import movegenerators.MoveGeneratorStrategy;
import movegenerators.ReyAbstractMoveGenerator;
import positioncaptures.ImprovedCapturer;

public class NoCheckLegalMoveCalculator extends AbstractLegalMoveCalculator {

	public NoCheckLegalMoveCalculator(DummyBoard dummyBoard, ColorBoard colorBoard, MoveCache moveCache,
			BoardState boardState, MoveGeneratorStrategy strategy, BoardAnalyzer analyzer) {
		super(dummyBoard, colorBoard, moveCache, boardState, strategy, analyzer);
		this.capturer = new ImprovedCapturer(dummyBoard);
	}

	@Override
	public Collection<Move> getLegalMovesNotKing() {
		turnoActual = boardState.getTurnoActual();
		opositeTurnoActual = turnoActual.opositeColor();
		
		Square 	kingSquare = getCurrentKingSquare();
		
		ReyAbstractMoveGenerator reyMoveGenerator = strategy.getReyMoveGenerator(turnoActual);
		
		// Casilleros donde se encuentran piezas propias que de moverse pueden poner en jaque al Rey.
		Collection<Square> pinnedSquares = reyMoveGenerator.getPinnedSquare(kingSquare);

		Collection<Move> moves = createContainer();

		for (SquareIterator iterator = colorBoard.iteratorSquareWhitoutKing(turnoActual); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();
			
			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);


			if (pinnedSquares.contains(origenSquare)) {
				for (Move move : pseudoMoves) {
					/*
					 * if(! origen.equals(move.getFrom()) ){ throw new
					 * RuntimeException("Que paso?!?!?"); }
					 */

					// assert origen.equals(move.getFrom());

					if (this.filterMove(move)) {
						moves.add(move);
					}
				}

			} else {
				moves.addAll(pseudoMoves);
			}
			
			//boardCache.validarCacheSqueare(dummyBoard);
		}
		
		return moves;
	}

}
