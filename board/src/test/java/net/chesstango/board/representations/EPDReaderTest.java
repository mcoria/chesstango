package net.chesstango.board.representations;

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
public class EPDReaderTest {

    private EPDReader EPDReader;

    @BeforeEach
    public void setup(){
        EPDReader = new EPDReader();
    }

    @Test
    public void testReadEDP01(){
        EPDEntry entry = EPDReader.readEdpLine("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k - bm Rd1xd8+; ce +M5; pv Rd1xd8+ Ke8xd8 Rc6-c8+ Kd8-e7 Bb8-d6+ Ke7-e6 Rc8-e8+ Ng8-e7 Re8xe7+; id \"5712\";");

        assertEquals("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k -", entry.fen);
        assertEquals("Rd1xd8+", entry.bestMovesString);
        assertEquals("5712", entry.id);
        assertNotNull(entry.game);
        assertNotNull(entry.bestMoves);
    }


    @Test
    public void testReadEDP02(){
        EPDEntry entry = EPDReader.readEdpLine("r3k3/8/2P1b3/p3p1P1/P6B/1pKBR1R1/1P3b2/3r1N2 b q - bm O-O-O; ce -M5; pv O-O-O Bd3-a6+ Kc8-c7 Ba6-c4 Rd1-c1+ Kc3xb3 Be6xc4+ Kb3-a3 Rc1-a1+; id \"6067\";");

        assertEquals("r3k3/8/2P1b3/p3p1P1/P6B/1pKBR1R1/1P3b2/3r1N2 b q -", entry.fen);
        assertEquals("O-O-O", entry.bestMovesString);
        assertEquals("6067", entry.id);
        assertNotNull(entry.game);
        assertNotNull(entry.bestMoves);
    }

    @Test
    public void testReadEDP03(){
        EPDEntry entry = EPDReader.readEdpLine("8/6R1/1p6/p2r2pk/3P1R2/6P1/q4P2/2B2K2 w - - bm Rh4+; ce +M2; pv Rh4+ gxh4 g4+; id \"918\";");

        assertEquals("8/6R1/1p6/p2r2pk/3P1R2/6P1/q4P2/2B2K2 w - -", entry.fen);
        assertEquals("Rh4+", entry.bestMovesString);
        assertEquals("918", entry.id);
        assertNotNull(entry.game);
        assertNotNull(entry.bestMoves);
    }

    @Test
    public void testReadEDP04(){
        EPDEntry entry = EPDReader.readEdpLine("q1r3r1/1b3Nbk/p3QBpp/1p1P3P/B1n5/8/P4PP1/3RR1K1 w - - bm h5xg6+; ce +M3; pv h5xg6+ Kh7xg6 Ba4-c2+ Kg6-h5 g2-g4+; id \"3050\";");

        assertEquals("q1r3r1/1b3Nbk/p3QBpp/1p1P3P/B1n5/8/P4PP1/3RR1K1 w - -", entry.fen);
        assertEquals("h5xg6+", entry.bestMovesString);
        assertEquals("3050", entry.id);
        assertNotNull(entry.game);
        assertNotNull(entry.bestMoves);
    }

    @Test
    public void testReadEDP05(){
        EPDEntry entry = EPDReader.readEdpLine("8/kpP2r1p/p6r/n3Q1p1/P6q/1PN3p1/5P2/2RR2K1 w - - bm c7-c8N+; ce +M6; pv c7-c8N+ Ka7-a8 Nc8-b6+ Ka8-a7 Qe5-b8+ Ka7xb8 Rd1-d8+ Kb8-a7 Rd8-a8+ Ka7xb6 Nc3-d5+; id \"7510\";");

        assertEquals("8/kpP2r1p/p6r/n3Q1p1/P6q/1PN3p1/5P2/2RR2K1 w - -", entry.fen);
        assertEquals("c7-c8N+", entry.bestMovesString);
        assertEquals("7510", entry.id);
        assertNotNull(entry.game);
        assertNotNull(entry.bestMoves);
    }

    @Test
    public void testReadEDP06(){
        EPDEntry entry = EPDReader.readEdpLine("r1bq2rk/pp3pbp/2p1p1pQ/7P/3P4/2PB1N2/PP3PPR/2KR4 w - - bm Qxh7+; id \"WAC.004\";");

        assertEquals("r1bq2rk/pp3pbp/2p1p1pQ/7P/3P4/2PB1N2/PP3PPR/2KR4 w - -", entry.fen);
        assertEquals("Qxh7+", entry.bestMovesString);
        assertEquals("WAC.004", entry.id);
        assertNotNull(entry.game);
        assertNotNull(entry.bestMoves);
    }

    @Test
    public void testReadEDP07(){
        EPDEntry entry = EPDReader.readEdpLine("1n2rr2/1pk3pp/pNn2p2/2N1p3/8/6P1/PP2PPKP/2RR4 w - - bm Nca4; id \"WAC.299\";");

        assertEquals("1n2rr2/1pk3pp/pNn2p2/2N1p3/8/6P1/PP2PPKP/2RR4 w - -", entry.fen);
        assertEquals("Nca4", entry.bestMovesString);
        assertEquals("WAC.299", entry.id);
        assertNotNull(entry.game);
        assertNotNull(entry.bestMoves);
    }

