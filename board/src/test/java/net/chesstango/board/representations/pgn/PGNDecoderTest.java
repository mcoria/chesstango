package net.chesstango.board.representations.pgn;

import net.chesstango.board.perft.PerftMainTestSuite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;

public class PGNDecoderTest {

    private PGNDecoder decoder;

    @Before
    public void settup() {
        decoder = new PGNDecoder();
    }

    @Test
    public void decodeMoveList01() {
        List<String> moves = decoder.decodeMovesList("1. e4 c5 2. Nf3 Nc6 3. d4 cxd4 4. Nxd4 Nf6 5. Nc3 d6 6. Bg5 e6 7. Qd2 a6 8. O-O-O *");
        Assert.assertEquals("e4", moves.get(0));
        Assert.assertEquals("c5", moves.get(1));
        Assert.assertEquals("Nf3", moves.get(2));
        Assert.assertEquals("Nc6", moves.get(3));
        Assert.assertEquals("d4", moves.get(4));
        Assert.assertEquals("cxd4", moves.get(5));
        Assert.assertEquals("Nxd4", moves.get(6));
        Assert.assertEquals("Nf6", moves.get(7));
        Assert.assertEquals("Nc3", moves.get(8));
        Assert.assertEquals("d6", moves.get(9));
        Assert.assertEquals("Bg5", moves.get(10));
        Assert.assertEquals("e6", moves.get(11));
        Assert.assertEquals("Qd2", moves.get(12));
        Assert.assertEquals("a6", moves.get(13));
        Assert.assertEquals("O-O-O", moves.get(14));
    }

    @Test
    public void decodeMoveList02() {
        List<String> moves = decoder.decodeMovesList("1. e3 d5 2. Nc3 e5 3. Bb5+ c6 4. Qh5 cxb5 5. Qxe5+ Be6 6. Nxb5 Na6 7. h4 Nf6 8. Nd4 Bd6 9. Qg5 O-O 10. Nf5 Bxf5 11. Qxf5 g6 12. Qg5 Rc8 13. c3 Nc5 14. Ke2 Qe7 15. f3 Kh8 16. b3 b6 17. Bb2 Rfe8 18. c4 Be5 19. d4 Bxd4 20. Bxd4 Kg7 21. cxd5 h6 22. Qf4 a5 23. h5 g5 24. Qf5 Red8 25. Nh3 Re8 26. f4 gxf4 27. Qxf4 Ne4 28. Qg4+ Kh8 29. Qf4 Rc2+ 30. Kd1 Rd2+ 31. Ke1 Kg7 32. Bxf6+ Qxf6 33. Qxf6+ Kxf6 34. g4 Rxd5 35. Rf1+ Ke7 36. Rc1 Rd3 37. Ke2 Rc3 38. Rg1 Rd8 39. Kf3 Nd2+ 40. Ke2 Ne4 41. Kf3 Nd2+ 42. Ke2 Ne4 1/2-1/2");
        Assert.assertEquals("e3", moves.get(0));
        Assert.assertEquals("d5", moves.get(1));
        Assert.assertEquals("Ke2", moves.get(82));
        Assert.assertEquals("Ne4", moves.get(83));
    }

    @Test
    public void decodeMoveList03() {
        List<String> moves = decoder.decodeMovesList("1. e3 d5 2. Nc3 e5 3. Bb5+ c6 4. Qh5 cxb5 5. Qxe5+ Be6 6. Nxb5 Na6 7. h4 Nf6 8. Nd4 Bd6 9. Qg5 O-O 10. Nf5 Bxf5 11. Qxf5 g6 12. Qg5 Rc8 13. c3 Nc5 14. Ke2 Qe7 15. f3 Kh8 16. b3 b6 17. Bb2 Rfe8 18. c4 Be5 19. d4 Bxd4 20. Bxd4 Kg7 21. cxd5 h6 22. Qf4 a5 23. h5 g5 24. Qf5 Red8 25. Nh3 Re8 26. f4 gxf4 27. Qxf4 Ne4 28. Qg4+ Kh8 29. Qf4 Rc2+ 30. Kd1 Rd2+ 31. Ke1 Kg7 32. Bxf6+ Qxf6 33. Qxf6+ Kxf6 34. g4 Rxd5 35. Rf1+ Ke7 36. Rc1 Rd3 37. Ke2 Rc3 38. Rg1 Rd8 39. Kf3 Nd2+ 40. Ke2 Ne4 41. Kf3 Nd2+ 42. Ke2 Ne4 1/2-1/2 ");
        Assert.assertEquals("e3", moves.get(0));
        Assert.assertEquals("d5", moves.get(1));
        Assert.assertEquals("Ke2", moves.get(82));
        Assert.assertEquals("Ne4", moves.get(83));
    }


