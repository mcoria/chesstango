package net.chesstango;

import net.chesstango.uci.protocol.UCIDecoderCmdTest;
import net.chesstango.uci.protocol.UCIDecoderRspTest;
import net.chesstango.uci.protocol.UCIEncoderTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({UCIDecoderCmdTest.class, UCIDecoderRspTest.class, UCIEncoderTest.class})
public class UciProtocolTestSuite {

}
