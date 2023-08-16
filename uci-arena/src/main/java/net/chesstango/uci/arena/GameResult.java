package net.chesstango.uci.arena;

import lombok.Getter;
import net.chesstango.board.representations.pgn.PGNEncoder;
import net.chesstango.board.representations.pgn.PGNGame;
import net.chesstango.engine.Session;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.proxy.UciProxy;
import net.chesstango.uci.ServiceVisitor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class GameResult {
    private final PGNGame pgnGame;

    @Getter
    private final EngineController engineWhite;
    @Getter
    private final EngineController engineBlack;
    @Getter
    private final EngineController winner;
    @Getter
    private final int points;
    @Getter
    private Session sessionWhite;
    @Getter
    private Session sessionBlack;

    public GameResult(PGNGame pgnGame, EngineController engineWhite, EngineController engineBlack, EngineController winner, int points) {
        this.pgnGame = pgnGame;
        this.engineWhite = engineWhite;
        this.engineBlack = engineBlack;
        this.winner = winner;
        this.points = points;
        this.sessionWhite = null;
        this.sessionBlack = null;

        discoverEngineController(engineWhite, session -> this.sessionWhite = session);
        discoverEngineController(engineBlack, session -> this.sessionBlack = session);
    }

    public void save() {
        PGNEncoder encoder = new PGNEncoder();
        String encodedGame = encoder.encode(pgnGame);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./matches.pgn", true));
            writer.append(encodedGame);
            writer.append("\n\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void discoverEngineController(EngineController controller, Consumer<Session> sessionSetter) {
        controller.accept(new ServiceVisitor() {
            @Override
            public void visit(EngineController engineController) {
                if (controller != engineController) {
                    throw new RuntimeException("visiting unknown controller");
                }
            }

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
