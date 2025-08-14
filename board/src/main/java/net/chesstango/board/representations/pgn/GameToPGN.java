package net.chesstango.board.representations.pgn;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Status;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.GameHistoryRecord;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.gardel.move.SANEncoder;
import net.chesstango.gardel.pgn.PGN;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class GameToPGN {
    private final SANEncoder sanEncoder = new SANEncoder();

    public PGN decode(Game game) {
        PGN pgn = new PGN();
        pgn.setResult(encodeGameResult(game));
        if (!game.getInitialFEN().toString().equals(FENParser.INITIAL_FEN)) {
            pgn.setFen(game.getInitialFEN());
        }

        List<String> moveList = new ArrayList<>();

        Iterator<GameHistoryRecord> careTakerRecordIterator = game.getHistory().iteratorReverse();

        Game theGame = Game.from(game.getInitialFEN());
        while (careTakerRecordIterator.hasNext()) {
            GameHistoryRecord currentStateHistory = careTakerRecordIterator.next();

            Move playedMove = currentStateHistory.playedMove();

            String currentMoveStr = sanEncoder.encodeAlgebraicNotation(toMove(playedMove), theGame.getCurrentFEN());

            if (playedMove instanceof MovePromotion playedMovePromotion) {
                theGame.executeMove(playedMovePromotion.getFrom().square(), playedMovePromotion.getTo().square(), playedMovePromotion.getPromotion());
            } else {
                theGame.executeMove(playedMove.getFrom().square(), playedMove.getTo().square());
            }

            currentMoveStr += encodeGameStatusAtMove(theGame.getStatus());

            moveList.add(currentMoveStr);
        }

        pgn.setMoveList(moveList);

        return pgn;
    }

    private net.chesstango.gardel.move.Move toMove(Move playedMove) {
        net.chesstango.gardel.move.Move.Square from = net.chesstango.gardel.move.Move.Square.of(playedMove.getFrom().square().getFile(), playedMove.getFrom().square().getRank());
        net.chesstango.gardel.move.Move.Square to = net.chesstango.gardel.move.Move.Square.of(playedMove.getTo().square().getFile(), playedMove.getTo().square().getRank());

        if (playedMove instanceof MovePromotion movePromotion) {
            return net.chesstango.gardel.move.Move.of(from, to, switch (movePromotion.getPromotion()) {
                case KNIGHT_WHITE, KNIGHT_BLACK -> net.chesstango.gardel.move.Move.PromotionPiece.KNIGHT;
                case BISHOP_WHITE, BISHOP_BLACK -> net.chesstango.gardel.move.Move.PromotionPiece.BISHOP;
                case ROOK_WHITE, ROOK_BLACK -> net.chesstango.gardel.move.Move.PromotionPiece.ROOK;
                case QUEEN_WHITE, QUEEN_BLACK -> net.chesstango.gardel.move.Move.PromotionPiece.QUEEN;
                default -> throw new RuntimeException("Invalid promotion " + movePromotion);
            });
        } else {
            return net.chesstango.gardel.move.Move.of(from, to);
        }
    }

    private String encodeGameStatusAtMove(Status status) {
        return switch (status) {
            case NO_CHECK, STALEMATE, DRAW_BY_FIFTY_RULE, DRAW_BY_FOLD_REPETITION -> "";
            case CHECK -> "+";
            case MATE -> "#";
            default -> throw new RuntimeException("Invalid game status");
        };
    }

    private PGN.Result encodeGameResult(Game game) {
        return switch (game.getStatus()) {
                case NO_CHECK, CHECK -> PGN.Result.ONGOING;
                case STALEMATE, DRAW_BY_FIFTY_RULE, DRAW_BY_FOLD_REPETITION -> PGN.Result.DRAW;
                case MATE -> Color.BLACK.equals(game.getPosition().getCurrentTurn()) ? PGN.Result.WHITE_WINS : PGN.Result.BLACK_WINS;
        };
    }
}
