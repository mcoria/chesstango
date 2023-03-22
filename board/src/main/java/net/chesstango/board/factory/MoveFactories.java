package net.chesstango.board.factory;

import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.bridge.MoveFactoryBlack;
import net.chesstango.board.moves.bridge.MoveFactoryWhite;

/**
 * @author Mauricio Coria
 *
 */
public class MoveFactories {
    private static MoveFactory factoryWhite;

    private static MoveFactory factoryBlack;


    public static MoveFactory getDefaultMoveFactoryWhite(){
        if(factoryWhite == null) {
            factoryWhite =  new MoveFactoryWhite();
        }
        return factoryWhite;
    }

    public static MoveFactory getDefaultMoveFactoryBlack(){
        if(factoryBlack == null) {
            factoryBlack =  new MoveFactoryBlack();
        }
        return factoryBlack;
    }

}

