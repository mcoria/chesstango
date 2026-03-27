
package net.chesstango.uci.engine;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({UciMainTest.class, UciTangoTest.class, SearchingStateTest.class})
public class UciEngineTestSuite {
}
