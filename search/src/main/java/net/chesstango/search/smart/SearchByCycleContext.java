package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 */

@Setter
@Getter
public class SearchByCycleContext {

    private final Game game;

    public SearchByCycleContext(Game game) {
        this.game = game;
    }
}
