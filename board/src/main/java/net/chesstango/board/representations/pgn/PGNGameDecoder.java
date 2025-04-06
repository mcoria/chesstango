package net.chesstango.board.representations.pgn;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.GameStatus;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.representations.move.SANEncoder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PGNGameDecoder {

    public PGN decode(Game game) {
        SANEncoder sanEncoder = new SANEncoder();
        PGN pgn = new PGN();
        pgn.setResult(encodeGameResult(game));
        pgn.setFen(game.getInitialFEN().toString());

        List<String> moveList = new ArrayList<>();

        Iterator<GameStateReader> stateIterator = game.stateIteratorReverse();

        GameStateReader previousState = null;

        while (stateIterator.hasNext()) {
            GameStateReader gameState = stateIterator.next();

            // Encode previous move + current iterated state
            if (previousState != null) {
                String moveStrTmp = sanEncoder.encodeAlgebraicNotation(previousState.getSelectedMove(), previousState.getLegalMoves())
                        + encodeGameStatusAtMove(gameState.getStatus());
                moveList.add(moveStrTmp);
            }

            previousState = gameState;
        }

        // Encode previous move + current state
        if (previousState != null) {
            String moveStrTmp = sanEncoder.encodeAlgebraicNotation(previousState.getSelectedMove(), previousState.getLegalMoves())
                    + encodeGameStatusAtMove(game.getStatus());
            moveList.add(moveStrTmp);
        }


        pgn.setMoveList(moveList);

        return pgn;
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
