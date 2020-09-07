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
import chess.PositionCaptured;
import chess.Square;
import iterators.SquareIterator;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;

public class DefaultLegalMoveCalculator implements LegalMoveCalculator {
	
	protected PositionCaptured positionCaptured = (Color color, Square square) -> false;
	
	// Al final del dia estas son dos representaciones distintas del tablero
	private DummyBoard dummyBoard = null; 
	private BoardCache boardCache = null;
	
	private BoardState boardState = null;	
	
	private MoveGeneratorStrategy strategy = null; 		
	
	public DefaultLegalMoveCalculator(DummyBoard dummyBoard, BoardState boardState, BoardCache boardCache,
			MoveGeneratorStrategy strategy, PositionCaptured positionCaptured) {
		this.dummyBoard = dummyBoard;
		this.boardState = boardState;
		this.boardCache = boardCache;
		this.strategy = strategy;
		this.positionCaptured = positionCaptured;
	}	

	@Override
	public Collection<Move> getLegalMoves(BoardAnalyzer analyzer) {
		Color 	turnoActual = boardState.getTurnoActual();

		Collection<Move> moves = createContainer();
		

		for (SquareIterator iterator = boardCache.iteratorSquare(turnoActual); iterator.hasNext();) {
				
			//boardCache.validarCacheSqueare(dummyBoard);
			
			Square origenSquare = iterator.next();
			
			//assert turnoActual.equals(origen.getValue().getColor());

			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);
			

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

	private boolean filterMove(Move move) {
		boolean result = false;
		
		//boardCache.validarCacheSqueare(dummyBoard);
				
		move.executeMove(this.boardCache);
		

		// Habria que preguntar si aquellos para los cuales su situacion cambió ahora pueden capturar al rey. 
		if(! this.isKingInCheck() ) {
			result = true;
		}
		
		move.undoMove(this.boardCache);
		
		//boardCache.validarCacheSqueare(dummyBoard);
		
		return result;
	}
	
	private boolean isKingInCheck() {
		Color turno = boardState.getTurnoActual();
		
		Square kingSquare = boardCache.getKingSquare(turno);

		return  positionCaptured.check(turno.opositeColor(), kingSquare);
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
