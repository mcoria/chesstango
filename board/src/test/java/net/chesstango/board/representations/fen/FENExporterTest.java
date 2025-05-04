package net.chesstango.board.representations.fen;

import net.chesstango.board.representations.PositionBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;


/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class FENExporterTest {

    private FENExporter fenExporter = null;

    @Mock
    private PositionBuilder<?> positionBuilder;

    @BeforeEach
    public void setUp() throws Exception {
        fenExporter = new FENExporter(positionBuilder);
    }

    @Test
    public void testParseColorWhite() {
        assertTrue(fenExporter.parseTurn("w"));
    }

    @Test
    public void testParseColorBlack() {
        assertFalse(fenExporter.parseTurn("b"));
    }

    @Test
    public void testParseEnPassantSquare01() {
        assertThrows(IllegalArgumentException.class, () -> {
            fenExporter.parseFileEnPassantSquare("-");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            fenExporter.parseRankEnPassantSquare("-");
        });
    }

    @Test
    public void testParseEnPassantSquare02() {
        int filePawnPasanteSquare = fenExporter.parseFileEnPassantSquare("a3");
        int rankPawnPasanteSquare = fenExporter.parseRankEnPassantSquare("a3");

        assertEquals(0, filePawnPasanteSquare);
        assertEquals(2, rankPawnPasanteSquare);
    }

    @Test
    public void testParseEnPassantSquare03() {
        int filePawnPasanteSquare = fenExporter.parseFileEnPassantSquare("h6");
        int rankPawnPasanteSquare = fenExporter.parseRankEnPassantSquare("h6");

        assertEquals(7, filePawnPasanteSquare);
        assertEquals(5, rankPawnPasanteSquare);
    }

    @Test
    public void testParseWithEnPassant() {
        fenExporter.export(FEN.of("rnbqkb1r/5ppp/p2ppn2/1p6/3NP3/2N1BP2/PPP3PP/R2QKB1R w KQkq b6 0 8"));

        verify(positionBuilder).withWhiteTurn(true);
    }

    @Test
    public void testParseFenWithSpaces() {
        fenExporter.export(FEN.of("8/5kpp/8/8/1p3P2/6PP/r3KP2/1R1q4  w   -   -    4       10"));

        verify(positionBuilder).withWhiteTurn(true);
    }

    @Test
    public void testExportInitial() {
        FENStringBuilder fenStringBuilder = new FENStringBuilder();

        FENExporter fenExporter = new FENExporter(fenStringBuilder);

        fenExporter.export(FEN.of(FENParser.INITIAL_FEN));

        assertEquals(FENParser.INITIAL_FEN, fenStringBuilder.getPositionRepresentation());
    }
}
