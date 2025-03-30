package net.chesstango.board.representations.pgn;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.position.GameStateReader;
import net.chesstango.board.GameStatus;
import net.chesstango.board.representations.move.SANEncoder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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

        GameStateReader currentState = game.getState();
        LinkedList<GameStateReader> gameStateList = new LinkedList<>();
        do {
            gameStateList.add(currentState);
            currentState = currentState.getPreviousState();
        } while (currentState != null);

        String moveStrTmp = "";
        Iterator<GameStateReader> listIt = gameStateList.descendingIterator();
        while (listIt.hasNext()) {
            GameStateReader gameState = listIt.next();
            if (!"".equals(moveStrTmp)) {
                moveStrTmp = moveStrTmp + encodeGameStatusAtMove(gameState.getStatus());
                moveList.add(moveStrTmp);
                moveStrTmp = "";
            }
            if (gameState.getSelectedMove() != null) {
                moveStrTmp = sanEncoder.encodeAlgebraicNotation(gameState.getSelectedMove(), gameState.getLegalMoves());
            }
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
