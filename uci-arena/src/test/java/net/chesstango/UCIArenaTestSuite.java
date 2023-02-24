/**
 * 
 */
package net.chesstango;


import net.chesstango.uci.arena.MatchTest;
import net.chesstango.uci.arena.imp.UCIServiceControllerImpTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Mauricio Coria
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ UCIServiceControllerImpTest.class, MatchTest.class })
public class UCIArenaTestSuite {

}
