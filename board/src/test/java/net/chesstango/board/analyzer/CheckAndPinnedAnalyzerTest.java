package net.chesstango.board.analyzer;

import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 *
 */
public class CheckAndPinnedAnalyzerTest {

    @Test
    public void testCheck(){
        ChessPosition gamePosition = FENDecoder.loadChessPosition("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1");
        CheckAndPinnedAnalyzer checkAndPinnedAnalyzer =  new CheckAndPinnedAnalyzer(gamePosition);

        checkAndPinnedAnalyzer.analyze();

        Assertions.assertTrue(checkAndPinnedAnalyzer.isKingInCheck());
    }
}