    @Test
    public void decodeMoveListInLines01() throws IOException {
        String lines =
                "1. e3 d5 2. Nc3 e5 3. Bb5+ c6 4. Qh5 cxb5 5. Qxe5+ Be6\n" +
                "6. Nxb5 Na6 7. h4 Nf6 8. Nd4 Bd6 9. Qg5 O-O 10. Nf5 Bxf5\n" +
                "11. Qxf5 g6 12. Qg5 Rc8 13. c3 Nc5 14. Ke2 Qe7 15. f3 Kh8\n" +
                "16. b3 b6 17. Bb2 Rfe8 18. c4 Be5 19. d4 Bxd4 20. Bxd4 Kg7\n" +
                "21. cxd5 h6 22. Qf4 a5 23. h5 g5 24. Qf5 Red8 25. Nh3 Re8\n" +
                "26. f4 gxf4 27. Qxf4 Ne4 28. Qg4+ Kh8 29. Qf4 Rc2+ 30. Kd1 Rd2+\n" +
                "31. Ke1 Kg7 32. Bxf6+ Qxf6 33. Qxf6+ Kxf6 34. g4 Rxd5 35. Rf1+ Ke7\n" +
                "36. Rc1 Rd3 37. Ke2 Rc3 38. Rg1 Rd8 39. Kf3 Nd2+ 40. Ke2 Ne4\n" +
                "41. Kf3 Nd2+ 42. Ke2 Ne4 1/2-1/2";

        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        List<String> moves = decoder.decodeMovesList(bufferReader);

        Assert.assertEquals("e3", moves.get(0));
        Assert.assertEquals("d5", moves.get(1));
        Assert.assertEquals("Ke2", moves.get(82));
        Assert.assertEquals("Ne4", moves.get(83));
    }

    @Test
    public void decodeMoveListInLines02() throws IOException {
        String lines =
                "1. e4 c5 2. Nf3 Nc6 3. d4 cxd4 4. Nxd4 Nf6 5. Nc3 d6 6. Bg5 e6 7. Qd2 a6 8.\n" +
                "O-O-O *";

        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        List<String> moves = decoder.decodeMovesList(bufferReader);

        Assert.assertEquals("e4", moves.get(0));
        Assert.assertEquals("c5", moves.get(1));
        Assert.assertEquals("O-O-O", moves.get(14));
    }


    @Test
    public void decodeHeader01() throws IOException {
        String lines =
                "[Event \"Computer chess game\"]\n" +
                "[Site \"KANO-LENOVO\"]\n" +
                "[Date \"2023.03.02\"]\n" +
                "[Round \"10\"]\n" +
                "[White \"Tango\"]\n" +
                "[Black \"Chacarera\"]\n" +
                "[FEN \"rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "\n";
        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        PGNGame pgnGame = decoder.decodeHeader(bufferReader);

        Assert.assertEquals("Computer chess game", pgnGame.getEvent());
        Assert.assertEquals("KANO-LENOVO", pgnGame.getSite());
        Assert.assertEquals("2023.03.02", pgnGame.getDate());
        Assert.assertEquals("10", pgnGame.getRound());
        Assert.assertEquals("Tango", pgnGame.getWhite());
        Assert.assertEquals("Chacarera", pgnGame.getBlack());
        Assert.assertEquals("rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5", pgnGame.getFen());
        Assert.assertEquals("1/2-1/2", pgnGame.getResult());
    }

    @Test
    public void decodeGame01() throws IOException {
        String lines = "[Event \"Balsa - Top 10\"]\n" +
                "[Site \"KANO-LENOVO\"]\n" +
                "[Date \"2023.03.02\"]\n" +
                "[Round \"10\"]\n" +
                "[White \"Tango\"]\n" +
                "[Black \"Chacarera\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "[ECO \"B12\"]\n" +
                "[PlyCount \"11\"]\n" +
                "[EventDate \"2017.09.05\"]\n" +
                "[EventType \"simul\"]\n" +
                "[Source \"Sedat Canbaz\"]\n" +
                "\n" +
                "1. e4 c6 2. d4 d5 3. e5 Bf5 4. c3 e6 5. Nf3 Nd7 6. Be2 *\n";
        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        PGNGame game = decoder.decodeGame(bufferReader);

        Assert.assertEquals("Balsa - Top 10", game.getEvent());
        Assert.assertEquals("KANO-LENOVO", game.getSite());
        Assert.assertEquals("2023.03.02", game.getDate());
        Assert.assertEquals("10", game.getRound());
        Assert.assertEquals("Tango", game.getWhite());
        Assert.assertEquals("Chacarera", game.getBlack());
        Assert.assertEquals("1/2-1/2", game.getResult());

        List<String> moves = game.getMoveList();
        Assert.assertEquals("e4", moves.get(0));
        Assert.assertEquals("c6", moves.get(1));
        Assert.assertEquals("Be2", moves.get(10));
    }

    @Test
    public void readGames() throws IOException {
        InputStream instr = PerftMainTestSuite.class.getClassLoader().getResourceAsStream("main/pgn/Balsa_Top10.pgn");

        InputStreamReader inputStreamReader = new InputStreamReader(instr);

        BufferedReader bufferReader = new BufferedReader(inputStreamReader);

        List<PGNGame> games = decoder.decodeGames(bufferReader);

        Assert.assertEquals(10, games.size());

        // 1st Game
        Assert.assertEquals("Balsa - Top 10", games.get(0).getEvent());
        Assert.assertEquals("Be2", games.get(0).getMoveList().get(10));

        // 10th Game
        Assert.assertEquals("13", games.get(9).getRound());
        Assert.assertEquals("Bd2", games.get(9).getMoveList().get(10));
    }
}

