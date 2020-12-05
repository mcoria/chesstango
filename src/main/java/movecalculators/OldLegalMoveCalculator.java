package movecalculators;

import java.util.ArrayList;
import java.util.Collection;

import chess.Board;
import chess.BoardAnalyzer;
import chess.BoardState;
import chess.Color;
import chess.Move;
import chess.MoveCache;
import chess.PosicionPieza;
import chess.Square;
import iterators.SquareIterator;
import layers.ColorBoard;
import layers.DummyBoard;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;
import movegenerators.ReyAbstractMoveGenerator;

public class OldLegalMoveCalculator implements LegalMoveCalculator {
	
	private Board board = null;
	
	//protected IsPositionCaptured positionCaptured = (Square square) -> false;
	
	// Al final del dia estas son dos representaciones distintas del tablero
	private DummyBoard dummyBoard = null; 
	private ColorBoard boardCache = null;
	
	// Esta es una capa mas de informacion del tablero
	private MoveCache moveCache = null;	
	
	private BoardState boardState = null;	
	
	private MoveGeneratorStrategy strategy = null; 		
	
	private final boolean useMoveCache = false;
	
	public OldLegalMoveCalculator(Board board, DummyBoard dummyBoard, BoardState boardState, ColorBoard boardCache,
			MoveGeneratorStrategy strategy) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.boardCache = boardCache;
		this.strategy = strategy;
		//this.positionCaptured = positionCaptured;
	}	

	@Override
	public Collection<Move> getLegalMoves(BoardAnalyzer analyzer) {
		Color 	turnoActual = boardState.getTurnoActual();
		
		boolean isKingInCheck = analyzer.isKingInCheck();
		
		Square 	kingSquare = null;
		Collection<Square> pinnedSquares = null; // Casilleros donde se encuentran piezas propias que de moverse pueden desproteger al Rey.

		if(! isKingInCheck ){
			kingSquare = board.getKingSquare();
			ReyAbstractMoveGenerator reyMoveGenerator = strategy.getReyMoveGenerator(turnoActual);
			pinnedSquares = reyMoveGenerator.getPinnedSquare(kingSquare);
		}

		Collection<Move> moves = createContainer();
		

		for (SquareIterator iterator = boardCache.iteratorSquare(turnoActual); iterator.hasNext();) {
				
			//boardCache.validarCacheSqueare(dummyBoard);
			
			Square origenSquare = iterator.next();
			
			//assert turnoActual.equals(origen.getValue().getColor());

			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);
			
			// Si el rey esta en jaque
			// O se mueve el rey
			// O se mueve un pieza que protege al Rey
			if( isKingInCheck || pinnedSquares.contains(origenSquare) || origenSquare.equals(kingSquare)){
				for (Move move : pseudoMoves) {
					/*
					if(! origen.equals(move.getFrom()) ){
						throw new RuntimeException("Que paso?!?!?");
					}
					*/
					
					//assert  origen.equals(move.getFrom());
					
					if(this.filterMove(move)){
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
	
	private Collection<Move> getPseudoMoves(Square origenSquare) {
		Collection<Move> pseudoMoves = null;
		
		
		if (useMoveCache) {

			pseudoMoves = moveCache.getPseudoMoves(origenSquare);

			if (pseudoMoves == null) {

				PosicionPieza origen = dummyBoard.getPosicion(origenSquare);

				MoveGenerator moveGenerator = strategy.getMoveGenerator(origen.getValue());

				MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);

				pseudoMoves = generatorResult.getPseudoMoves();

				if (generatorResult.isSaveMovesInCache()) {
					moveCache.setPseudoMoves(origen.getKey(), pseudoMoves);
					moveCache.setAffectedBy(origen.getKey(), generatorResult.getAffectedBy());
				}
			}
		} else {

			PosicionPieza origen = dummyBoard.getPosicion(origenSquare);

			MoveGenerator moveGenerator = strategy.getMoveGenerator(origen.getValue());

			MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);

			pseudoMoves = generatorResult.getPseudoMoves();

		}
		
		return pseudoMoves;
	}

	private boolean filterMove(Move move) {
		boolean result = false;
		
		//boardCache.validarCacheSqueare(dummyBoard);
				
		move.executeMove(this.boardCache);
		

		// Habria que preguntar si aquellos para los cuales su situacion cambió ahora pueden capturar al rey. 
		//if(! this.isKingInCheck() ) {
		//	result = true;
		//}
		
		move.undoMove(this.boardCache);
		
		//boardCache.validarCacheSqueare(dummyBoard);
		
		return result;
	}
	
	/*
	private boolean isKingInCheck() {
		boolean result = false;
		Color turno = boardState.getTurnoActual();
		Square kingSquare = boardCache.getKingSquare(turno);

		PosicionPieza checker = boardCache.getLastChecker();

		// Si no existe checker, recalculamos
		if (checker == null) {
			result = positionCaptured.check(turno.opositeColor(), kingSquare);
		} else {
			// Si existe checker pero es del mismo color que el turno
			// O si la posicion de checker fué capturada por una de nuestras
			// fichas
			if (turno.equals(checker.getValue().getColor()) || boardCache.isColor(turno, checker.getKey())) {
				result = positionCaptured.check(turno.opositeColor(), kingSquare);
			} else {
				// Si checker sigue estando en la misma posicion
				if (checker.equals(dummyBoard.getPosicion(checker.getKey()))) {
					// Checker todavia puede capturar al Rey...
					MoveGenerator moveGenerator = strategy.getMoveGenerator(checker.getValue());
					if (moveGenerator.puedeCapturarPosicion(checker, kingSquare)) {
						result = true;
					} else {
						// Pero no puede capturar...
						result = positionCaptured.check(turno.opositeColor(), kingSquare);
					}
				} else {
					// Checker todavia puede capturar al Rey...
					result = positionCaptured.check(turno.opositeColor(), kingSquare);
				}
			}
		}

		return result;
	}*/
	
	
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
