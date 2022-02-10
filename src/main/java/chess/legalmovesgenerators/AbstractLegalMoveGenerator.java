package chess.legalmovesgenerators;

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
import chess.pseudomovesgenerators.MoveGeneratorResult;
import chess.pseudomovesgenerators.imp.MoveGeneratorImp;
import chess.pseudomovesgenerators.imp.MoveGeneratorPawnPasanteImp;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractLegalMoveGenerator implements LegalMoveGenerator {

	protected PiecePlacement dummyBoard = null;
	protected KingCacheBoard kingCacheBoard = null;
	protected ColorBoard colorBoard = null;	
	protected MoveCacheBoard moveCache = null;
	protected PositionState positionState = null;
	
	protected MoveGeneratorImp pseudoMovesGenerator = null;
	
	protected MoveGeneratorPawnPasanteImp peonPasanteMoveGenerator = null;
	
	protected MoveFilter filter = null;
	
	public AbstractLegalMoveGenerator(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
			MoveCacheBoard moveCache, PositionState positionState, MoveGeneratorImp strategy, MoveFilter filter) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.moveCache = moveCache;
		this.positionState = positionState;
		this.pseudoMovesGenerator = strategy;
		this.filter = filter;
	}

	protected MoveGeneratorResult getPseudoMovesResult(Square origenSquare) {
		MoveGeneratorResult generatorResult = moveCache.getPseudoMovesResult(origenSquare);
	
		if (generatorResult == null) {
	
			PiecePositioned origen = dummyBoard.getPosicion(origenSquare);
	
			generatorResult = pseudoMovesGenerator.generatePseudoMoves(origen);
	
			moveCache.setPseudoMoves(origenSquare, generatorResult);
		}
		
		return generatorResult;
	}
	
	/**
	 * @param moves
	 */
	protected void getLegalMovesSpecial(Collection<Move> moves) {
		Collection<Move> pseudoMoves = pseudoMovesGenerator.generatoPawnPasantePseudoMoves();
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