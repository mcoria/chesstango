package net.chesstango.board.representations.pgn;

import net.chesstango.board.representations.epd.EPD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PGNDecoderTest {

    private PGNStringDecoder decoder;

    @BeforeEach
    public void settup() {
        decoder = new PGNStringDecoder();
    }

    @Test
    public void decodeMoveList01() {
        List<String> moves = decoder.decodePGNBody("1. e4 c5 2. Nf3 Nc6 3. d4 cxd4 4. Nxd4 Nf6 5. Nc3 d6 6. Bg5 e6 7. Qd2 a6 8. O-O-O *");
        assertEquals("e4", moves.get(0));
        assertEquals("c5", moves.get(1));
        assertEquals("Nf3", moves.get(2));
        assertEquals("Nc6", moves.get(3));
        assertEquals("d4", moves.get(4));
        assertEquals("cxd4", moves.get(5));
        assertEquals("Nxd4", moves.get(6));
        assertEquals("Nf6", moves.get(7));
        assertEquals("Nc3", moves.get(8));
        assertEquals("d6", moves.get(9));
        assertEquals("Bg5", moves.get(10));
        assertEquals("e6", moves.get(11));
        assertEquals("Qd2", moves.get(12));
        assertEquals("a6", moves.get(13));
        assertEquals("O-O-O", moves.get(14));
    }

    @Test
    public void decodeMoveList02() {
        List<String> moves = decoder.decodePGNBody("1. e3 d5 2. Nc3 e5 3. Bb5+ c6 4. Qh5 cxb5 5. Qxe5+ Be6 6. Nxb5 Na6 7. h4 Nf6 8. Nd4 Bd6 9. Qg5 O-O 10. Nf5 Bxf5 11. Qxf5 g6 12. Qg5 Rc8 13. c3 Nc5 14. Ke2 Qe7 15. f3 Kh8 16. b3 b6 17. Bb2 Rfe8 18. c4 Be5 19. d4 Bxd4 20. Bxd4 Kg7 21. cxd5 h6 22. Qf4 a5 23. h5 g5 24. Qf5 Red8 25. Nh3 Re8 26. f4 gxf4 27. Qxf4 Ne4 28. Qg4+ Kh8 29. Qf4 Rc2+ 30. Kd1 Rd2+ 31. Ke1 Kg7 32. Bxf6+ Qxf6 33. Qxf6+ Kxf6 34. g4 Rxd5 35. Rf1+ Ke7 36. Rc1 Rd3 37. Ke2 Rc3 38. Rg1 Rd8 39. Kf3 Nd2+ 40. Ke2 Ne4 41. Kf3 Nd2+ 42. Ke2 Ne4 1/2-1/2");
        assertEquals("e3", moves.get(0));
        assertEquals("d5", moves.get(1));
        assertEquals("Ke2", moves.get(82));
        assertEquals("Ne4", moves.get(83));
    }

    @Test
    public void decodeMoveList03() {
        List<String> moves = decoder.decodePGNBody("1. e3 d5 2. Nc3 e5 3. Bb5+ c6 4. Qh5 cxb5 5. Qxe5+ Be6 6. Nxb5 Na6 7. h4 Nf6 8. Nd4 Bd6 9. Qg5 O-O 10. Nf5 Bxf5 11. Qxf5 g6 12. Qg5 Rc8 13. c3 Nc5 14. Ke2 Qe7 15. f3 Kh8 16. b3 b6 17. Bb2 Rfe8 18. c4 Be5 19. d4 Bxd4 20. Bxd4 Kg7 21. cxd5 h6 22. Qf4 a5 23. h5 g5 24. Qf5 Red8 25. Nh3 Re8 26. f4 gxf4 27. Qxf4 Ne4 28. Qg4+ Kh8 29. Qf4 Rc2+ 30. Kd1 Rd2+ 31. Ke1 Kg7 32. Bxf6+ Qxf6 33. Qxf6+ Kxf6 34. g4 Rxd5 35. Rf1+ Ke7 36. Rc1 Rd3 37. Ke2 Rc3 38. Rg1 Rd8 39. Kf3 Nd2+ 40. Ke2 Ne4 41. Kf3 Nd2+ 42. Ke2 Ne4 1/2-1/2 ");
        assertEquals("e3", moves.get(0));
        assertEquals("d5", moves.get(1));
        assertEquals("Ke2", moves.get(82));
        assertEquals("Ne4", moves.get(83));
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

        List<String> moves = decoder.decodePGNBody(bufferReader);

        assertEquals("e3", moves.get(0));
        assertEquals("d5", moves.get(1));
        assertEquals("Ke2", moves.get(82));
        assertEquals("Ne4", moves.get(83));
    }

    @Test
    public void decodeMoveListInLines02() throws IOException {
        String lines =
                "1. e4 c5 2. Nf3 Nc6 3. d4 cxd4 4. Nxd4 Nf6 5. Nc3 d6 6. Bg5 e6 7. Qd2 a6 8.\n" +
                        "O-O-O *";

        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        List<String> moves = decoder.decodePGNBody(bufferReader);

        assertEquals("e4", moves.get(0));
        assertEquals("c5", moves.get(1));
        assertEquals("O-O-O", moves.get(14));
    }


    @Test
    public void decodePGNHeaders01() throws IOException {
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

        PGN pgn = decoder.decodePGNHeaders(bufferReader);

        assertEquals("Computer chess game", pgn.getEvent());
        assertEquals("KANO-LENOVO", pgn.getSite());
        assertEquals("2023.03.02", pgn.getDate());
        assertEquals("10", pgn.getRound());
        assertEquals("Tango", pgn.getWhite());
        assertEquals("Chacarera", pgn.getBlack());
        assertEquals("rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5", pgn.getFen());
        assertEquals("1/2-1/2", pgn.getResult());
    }

    @Test
    public void decodePGN01() throws IOException {
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

        PGN game = decoder.decodePGN(bufferReader);

        assertEquals("Balsa - Top 10", game.getEvent());
        assertEquals("KANO-LENOVO", game.getSite());
        assertEquals("2023.03.02", game.getDate());
        assertEquals("10", game.getRound());
        assertEquals("Tango", game.getWhite());
        assertEquals("Chacarera", game.getBlack());
        assertEquals("1/2-1/2", game.getResult());

        List<String> moves = game.getMoveList();
        assertEquals("e4", moves.get(0));
        assertEquals("c6", moves.get(1));
        assertEquals("Be2", moves.get(10));
    }


    @Test
    public void decodePGN02() throws IOException {
        String lines = "[Event \"Rated Rapid game\"]\n" +
                "[Site \"https://lichess.org/cjatYH5c\"]\n" +
                "[Date \"2024.02.20\"]\n" +
                "[White \"ChessChildren\"]\n" +
                "[Black \"chesstango_bot\"]\n" +
                "[Result \"1-0\"]\n" +
                "[UTCDate \"2024.02.20\"]\n" +
                "[UTCTime \"21:23:34\"]\n" +
                "[WhiteElo \"1765\"]\n" +
                "[BlackElo \"1863\"]\n" +
                "[WhiteRatingDiff \"+7\"]\n" +
                "[BlackRatingDiff \"-7\"]\n" +
                "[WhiteTitle \"BOT\"]\n" +
                "[BlackTitle \"BOT\"]\n" +
                "[Variant \"Standard\"]\n" +
                "[TimeControl \"600+0\"]\n" +
                "[ECO \"E25\"]\n" +
                "[Opening \"Nimzo-Indian Defense: Sämisch Variation, Keres Variation\"]\n" +
                "[Termination \"Time forfeit\"]\n" +
                "[Annotator \"lichess.org\"]\n" +
                "\n" +
                "1. d4 Nf6 2. c4 e6 3. Nc3 Bb4 4. a3 Bxc3+ 5. bxc3 c5 6. f3 d5 7. cxd5 Nxd5 8. dxc5 { E25 Nimzo-Indian Defense: Sämisch Variation, Keres Variation } Qa5 9. Bd2 Qxc5 10. e4 Nf6 11. Qb3 O-O 12. Qb4 Re8 13. Qxc5 Na6 14. Bxa6 bxa6 15. e5 Nd7 16. Qc6 Rb8 17. Be3 Rf8 18. Qd6 { White wins on time. } 1-0";
        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        PGN game = decoder.decodePGN(bufferReader);

        assertEquals("Rated Rapid game", game.getEvent());
        assertEquals("https://lichess.org/cjatYH5c", game.getSite());
        assertEquals("2024.02.20", game.getDate());
        assertEquals("ChessChildren", game.getWhite());
        assertEquals("chesstango_bot", game.getBlack());
        assertEquals("1-0", game.getResult());

        List<String> moves = game.getMoveList();
        assertEquals("Qb4", moves.get(22));
    }

    @Test
    public void decodePGN03() throws IOException {
        String lines = "[Event \"b3644c68-3c6a-40ab-870a-3b965dd38c6c\"]\n" +
                "[Site \"LAPTOP-PTVVKHNB\"]\n" +
                "[Date \"2024.06.12\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"Tango v0.0.28-SNAPSHOT\"]\n" +
                "[Black \"Spike 1.4\"]\n" +
                "[FEN \"r1bqkb1r/pp1p1ppp/2n1pn2/8/2PN4/2N5/PP2PPPP/R1BQKB1R w KQkq - 0 6\"]\n" +
                "[Result \"0-1\"]\n" +
                "\n" +
                "1. e4 e5 2. Nf5 b6 3. Bd3 g6 4. Ne3 Nb4 5. O-O Bb7\n" +
                "6. Ncd5 Nfxd5 7. cxd5 Rc8 8. Re1 Nxd3 9. Qxd3 Bb4 10. Bd2 Bc5\n" +
                "11. Bc3 Qg5 12. Rad1 O-O 13. d6 Qf4 14. g3 Qf3 15. Bxe5 Bxe4\n" +
                "16. Qb3 Qh1# 0-1\n";
        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        PGN game = decoder.decodePGN(bufferReader);

        assertEquals("b3644c68-3c6a-40ab-870a-3b965dd38c6c", game.getEvent());
        assertEquals("LAPTOP-PTVVKHNB", game.getSite());
        assertEquals("2024.06.12", game.getDate());
        assertEquals("Tango v0.0.28-SNAPSHOT", game.getWhite());
        assertEquals("Spike 1.4", game.getBlack());
        assertEquals("0-1", game.getResult());

        List<String> moves = game.getMoveList();
        assertEquals("Qh1#", moves.get(31));
    }


    @Test
    public void readGames() throws IOException {
        InputStream instr = this.getClass().getClassLoader().getResourceAsStream("main/pgn/Balsa_Top10.pgn");

        InputStreamReader inputStreamReader = new InputStreamReader(instr);

        BufferedReader bufferReader = new BufferedReader(inputStreamReader);

        List<PGN> games = decoder.decodePGNs(bufferReader).toList();

        assertEquals(10, games.size());

        // 1st Game
        assertEquals("Balsa - Top 10", games.get(0).getEvent());
        assertEquals("Be2", games.get(0).getMoveList().get(10));

        // 10th Game
        assertEquals("13", games.get(9).getRound());
        assertEquals("Bd2", games.get(9).getMoveList().get(10));
    }


    @Test
    @Disabled
    public void testKasparovGames() throws IOException {
        InputStream instr = new FileInputStream("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\players\\Kasparov.pgn");

        InputStreamReader inputStreamReader = new InputStreamReader(instr);

        BufferedReader bufferReader = new BufferedReader(inputStreamReader);

        List<PGN> pgnGames = decoder.decodePGNs(bufferReader).toList();
        assertEquals(2128, pgnGames.size());

        List<EPD> kasparovEPDs = new LinkedList<>();
        pgnGames.stream()
                .map(PGN::toEPD)
                .forEach(fenStream -> kasparovEPDs.addAll(fenStream.toList()));

        assertEquals(162894, kasparovEPDs.size());
    }
}

