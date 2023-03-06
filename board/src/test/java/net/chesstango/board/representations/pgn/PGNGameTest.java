package net.chesstango.board.representations.pgn;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.GameStatus;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class PGNGameTest {

    @Test
    public void testBuildGame01() throws IOException {
        String lines = "[Event \"Computer chess game\"]\n" +
                "[Site \"KANO-LENOVO\"]\n" +
                "[Date \"2023.03.03\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"Spike 1.4\"]\n" +
                "[Black \"Tango\"]\n" +
                "[FEN \"rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5\"]\n" +
                "[Result \"1-0\"]\n" +
                "\n" +
                "1. Be4 f3 2. Bg6 h5 3. Bh7 Nc3 4. Qa5 Bf4 5. Qb4 Bc1\n" +
                "6. e6 a3 7. Qa5 Bf4 8. c5 Bb5+ 9. Kd8 Ne2 10. c4 O-O\n" +
                "11. a6 Ba4 12. g6 Ng3 13. b5 hxg6 14. Bxg6 b4 15. b3 Nxd5\n" +
                "16. bxa4 Nb4 17. bxc2 Nxc2 18. Qc3 Ne4 19. Bxe4 fxe4 20. Nd7 Rf3\n" +
                "21. Qc6 d5 22. exd5 exd5 23. Qg6 Rf2 24. Nb6 d6 25. Kd7 Nb4\n" +
                "26. Qe4 Rc1 27. a5 Na2 28. Ke6 Nc3 29. Qc6 Bg3 30. Qc5 Nb1\n" +
                "31. Qe3 Rc3 32. Qg5 Rc6 33. Nd7 Rf5 34. Qe3+ Bf2 35. Qe4 Rc7\n" +
                "36. Nxe5 Bg3 37. Bxd6 Bxe5 38. Qe3+ Kf1 39. Qh3+ Ke1 40. Qh4+ Rf2\n" +
                "41. Kxe5 Qe2+ 42. Kd5 Nc3+ 43. Kd4 Rc4# 1-0";
        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        PGNGame pgnGame = new PGNDecoder().decodeGame(bufferReader);

        Game game = pgnGame.buildGame();

        Assert.assertEquals(GameStatus.MATE, game.getStatus());
        Assert.assertEquals(Color.BLACK, game.getChessPosition().getCurrentTurn());
    }

    @Test
    public void testBuildGame02() throws IOException {
        String lines = "[Event \"Testspel av Tony Hed\"]\n" +
                "[Site \"?\"]\n" +
                "[Date \"1991.01.01\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"CXG Sphinx Galaxy 6502 4 MHz\"]\n" +
                "[Black \"MChess 1.1-1.71 486/33 MHz\"]\n" +
                "[Result \"1-0\"]\n" +
                "\n" +
                "1. e4 c5 2. Nc3 Nc6 3. g3 g6 4. Bg2 Bg7 5. d3 e6\n" +
                "6. Be3 Nd4 7. Nf3 d5 8. exd5 exd5 9. Bxd4 cxd4 10. Ne2 Qb6\n" +
                "11. O-O Ne7 12. Re1 O-O 13. Nf4 Nf5 14. Rb1 Qd6 15. Ng5 Bh6\n" +
                "16. Ngh3 Be6 17. Qf3 Rac8 18. Re2 Ne7 19. Rbe1 Qd7 20. Nxe6 fxe6\n" +
                "21. Qg4 Nf5 22. Ng5 Bxg5 23. Qxg5 Rc6 24. g4 Ng7 25. Qe5 Ra6\n" +
                "26. a3 Qd6 27. Qxd4 Rf4 28. Qe5 Rxg4 29. Qxd6 Rxd6 30. c3 Nh5\n" +
                "31. f3 Rg5 32. h4 Rg3 33. Kh2 d4 34. c4 Kf7 35. c5 Rc6\n" +
                "36. b4 b6 37. cxb6 axb6 38. a4 Rc3 39. a5 bxa5 40. bxa5 Rxd3\n" +
                "41. Ra2 Rdxf3 42. Bxf3 Rxf3 43. a6 Nf4 44. Rea1 Rh3 45. Kg1 Rg3\n" +
                "46. Kf2 Rg2 47. Kf3 Rxa2 48. Rxa2 Nd5 49. a7 Nc7 50. Rc2 Na8\n" +
                "51. Rc8 Nb6 52. Rb8 Nd7 53. Rb7 Ke7 54. a8Q g5 55. Ke4 gxh4\n" +
                "56. Qc8 Kf6 57. Qxd7 Kg5 58. Qg7 Kh5 59. Rb5 e5 60. Rxe5 1-0\n";
        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        PGNGame pgnGame = new PGNDecoder().decodeGame(bufferReader);

        Game game = pgnGame.buildGame();

        Assert.assertEquals(GameStatus.MATE, game.getStatus());
        Assert.assertEquals(Color.BLACK, game.getChessPosition().getCurrentTurn());
    }

    @Test
    public void testBuildGame03() throws IOException {
        String lines = "[Event \"Testspel av Tony Hed\"]\n" +
                "[Site \"?\"]\n" +
                "[Date \"1996.01.01\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"Hiarcs 3 486/66 MHz\"]\n" +
                "[Black \"Genius 4 P90\"]\n" +
                "[Result \"0-1\"]\n" +
                "\n" +
                "1. e4 e5 2. Nf3 Nf6 3. d4 Nxe4 4. Bd3 d5 5. Nxe5 Nd7\n" +
                "6. Nxd7 Bxd7 7. O-O Qh4 8. c4 O-O-O 9. c5 g5 10. Nc3 Bg7\n" +
                "11. Ne2 Rhe8 12. f3 Nf6 13. Bd2 Ng8 14. Be1 Qh6 15. Qb3 f5\n" +
                "16. a4 Qe6 17. f4 g4 18. a5 a6 19. Bh4 Ne7 20. Rfe1 Qh6\n" +
                "21. Bg5 Qg6 22. Kh1 h6 23. Bh4 Bf6 24. Bxf6 Qxf6 25. Rad1 h5\n" +
                "26. Nc3 h4 27. Nxd5 Nxd5 28. Qxd5 h3 29. Rxe8 Rxe8 30. b4 hxg2\n" +
                "31. Kg1 c6 32. Qc4 Qh6 33. Qc2 Qxf4 34. Qf2 Qf3 35. Qxf3 gxf3\n" +
                "36. Kf2 f4 37. Kxf3 g1 38. Rxg1 Re3 39. Kxf4 Rxd3 40. Ke4 Rh3\n" +
                "41. Rg2 Be6 42. Ke5 Bd5 43. Re2 Kd7 44. Rb2 Rf3 45. Re2 Rb3\n" +
                "46. Re1 Rxb4 47. h4 Rb2 48. Rg1 Kc8 49. Kd6 Rf2 50. Rg4 Rf3\n" +
                "51. Ke5 Ra3 52. Kd6 Rxa5 53. h5 Ra2 54. Rf4 b5 55. cxb6 Rh2\n" +
                "56. Rf5 a5 57. Re5 Kb7 58. Kc5 Rc2 59. Kd6 Kxb6 60. Re8 Kb5\n" +
                "61. Kc7 Rh2 62. Rh8 a4 63. Rb8 Kc4 64. Ra8 Kb3 65. Rb8 Ka2\n" +
                "66. h6 a3 67. h7 Rxh7 0-1\n";

        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        PGNGame pgnGame = new PGNDecoder().decodeGame(bufferReader);

        Game game = pgnGame.buildGame();

        Assert.assertEquals(GameStatus.CHECK, game.getStatus());
    }
}
