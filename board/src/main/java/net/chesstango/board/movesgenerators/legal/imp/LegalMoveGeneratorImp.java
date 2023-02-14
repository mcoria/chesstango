/**
 * 
 */
package net.chesstango.board.movesgenerators.legal.imp;

import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;
import net.chesstango.board.moves.MoveContainerReader;

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
