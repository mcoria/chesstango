package chess.legalmovesgenerators;

import java.util.ArrayList;
import java.util.Collection;

import chess.Square;
import chess.moves.Move;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractLegalMoveGenerator implements LegalMoveGenerator {

	protected PiecePlacement dummyBoard = null;
	protected KingCacheBoard kingCacheBoard = null;
	protected ColorBoard colorBoard = null;
	protected PositionState positionState = null;
	
	protected MoveGenerator pseudoMovesGenerator = null;
	
	protected MoveFilter filter = null;
	
	public AbstractLegalMoveGenerator(PiecePlacement dummyBoard, KingCacheBoard kingCacheBoard, ColorBoard colorBoard,
			PositionState positionState, MoveGenerator strategy, MoveFilter filter) {
		this.dummyBoard = dummyBoard;
		this.kingCacheBoard = kingCacheBoard;
		this.colorBoard = colorBoard;
		this.positionState = positionState;
		this.pseudoMovesGenerator = strategy;
		this.filter = filter;
	}

	protected MoveGeneratorResult getPseudoMovesResult(Square origenSquare) {
		return pseudoMovesGenerator.generatePseudoMoves(dummyBoard.getPosicion(origenSquare));
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