    @Test
    public void testReadEDP08(){
        EPDEntry entry = EPDReader.readEdpLine("rb3qk1/pQ3ppp/4p3/3P4/8/1P3N2/1P3PPP/3R2K1 w - - bm Qxa8 d6 dxe6; id \"WAC.031\";");

        assertEquals("rb3qk1/pQ3ppp/4p3/3P4/8/1P3N2/1P3PPP/3R2K1 w - -", entry.fen);
        assertEquals("Qxa8 d6 dxe6", entry.bestMovesString);
        assertEquals("WAC.031", entry.id);
        assertNotNull(entry.game);
        assertEquals(3, entry.bestMoves.size());
    }


    @Test
    public void testReadEDP09(){
        EPDEntry entry = EPDReader.readEdpLine("5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - - bm Rg3; id \"WAC.003\";");

        assertEquals("5rk1/1ppb3p/p1pb4/6q1/3P1p1r/2P1R2P/PP1BQ1P1/5RKN w - -", entry.fen);
        assertEquals("Rg3", entry.bestMovesString);
        assertEquals("WAC.003", entry.id);
        assertNotNull(entry.game);
        assertEquals(1, entry.bestMoves.size());
        assertEquals(Piece.ROOK_WHITE, entry.bestMoves.get(0).getFrom().getPiece());
    }

    @Test
    public void testReadEDP10(){
        EPDEntry entry = EPDReader.readEdpLine("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - bm e6; id \"WAC.072\";");

        assertEquals("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - -", entry.fen);
        assertEquals("e6", entry.bestMovesString);
        assertEquals("WAC.072", entry.id);
        assertNotNull(entry.game);
        assertEquals(1, entry.bestMoves.size());
        assertEquals(Piece.PAWN_WHITE, entry.bestMoves.get(0).getFrom().getPiece());
    }

    @Test
    public void testReadEDP11(){
        EPDEntry entry = EPDReader.readEdpLine("r2r2k1/p2n1p2/4q2p/3p2p1/1PpB4/P1NnPP2/2Q3PP/R2R2K1 b - - bm N7e5; c0 \"N7e5=10, a5=6, a6=6, Nb8=5\"; id \"STS(v12.0) Center Control.081\";");

        assertEquals("r2r2k1/p2n1p2/4q2p/3p2p1/1PpB4/P1NnPP2/2Q3PP/R2R2K1 b - -", entry.fen);
        assertEquals("N7e5", entry.bestMovesString);
        assertEquals("STS(v12.0) Center Control.081", entry.id);
        assertNotNull(entry.game);
        assertEquals(1, entry.bestMoves.size());
        assertEquals(Piece.KNIGHT_BLACK, entry.bestMoves.get(0).getFrom().getPiece());
        assertEquals(Square.d7, entry.bestMoves.get(0).getFrom().getSquare());
        assertEquals(Square.e5, entry.bestMoves.get(0).getTo().getSquare());
    }

    //3r2k1/1p3ppp/2pq4/p1n5/P6P/1P6/1PB2QP1/1K2R3 w - - am Rd1; id "position 03";

    @Test
    public void testReadEDP12(){
        EPDEntry entry = EPDReader.readEdpLine("3r2k1/1p3ppp/2pq4/p1n5/P6P/1P6/1PB2QP1/1K2R3 w - - am Rd1; id \"position 03\";");

        assertEquals("3r2k1/1p3ppp/2pq4/p1n5/P6P/1P6/1PB2QP1/1K2R3 w - -", entry.fen);
        assertEquals("Rd1", entry.avoidMovesString);
        assertEquals("position 03", entry.id);
        assertNotNull(entry.game);
        assertEquals(1, entry.avoidMoves.size());
        assertEquals(Piece.ROOK_WHITE, entry.avoidMoves.get(0).getFrom().getPiece());
        assertEquals(Square.e1, entry.avoidMoves.get(0).getFrom().getSquare());
        assertEquals(Square.d1, entry.avoidMoves.get(0).getTo().getSquare());
    }

    @Test
    @Disabled
    public void testReadMateAll(){
        List<EPDEntry> entryList = EPDReader.readEdpFile("C:\\Java\\projects\\chess\\chess-utils\\testing\\40H-EPD-databases\\mate-all.epd");

        assertEquals(23268, entryList.size());
    }


    @Test
    @Disabled
    public void testReadWAC(){
        List<EPDEntry> entryList = EPDReader.readEdpFile("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac.epd");

        assertEquals(300, entryList.size());
    }

    @Test
    @Disabled
    public void testReadWAC2018(){
        List<EPDEntry> entryList = EPDReader.readEdpFile("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\wac-2018.epd");

        assertEquals(201, entryList.size());
    }

    @Test
    @Disabled
    public void testSilentButDeadly(){
        List<EPDEntry> entryList = EPDReader.readEdpFile("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\sbd.epd");

        assertEquals(134, entryList.size());
    }

    @Test
    @Disabled
    public void testPET(){
        List<EPDEntry> entryList = EPDReader.readEdpFile("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\database\\pet.epd");

        assertEquals(48, entryList.size());
    }


    @Test
    @Disabled
    public void testReadEvalTunnerWhite(){
        List<EPDEntry> entryList = EPDReader.readEdpFile("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\Texel\\eval-tunner-white.txt");

        assertEquals(32571, entryList.size());
    }
}
