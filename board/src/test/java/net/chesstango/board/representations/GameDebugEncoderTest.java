package net.chesstango.board.representations;

import net.chesstango.board.Game;
import net.chesstango.gardel.pgn.PGN;
import net.chesstango.gardel.pgn.PGNStringDecoder;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class GameDebugEncoderTest {

    @Test
    public void testEncode() throws IOException {
        String lines = "[Event \"0f394c7e-70f6-4279-88c2-e6c3b67af09e\"]\n" +
                "[Site \"LAPTOP-PTVVKHNB\"]\n" +
                "[Date \"2025.05.11\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"Tango v1.0.0-SNAPSHOT\"]\n" +
                "[Black \"Spike 1.4\"]\n" +
                "[Result \"1-0\"]\n" +
                "\n" +
                "1. Nf3 d6 2. d4 Nf6 3. Nc3 g6 4. e4 Nc6 5. d5 Ne5\n" +
                "6. Bf4 Nxf3+ 7. Qxf3 Bg4 8. Bb5+ c6 9. Qg3 cxb5 10. Nxb5 Qb6\n" +
                "11. c4 Nxe4 12. Qxg4 Qxf2+ 13. Kd1 h5 14. Nc7+ Kd8 15. Qf3 Qd4+\n" +
                "16. Kc1 Qxc4+ 17. Kb1 Kxc7 18. Rc1 Qc5 19. Qxe4 Kb6 20. Be3 Bh6\n" +
                "21. Bxc5+ dxc5 22. Rf1 Rhd8 23. Rxf7 Rd6 24. Rxe7 Bg5 25. Rg7 Rf8\n" +
                "26. Rxg6 Rff6 27. Rxg5 Rf1+ 28. Kc2 Rxa1 29. Qe7 Rh6 30. Qf8 Rh7\n" +
                "31. Rg6+ Kb5 32. Qe8+ Ka5 33. a3 Rf1 34. d6 Rhf7 35. d7 R7f2+\n" +
                "36. Kb3 c4+ 37. Ka2 Rd1 38. d8=Q+ Rxd8 39. Qxd8+ Kb5 40. Qd5+ Ka4\n" +
                "41. Qxc4+ Ka5 42. Qb4# 1-0";
        PGN pgn = new PGNStringDecoder().decodePGN(lines);
        Game game = Game.from(pgn);

        GameDebugEncoder gameDebugEncoder = new GameDebugEncoder();
        String gameEncoded = gameDebugEncoder.encode(game);

        assertEquals("Game game = getGame(\"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1\")\n" +
                ".executeMove(Square.g1, Square.f3) // rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1\n" +
                ".executeMove(Square.d7, Square.d6) // rnbqkbnr/ppp1pppp/3p4/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 0 2\n" +
                ".executeMove(Square.d2, Square.d4) // rnbqkbnr/ppp1pppp/3p4/8/3P4/5N2/PPP1PPPP/RNBQKB1R b KQkq d3 0 2\n" +
                ".executeMove(Square.g8, Square.f6) // rnbqkb1r/ppp1pppp/3p1n2/8/3P4/5N2/PPP1PPPP/RNBQKB1R w KQkq - 1 3\n" +
                ".executeMove(Square.b1, Square.c3) // rnbqkb1r/ppp1pppp/3p1n2/8/3P4/2N2N2/PPP1PPPP/R1BQKB1R b KQkq - 2 3\n" +
                ".executeMove(Square.g7, Square.g6) // rnbqkb1r/ppp1pp1p/3p1np1/8/3P4/2N2N2/PPP1PPPP/R1BQKB1R w KQkq - 0 4\n" +
                ".executeMove(Square.e2, Square.e4) // rnbqkb1r/ppp1pp1p/3p1np1/8/3PP3/2N2N2/PPP2PPP/R1BQKB1R b KQkq e3 0 4\n" +
                ".executeMove(Square.b8, Square.c6) // r1bqkb1r/ppp1pp1p/2np1np1/8/3PP3/2N2N2/PPP2PPP/R1BQKB1R w KQkq - 1 5\n" +
                ".executeMove(Square.d4, Square.d5) // r1bqkb1r/ppp1pp1p/2np1np1/3P4/4P3/2N2N2/PPP2PPP/R1BQKB1R b KQkq - 0 5\n" +
                ".executeMove(Square.c6, Square.e5) // r1bqkb1r/ppp1pp1p/3p1np1/3Pn3/4P3/2N2N2/PPP2PPP/R1BQKB1R w KQkq - 1 6\n" +
                ".executeMove(Square.c1, Square.f4) // r1bqkb1r/ppp1pp1p/3p1np1/3Pn3/4PB2/2N2N2/PPP2PPP/R2QKB1R b KQkq - 2 6\n" +
                ".executeMove(Square.e5, Square.f3) // r1bqkb1r/ppp1pp1p/3p1np1/3P4/4PB2/2N2n2/PPP2PPP/R2QKB1R w KQkq - 0 7\n" +
                ".executeMove(Square.d1, Square.f3) // r1bqkb1r/ppp1pp1p/3p1np1/3P4/4PB2/2N2Q2/PPP2PPP/R3KB1R b KQkq - 0 7\n" +
                ".executeMove(Square.c8, Square.g4) // r2qkb1r/ppp1pp1p/3p1np1/3P4/4PBb1/2N2Q2/PPP2PPP/R3KB1R w KQkq - 1 8\n" +
                ".executeMove(Square.f1, Square.b5) // r2qkb1r/ppp1pp1p/3p1np1/1B1P4/4PBb1/2N2Q2/PPP2PPP/R3K2R b KQkq - 2 8\n" +
                ".executeMove(Square.c7, Square.c6) // r2qkb1r/pp2pp1p/2pp1np1/1B1P4/4PBb1/2N2Q2/PPP2PPP/R3K2R w KQkq - 0 9\n" +
                ".executeMove(Square.f3, Square.g3) // r2qkb1r/pp2pp1p/2pp1np1/1B1P4/4PBb1/2N3Q1/PPP2PPP/R3K2R b KQkq - 1 9\n" +
                ".executeMove(Square.c6, Square.b5) // r2qkb1r/pp2pp1p/3p1np1/1p1P4/4PBb1/2N3Q1/PPP2PPP/R3K2R w KQkq - 0 10\n" +
                ".executeMove(Square.c3, Square.b5) // r2qkb1r/pp2pp1p/3p1np1/1N1P4/4PBb1/6Q1/PPP2PPP/R3K2R b KQkq - 0 10\n" +
                ".executeMove(Square.d8, Square.b6) // r3kb1r/pp2pp1p/1q1p1np1/1N1P4/4PBb1/6Q1/PPP2PPP/R3K2R w KQkq - 1 11\n" +
                ".executeMove(Square.c2, Square.c4) // r3kb1r/pp2pp1p/1q1p1np1/1N1P4/2P1PBb1/6Q1/PP3PPP/R3K2R b KQkq c3 0 11\n" +
                ".executeMove(Square.f6, Square.e4) // r3kb1r/pp2pp1p/1q1p2p1/1N1P4/2P1nBb1/6Q1/PP3PPP/R3K2R w KQkq - 0 12\n" +
                ".executeMove(Square.g3, Square.g4) // r3kb1r/pp2pp1p/1q1p2p1/1N1P4/2P1nBQ1/8/PP3PPP/R3K2R b KQkq - 0 12\n" +
                ".executeMove(Square.b6, Square.f2) // r3kb1r/pp2pp1p/3p2p1/1N1P4/2P1nBQ1/8/PP3qPP/R3K2R w KQkq - 0 13\n" +
                ".executeMove(Square.e1, Square.d1) // r3kb1r/pp2pp1p/3p2p1/1N1P4/2P1nBQ1/8/PP3qPP/R2K3R b kq - 1 13\n" +
                ".executeMove(Square.h7, Square.h5) // r3kb1r/pp2pp2/3p2p1/1N1P3p/2P1nBQ1/8/PP3qPP/R2K3R w kq h6 0 14\n" +
                ".executeMove(Square.b5, Square.c7) // r3kb1r/ppN1pp2/3p2p1/3P3p/2P1nBQ1/8/PP3qPP/R2K3R b kq - 1 14\n" +
                ".executeMove(Square.e8, Square.d8) // r2k1b1r/ppN1pp2/3p2p1/3P3p/2P1nBQ1/8/PP3qPP/R2K3R w - - 2 15\n" +
                ".executeMove(Square.g4, Square.f3) // r2k1b1r/ppN1pp2/3p2p1/3P3p/2P1nB2/5Q2/PP3qPP/R2K3R b - - 3 15\n" +
                ".executeMove(Square.f2, Square.d4) // r2k1b1r/ppN1pp2/3p2p1/3P3p/2PqnB2/5Q2/PP4PP/R2K3R w - - 4 16\n" +
                ".executeMove(Square.d1, Square.c1) // r2k1b1r/ppN1pp2/3p2p1/3P3p/2PqnB2/5Q2/PP4PP/R1K4R b - - 5 16\n" +
                ".executeMove(Square.d4, Square.c4) // r2k1b1r/ppN1pp2/3p2p1/3P3p/2q1nB2/5Q2/PP4PP/R1K4R w - - 0 17\n" +
                ".executeMove(Square.c1, Square.b1) // r2k1b1r/ppN1pp2/3p2p1/3P3p/2q1nB2/5Q2/PP4PP/RK5R b - - 1 17\n" +
                ".executeMove(Square.d8, Square.c7) // r4b1r/ppk1pp2/3p2p1/3P3p/2q1nB2/5Q2/PP4PP/RK5R w - - 0 18\n" +
                ".executeMove(Square.h1, Square.c1) // r4b1r/ppk1pp2/3p2p1/3P3p/2q1nB2/5Q2/PP4PP/RKR5 b - - 1 18\n" +
                ".executeMove(Square.c4, Square.c5) // r4b1r/ppk1pp2/3p2p1/2qP3p/4nB2/5Q2/PP4PP/RKR5 w - - 2 19\n" +
                ".executeMove(Square.f3, Square.e4) // r4b1r/ppk1pp2/3p2p1/2qP3p/4QB2/8/PP4PP/RKR5 b - - 0 19\n" +
                ".executeMove(Square.c7, Square.b6) // r4b1r/pp2pp2/1k1p2p1/2qP3p/4QB2/8/PP4PP/RKR5 w - - 1 20\n" +
                ".executeMove(Square.f4, Square.e3) // r4b1r/pp2pp2/1k1p2p1/2qP3p/4Q3/4B3/PP4PP/RKR5 b - - 2 20\n" +
                ".executeMove(Square.f8, Square.h6) // r6r/pp2pp2/1k1p2pb/2qP3p/4Q3/4B3/PP4PP/RKR5 w - - 3 21\n" +
                ".executeMove(Square.e3, Square.c5) // r6r/pp2pp2/1k1p2pb/2BP3p/4Q3/8/PP4PP/RKR5 b - - 0 21\n" +
                ".executeMove(Square.d6, Square.c5) // r6r/pp2pp2/1k4pb/2pP3p/4Q3/8/PP4PP/RKR5 w - - 0 22\n" +
                ".executeMove(Square.c1, Square.f1) // r6r/pp2pp2/1k4pb/2pP3p/4Q3/8/PP4PP/RK3R2 b - - 1 22\n" +
                ".executeMove(Square.h8, Square.d8) // r2r4/pp2pp2/1k4pb/2pP3p/4Q3/8/PP4PP/RK3R2 w - - 2 23\n" +
                ".executeMove(Square.f1, Square.f7) // r2r4/pp2pR2/1k4pb/2pP3p/4Q3/8/PP4PP/RK6 b - - 0 23\n" +
                ".executeMove(Square.d8, Square.d6) // r7/pp2pR2/1k1r2pb/2pP3p/4Q3/8/PP4PP/RK6 w - - 1 24\n" +
                ".executeMove(Square.f7, Square.e7) // r7/pp2R3/1k1r2pb/2pP3p/4Q3/8/PP4PP/RK6 b - - 0 24\n" +
                ".executeMove(Square.h6, Square.g5) // r7/pp2R3/1k1r2p1/2pP2bp/4Q3/8/PP4PP/RK6 w - - 1 25\n" +
                ".executeMove(Square.e7, Square.g7) // r7/pp4R1/1k1r2p1/2pP2bp/4Q3/8/PP4PP/RK6 b - - 2 25\n" +
                ".executeMove(Square.a8, Square.f8) // 5r2/pp4R1/1k1r2p1/2pP2bp/4Q3/8/PP4PP/RK6 w - - 3 26\n" +
                ".executeMove(Square.g7, Square.g6) // 5r2/pp6/1k1r2R1/2pP2bp/4Q3/8/PP4PP/RK6 b - - 0 26\n" +
                ".executeMove(Square.f8, Square.f6) // 8/pp6/1k1r1rR1/2pP2bp/4Q3/8/PP4PP/RK6 w - - 1 27\n" +
                ".executeMove(Square.g6, Square.g5) // 8/pp6/1k1r1r2/2pP2Rp/4Q3/8/PP4PP/RK6 b - - 0 27\n" +
                ".executeMove(Square.f6, Square.f1) // 8/pp6/1k1r4/2pP2Rp/4Q3/8/PP4PP/RK3r2 w - - 1 28\n" +
                ".executeMove(Square.b1, Square.c2) // 8/pp6/1k1r4/2pP2Rp/4Q3/8/PPK3PP/R4r2 b - - 2 28\n" +
                ".executeMove(Square.f1, Square.a1) // 8/pp6/1k1r4/2pP2Rp/4Q3/8/PPK3PP/r7 w - - 0 29\n" +
                ".executeMove(Square.e4, Square.e7) // 8/pp2Q3/1k1r4/2pP2Rp/8/8/PPK3PP/r7 b - - 1 29\n" +
                ".executeMove(Square.d6, Square.h6) // 8/pp2Q3/1k5r/2pP2Rp/8/8/PPK3PP/r7 w - - 2 30\n" +
                ".executeMove(Square.e7, Square.f8) // 5Q2/pp6/1k5r/2pP2Rp/8/8/PPK3PP/r7 b - - 3 30\n" +
                ".executeMove(Square.h6, Square.h7) // 5Q2/pp5r/1k6/2pP2Rp/8/8/PPK3PP/r7 w - - 4 31\n" +
                ".executeMove(Square.g5, Square.g6) // 5Q2/pp5r/1k4R1/2pP3p/8/8/PPK3PP/r7 b - - 5 31\n" +
                ".executeMove(Square.b6, Square.b5) // 5Q2/pp5r/6R1/1kpP3p/8/8/PPK3PP/r7 w - - 6 32\n" +
                ".executeMove(Square.f8, Square.e8) // 4Q3/pp5r/6R1/1kpP3p/8/8/PPK3PP/r7 b - - 7 32\n" +
                ".executeMove(Square.b5, Square.a5) // 4Q3/pp5r/6R1/k1pP3p/8/8/PPK3PP/r7 w - - 8 33\n" +
                ".executeMove(Square.a2, Square.a3) // 4Q3/pp5r/6R1/k1pP3p/8/P7/1PK3PP/r7 b - - 0 33\n" +
                ".executeMove(Square.a1, Square.f1) // 4Q3/pp5r/6R1/k1pP3p/8/P7/1PK3PP/5r2 w - - 1 34\n" +
                ".executeMove(Square.d5, Square.d6) // 4Q3/pp5r/3P2R1/k1p4p/8/P7/1PK3PP/5r2 b - - 0 34\n" +
                ".executeMove(Square.h7, Square.f7) // 4Q3/pp3r2/3P2R1/k1p4p/8/P7/1PK3PP/5r2 w - - 1 35\n" +
                ".executeMove(Square.d6, Square.d7) // 4Q3/pp1P1r2/6R1/k1p4p/8/P7/1PK3PP/5r2 b - - 0 35\n" +
                ".executeMove(Square.f7, Square.f2) // 4Q3/pp1P4/6R1/k1p4p/8/P7/1PK2rPP/5r2 w - - 1 36\n" +
                ".executeMove(Square.c2, Square.b3) // 4Q3/pp1P4/6R1/k1p4p/8/PK6/1P3rPP/5r2 b - - 2 36\n" +
                ".executeMove(Square.c5, Square.c4) // 4Q3/pp1P4/6R1/k6p/2p5/PK6/1P3rPP/5r2 w - - 0 37\n" +
                ".executeMove(Square.b3, Square.a2) // 4Q3/pp1P4/6R1/k6p/2p5/P7/KP3rPP/5r2 b - - 1 37\n" +
                ".executeMove(Square.f1, Square.d1) // 4Q3/pp1P4/6R1/k6p/2p5/P7/KP3rPP/3r4 w - - 2 38\n" +
                ".executeMove(Square.d7, Square.d8, Piece.QUEEN_WHITE) // 3QQ3/pp6/6R1/k6p/2p5/P7/KP3rPP/3r4 b - - 0 38\n" +
                ".executeMove(Square.d1, Square.d8) // 3rQ3/pp6/6R1/k6p/2p5/P7/KP3rPP/8 w - - 0 39\n" +
                ".executeMove(Square.e8, Square.d8) // 3Q4/pp6/6R1/k6p/2p5/P7/KP3rPP/8 b - - 0 39\n" +
                ".executeMove(Square.a5, Square.b5) // 3Q4/pp6/6R1/1k5p/2p5/P7/KP3rPP/8 w - - 1 40\n" +
                ".executeMove(Square.d8, Square.d5) // 8/pp6/6R1/1k1Q3p/2p5/P7/KP3rPP/8 b - - 2 40\n" +
                ".executeMove(Square.b5, Square.a4) // 8/pp6/6R1/3Q3p/k1p5/P7/KP3rPP/8 w - - 3 41\n" +
                ".executeMove(Square.d5, Square.c4) // 8/pp6/6R1/7p/k1Q5/P7/KP3rPP/8 b - - 0 41\n" +
                ".executeMove(Square.a4, Square.a5) // 8/pp6/6R1/k6p/2Q5/P7/KP3rPP/8 w - - 1 42\n" +
                ".executeMove(Square.c4, Square.b4) // 8/pp6/6R1/k6p/1Q6/P7/KP3rPP/8 b - - 2 42\n", gameEncoded);
    }
}
