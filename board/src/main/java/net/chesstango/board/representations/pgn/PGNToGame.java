package net.chesstango.board.representations.pgn;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.TangoMoveSupplier;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.gardel.move.SANDecoder;
import net.chesstango.gardel.pgn.PGN;

/**
 * @author Mauricio Coria
 */
public class PGNToGame {

    public Game encode(PGN pgn) {
        FEN fen = pgn.getFen() == null ? FEN.of(FENParser.INITIAL_FEN) : pgn.getFen();
        Game game = Game.from(fen);
        SANDecoder<Move> sanDecoder = new SANDecoder<>(new TangoMoveSupplier(game));
        pgn.getMoveList().forEach(moveStr -> {
            Move move = sanDecoder.decode(moveStr, game.getCurrentFEN());
            if (move != null) {
                move.executeMove();
            } else {
                throw new RuntimeException(String.format("[%s] %s is not in the list of legal moves for %s", pgn.getEvent(), moveStr, game.getCurrentFEN().toString()));
            }
        });
        return game;
    }

}
