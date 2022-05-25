package chess.board.movesgenerators.legal.strategies;

import java.util.Collection;

import chess.board.Square;
import chess.board.analyzer.AnalyzerResult;
import chess.board.iterators.SquareIterator;
import chess.board.movesgenerators.legal.MoveFilter;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainer;
import chess.board.position.ChessPositionReader;
import chess.board.movesgenerators.pseudo.MoveGenerator;

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
	public MoveContainer getLegalMoves(AnalyzerResult analysis) {

		MoveContainer moves = new MoveContainer();
		
		getBySquareMoves(moves);
		
		getEnPassantMoves(moves);
		
		return moves;
	}

	protected MoveContainer getBySquareMoves(MoveContainer moves) {
		for (SquareIterator iterator = this.positionReader.iteratorSquare(this.positionReader.getCurrentTurn()); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();

			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);

			filterMoveCollection(pseudoMoves, moves);
		}
		
		return moves;
	}
}
