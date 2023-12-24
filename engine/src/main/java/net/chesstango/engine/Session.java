package net.chesstango.engine;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.SearchMoveResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Session {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    /**
     * Resultado de las busquedas efectuadas durante el juego.
     */
    @Getter
    private final List<SearchMoveResult> searches = new ArrayList<>();

    @Getter
    private Game game;

    public void setPosition(String fen, List<String> moves) {
        game = FENDecoder.loadGame(fen);
        if (moves != null && !moves.isEmpty()) {
            for (String moveStr : moves) {
                Move move = simpleMoveEncoder.selectMove(game.getPossibleMoves(), moveStr);
                if (move == null) {
                    throw new RuntimeException(String.format("No move found %s", moveStr));
                }
                game.executeMove(move);
            }
        }
    }

    public String getInitialFen() {
        return game == null ? null : game.getInitialFen();
    }

    public void addResult(SearchMoveResult result) {
        searches.add(result);
    }
}
