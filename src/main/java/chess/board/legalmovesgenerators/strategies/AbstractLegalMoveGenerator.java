package chess.board.legalmovesgenerators.strategies;

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

	protected Collection<Move> getPseudoMoves(Square origenSquare) {
		MoveGeneratorResult generatorResult = pseudoMovesGenerator.generatePseudoMoves(positionReader.getPosicion(origenSquare));		
		
		return generatorResult.getPseudoMoves();
	}

	protected void getEnPassantLegalMoves(Collection<Move> moves) {
		Collection<Move> pseudoMoves = pseudoMovesGenerator.generateEnPassantPseudoMoves();
		filerMoveCollection(pseudoMoves, moves);		
	}
	
	protected void getCastlingMoves(Collection<Move> moves) {
		Collection<Move> pseudoMoves = pseudoMovesGenerator.generateCastlingPseudoMoves();
		filerMoveCollection(pseudoMoves, moves);	
	}
	
	protected void filerMoveCollection(Collection<Move> collectionToFilter, Collection<Move> collectionToAdd){
		for (Move move : collectionToFilter) {
			if(move.filter(filter)){
				collectionToAdd.add(move);
			}
		}
	}
	
	protected Square getCurrentKingSquare() {
		return positionReader.getKingSquare(positionReader.getTurnoActual());
	}	

}