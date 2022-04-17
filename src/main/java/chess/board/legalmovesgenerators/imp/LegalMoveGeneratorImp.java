/**
 * 
 */
package chess.board.legalmovesgenerators.imp;

import java.util.Collection;

import chess.board.analyzer.AnalyzerResult;
import chess.board.legalmovesgenerators.LegalMoveGenerator;
import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class LegalMoveGeneratorImp implements LegalMoveGenerator{

	private LegalMoveGenerator checkMoveGenerator;
	
	private LegalMoveGenerator noCheckLegalMoveGenerator;	

	@Override
	public Collection<Move> getLegalMoves(AnalyzerResult analysis) {
		if(analysis.isKingInCheck()){
			return checkMoveGenerator.getLegalMoves(analysis);
		}
		return noCheckLegalMoveGenerator.getLegalMoves(analysis);
	}
	
	public void setDefaultMoveCalculator(LegalMoveGenerator defaultMoveCalculator) {
		this.checkMoveGenerator = defaultMoveCalculator;
	}

	public void setNoCheckLegalMoveGenerator(LegalMoveGenerator noCheckLegalMoveGenerator) {
		this.noCheckLegalMoveGenerator = noCheckLegalMoveGenerator;
	}	

}
