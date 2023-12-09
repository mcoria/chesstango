package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.smart.transposition.TTable;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class SearchByCycleContext {

    private final Game game;

    private TTable maxMap;
    private TTable minMap;
    private TTable qMaxMap;
    private TTable qMinMap;

    public SearchByCycleContext(Game game) {
        this.game = game;
    }
}
