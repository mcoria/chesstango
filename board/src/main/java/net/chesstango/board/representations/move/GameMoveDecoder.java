package net.chesstango.board.representations.move;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.move.AgregateMoveDecoder;
import net.chesstango.gardel.move.MoveDecoder;


/**
 * @author Mauricio Coria
 *
 */
public class GameMoveDecoder{

    private final MoveDecoder agregateMoveDecoder = new AgregateMoveDecoder();


    public Move decode(String moveStr, FEN fen) {



        return null;
    }

    private net.chesstango.board.moves.Move mapLegalMove(Game game, net.chesstango.gardel.move.Move move) {
        Square from = Square.of(move.from().getFile(), move.from().getRank());
        Square to = Square.of(move.to().getFile(), move.to().getRank());

        Color turn = game.getPosition().getCurrentTurn();
        Piece promotion = switch (move.promotionPiece()) {
            case KNIGHT -> Piece.getKnight(turn);
            case BISHOP -> Piece.getBishop(turn);
            case ROOK -> Piece.getRook(turn);
            case QUEEN -> Piece.getQueen(turn);
            default -> null;
        };
        return promotion == null ? game.getMove(from, to) : game.getMove(from, to, promotion);
    }
}
