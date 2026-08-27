package net.chesstango.search.smart.statistics.game;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.SearchListener;

/**
 * @author Mauricio Coria
 */
public class GameCountersCollector implements Acceptor, SearchListener {

    @Setter
    private Game game;


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        game.resetExecutedMovesCounter();
    }

    public long getExecutedMoves() {
        return game.getExecutedMovesCounter();
    }
}
