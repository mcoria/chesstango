package movecalculators;

import java.util.ArrayList;
import java.util.Collection;

import chess.BoardState;
import chess.PosicionPieza;
import chess.Square;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import moveexecutors.Move;
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
	
	protected MoveFilter filter = null;
	
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

	
	protected MoveGeneratorResult getPseudoMovesResult(Square origenSquare) {
		MoveGeneratorResult generatorResult = moveCache.getPseudoMovesResult(origenSquare);
	
		if (generatorResult == null) {
	
			PosicionPieza origen = dummyBoard.getPosicion(origenSquare);
	
			MoveGenerator moveGenerator = origen.getValue().getMoveGenerator(strategy);
	
			generatorResult = moveGenerator.calculatePseudoMoves(origen);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
		}
		
		return generatorResult;
	}
	
	protected Collection<Move> getPseudoMoves(Square origenSquare) {		
		return getPseudoMovesResult(origenSquare).getPseudoMoves();
	}	
	

	public Square getCurrentKingSquare() {
		return kingCacheBoard.getKingSquare(boardState.getTurnoActual());
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