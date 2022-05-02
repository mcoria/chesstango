package chess.board.legalmovesgenerators.strategies;

import java.util.Collection;

import chess.board.Square;
import chess.board.analyzer.AnalyzerResult;
import chess.board.iterators.square.SquareIterator;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.containsers.ArrayMoveContainer;
import chess.board.position.ChessPositionReader;
import chess.board.pseudomovesgenerators.MoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class CheckLegalMoveGenerator extends AbstractLegalMoveGenerator {
	
	public CheckLegalMoveGenerator(ChessPositionReader positionReader, 
			MoveGenerator strategy, MoveFilter filter) {
		super(positionReader, strategy, filter);
	}	

	@Override
	public Collection<Move> getLegalMoves(AnalyzerResult analysis) {
		
		Collection<Move> moves = new ArrayMoveContainer<Move>();
		
		getBySquareMoves(moves);
		
		getEnPassantMoves(moves);
		
		return moves;
	}

	protected Collection<Move> getBySquareMoves(Collection<Move> moves) {
		for (SquareIterator iterator = this.positionReader.iteratorSquare(this.positionReader.getTurnoActual()); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();

			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);

			filterMoveCollection(pseudoMoves, moves);
		}
		
		return moves;
	}
}
