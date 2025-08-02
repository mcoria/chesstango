package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.representations.move.SimpleMoveDecoder;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
class Session {
    private final SimpleMoveDecoder simpleMoveDecoder = new SimpleMoveDecoder();

    /**
     * Resultado de las busquedas efectuadas durante el juego.
     */
    @Getter
    private final List<SearchResult> searches = new ArrayList<>();

    private final FEN fen;

    @Setter
    private List<String> moves;

    Session(FEN fen) {
        this.fen = fen;
    }

    Game getGame() {
        return Game.from(fen, moves);
    }

    void addResult(SearchResult result) {
        searches.add(result);
    }

    public FEN getStartPosition() {
        return fen;
    }
}
