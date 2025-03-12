package net.chesstango.board.factory;

import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.moves.factories.imp.MoveFactoryBlack;
import net.chesstango.board.moves.factories.imp.MoveFactoryWhite;

/**
 * @author Mauricio Coria
 *
 */
public class SingletonMoveFactories {
    private static MoveFactory factoryWhite;

    private static MoveFactory factoryBlack;


    public static MoveFactory getDefaultMoveFactoryWhite(){
        if(factoryWhite == null) {
            factoryWhite =  new MoveFactoryWhite();
            //factoryWhite =  new MoveFactoryCache(new MoveFactoryWhite());
        }
        return factoryWhite;
    }

    public static MoveFactory getDefaultMoveFactoryBlack(){
        if(factoryBlack == null) {
            factoryBlack =  new MoveFactoryBlack();
            //factoryBlack =  new MoveFactoryCache(new MoveFactoryBlack());
        }
        return factoryBlack;
    }

}

