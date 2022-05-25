/**
 * 
 */
package chess.board.movesgenerators.legal.imp;

import chess.board.analyzer.AnalyzerResult;
import chess.board.movesgenerators.legal.LegalMoveGenerator;
import chess.board.moves.containers.MoveContainerReader;

/**
 * @author Mauricio Coria
 *
 */
public class LegalMoveGeneratorImp implements LegalMoveGenerator {

	private LegalMoveGenerator checkMoveGenerator;
	
	private LegalMoveGenerator noCheckLegalMoveGenerator;	

	@Override
	public MoveContainerReader getLegalMoves(AnalyzerResult analysis) {
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
