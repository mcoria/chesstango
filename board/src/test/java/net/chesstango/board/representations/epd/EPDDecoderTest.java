package net.chesstango.board.representations.epd;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class EPDDecoderTest {

    private EPDDecoder epdDecoder;

    @BeforeEach
    public void setup() {
        epdDecoder = new EPDDecoder();
    }

    @Test
    public void testReadEDP01() {
        EPD epd = epdDecoder.readEdpLine("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k - bm Rd1xd8+; ce +M5; pv Rd1xd8+ Ke8xd8 Rc6-c8+ Kd8-e7 Bb8-d6+ Ke7-e6 Rc8-e8+ Ng8-e7 Re8xe7+; id \"5712\";");

        assertEquals("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k -", epd.getFenWithoutClocks().toString());
        assertEquals("Rd1xd8+", epd.getBestMovesStr());
        assertEquals("5712", epd.getId());
        assertNotNull(epd.getBestMoves());
    }


    @Test
    public void testReadEDP02() {
        EPD epd = epdDecoder.readEdpLine("r3k3/8/2P1b3/p3p1P1/P6B/1pKBR1R1/1P3b2/3r1N2 b q - bm O-O-O; ce -M5; pv O-O-O Bd3-a6+ Kc8-c7 Ba6-c4 Rd1-c1+ Kc3xb3 Be6xc4+ Kb3-a3 Rc1-a1+; id \"6067\";");

        assertEquals("r3k3/8/2P1b3/p3p1P1/P6B/1pKBR1R1/1P3b2/3r1N2 b q -", epd.getFenWithoutClocks().toString());
        assertEquals("O-O-O", epd.getBestMovesStr());
        assertEquals("6067", epd.getId());
        assertNotNull(epd.getBestMoves());
    }

    @Test
    public void testReadEDP03() {
        EPD epd = epdDecoder.readEdpLine("8/6R1/1p6/p2r2pk/3P1R2/6P1/q4P2/2B2K2 w - - bm Rh4+; ce +M2; pv Rh4+ gxh4 g4+; id \"918\";");

        assertEquals("8/6R1/1p6/p2r2pk/3P1R2/6P1/q4P2/2B2K2 w - -", epd.getFenWithoutClocks().toString());
        assertEquals("Rh4+", epd.getBestMovesStr());
        assertEquals("918", epd.getId());
        assertNotNull(epd.getBestMoves());
    }

    @Test
    public void testReadEDP04() {
        EPD epd = epdDecoder.readEdpLine("q1r3r1/1b3Nbk/p3QBpp/1p1P3P/B1n5/8/P4PP1/3RR1K1 w - - bm h5xg6+; ce +M3; pv h5xg6+ Kh7xg6 Ba4-c2+ Kg6-h5 g2-g4+; id \"3050\";");

        assertEquals("q1r3r1/1b3Nbk/p3QBpp/1p1P3P/B1n5/8/P4PP1/3RR1K1 w - -", epd.getFenWithoutClocks().toString());
        assertEquals("h5xg6+", epd.getBestMovesStr());
        assertEquals("3050", epd.getId());
        assertNotNull(epd.getBestMoves());
    }

    @Test
    public void testReadEDP05() {
        EPD epd = epdDecoder.readEdpLine("8/kpP2r1p/p6r/n3Q1p1/P6q/1PN3p1/5P2/2RR2K1 w - - bm c7-c8N+; ce +M6; pv c7-c8N+ Ka7-a8 Nc8-b6+ Ka8-a7 Qe5-b8+ Ka7xb8 Rd1-d8+ Kb8-a7 Rd8-a8+ Ka7xb6 Nc3-d5+; id \"7510\";");

        assertEquals("8/kpP2r1p/p6r/n3Q1p1/P6q/1PN3p1/5P2/2RR2K1 w - -", epd.getFenWithoutClocks().toString());
        assertEquals("c7-c8N+", epd.getBestMovesStr());
        assertEquals("7510", epd.getId());
        assertNotNull(epd.getBestMoves());
    }

    @Test
    public void testReadEDP06() {
        EPD epd = epdDecoder.readEdpLine("r1bq2rk/pp3pbp/2p1p1pQ/7P/3P4/2PB1N2/PP3PPR/2KR4 w - - bm Qxh7+; id \"WAC.004\";");

        assertEquals("r1bq2rk/pp3pbp/2p1p1pQ/7P/3P4/2PB1N2/PP3PPR/2KR4 w - -", epd.getFenWithoutClocks().toString());
        assertEquals("Qxh7+", epd.getBestMovesStr());
        assertEquals("WAC.004", epd.getId());
        assertNotNull(epd.getBestMoves());
    }

    @Test
    public void testReadEDP07() {
        EPD epd = epdDecoder.readEdpLine("1n2rr2/1pk3pp/pNn2p2/2N1p3/8/6P1/PP2PPKP/2RR4 w - - bm Nca4; id \"WAC.299\";");

        assertEquals("1n2rr2/1pk3pp/pNn2p2/2N1p3/8/6P1/PP2PPKP/2RR4 w - -", epd.getFenWithoutClocks().toString());
        assertEquals("Nca4", epd.getBestMovesStr());
        assertEquals("WAC.299", epd.getId());
        assertNotNull(epd.getBestMoves());
    }

    @Test
    public void testReadEDP08() {
        EPD epd = epdDecoder.readEdpLine("rb3qk1/pQ3ppp/4p3/3P4/8/1P3N2/1P3PPP/3R2K1 w - - bm Qxa8 d6 dxe6; id \"WAC.031\";");

        assertEquals("rb3qk1/pQ3ppp/4p3/3P4/8/1P3N2/1P3PPP/3R2K1 w - -", epd.getFenWithoutClocks().toString());
        assertEquals("Qxa8 d6 dxe6", epd.getBestMovesStr());
        assertEquals("WAC.031", epd.getId());
        assertEquals(3, epd.getBestMoves().size());
    }


    @Test
    public void testReadEDP09() {
        EPD epd = epdDecoder.readEdpLine("5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - - bm Rg3; id \"WAC.003\";");

        assertEquals("5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - -", epd.getFenWithoutClocks().toString());
        assertEquals("Rg3", epd.getBestMovesStr());
        assertEquals("WAC.003", epd.getId());
        assertEquals(1, epd.getBestMoves().size());
        assertEquals(Piece.ROOK_WHITE, epd.getBestMoves().getFirst().getFrom().getPiece());
    }

    @Test
    public void testReadEDP10() {
        EPD epd = epdDecoder.readEdpLine("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - bm e6; id \"WAC.072\";");

        assertEquals("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - -", epd.getFenWithoutClocks().toString());
        assertEquals("e6", epd.getBestMovesStr());
        assertEquals("WAC.072", epd.getId());
        assertEquals(1, epd.getBestMoves().size());
        assertEquals(Piece.PAWN_WHITE, epd.getBestMoves().getFirst().getFrom().getPiece());
    }

    @Test
    public void testReadEDP11() {
        EPD epd = epdDecoder.readEdpLine("r2r2k1/p2n1p2/4q2p/3p2p1/1PpB4/P1NnPP2/2Q3PP/R2R2K1 b - - bm N7e5; c0 \"N7e5=10, a5=6, a6=6, Nb8=5\"; id \"STS(v12.0) Center Control.081\";");

        assertEquals("r2r2k1/p2n1p2/4q2p/3p2p1/1PpB4/P1NnPP2/2Q3PP/R2R2K1 b - -", epd.getFenWithoutClocks().toString());
        assertEquals("N7e5", epd.getBestMovesStr());
        assertEquals("STS(v12.0) Center Control.081", epd.getId());
        assertEquals(1, epd.getBestMoves().size());
        assertEquals(Piece.KNIGHT_BLACK, epd.getBestMoves().getFirst().getFrom().getPiece());
        assertEquals(Square.d7, epd.getBestMoves().getFirst().getFrom().getSquare());
        assertEquals(Square.e5, epd.getBestMoves().getFirst().getTo().getSquare());
    }

    @Test
    public void testReadEDP12() {
        EPD epd = epdDecoder.readEdpLine("3r2k1/1p3ppp/2pq4/p1n5/P6P/1P6/1PB2QP1/1K2R3 w - - am Rd1; id \"position 03\";");

        assertEquals("3r2k1/1p3ppp/2pq4/p1n5/P6P/1P6/1PB2QP1/1K2R3 w - -", epd.getFenWithoutClocks().toString());
        assertEquals("Rd1", epd.getAvoidMovesStr());
        assertEquals("position 03", epd.getId());
        assertEquals(1, epd.getAvoidMoves().size());
        assertEquals(Piece.ROOK_WHITE, epd.getAvoidMoves().getFirst().getFrom().getPiece());
        assertEquals(Square.e1, epd.getAvoidMoves().getFirst().getFrom().getSquare());
        assertEquals(Square.d1, epd.getAvoidMoves().getFirst().getTo().getSquare());
    }

    @Test
    public void testReadSM() {
        EPD epd = epdDecoder.readEdpLine("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - sm a4; c0 \"c0\"; c1 \"c1\"; c2 \"c2\"; c3 \"c3\"; c4 \"c4\"; c5 \"c5\"; c6 \"c6\"; id \"1\";");

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -", epd.getFenWithoutClocks().toString());
        assertEquals("a4", epd.getSuppliedMoveStr());
        assertEquals("c0", epd.getC0());
        assertEquals("c1", epd.getC1());
        assertEquals("c2", epd.getC2());
        assertEquals("c3", epd.getC3());
        assertEquals("c4", epd.getC4());
        assertEquals("c5", epd.getC5());
        assertEquals("c6", epd.getC6());
        assertEquals("1", epd.getId());
    }

    @Test
    public void testReadSM01() {
        EPD epd = epdDecoder.readEdpLine("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - sm e4; c0 \"event='Wch U16'\"; c1 \"site='Wattignies'\"; c2 \"date='1976.08.27'\"; c3 \"white='Chandler, Murray G'\"; c4 \"black='Kasparov, Gary'\"; c5 \"result='1-0'\"; c6 \"clock=1\"; id \"463b96181691fc9c\";");

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -", epd.getFenWithoutClocks().toString());
        assertEquals("e4", epd.getSuppliedMoveStr());
        assertEquals("event='Wch U16'", epd.getC0());
        assertEquals("site='Wattignies'", epd.getC1());
        assertEquals("date='1976.08.27'", epd.getC2());
        assertEquals("white='Chandler, Murray G'", epd.getC3());
        assertEquals("black='Kasparov, Gary'", epd.getC4());
        assertEquals("result='1-0'", epd.getC5());
        assertEquals("clock=1", epd.getC6());
        assertEquals("463b96181691fc9c", epd.getId());

        assertEquals(0x463b96181691fc9cL, Long.parseUnsignedLong(epd.getId(), 16));
    }

    @Test
    @Disabled
    public void testReadMateAll() {
        List<EPD> entryList = epdDecoder.readEdpFile("C:\\Java\\projects\\chess\\chess-utils\\testing\\40H-EPD-databases\\mate-all.epd").toList();

        assertEquals(23268, entryList.size());
    }


    @Test
    @Disabled
    public void testReadWAC() {
        List<EPD> entryList = epdDecoder.readEdpFile("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac.epd").toList();

        assertEquals(300, entryList.size());
    }

    @Test
    @Disabled
    public void testReadWAC2018() {
        List<EPD> entryList = epdDecoder.readEdpFile("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd").toList();

        assertEquals(201, entryList.size());
    }

    @Test
    @Disabled
    public void testSilentButDeadly() {
        List<EPD> entryList = epdDecoder.readEdpFile("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\sbd.epd").toList();

        assertEquals(134, entryList.size());
    }

    @Test
    @Disabled
    public void testPET() {
        List<EPD> entryList = epdDecoder.readEdpFile("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\pet.epd").toList();

        assertEquals(48, entryList.size());
    }


    @Test
    @Disabled
    public void testReadEvalTunnerWhite() {
        List<EPD> entryList = epdDecoder.readEdpFile("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\Texel\\eval-tunner-white.txt").toList();

        assertEquals(32571, entryList.size());
    }

    @Test
    @Disabled
    public void allEPDsFiles() {
        List<String> epdFiles = List.of(
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\mate-all.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Bratko-Kopec.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\sbd.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\Nolot.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\pet.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS1.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS2.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS3.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS4.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS5.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS6.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS7.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS8.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS9.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS10.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS11.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS12.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS13.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS14.epd",
                "C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\STS15.epd"
        );

        epdFiles.forEach(fileName -> epdDecoder.readEdpFile(fileName));
    }

}
