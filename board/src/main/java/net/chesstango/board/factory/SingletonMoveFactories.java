package net.chesstango.board.factory;

import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.impl.bridge.MoveFactoryBlack;
import net.chesstango.board.moves.impl.bridge.MoveFactoryWhite;

/**
 * @author Mauricio Coria
 *
 */
public class SingletonMoveFactories {
    private static MoveFactory factoryWhite;

    private static MoveFactory factoryBlack;


    public static MoveFactory getDefaultMoveFactoryWhite(){
        if(factoryWhite == null) {
            //factoryWhite =  new net.chesstango.board.moves.impl.inheritance.MoveFactoryWhite();
            factoryWhite =  new MoveFactoryWhite();
        }
        return factoryWhite;
    }

    public static MoveFactory getDefaultMoveFactoryBlack(){
        if(factoryBlack == null) {
            //factoryBlack =  new net.chesstango.board.moves.impl.inheritance.MoveFactoryBlack();
            factoryBlack =  new MoveFactoryBlack();
        }
        return factoryBlack;
    }

}

