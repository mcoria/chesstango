package chess.uci.ui;

import chess.board.representations.fen.FENDecoder;
import chess.uci.engine.Engine;
import chess.uci.engine.imp.EngineProxy;
import chess.uci.engine.imp.EngineZonda;
import chess.uci.ui.imp.EngineControllerImp;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class MatchTest {

    @Test
    public void test() {
        EngineControllerImp engine1 = new EngineControllerImp(new EngineZonda());
        EngineControllerImp engine2 = new EngineControllerImp(new EngineProxy());

        Match match = new Match(engine1, engine2);
        match.startEngines();

        match.compete(FENDecoder.INITIAL_FEN);
        match.switchChairs();
        match.compete(FENDecoder.INITIAL_FEN);

        match.quitEngines();
    }
}
