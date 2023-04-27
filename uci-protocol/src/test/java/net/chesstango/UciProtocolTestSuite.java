/**
 * 
 */
package net.chesstango;

import net.chesstango.uci.protocol.UCIDecoderCmdTest;
import net.chesstango.uci.protocol.UCIDecoderRspTest;
import net.chesstango.uci.protocol.UCIEncoderTest;

import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({UCIDecoderCmdTest.class, UCIDecoderRspTest.class, UCIEncoderTest.class})
public class UciProtocolTestSuite {

}
