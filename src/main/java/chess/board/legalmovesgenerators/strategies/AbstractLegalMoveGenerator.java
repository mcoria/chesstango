package chess.board.legalmovesgenerators.strategies;

import java.util.ArrayList;
import java.util.Collection;

import chess.board.Square;
import chess.board.legalmovesgenerators.LegalMoveGenerator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.position.ChessPositionReader;
import chess.board.pseudomovesgenerators.MoveGenerator;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;

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

	protected void getEnPassantLegalMoves(Collection<Move> moves) {
		Collection<Move> pseudoMoves = pseudoMovesGenerator.generateEnPassantPseudoMoves();
		for (Move move : pseudoMoves) {
			if(move.filter(filter)){
				moves.add(move);
			}
		}		
	}	

	protected Square getCurrentKingSquare() {
		return positionReader.getKingSquare(positionReader.getTurnoActual());
	}
	
	protected void getCastlingMoves(Collection<Move> moves) {
		Collection<Move> pseudoMoves = pseudoMovesGenerator.generateCastlingPseudoMoves();
		for (Move move : pseudoMoves) {
			if(move.filter(filter)){
				moves.add(move);
			}
		}			
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