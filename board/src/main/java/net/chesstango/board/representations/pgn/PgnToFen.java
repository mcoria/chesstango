package net.chesstango.board.representations.pgn;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENEncoder;

/**
 * @author Mauricio Coria
 */
public class PgnToFen {

    public static FEN pgnToFen(PGN pgnGame) {
        FENEncoder fenEncoder = new FENEncoder();
        Game game = pgnGame.toGame();
        game.getChessPosition().constructChessPositionRepresentation(fenEncoder);
        return fenEncoder.getChessRepresentation();
    }
}
