package net.chesstango.board.representations.pgn;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;



/**
 * @author Mauricio Coria
 *
 */
public class PGNEncoderTest {

    private PGNEncoder encoder;

    @BeforeEach
    public void settup(){
        encoder = new PGNEncoder();
    }

    @Test
    public void test_encodeGame1(){
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        game.executeMove(Square.e2, Square.e4)
                .executeMove(Square.d7, Square.d5)
                .executeMove(Square.g1, Square.f3)
                .executeMove(Square.d5, Square.e4);

        PGNGame pgnGame = PGNGame.createFromGame(game);
        overrideHeaders(pgnGame);

        String expectedResult = "[Event \"Computer chess game\"]\n" +
                        "[Site \"KANO-COMPUTER\"]\n" +
                        "[Date \"2022.06.17\"]\n" +
                        "[Round \"?\"]\n" +
                        "[White \"mauricio\"]\n" +
                        "[Black \"opponent\"]\n" +
                        "[Result \"*\"]\n" +
                        "\n" +
                        "1. e4 d5 2. Nf3 dxe4 *";


        String encodedGame = encoder.encode(pgnGame);

        assertEquals(expectedResult, encodedGame);
    }



    @Test
    public void test_check_mate(){
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        game.executeMove(Square.e2, Square.e4)
        .executeMove(Square.e7, Square.e5)
        .executeMove(Square.f1, Square.c4)
        .executeMove(Square.b8, Square.c6)
        .executeMove(Square.d1, Square.f3)
        .executeMove(Square.f8, Square.c5)
        .executeMove(Square.f3, Square.f7);

        PGNGame pgnGame = PGNGame.createFromGame(game);
        overrideHeaders(pgnGame);

        String expectedResult = "[Event \"Computer chess game\"]\n" +
                "[Site \"KANO-COMPUTER\"]\n" +
                "[Date \"2022.06.17\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"mauricio\"]\n" +
                "[Black \"opponent\"]\n" +
                "[Result \"1-0\"]\n" +
                "\n" +
                "1. e4 e5 2. Bc4 Nc6 3. Qf3 Bc5 4. Qxf7# 1-0";


        String encodedGame = encoder.encode(pgnGame);

        assertEquals(expectedResult, encodedGame);
    }

    @Test
    public void test_draw(){
        Game game =  FENDecoder.loadGame("k7/7Q/K7/8/8/8/8/8 w - - 0 1");
        game.executeMove(Square.h7, Square.c7);

        PGNGame pgnGame = PGNGame.createFromGame(game);
        overrideHeaders(pgnGame);

        String expectedResult = "[Event \"Computer chess game\"]\n" +
                "[Site \"KANO-COMPUTER\"]\n" +
                "[Date \"2022.06.17\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"mauricio\"]\n" +
                "[Black \"opponent\"]\n" +
                "[FEN \"k7/7Q/K7/8/8/8/8/8 w - - 0 1\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "\n" +
                "1. Qc7 1/2-1/2";


        String encodedGame = encoder.encode(pgnGame);

        assertEquals(expectedResult, encodedGame);
    }

    @Test
    public void test_check_draw(){
        Game game =  FENDecoder.loadGame("k7/8/K7/2Q5/8/8/8/8 w - - 1 1");

        game.executeMove(Square.c5, Square.c6);
        game.executeMove(Square.a8, Square.b8);
        game.executeMove(Square.c6, Square.d6);
        game.executeMove(Square.b8, Square.a8);
        game.executeMove(Square.d6, Square.c7);

        PGNGame pgnGame = PGNGame.createFromGame(game);
        overrideHeaders(pgnGame);

        String expectedResult = "[Event \"Computer chess game\"]\n" +
                "[Site \"KANO-COMPUTER\"]\n" +
                "[Date \"2022.06.17\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"mauricio\"]\n" +
                "[Black \"opponent\"]\n" +
                "[FEN \"k7/8/K7/2Q5/8/8/8/8 w - - 1 1\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "\n" +
                "1. Qc6+ Kb8 2. Qd6+ Ka8 3. Qc7 1/2-1/2";


        String encodedGame = encoder.encode(pgnGame);

        assertEquals(expectedResult, encodedGame);
    }

    public void overrideHeaders(PGNGame pgnGame){
        pgnGame.setEvent("Computer chess game");
        pgnGame.setSite("KANO-COMPUTER");
        pgnGame.setDate("2022.06.17");
        pgnGame.setRound("?");
        pgnGame.setWhite("mauricio");
        pgnGame.setBlack("opponent");
    }

}
