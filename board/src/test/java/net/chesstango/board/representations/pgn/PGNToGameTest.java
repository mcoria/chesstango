package net.chesstango.board.representations.pgn;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.Status;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.pgn.PGN;
import net.chesstango.gardel.pgn.PGNStringDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class PGNToGameTest {

    private PGNToGame pgnToGame;

    @BeforeEach
    public void setUp() {
        pgnToGame = new PGNToGame();
    }

    @Test
    public void testToGame01() throws IOException {
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

        PGN pgn = new PGNStringDecoder().decodePGN(lines);

        Game game = pgnToGame.encode(pgn);

        assertEquals(Status.MATE, game.getStatus());
        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
        assertEquals("rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5", game.getInitialFEN().toString());
    }

    @Test
    public void testToGame02() throws IOException {
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

        PGN pgn = new PGNStringDecoder().decodePGN(lines);

        Game game = pgnToGame.encode(pgn);

        assertEquals(Status.MATE, game.getStatus());
        assertEquals(Color.BLACK, game.getPosition().getCurrentTurn());
    }

    @Test
    public void testToGame03() throws IOException {
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

        PGN pgn = new PGNStringDecoder().decodePGN(lines);

        Game game = pgnToGame.encode(pgn);

        assertEquals(Status.CHECK, game.getStatus());
    }

    @Test
    public void testToGame04() throws IOException {
        String lines = "[Event \"Tango v0.0.28-SNAPSHOT vs Spike 1.4 - Match\"]\n" +
                "[Site \"LAPTOP-PTVVKHNB\"]\n" +
                "[Date \"2024.06.12\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"Tango v0.0.28-SNAPSHOT\"]\n" +
                "[Black \"Spike 1.4\"]\n" +
                "[FEN \"r1bq1rk1/pppp1ppp/2n2n2/4p3/1bP5/2N2NP1/PP1PPPBP/R1BQK2R w KQ - 3 6\"]\n" +
                "[Result \"0-1\"]\n" +
                "\n" +
                "1. d4 d6 2. O-O e4 3. Ng5 Bxc3 4. bxc3 Bf5 5. Bf4 h6\n" +
                "6. Nh3 Be6 7. Qb3 b6 8. e3 a6 9. Rfe1 Re8 10. Rad1 Bg4\n" +
                "11. Rd2 Be6 12. d5 Na5 13. Qb4 c5 14. Qa4 Bd7 15. Qc2 Nxc4\n" +
                "16. Rdd1 Bg4 17. Rc1 b5 18. Ra1 Re7 19. Rac1 Qa5 20. Qb3 Nxd5\n" +
                "21. Bxd6 Nxd6 22. Qxd5 Rd8 23. Nf4 g5 24. h3 gxf4 25. hxg4 f3\n" +
                "26. Bh3 Red7 27. Qxc5 Qxa2 28. Rcd1 Nc4 29. Rxd7 Rxd7 30. g5 Rd2\n" +
                "31. Bf5 Rxf2 32. Bxe4 Rf1+ 33. Rxf1 Qg2# 0-1\n";

        PGN pgn = new PGNStringDecoder().decodePGN(lines);

        Game game = pgnToGame.encode(pgn);

        assertEquals(Status.MATE, game.getStatus());
    }

    @Test
    public void testToGame05() throws IOException {
        String lines = "[Event \"d72e3dd3-3eaf-43e6-8fce-83fe5dc79d2b\"]\n" +
                "[Site \"LAPTOP-PTVVKHNB\"]\n" +
                "[Date \"2024.06.12\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"Tango v0.0.28-SNAPSHOT\"]\n" +
                "[Black \"Spike 1.4\"]\n" +
                "[FEN \"r1bqkb1r/pp1p1ppp/2n1pn2/8/2PN4/2N5/PP2PPPP/R1BQKB1R w KQkq - 0 6\"]\n" +
                "[Result \"0-1\"]\n" +
                "\n" +
                "1. e4 e5 2. Nf5 b6 3. Bd3 g6 4. Ne3 Nb4 5. O-O Bb7\n" +
                "6. Ncd5 Nfxd5 7. cxd5 Rc8 8. Bc4 Qh4 9. Qf3 Bc5 10. Bb3 Nd3\n" +
                "11. Rd1 Ba6 12. Rb1 O-O 13. Bd2 Nf4 14. Bc3 Be2 15. Qg3 Qxg3\n" +
                "16. hxg3 Bxd1 17. Rxd1 Ne2+ 18. Kh2 Nxc3 19. bxc3 Bxe3 20. fxe3 Rxc3\n" +
                "21. Re1 Rfc8 22. Kg1 b5 23. d6 Rc1 24. Rxc1 Rxc1+ 25. Kh2 Kf8\n" +
                "26. g4 Rc4 27. Bxc4 bxc4 28. Kg1 c3 29. g5 Ke8 30. a3 Kd8\n" +
                "31. a4 c2 32. a5 c1=Q+ 33. Kf2 Qd2+ 34. Kg1 Qxe3+ 35. Kh2 Qxe4\n" +
                "36. Kg1 Qe1+ 37. Kh2 Qxa5 38. Kg1 Qe1+ 39. Kh2 a5 40. g4 a4\n" +
                "41. Kg2 a3 42. Kh2 a2 43. Kg2 Qe2+ 44. Kh3 a1=Q 45. Kh4 Qh2# 0-1\n";

        PGN pgn = new PGNStringDecoder().decodePGN(lines);

        Game game = pgnToGame.encode(pgn);

        assertEquals(Status.MATE, game.getStatus());
    }

    @Test
    public void testToGame06() throws IOException {
        String lines = "[Event \"03e76304-8992-4db3-b638-d52d3f08c5f8\"]\n" +
                "[Site \"LAPTOP-PTVVKHNB\"]\n" +
                "[Date \"2024.06.12\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"Tango v0.0.28-SNAPSHOT\"]\n" +
                "[Black \"Spike 1.4\"]\n" +
                "[FEN \"r1bq1rk1/pppp1ppp/2n2n2/4p3/1bP5/2N2NP1/PP1PPPBP/R1BQK2R w KQ - 3 6\"]\n" +
                "[Result \"0-1\"]\n" +
                "\n" +
                "1. e4 d6 2. O-O Nd4 3. Nd5 Nxf3+ 4. Qxf3 Nxd5 5. cxd5 Bd7\n" +
                "6. d3 f6 7. Be3 Bb5 8. d4 Bxf1 9. Rxf1 exd4 10. Bxd4 Bc5\n" +
                "11. Bxc5 dxc5 12. Re1 Re8 13. g4 Qd6 14. Qf5 g6 15. Qf3 Rad8\n" +
                "16. Qe3 f5 17. e5 Qb6 18. gxf5 Qxb2 19. f6 c4 20. f7+ Kxf7\n" +
                "21. e6+ Kg7 22. Be4 Qxa2 23. Qc5 Rc8 24. Qd4+ Kh6 25. Qf6 Qd2\n" +
                "26. Re3 Qd1+ 27. Kg2 Qg4+ 28. Kh1 b5 29. Re1 Rf8 30. Qd4 a5\n" +
                "31. e7 Rf4 32. Qe3 Re8 33. d6 cxd6 34. Rg1 Qh4 35. Rxg6+ hxg6\n" +
                "36. Kg1 Rxe7 37. f3 Qg5+ 38. Kh1 Rfxe4 39. Qxg5+ Kxg5 40. fxe4 c3\n" +
                "41. h4+ Kxh4 42. Kg1 Rxe4 43. Kh2 c2 44. Kg1 c1=Q+ 45. Kh2 Re2# 0-1\n";

        PGN pgn = new PGNStringDecoder().decodePGN(lines);

        Game game = pgnToGame.encode(pgn);

        assertEquals(Status.MATE, game.getStatus());
    }

    @Test
    public void testToGame07() throws IOException {
        String lines = "[Event \"Freestyle Chess Grand Slam Tour 2025 - Paris | Round Robin\"]\n" +
                "[Site \"?\"]\n" +
                "[Date \"2025.04.08\"]\n" +
                "[Round \"7\"]\n" +
                "[White \"Nakamura, Hikaru\"]\n" +
                "[Black \"Caruana, Fabiano\"]\n" +
                "[FEN \"rbbqknnr/pppppppp/8/8/8/8/PPPPPPPP/RBBQKNNR w KQkq - 0 1\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "\n" +
                "1. c4 c6 2. Nf3 d5 3. cxd5 cxd5 4. d4 Nf6 5. Ng3 Ng6\n" +
                "6. O-O O-O 7. Bd3 Bg4 8. Bg5 Qb6 9. Qb3 Nf4 10. Qxb6 axb6\n" +
                "11. Bxf4 Bxf4 12. e3 Bd6 13. Nf5 Bxf5 14. Bxf5 b5 15. a3 b4\n" +
                "16. axb4 Bxb4 17. Rfc1 e6 18. Bd3 Bd6 19. g3 g6 20. Kg2 Kg7\n" +
                "21. Kg1 Kg8 22. Kg2 Kg7 23. Kg1 Kg8 24. Kg2 Kg7 25. Kg1 Kg8 1/2-1/2";

        PGN pgn = new PGNStringDecoder().decodePGN(lines);

        Game game = pgnToGame.encode(pgn);

        assertEquals(Status.DRAW_BY_FOLD_REPETITION, game.getStatus());
    }

    @Test
    public void testToGame08() throws IOException {
        String lines = "[Event \"British Rapidplay Championship 2025 | Boards 1-80\"]\n" +
                "[Site \"?\"]\n" +
                "[Date \"2025.03.01\"]\n" +
                "[Round \"6\"]\n" +
                "[White \"Boswell, Jacob Connor\"]\n" +
                "[Black \"Bowers, Francis J\"]\n" +
                "[Result \"1-0\"]\n" +
                "\n" +
                "1. a3 Nf6 2. Nf3 g6 3. g3 Bg7 4. Bg2 O-O 5. O-O d6\n" +
                "6. d4 Nbd7 7. c4 c5 8. d5 a6 9. a4 Qb6 10. Qc2 Ne8\n" +
                "11. Ra2 Ndf6 12. Nc3 e5 13. e4 h6 14. Nh4 Nh7 15. Kh1 Qd8\n" +
                "16. Bd2 g5 17. Nf5 Bxf5 18. exf5 Nhf6 19. Ra3 b6 20. a5 bxa5\n" +
                "21. Rfa1 Nc7 22. Nd1 Qe7 23. Be4 Nfe8 24. Bxa5 g4 25. f3 gxf3\n" +
                "26. Rxf3 Nf6 27. Nf2 Rfb8 28. Rg1 Nce8 29. g4 Nxe4 30. Nxe4 Nf6\n" +
                "31. Bc3 Nxe4 32. Qxe4 Bf6 33. g5 hxg5 34. h4 Kg7 35. Rg2 Rh8\n" +
                "36. Kg1 Rag8 37. hxg5 Kf8 38. gxf6 Qxf6 39. Rfg3 Qh6 40. Rxg8+ Rxg8\n" +
                "41. Rxg8+ Kxg8 42. Kf1 Qc1+ 43. Ke2 a5 44. f6 Qg5 45. Qf3 a4\n" +
                "46. Kf2 Kf8 47. Ke2 Ke8 48. Kd1 Qg1+ 49. Kc2 Qg6+ 50. Kc1 Qg1+\n" +
                "51. Qd1 Qg5+ 52. Kb1 Qxf6 53. Qxa4+ Kf8 54. Qd1 Qf5+ 55. Ka2 Qc8\n" +
                "56. b3 Qa8+ 57. Kb2 Qc8 58. Qh5 Qd7 59. Bd2 f6 60. Qh8+ Kf7\n" +
                "61. Qh7+ Ke8 62. Qg8+ Ke7 63. Qg7+ Ke8 64. Qxf6 Qe7 65. Qxe7+ Kxe7\n" +
                "66. Bg5+ Kf7 67. Kc2 Ke8 68. Kd3 Kf7 69. Ke4 Ke8 70. Bf6 Kf7\n" +
                "71. Bxe5 Ke7 72. Bxd6+ Kxd6 73. Kf5 Kd7 74. Ke5 Ke7 75. d6+ Kd7\n" +
                "76. Kd5 Kd8 77. Kxc5 Kd7 78. b4 Kd8 79. b5 Kd7 80. b6 Kd8\n" +
                "81. b7 Kd7 82. Kd5 Ke8 83. Ke6 Kf8 84. Kf6 Kg8 85. b8=R+ Kh7\n" +
                "86. Ra8 Kh6 87. Rh8# 1-0";

        PGN pgn = new PGNStringDecoder().decodePGN(lines);

        Game game = pgnToGame.encode(pgn);

        assertEquals(Status.MATE, game.getStatus());
    }

    @Test
    public void testOf01() throws IOException {
        Game game = Game.from(FEN.of("rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5"));
        game.executeMove(Square.a7, Square.a6);

        PGN pgn = game.encode();

        assertEquals("rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5", pgn.getFen().toString());
    }

    @Test
    public void testTranscoding01() throws IOException {
        Stream<PGN> pgnStream = new PGNStringDecoder().decodePGNs(this.getClass().getClassLoader().getResourceAsStream("main/pgn/Balsa_Top10.pgn"));

        List<FEN> fenPositions = pgnStream.map(Game::from).map(Game::getCurrentFEN).toList();

        List<FEN> expectedFenPositions = Stream.of(
                        "r2qkbnr/pp1n1ppp/2p1p3/3pPb2/3P4/2P2N2/PP2BPPP/RNBQK2R b KQkq - 3 6",
                        "r1bqkb1r/1p3ppp/p1nppn2/6B1/3NP3/2N5/PPPQ1PPP/2KR1B1R b kq - 1 8",
                        "rnbqkb1r/5ppp/p2ppn2/1p6/3NP3/2N1BP2/PPP3PP/R2QKB1R w KQkq b6 0 8",
                        "rn1qkb1r/1p3ppp/p2pbn2/4p3/4P3/1NN1BP2/PPP3PP/R2QKB1R b KQkq - 0 8",
                        "r1bqkb1r/pp1n1ppp/2n1p3/2ppP3/3P1P2/2N1BN2/PPP3PP/R2QKB1R b KQkq - 3 7",
                        "r1bq1rk1/ppp2ppp/2np1n2/2b1p3/2B1P3/2PP1N2/PP3PPP/RNBQ1RK1 w - - 2 7",
                        "r1bq1rk1/2ppbppp/p1n2n2/1p2p3/4P3/1B3N2/PPPP1PPP/RNBQR1K1 w - - 2 8",
                        "r1b1kb1r/pp1n1pp1/2p1pq1p/3p4/2PP4/2N1PN2/PP3PPP/R2QKB1R w KQkq - 1 8",
                        "r1bqk2r/pp1n1ppp/2pbpn2/3p4/2PP4/2N1PN2/PPQ2PPP/R1B1KB1R w KQkq - 4 7",
                        "rn1qk2r/p1pp1ppp/bp2pn2/8/1bPP4/1P3NP1/P2BPP1P/RN1QKB1R b KQkq - 2 6")
                .map(FEN::of)
                .toList();

        for (int i = 0; i < 10; i++) {
            assertEquals(expectedFenPositions.get(i), fenPositions.get(i));
        }
    }
}
