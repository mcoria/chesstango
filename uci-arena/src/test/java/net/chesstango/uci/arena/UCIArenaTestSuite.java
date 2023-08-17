package net.chesstango.uci.arena;



import net.chesstango.uci.arena.gui.EngineControllerImpTangoTest;
import net.chesstango.uci.arena.gui.ProxyConfigTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * @author Mauricio Coria
 *
 */
@Suite
@SelectClasses({ MatchTest.class, EngineControllerImpTangoTest.class, ProxyConfigTest.class })
public class UCIArenaTestSuite {

}
