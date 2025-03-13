package net.chesstango.board.moves.generators.legal.imp;

import lombok.Setter;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.generators.legal.LegalMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
@Setter
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

}
