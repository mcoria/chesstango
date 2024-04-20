/**
 * 
 */
package net.chesstango.board.movesgenerators.legal.imp;

import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.movesgenerators.legal.LegalMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class LegalMoveGeneratorImp implements LegalMoveGenerator {

	private LegalMoveGenerator checkLegalMoveGenerator;
	
	private LegalMoveGenerator noCheckLegalMoveGenerator;	

	@Override
	public MoveContainerReader getLegalMoves(AnalyzerResult analysis) {
		if(analysis.isKingInCheck()){
			return checkLegalMoveGenerator.getLegalMoves(analysis);
		}
		return noCheckLegalMoveGenerator.getLegalMoves(analysis);
	}
	
	public void setCheckLegalMoveGenerator(LegalMoveGenerator defaultMoveCalculator) {
		this.checkLegalMoveGenerator = defaultMoveCalculator;
	}

	public void setNoCheckLegalMoveGenerator(LegalMoveGenerator noCheckLegalMoveGenerator) {
		this.noCheckLegalMoveGenerator = noCheckLegalMoveGenerator;
	}	

}
