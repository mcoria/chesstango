package movecalculators;

import java.util.ArrayList;
import java.util.Collection;

import chess.BoardAnalyzer;
import chess.BoardState;
import chess.Color;
import chess.Move;
import chess.MoveCache;
import chess.PosicionPieza;
import chess.Square;
import layers.ColorBoard;
import layers.DummyBoard;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;
import positioncaptures.Capturer;

public abstract class AbstractLegalMoveCalculator implements LegalMoveCalculator{

	protected DummyBoard dummyBoard = null;
	protected ColorBoard colorBoard = null;
	protected MoveCache moveCache = null;
	protected BoardState boardState = null;
	protected MoveGeneratorStrategy strategy = null;
	protected BoardAnalyzer analyzer = null;
	protected Capturer capturer = null;
	
	protected Color turnoActual = null;
	protected Color opositeTurnoActual = null;
	
	public AbstractLegalMoveCalculator(DummyBoard dummyBoard, ColorBoard colorBoard, MoveCache moveCache, BoardState boardState,
			MoveGeneratorStrategy strategy, BoardAnalyzer analyzer) {
		this.dummyBoard = dummyBoard;
		this.colorBoard = colorBoard;
		this.moveCache = moveCache;		
		this.boardState = boardState;
		this.strategy = strategy;
		this.analyzer = analyzer;
	}

	protected Collection<Move> getPseudoMoves(Square origenSquare) {
		Collection<Move> pseudoMoves = null;
	
	
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
		
		return pseudoMoves;
	}

	protected boolean filterMove(Move move) {
		boolean result = false;
		
		//boardCache.validarCacheSqueare(dummyBoard);
				
		move.executeMove(this.dummyBoard);
		move.executeMove(this.colorBoard);
		 
		if(! capturer.positionCaptured(this.opositeTurnoActual, getCurrentKingSquare())) {
			result = true;
		}
		
		move.undoMove(this.colorBoard);
		move.undoMove(this.dummyBoard);
		
		//boardCache.validarCacheSqueare(dummyBoard);
		
		return result;
	}

	protected Square getCurrentKingSquare() {
		return Color.BLANCO.equals(this.turnoActual) ? colorBoard.getSquareKingBlancoCache() : colorBoard.getSquareKingNegroCache();
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