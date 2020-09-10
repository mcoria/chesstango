package movecalculators;

import java.util.ArrayList;
import java.util.Collection;

import chess.Board;
import chess.BoardAnalyzer;
import chess.BoardState;
import chess.Color;
import chess.IsPositionCaptured;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;
import iterators.SquareIterator;
import layers.ColorBoard;
import layers.DummyBoard;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;

public class DefaultLegalMoveCalculator implements LegalMoveCalculator {
	
	private Board board = null;
	
	// Al final del dia estas son dos representaciones distintas del tablero
	private DummyBoard dummyBoard = null; 
	private ColorBoard boardCache = null;
	
	private BoardState boardState = null;	
	
	private MoveGeneratorStrategy strategy = null;
	
	protected IsPositionCaptured positionCaptured = (Square square) -> false;
	
	public DefaultLegalMoveCalculator(Board board, DummyBoard dummyBoard, BoardState boardState, ColorBoard boardCache,
			MoveGeneratorStrategy strategy, IsPositionCaptured positionCaptured) {
		this.board = board;
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
			
			Square origenSquare = iterator.next();
			
			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);

			// De almacenar movimientos en un cache, estos moviemientos son pseudo dado que
			// Ejemplo supongamos que almacenamos movimientos de torre blanca a5
			// Reina Negra se mueve desde h7 a e7 y rey e1 queda en jaque 
			// Solo movimiento de torre a5 e7 es VALIDO, el resto deja al rey en Jaque
			for (Move move : pseudoMoves) {
				if(this.filterKingMove(move)){
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

	private boolean filterKingMove(Move move) {
		boolean result = false;
		
		//boardCache.validarCacheSqueare(dummyBoard);
				
		move.executeMove(this.boardCache);
		 
		if(! positionCaptured.check(board.getKingSquare())) {
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
