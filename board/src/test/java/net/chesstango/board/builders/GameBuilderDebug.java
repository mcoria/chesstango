package net.chesstango.board.builders;

import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.factory.ChessInjector;

/**
 * @author Mauricio Coria
 */
public class GameBuilderDebug extends GameBuilder {
    public GameBuilderDebug() {
        super(new ChessInjector(new ChessFactoryDebug()));
    }

    public GameBuilderDebug(ChessFactory chessFactory) {
        super(new ChessInjector(chessFactory));
    }

    public GameBuilderDebug(ChessInjector injector) {
        super(injector);
    }
}
