
package net.chesstango.uci.engine;

import net.chesstango.uci.engine.engine.UciTangoTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({UciMainTangoTest.class, UciTangoTest.class})
public class UciEngineTestSuite {

}
