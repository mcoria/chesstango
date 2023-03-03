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
    public void testBuildGame() throws IOException {
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
}
