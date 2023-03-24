package net.chesstango.board.factory;

import net.chesstango.board.moves.MoveFactory;

/**
 * @author Mauricio Coria
 *
 */
public class SingletonMoveFactories {
    private static MoveFactory factoryWhite;

    private static MoveFactory factoryBlack;


    public static MoveFactory getDefaultMoveFactoryWhite(){
        if(factoryWhite == null) {
            //factoryWhite =  new net.chesstango.board.moves.imp.MoveFactoryWhite();
            factoryWhite =  new net.chesstango.board.moves.bridge.MoveFactoryWhite();
        }
        return factoryWhite;
    }

    public static MoveFactory getDefaultMoveFactoryBlack(){
        if(factoryBlack == null) {
            //factoryBlack =  new net.chesstango.board.moves.imp.MoveFactoryBlack();
            factoryBlack =  new net.chesstango.board.moves.bridge.MoveFactoryBlack();
        }
        return factoryBlack;
    }

}

