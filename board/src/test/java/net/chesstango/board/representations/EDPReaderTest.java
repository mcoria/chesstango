package net.chesstango.board.representations;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class EDPReaderTest {

    private EDPReader edpReader;

    @Before
    public void setup(){
        edpReader = new EDPReader();
    }

    @Test
    public void testReadEDP01(){
        EDPReader.EDPEntry entry = edpReader.readEdpLine("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k - bm Rd1xd8+; ce +M5; pv Rd1xd8+ Ke8xd8 Rc6-c8+ Kd8-e7 Bb8-d6+ Ke7-e6 Rc8-e8+ Ng8-e7 Re8xe7+; id \"5712\";");

        assertEquals("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k -", entry.fen);
        assertEquals("Rd1xd8+", entry.bestMove);
        assertNotNull(entry.game);
        assertNotNull(entry.expectedMove);
    }


    @Test
    public void testReadEDP02(){
        EDPReader.EDPEntry entry = edpReader.readEdpLine("r3k3/8/2P1b3/p3p1P1/P6B/1pKBR1R1/1P3b2/3r1N2 b q - bm O-O-O; ce -M5; pv O-O-O Bd3-a6+ Kc8-c7 Ba6-c4 Rd1-c1+ Kc3xb3 Be6xc4+ Kb3-a3 Rc1-a1+; id \"6067\";");

        assertEquals("r3k3/8/2P1b3/p3p1P1/P6B/1pKBR1R1/1P3b2/3r1N2 b q -", entry.fen);
        assertEquals("O-O-O", entry.bestMove);
        assertNotNull(entry.game);
        assertNotNull(entry.expectedMove);
    }

    @Test
    public void testReadEDP03(){
        EDPReader.EDPEntry entry = edpReader.readEdpLine("8/6R1/1p6/p2r2pk/3P1R2/6P1/q4P2/2B2K2 w - - bm Rh4+; ce +M2; pv Rh4+ gxh4 g4+; id \"918\";");

        assertEquals("8/6R1/1p6/p2r2pk/3P1R2/6P1/q4P2/2B2K2 w - -", entry.fen);
        assertEquals("Rh4+", entry.bestMove);
        assertNotNull(entry.game);
        assertNotNull(entry.expectedMove);
    }

    @Test
    public void testReadEDPFile(){
        List<EDPReader.EDPEntry> entryList = edpReader.readEdpFile("C:\\Java\\projects\\chess\\chess-utils\\testing\\40H-EPD-databases\\mate-all.epd");

        assertEquals(23268, entryList.size());
    }

}
