package net.chesstango.board.representations.pgn;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SANDecoder;

/**
 * @author Mauricio Coria
 */
public class PGNGameEncoder {

    public Game encode(PGN pgn) {
        Game game = FENDecoder.loadGame(pgn.getFen() == null ? FENDecoder.INITIAL_FEN : pgn.getFen());
        SANDecoder sanDecoder = new SANDecoder();
        pgn.getMoveList().forEach(moveStr -> {
            MoveContainerReader legalMoves = game.getPossibleMoves();
            Move legalMoveToExecute = sanDecoder.decode(moveStr, legalMoves);
            if (legalMoveToExecute != null) {
                legalMoveToExecute.executeMove();
            } else {
                throw new RuntimeException(String.format("[%s] %s is not in the list of legal moves for %s", pgn.getEvent(), moveStr, game.getCurrentFEN().toString()));
            }
        });
        return game;
    }
}
