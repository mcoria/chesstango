package net.chesstango.board.representations.pgn;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Transcoding {

    public List<FEN> pgnToFen(List<PGN> pgns) {
        List<FEN> fenPositions = new ArrayList<>();
        pgns.forEach(pgnGame -> {
            try {
                fenPositions.add(pgnToFenSingle(pgnGame));
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
                System.err.println(pgnGame);
            }
        });
        return fenPositions;
    }

    public FEN pgnToFenSingle(PGN pgnGame) {
        FENEncoder fenEncoder = new FENEncoder();
        Game game = pgnGame.toGame();
        game.getChessPosition().constructChessPositionRepresentation(fenEncoder);
        return fenEncoder.getChessRepresentation();
    }
}
