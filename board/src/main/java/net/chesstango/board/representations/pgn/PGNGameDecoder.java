package net.chesstango.board.representations.pgn;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.GameStatus;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.CareTakerRecord;
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

        Iterator<CareTakerRecord> stateIterator = game.stateIteratorReverse();

        CareTakerRecord stateHistory = null;

        while (stateIterator.hasNext()) {
            CareTakerRecord currentStateHistory = stateIterator.next();

            // Encode previous move + current iterated state
            if (stateHistory != null) {
                String moveStrTmp = encodeMove(stateHistory, currentStateHistory.gameState());
                moveList.add(moveStrTmp);
            }

            stateHistory = currentStateHistory;
        }

        // Encode previous move + current state
        if (stateHistory != null) {
            String moveStrTmp = encodeMove(stateHistory, game.getState());

            moveList.add(moveStrTmp);
        }


        pgn.setMoveList(moveList);

        return pgn;
    }

    private String encodeMove(CareTakerRecord previousStateHistory, GameStateReader currentState) {
        Move playedMove = previousStateHistory.playedMove();
        GameStateReader pastState = previousStateHistory.gameState();

        return sanEncoder.encodeAlgebraicNotation(playedMove, pastState.getLegalMoves())
                + encodeGameStatusAtMove(currentState.getGameStatus());
    }

    private String encodeGameStatusAtMove(GameStatus gameStatus) {
        return switch (gameStatus) {
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
            case MATE -> Color.BLACK.equals(game.getChessPosition().getCurrentTurn()) ? "1-0" : "0-1";
            default -> throw new RuntimeException("Invalid game status");
        };
    }
}
