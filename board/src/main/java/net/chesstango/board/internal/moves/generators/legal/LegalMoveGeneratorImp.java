package net.chesstango.board.internal.moves.generators.legal;

import lombok.Setter;
import net.chesstango.board.analyzer.AnalyzerResult;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.generators.legal.LegalMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
@Setter
public class LegalMoveGeneratorImp implements LegalMoveGenerator {

	private final LegalMoveGenerator checkLegalMoveGenerator;
	
	private final LegalMoveGenerator noCheckLegalMoveGenerator;

    public LegalMoveGeneratorImp(LegalMoveGenerator checkLegalMoveGenerator, LegalMoveGenerator noCheckLegalMoveGenerator) {
        this.checkLegalMoveGenerator = checkLegalMoveGenerator;
        this.noCheckLegalMoveGenerator = noCheckLegalMoveGenerator;
    }

    @Override
	public MoveContainerReader<Move> getLegalMoves(AnalyzerResult analysis) {
		if(analysis.isKingInCheck()){
			return checkLegalMoveGenerator.getLegalMoves(analysis);
		}
		return noCheckLegalMoveGenerator.getLegalMoves(analysis);
	}

}
