package net.chesstango.uci.arena;

import lombok.Getter;
import net.chesstango.board.representations.pgn.PGN;
import net.chesstango.engine.Session;
import net.chesstango.engine.Tango;
import net.chesstango.uci.ServiceVisitor;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.proxy.UciProxy;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class MatchResult {
    @Getter
    private final String mathId;
    @Getter
    private final PGN pgn;
    @Getter
    private final EngineController engineWhite;
    @Getter
    private final EngineController engineBlack;
    @Getter
    private final EngineController winner;

    @Getter
    private Session sessionWhite;
    @Getter
    private Session sessionBlack;

    public MatchResult(String mathId, PGN pgn, EngineController engineWhite, EngineController engineBlack, EngineController winner) {
        this.mathId = mathId;
        this.pgn = pgn;
        this.engineWhite = engineWhite;
        this.engineBlack = engineBlack;
        this.winner = winner;
        this.sessionWhite = null;
        this.sessionBlack = null;

        discoverEngineController(engineWhite, session -> this.sessionWhite = session);
        discoverEngineController(engineBlack, session -> this.sessionBlack = session);
    }

    private static void discoverEngineController(EngineController controller, Consumer<Session> sessionSetter) {
        controller.accept(new ServiceVisitor() {

            @Override
            public void visit(UciTango uciTango) {
                Tango tango = uciTango.getTango();
                sessionSetter.accept(tango.getCurrentSession());
            }

            @Override
            public void visit(UciProxy uciProxy) {
            }
        });
    }
}
