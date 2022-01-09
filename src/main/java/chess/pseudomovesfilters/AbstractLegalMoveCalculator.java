package chess.pseudomovesfilters;

import java.util.ArrayList;
import java.util.Collection;

import chess.PiecePositioned;
import chess.Square;
import chess.moves.Move;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.MoveGeneratorResult;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;
import chess.pseudomovesgenerators.PawnPasanteMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractLegalMoveCalculator implements LegalMoveCalculator {

	protected PiecePlacement dummyBoard = null;
	protected KingCacheBoard kingCacheBoard = null;
	protected ColorBoard colorBoard = null;	
	protected MoveCacheBoard moveCache = null;
	protected PositionState positionState = null;
	
	protected MoveGeneratorStrategy strategy = null;
	
	protected PawnPasanteMoveGenerator peonPasanteMoveGenerator = null;
	
	protected MoveFilter filter = null;
	
	public AbstractLegalMoveCalculator(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
			MoveCacheBoard moveCache, PositionState positionState, MoveGeneratorStrategy strategy, MoveFilter filter) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.moveCache = moveCache;
		this.positionState = positionState;
		this.strategy = strategy;
		this.filter = filter;
	}

	//TODO: Misteriosamente MoveGenerator moveGenerator = origen.getValue().getMoveGenerator(strategy); tiene mala performance !!!
	protected MoveGeneratorResult getPseudoMovesResult(Square origenSquare) {
		MoveGeneratorResult generatorResult = moveCache.getPseudoMovesResult(origenSquare);
	
		if (generatorResult == null) {
	
			PiecePositioned origen = dummyBoard.getPosicion(origenSquare);
	
			MoveGenerator moveGenerator =  strategy.getMoveGenerator(origen.getValue());
											//origen.getValue().getMoveGenerator(strategy); Mala performance
	
			generatorResult = moveGenerator.calculatePseudoMoves(origen);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
		}
		
		return generatorResult;
	}
	
	/**
	 * @param moves
	 */
	protected void getLegalMovesSpecial(Collection<Move> moves) {
		Collection<Move> pseudoMoves = strategy.getPawnPasanteMoveGenerator().getPseudoMoves();
		for (Move move : pseudoMoves) {
			if(move.filter(filter)){
				moves.add(move);
			}
		}		
	}	

	public Square getCurrentKingSquare() {
		return kingCacheBoard.getKingSquare(positionState.getTurnoActual());
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