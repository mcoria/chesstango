package net.chesstango.search.smart.alphabeta.egtb.liteners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;

/**
 * @author Mauricio Coria
 */
@Setter
public class SetGameToEndGameTableBase implements Acceptor {

    private EndGameTableBase endGameTableBase;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setGame(Game game) {
        endGameTableBase.setGame(game);
    }
}
