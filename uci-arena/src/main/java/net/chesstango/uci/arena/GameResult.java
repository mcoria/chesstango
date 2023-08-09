package net.chesstango.uci.arena;

import net.chesstango.board.representations.pgn.PGNEncoder;
import net.chesstango.board.representations.pgn.PGNGame;
import net.chesstango.engine.Session;
import net.chesstango.engine.Tango;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.gui.EngineController;
import net.chesstango.uci.proxy.EngineProxy;
import net.chesstango.uci.service.ServiceVisitor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class GameResult {
    private final PGNGame pgnGame;
    private final EngineController engineWhite;
    private final EngineController engineBlack;
    private final EngineController winner;
    private final int points;
    private Session sessionWhite;
    private Session sessionBlack;

    public GameResult(PGNGame pgnGame, EngineController engineWhite, EngineController engineBlack, EngineController winner, int points) {
        this.pgnGame = pgnGame;
        this.engineWhite = engineWhite;
        this.engineBlack = engineBlack;
        this.winner = winner;
        this.points = points;

        discoverEngineController(engineWhite, this::setSessionWhite);
        discoverEngineController(engineBlack, this::setSessionBlack);
    }


    public int getPoints() {
        return points;
    }

    public EngineController getEngineWhite() {
        return engineWhite;
    }

    public EngineController getEngineBlack() {
        return engineBlack;
    }

    public EngineController getWinner() {
        return winner;
    }

    private void setSessionWhite(Session sessionWhite) {
        this.sessionWhite = sessionWhite;
    }

    private void setSessionBlack(Session sessionBlack) {
        this.sessionBlack = sessionBlack;
    }

    public Session getSessionWhite() {
        return sessionWhite;
    }

    public Session getSessionBlack() {
        return sessionBlack;
    }

    public PGNGame getPgnGame() {
        return pgnGame;
    }

    private void discoverEngineController(EngineController controller, Consumer<Session> sessionSetter) {
        controller.accept(new ServiceVisitor() {
            @Override
            public void visit(EngineController engineController) {
                if (controller != engineController) {
                    throw new RuntimeException("visiting unknown controller");
                }
            }

            @Override
            public void visit(EngineTango engineTango) {
                Tango tango = engineTango.getTango();
                sessionSetter.accept(tango.getCurrentSession());
            }

            @Override
            public void visit(EngineProxy engineProxy) {
            }
        });
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
}
