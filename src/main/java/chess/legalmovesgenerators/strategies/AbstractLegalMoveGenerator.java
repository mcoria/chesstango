package chess.legalmovesgenerators.strategies;

import java.util.ArrayList;
import java.util.Collection;

import chess.ChessPositionReader;
import chess.Square;
import chess.legalmovesgenerators.LegalMoveGenerator;
import chess.legalmovesgenerators.MoveFilter;
import chess.moves.Move;
import chess.pseudomovesgenerators.MoveGenerator;
import chess.pseudomovesgenerators.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractLegalMoveGenerator implements LegalMoveGenerator {

	protected final ChessPositionReader positionReader;
	
	protected final MoveGenerator pseudoMovesGenerator;
	
	protected final MoveFilter filter;
	
	public AbstractLegalMoveGenerator(ChessPositionReader positionReader, MoveGenerator strategy, MoveFilter filter) {
		this.positionReader = positionReader;
		this.pseudoMovesGenerator = strategy;
		this.filter = filter;
	}

	protected MoveGeneratorResult getPseudoMoves(Square origenSquare) {
		return pseudoMovesGenerator.generatePseudoMoves(positionReader.getPosicion(origenSquare));
	}

	protected void getLegalMovesSpecial(Collection<Move> moves) {
		Collection<Move> pseudoMoves = pseudoMovesGenerator.generatoPawnPasantePseudoMoves();
		for (Move move : pseudoMoves) {
			if(move.filter(filter)){
				moves.add(move);
			}
		}		
	}	

	protected Square getCurrentKingSquare() {
		return positionReader.getKingSquare(positionReader.getTurnoActual());
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