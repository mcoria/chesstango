package net.chesstango.board.representations.pgn;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Status;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.GameHistoryRecord;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.representations.move.SANEncoder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PGNGameDecoder {
    private SANEncoder sanEncoder = new SANEncoder();

    public PGN decode(Game game) {
        PGN pgn = new PGN();
        pgn.setResult(encodeGameResult(game));
        pgn.setFen(game.getInitialFEN().toString());

        List<String> moveList = new ArrayList<>();

        Iterator<GameHistoryRecord> careTakerRecordIterator = game.getHistory().iteratorReverse();

        GameHistoryRecord gameHistoryRecord = null;

        while (careTakerRecordIterator.hasNext()) {
            GameHistoryRecord currentStateHistory = careTakerRecordIterator.next();

            // Encode previous move + current iterated state
            if (gameHistoryRecord != null) {
                String moveStrTmp = encodeMove(gameHistoryRecord, currentStateHistory.gameState());
                moveList.add(moveStrTmp);
            }

            gameHistoryRecord = currentStateHistory;
        }

        // Encode previous move + current state
        if (gameHistoryRecord != null) {
            String moveStrTmp = encodeMove(gameHistoryRecord, game.getState());

            moveList.add(moveStrTmp);
        }


        pgn.setMoveList(moveList);

        return pgn;
    }

    private String encodeMove(GameHistoryRecord gameHistoryRecord, GameStateReader currentState) {
        Move playedMove = gameHistoryRecord.playedMove();
        GameStateReader pastState = gameHistoryRecord.gameState();

        return sanEncoder.encodeAlgebraicNotation(playedMove, pastState.getLegalMoves())
                + encodeGameStatusAtMove(currentState.getStatus());
    }

    private String encodeGameStatusAtMove(Status status) {
        return switch (status) {
            case NO_CHECK, STALEMATE, DRAW_BY_FIFTY_RULE, DRAW_BY_FOLD_REPETITION -> "";
            case CHECK -> "+";
            case MATE -> "#";
            default -> throw new RuntimeException("Invalid game status");
        };
    }

    private String encodeGameResult(Game game) {
        return switch (game.getStatus()) {
            case NO_CHECK, CHECK -> "*";
            case STALEMATE, DRAW_BY_FIFTY_RULE, DRAW_BY_FOLD_REPETITION -> "1/2-1/2";
            case MATE -> Color.BLACK.equals(game.getPosition().getCurrentTurn()) ? "1-0" : "0-1";
            default -> throw new RuntimeException("Invalid game status");
        };
    }
}
