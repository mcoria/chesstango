package movecalculators;

import java.util.ArrayList;
import java.util.Collection;

import chess.BoardAnalyzer;
import chess.BoardCache;
import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.PosicionPieza;
import chess.IsPositionCaptured;
import chess.Square;
import iterators.SquareIterator;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;

public class DefaultLegalMoveCalculator implements LegalMoveCalculator {
	
	protected IsPositionCaptured positionCaptured = (Square square) -> false;
	
	// Al final del dia estas son dos representaciones distintas del tablero
	private DummyBoard dummyBoard = null; 
	private BoardCache boardCache = null;
	
	private BoardState boardState = null;	
	
	private MoveGeneratorStrategy strategy = null; 		
	
	public DefaultLegalMoveCalculator(DummyBoard dummyBoard, BoardState boardState, BoardCache boardCache,
			MoveGeneratorStrategy strategy, IsPositionCaptured positionCaptured) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.boardCache = boardCache;
		this.strategy = strategy;
		this.positionCaptured = positionCaptured;
	}	

	@Override
	public Collection<Move> getLegalMoves(BoardAnalyzer analyzer) {
		Color 	turnoActual = boardState.getTurnoActual();
		
		Square 	kingSquare = boardCache.getKingSquare(turnoActual);

		Collection<Move> moves = createContainer();
		

		for (SquareIterator iterator = boardCache.iteratorSquare(turnoActual); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();
			
			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);

			if(origenSquare.equals(kingSquare)){
				for (Move move : pseudoMoves) {
					if(this.filterKingMove(move, turnoActual)){
						moves.add(move);
					}
				}
			} else {			
				for (Move move : pseudoMoves) {
					if(this.filterMove(move, turnoActual, kingSquare)){
						moves.add(move);
					}
				}
			}
			
			//boardCache.validarCacheSqueare(dummyBoard);
		}
		
		return moves;
	}

	private Collection<Move> getPseudoMoves(Square origenSquare) {		

		PosicionPieza origen = dummyBoard.getPosicion(origenSquare);

		MoveGenerator moveGenerator = strategy.getMoveGenerator(origen.getValue());

		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		return generatorResult.getPseudoMoves();
	}
	
	//TODO: Esto no tiene sentido, el generador de movimientos de REY debiera generarlos validos
	private boolean filterKingMove(Move move, Color turnoActual) {
		boolean result = false;
		
		//boardCache.validarCacheSqueare(dummyBoard);
				
		move.executeMove(this.boardCache);
		 
		if(! positionCaptured.check(move.getTo().getKey())) {
			result = true;
		}
		
		move.undoMove(this.boardCache);
		
		//boardCache.validarCacheSqueare(dummyBoard);
		
		return result;
	}

	private boolean filterMove(Move move, Color turnoActual, Square kingSquare) {
		boolean result = false;
		
		//boardCache.validarCacheSqueare(dummyBoard);
				
		move.executeMove(this.boardCache);
		

		// Habria que preguntar si aquellos para los cuales su situacion cambió ahora pueden capturar al rey. 
		if(! positionCaptured.check(kingSquare) ) {
			result = true;
		}
		
		move.undoMove(this.boardCache);
		
		//boardCache.validarCacheSqueare(dummyBoard);
		
		return result;
	}
	
	
	private static <T> Collection<T> createContainer(){
		return new ArrayList<T>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (T move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
	}	
}
