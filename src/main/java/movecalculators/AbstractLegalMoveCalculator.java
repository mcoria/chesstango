package movecalculators;

import java.util.ArrayList;
import java.util.Collection;

import chess.BoardState;
import chess.Color;
import chess.KingMove;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;

public abstract class AbstractLegalMoveCalculator implements LegalMoveCalculator {

	protected PosicionPiezaBoard dummyBoard = null;
	protected KingCacheBoard kingCacheBoard = null;
	protected ColorBoard colorBoard = null;	
	protected MoveCacheBoard moveCache = null;
	protected BoardState boardState = null;
	
	protected MoveGeneratorStrategy strategy = null;
	
	protected Color turnoActual = null;
	protected Color opositeTurnoActual = null;
	
	protected MoveFilter filter = null;
	
	protected abstract Collection<Move> getLegalMovesNotKing(Collection<Move> moves);
	
	//TODO: deberiamos contabilizar aquellas piezas que se exploraron en busca de movimientos validos y no producieron resultados validos.
	//      de esta forma cuendo se busca en getLegalMovesNotKing() no volver a filtrar los mismos movimientos
	protected abstract boolean existsLegalMovesNotKing();
	
	public AbstractLegalMoveCalculator(PosicionPiezaBoard dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
			MoveCacheBoard moveCache, BoardState boardState, MoveGeneratorStrategy strategy, MoveFilter filter) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.moveCache = moveCache;
		this.boardState = boardState;
		this.strategy = strategy;
		this.filter = filter;
	}
	
	@Override
	public Collection<Move> getLegalMoves() {
		turnoActual = boardState.getTurnoActual();
		opositeTurnoActual = turnoActual.opositeColor();
		
		Collection<Move> moves = createContainer();
		
		getLegalMovesNotKing(moves);
		
		getLegalMovesKing(moves);
		
		return moves;
	}
	
	@Override
	public boolean existsLegalMove() {
		turnoActual = boardState.getTurnoActual();
		opositeTurnoActual = turnoActual.opositeColor();		
		return existsLegalMovesNotKing() || existsLegalMovesKing() ;
	}

	protected Collection<Move> getLegalMovesKing(Collection<Move> moves) {		
		Square 	kingSquare = getCurrentKingSquare();
		
		Collection<Move> pseudoMovesKing = getPseudoMoves(kingSquare);			

		for (Move move : pseudoMovesKing) {
			if(move.filer(filter)){
				moves.add(move);
			}
		}
		return moves;
	}
	
	private boolean existsLegalMovesKing() {
		Square 	kingSquare = getCurrentKingSquare();
		Collection<Move> pseudoMovesKing = getPseudoMoves(kingSquare);			

		for (Move move : pseudoMovesKing) {
			KingMove kingMove = (KingMove) move;
			if(filter.filterMove(kingMove)){
				return true;
			}
		}
		return false;
	}

	protected Collection<Move> getPseudoMoves(Square origenSquare) {
		Collection<Move> pseudoMoves = null;
	
		pseudoMoves = moveCache.getPseudoMoves(origenSquare);
	
		if (pseudoMoves == null) {
	
			PosicionPieza origen = dummyBoard.getPosicion(origenSquare);
	
			MoveGenerator moveGenerator = origen.getValue().getMoveGenerator(strategy);
	
			MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
			
			pseudoMoves = generatorResult.getPseudoMoves();
		}
		
		return pseudoMoves;
	}	
	

	public Square getCurrentKingSquare() {
		return kingCacheBoard.getKingSquare(this.turnoActual);
	}
	
	//TODO: Y si en vez de generar un Collection utilizamos una clase con un array
	protected static <T> Collection<T> createContainer() {
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