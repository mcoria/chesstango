package net.chesstango.board.representations.fen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class FENIntegrationTest {

    @Test
    public void testTurnoWhite() {
        FENParser decoder = new FENParser();

        FENBuilder encoder = new FENBuilder();

        FENExporter exporter = new FENExporter(encoder);

        FEN fen = decoder.parseFEN(FENParser.INITIAL_FEN);

        exporter.export(fen);

        FEN fenResult = encoder.getPositionRepresentation();

        assertEquals(fen, fenResult);

    }

}
