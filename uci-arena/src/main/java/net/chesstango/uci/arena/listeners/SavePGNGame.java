package net.chesstango.uci.arena.listeners;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.pgn.PGNStringEncoder;
import net.chesstango.board.representations.pgn.PGN;
import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Mauricio Coria
 */
public class SavePGNGame implements MatchListener {
    @Override
    public void notifyNewGame(Game game, EngineController white, EngineController black) {
    }

    @Override
    public void notifyMove(Game game, Move move) {
    }

    @Override
    public void notifyEndGame(Game game, MatchResult matchResult) {
        save(matchResult.getPgn());
    }


    protected static synchronized void save(PGN pgn) {
        PGNStringEncoder encoder = new PGNStringEncoder();
        String encodedGame = encoder.encode(pgn);
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
