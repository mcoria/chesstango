package net.chesstango.board.moves.generators.legal.strategies;

import net.chesstango.board.Square;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.moves.generators.legal.LegalMoveFilter;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPositionReader;

import java.util.Collection;

/**
 * @author Mauricio Coria
 *
 */
public class CheckLegalMoveGenerator extends AbstractLegalMoveGenerator {
	
	public CheckLegalMoveGenerator(ChessPositionReader positionReader,
                                   MoveGenerator strategy,
								   LegalMoveFilter filter) {
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
		for (SquareIterator iterator = positionReader.iteratorSquare(positionReader.getCurrentTurn()); iterator.hasNext();) {
			
			Square origenSquare = iterator.next();

			Collection<Move> pseudoMoves = getPseudoMoves(origenSquare);

			filterMoveCollection(pseudoMoves, moves);
		}
		
		return moves;
	}
}
