package net.chesstango.board.representations.pgn;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.gardel.move.Move;
import net.chesstango.gardel.move.SANDecoder;
import net.chesstango.gardel.pgn.PGN;

/**
 * @author Mauricio Coria
 */
public class PGNGameEncoder {

    public Game encode(PGN pgn) {
        FEN fen = pgn.getFen() == null ? FEN.of(FENParser.INITIAL_FEN) : pgn.getFen();
        Game game = Game.from(fen);
        SANDecoder sanDecoder = new SANDecoder();
        pgn.getMoveList().forEach(moveStr -> {
            Move move = sanDecoder.decode(moveStr, game.getCurrentFEN());
            if (move != null) {
                net.chesstango.board.moves.Move legalMoveToExecute = mapLegalMove(game, move);
                legalMoveToExecute.executeMove();
            } else {
                throw new RuntimeException(String.format("[%s] %s is not in the list of legal moves for %s", pgn.getEvent(), moveStr, game.getCurrentFEN().toString()));
            }
        });
        return game;
    }

    private net.chesstango.board.moves.Move mapLegalMove(Game game, Move move) {
        Square from = Square.of(move.from().getFile(), move.from().getRank());
        Square to = Square.of(move.to().getFile(), move.to().getRank());

        Color turn = game.getPosition().getCurrentTurn();
        Piece promotion = switch (move.promotionPiece()) {
            case KNIGHT -> Piece.getKnight(turn);
            case BISHOP -> Piece.getBishop(turn);
            case ROOK -> Piece.getRook(turn);
            case QUEEN -> Piece.getQueen(turn);
            case null -> null;
        };
        return promotion == null ? game.getMove(from, to) : game.getMove(from, to, promotion);
    }


}
