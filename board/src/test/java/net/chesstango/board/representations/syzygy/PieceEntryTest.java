package net.chesstango.board.representations.syzygy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class PieceEntryTest {

    private Syzygy syzygy;
    private PieceEntry pieceEntry;

    @BeforeEach
    public void setUp() throws Exception {
        syzygy = new Syzygy();
        pieceEntry = new PieceEntry(syzygy);
    }

    @Test
    public void test_init_table_KQvQ() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");

        pieceEntry.tableName = "KQvQ";
        pieceEntry.num = 3;
        pieceEntry.symmetric = false;
        pieceEntry.hasDtm = false;
        pieceEntry.hasDtz = true;
        pieceEntry.kk_enc = false;

        //pieceEntry.init_table(TableType.WDL);
    }

    @Test
    public void test_init_table_KQvKR() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");

        PieceEntry pieceEntry = new PieceEntry(syzygy);
        pieceEntry.num = 3;
        pieceEntry.symmetric = false;
        pieceEntry.hasDtm = false;
        pieceEntry.hasDtz = true;
        pieceEntry.kk_enc = false;

        //syzygy.init_table(pieceEntry, "KQvKR", TableType.WDL);
    }
}
