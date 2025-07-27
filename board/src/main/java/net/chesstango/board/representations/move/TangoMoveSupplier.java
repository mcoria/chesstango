package net.chesstango.board.representations.move;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.gardel.move.MoveSupplier;

/**
 * @author Mauricio Coria
 */
public class TangoMoveSupplier implements MoveSupplier<Move> {
    private final Game game;

    public TangoMoveSupplier(Game game) {
        this.game = game;
    }

    @Override
    public Move get(int fromFile, int fromRank, int toFile, int toRank, int fromPiece, int toPiece, int promotion) {
        MoveContainerReader<Move> moves = game.getPossibleMoves();
        for (Move move : moves) {
            if (move.getFrom().getSquare().getFile() == fromFile && move.getFrom().getSquare().getRank() == fromRank &&
                    move.getTo().getSquare().getFile() == toFile && move.getTo().getSquare().getRank() == toRank) {
                if (move instanceof MovePromotion movePromotion && promotion != 0) {
                    int promotionFilter = switch (movePromotion.getPromotion()) {
                        case ROOK_WHITE, ROOK_BLACK -> 1;
                        case KNIGHT_WHITE, KNIGHT_BLACK -> 2;
                        case BISHOP_WHITE, BISHOP_BLACK -> 3;
                        case QUEEN_WHITE, QUEEN_BLACK -> 4;
                        default -> -1;
                    };
                    if (promotionFilter == promotion) {
                        return move;
                    }
                } else {
                    return move;
                }
            }
        }
        return null;
    }
}
