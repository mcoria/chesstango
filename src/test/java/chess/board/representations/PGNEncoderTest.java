package chess.board.representations;

import chess.board.Game;
import chess.board.Square;
import chess.board.representations.fen.FENDecoder;
import chess.board.representations.fen.FENEncoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class PGNEncoderTest {

    private PGNEncoder encoder;

    private PGNEncoder.PGNHeader header;

    @Before
    public void settup(){
        encoder = new PGNEncoder();
        header = new PGNEncoder.PGNHeader();

        header.setEvent("Computer chess game");
        header.setSite("KANO-COMPUTER");
        header.setDate("2022.06.17");
        header.setRound("?");
        header.setWhite("mauricio");
        header.setBlack("opponent");
    }

    @Test
    public void test_encodeGame1(){
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        game.executeMove(Square.e2, Square.e4)
                .executeMove(Square.d7, Square.d5)
                .executeMove(Square.g1, Square.f3)
                .executeMove(Square.d5, Square.e4);

        String expectedResult = "[Event \"Computer chess game\"]\n" +
                        "[Site \"KANO-COMPUTER\"]\n" +
                        "[Date \"2022.06.17\"]\n" +
                        "[Round \"?\"]\n" +
                        "[White \"mauricio\"]\n" +
                        "[Black \"opponent\"]\n" +
                        "[Result \"*\"]\n" +
                        "\n" +
                        "1. e4 d5 2. Nf3 dxe4 *";


        String encodedGame = encoder.encode(header, game);

        Assert.assertEquals(expectedResult, encodedGame);
    }



    @Test
    public void test_checkmate(){
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        game.executeMove(Square.e2, Square.e4)
        .executeMove(Square.e7, Square.e5)
        .executeMove(Square.f1, Square.c4)
        .executeMove(Square.b8, Square.c6)
        .executeMove(Square.d1, Square.f3)
        .executeMove(Square.f8, Square.c5)
        .executeMove(Square.f3, Square.f7);

        String expectedResult = "[Event \"Computer chess game\"]\n" +
                "[Site \"KANO-COMPUTER\"]\n" +
                "[Date \"2022.06.17\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"mauricio\"]\n" +
                "[Black \"opponent\"]\n" +
                "[Result \"1-0\"]\n" +
                "\n" +
                "1. e4 e5 2. Bc4 Nc6 3. Qf3 Bc5 4. Qxf7# 1-0";


        String encodedGame = encoder.encode(header, game);

        Assert.assertEquals(expectedResult, encodedGame);
    }

}
