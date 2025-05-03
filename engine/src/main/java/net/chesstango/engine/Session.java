package net.chesstango.engine;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENParser;
import net.chesstango.board.representations.move.SimpleMoveDecoder;
import net.chesstango.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class Session {
    private final SimpleMoveDecoder simpleMoveDecoder = new SimpleMoveDecoder();

    /**
     * Resultado de las busquedas efectuadas durante el juego.
     */
    @Getter
    private final List<SearchResult> searches = new ArrayList<>();

    @Getter
    private Game game;

    public void setPosition(FEN fen, List<String> moves) {
        game = Game.from(fen);
        if (moves != null && !moves.isEmpty()) {
            for (String moveStr : moves) {
                Move move = simpleMoveDecoder.decode(game.getPossibleMoves(), moveStr);
                if (move == null) {
                    throw new RuntimeException(String.format("No move found %s", moveStr));
                }
                move.executeMove();
            }
        }
    }

    public FEN getInitialFen() {
        return game == null ? null : game.getInitialFEN();
    }

    public void addResult(SearchResult result) {
        searches.add(result);
    }
}
