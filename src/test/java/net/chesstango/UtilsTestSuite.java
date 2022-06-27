/**
 * 
 */
package net.chesstango;

import net.chesstango.board.representations.PGNEncoderTest;
import net.chesstango.board.representations.SANEncoderTest;
import net.chesstango.board.representations.ascii.ASCIIEncoderTest;
import net.chesstango.board.representations.fen.FENDecoderTest;
import net.chesstango.board.representations.fen.FENEncoderTest;
import net.chesstango.board.representations.fen.FENIntegrationTest;
import net.chesstango.uci.protocol.UCIEncoderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ASCIIEncoderTest.class, FENDecoderTest.class, FENEncoderTest.class, FENIntegrationTest.class,
        UCIEncoderTest.class, SANEncoderTest.class, PGNEncoderTest.class})
public class UtilsTestSuite {

}
