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
    public void testReadEDP(){
        EDPReader.EDPEntry entry = edpReader.readEdpLine("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k - bm Rd1xd8+; ce +M5; pv Rd1xd8+ Ke8xd8 Rc6-c8+ Kd8-e7 Bb8-d6+ Ke7-e6 Rc8-e8+ Ng8-e7 Re8xe7+; id \"5712\";");

        assertEquals("1B1bk1nr/5pp1/rNR5/p3Pp1p/1p2pP2/1P5P/1PP3P1/2KR4 w k -", entry.fen);
        assertEquals("Rd1xd8+", entry.bestMove);
        assertNotNull(entry.game);
        assertNotNull(entry.expectedMove);
    }

    @Test
    public void testReadEDPFile(){
        List<EDPReader.EDPEntry> entryList = edpReader.readEdpFile("C:\\Java\\projects\\chess\\chess-utils\\testing\\40H-EPD-databases\\mate-all.epd");

        assertEquals(23268, entryList.size());
    }

}
