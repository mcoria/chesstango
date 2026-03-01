package net.chesstango.search.smart.alphabeta.statistics.game;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.GameListener;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

/**
 * @author Mauricio Coria
 */
public class GameCounters implements SearchByCycleListener {

    @Setter
    private Game game;

    @Getter
    private long executedMoves;


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.executedMoves = 0;

        game.addGameListener(new GameListener() {
            @Override
            public void notifyDoMove(Move move) {
                executedMoves++;
            }

            @Override
            public void notifyUndoMove(Move move) {
            }
        });
    }
}
