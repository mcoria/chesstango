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
import positioncaptures.Capturer;

public abstract class AbstractLegalMoveCalculator implements LegalMoveCalculator{

	protected PosicionPiezaBoard dummyBoard = null;
	protected KingCacheBoard kingCacheBoard = null;
	protected ColorBoard colorBoard = null;
	protected MoveCacheBoard moveCache = null;
	protected BoardState boardState = null;
	protected MoveGeneratorStrategy strategy = null;
	protected Capturer capturer = null;
	
	protected Color turnoActual = null;
	protected Color opositeTurnoActual = null;
	
	protected abstract Collection<Move> getLegalMovesNotKing();
	
	public AbstractLegalMoveCalculator(PosicionPiezaBoard dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
			MoveCacheBoard moveCache, BoardState boardState, MoveGeneratorStrategy strategy) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.moveCache = moveCache;
		this.boardState = boardState;
		this.strategy = strategy;
	}
	
	@Override
	public Collection<Move> getLegalMoves() {
		turnoActual = boardState.getTurnoActual();
		opositeTurnoActual = turnoActual.opositeColor();
		
		Collection<Move> movesNotKing = getLegalMovesNotKing();
		
		Collection<Move> movesKing = getLegalMovesKing();
		
		movesNotKing.addAll(movesKing);
		
		return movesNotKing;
	}	

	protected Collection<Move> getLegalMovesKing() {
		Collection<Move> movesKing = createContainer();
		
		Square 	kingSquare = getCurrentKingSquare();
		
		Collection<Move> pseudoMovesKing = getPseudoMoves(kingSquare);			

		for (Move move : pseudoMovesKing) {
			KingMove kingMove = (KingMove) move;
			if(filterMove(kingMove)){
				movesKing.add(move);
			}
		}
		return movesKing;
	}

	protected Collection<Move> getPseudoMoves(Square origenSquare) {
		Collection<Move> pseudoMoves = null;
	
	
		pseudoMoves = moveCache.getPseudoMoves(origenSquare);
	
		if (pseudoMoves == null) {
	
			PosicionPieza origen = dummyBoard.getPosicion(origenSquare);
	
			MoveGenerator moveGenerator = origen.getValue().getMoveGenerator(strategy);
	
			MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
	
			moveCache.setPseudoMoves(origen.getKey(), generatorResult);
			
			pseudoMoves = generatorResult.getPseudoMoves();
		}
		
		return pseudoMoves;
	}

	//TODO: Podriamos implemetar las validaciones en una clase derivada
	protected boolean filterMove(Move move) {
		boolean result = false;
		
		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);
		
		if(! capturer.positionCaptured(this.opositeTurnoActual, getCurrentKingSquare())) {
			result = true;
		}

		move.undoMove(this.colorBoard);
		move.undoMove(this.dummyBoard);
		
		return result;
	}
	
	protected boolean filterMove(KingMove move) {
		boolean result = false;
		
		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);
		move.executeMove(this.kingCacheBoard);

		if(! capturer.positionCaptured(this.opositeTurnoActual, getCurrentKingSquare())) {
			result = true;
		}

		move.undoMove(this.kingCacheBoard);
		move.undoMove(this.colorBoard);
		move.undoMove(this.dummyBoard);
		
		return result;
	}	
	
	public Square getCurrentKingSquare() {
		return kingCacheBoard.getKingSquare(this.turnoActual);
	}	
	
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