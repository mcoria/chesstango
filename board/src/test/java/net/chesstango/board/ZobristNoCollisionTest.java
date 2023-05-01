package net.chesstango.board;

import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class ZobristNoCollisionTest {

    @Test
    public void testNoCollisions() {
        Set<Long> hashes = new HashSet<>();

        Game game01 = getGame("2r3k1/p4p2/3Rp2p/1p2P1pK/8/1P4P1/P3Q2P/1q6 b - - 0 1");
        game01.executeMove(Square.b1, Square.f5);
        hashes.add(game01.getChessPosition().getPositionHash());

        game01.executeMove(Square.d6, Square.d8);
        hashes.add(game01.getChessPosition().getPositionHash());

        game01.executeMove(Square.c8, Square.d8);
        hashes.add(game01.getChessPosition().getPositionHash());

        game01.executeMove(Square.e2, Square.g2);
        hashes.add(game01.getChessPosition().getPositionHash());

        game01.executeMove(Square.f5, Square.f3);
        hashes.add(game01.getChessPosition().getPositionHash());


        // Segundo Juego
        Game game02 = getGame("2r3k1/p4p2/3Rp2p/1p2P1pK/8/1P4P1/P3Q2P/1q6 b - - 0 1");
        game02.executeMove(Square.b1, Square.g6);
        hashes.add(game02.getChessPosition().getPositionHash());
        //System.out.println(game02.getChessPosition().getPositionHash());

        game02.executeMove(Square.h5, Square.g4);
        hashes.add(game02.getChessPosition().getPositionHash());
        //System.out.println(game02.getChessPosition().getPositionHash());

        game02.executeMove(Square.g6, Square.f5);
        hashes.add(game02.getChessPosition().getPositionHash());
        //System.out.println(game02.getChessPosition().getPositionHash());

        game02.executeMove(Square.g4, Square.h5);
        hashes.add(game02.getChessPosition().getPositionHash());
        //System.out.println(game02.getChessPosition().getPositionHash());

        game02.executeMove(Square.f5, Square.h3);
        hashes.add(game02.getChessPosition().getPositionHash());
        //System.out.println(game02.getChessPosition().getPositionHash());

        assertEquals(10, hashes.size());
    }


    private Game getGame(String string) {
        GameBuilder builder = new GameBuilder(new ChessFactoryDebug());

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(string);

        return builder.getChessRepresentation();
    }


    private Game getDefaultGame() {
        return getGame(FENDecoder.INITIAL_FEN);
    }

}

