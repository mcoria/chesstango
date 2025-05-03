package net.chesstango.board.representations.ascii;

import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENParser;
import net.chesstango.board.representations.fen.FENExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class ASCIIBuilderTest {

    private ASCIIBuilder builder;

    private FENExporter exporter;

    @BeforeEach
    public void setUp() throws Exception {
        builder = new ASCIIBuilder();
        exporter = new FENExporter(builder);
    }

    @Test
    public void testGetPiecePlacement() {
        // Expected
        final ByteArrayOutputStream baosExp = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baosExp)) {
            ps.println("  -------------------------------");
            ps.println("8| r | n | b | q | k | b | n | r |");
            ps.println("  -------------------------------");
            ps.println("7| p | p | p | p | p | p | p | p |");
            ps.println("  -------------------------------");
            ps.println("6|   |   |   |   |   |   |   |   |");
            ps.println("  -------------------------------");
            ps.println("5|   |   |   |   |   |   |   |   |");
            ps.println("  -------------------------------");
            ps.println("4|   |   |   |   |   |   |   |   |");
            ps.println("  -------------------------------");
            ps.println("3|   |   |   |   |   |   |   |   |");
            ps.println("  -------------------------------");
            ps.println("2| P | P | P | P | P | P | P | P |");
            ps.println("  -------------------------------");
            ps.println("1| R | N | B | Q | K | B | N | R |");
            ps.println("  -------------------------------");
            ps.println("   a   b   c   d   e   f   g   h");
            ps.flush();
        }

        //Actual
        exporter.export(FEN.of(FENParser.INITIAL_FEN));

        final ByteArrayOutputStream baosActual = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baosActual)) {
            builder.getPiecePlacement(ps);
        }

        assertEquals(baosExp.toString(), baosActual.toString());
    }

    @Test
    public void testGetState() {
        // Expected
        final ByteArrayOutputStream baosExp = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baosExp)) {
            ps.println("Turn: WHITE , enPassantSquare: - , castlingWhiteQueenAllowed: true, castlingWhiteKingAllowed: true, castlingBlackQueenAllowed: true, castlingBlackKingAllowed: true");
            ps.flush();
        }

        //Actual
        exporter.export(FEN.of(FENParser.INITIAL_FEN));

        final ByteArrayOutputStream baosActual = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baosActual)) {
            builder.getState(ps);
        }

        assertEquals(baosExp.toString(), baosActual.toString());
    }

    @Test
    public void testGetInitial() {
        // Expected
        final ByteArrayOutputStream baosExp = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baosExp)) {
            ps.println("  -------------------------------");
            ps.println("8| r | n | b | q | k | b | n | r |");
            ps.println("  -------------------------------");
            ps.println("7| p | p | p | p | p | p | p | p |");
            ps.println("  -------------------------------");
            ps.println("6|   |   |   |   |   |   |   |   |");
            ps.println("  -------------------------------");
            ps.println("5|   |   |   |   |   |   |   |   |");
            ps.println("  -------------------------------");
            ps.println("4|   |   |   |   |   |   |   |   |");
            ps.println("  -------------------------------");
            ps.println("3|   |   |   |   |   |   |   |   |");
            ps.println("  -------------------------------");
            ps.println("2| P | P | P | P | P | P | P | P |");
            ps.println("  -------------------------------");
            ps.println("1| R | N | B | Q | K | B | N | R |");
            ps.println("  -------------------------------");
            ps.println("   a   b   c   d   e   f   g   h");
            ps.println("Turn: WHITE , enPassantSquare: - , castlingWhiteQueenAllowed: true, castlingWhiteKingAllowed: true, castlingBlackQueenAllowed: true, castlingBlackKingAllowed: true");
            ps.flush();
        }

        //Actual
        exporter.export(FEN.of(FENParser.INITIAL_FEN));

        String stringRepresentation = builder.getPositionRepresentation();

        assertEquals(baosExp.toString(), stringRepresentation);
    }

    @Test
    public void testGetWithEnPassant() {
        // Expected
        final ByteArrayOutputStream baosExp = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baosExp)) {
            ps.println("  -------------------------------");
            ps.println("8| r | n | b | q | k | b |   | r |");
            ps.println("  -------------------------------");
            ps.println("7|   |   |   |   |   | p | p | p |");
            ps.println("  -------------------------------");
            ps.println("6| p |   |   | p | p | n |   |   |");
            ps.println("  -------------------------------");
            ps.println("5|   | p |   |   |   |   |   |   |");
            ps.println("  -------------------------------");
            ps.println("4|   |   |   | N | P |   |   |   |");
            ps.println("  -------------------------------");
            ps.println("3|   |   | N |   | B | P |   |   |");
            ps.println("  -------------------------------");
            ps.println("2| P | P | P |   |   |   | P | P |");
            ps.println("  -------------------------------");
            ps.println("1| R |   |   | Q | K | B |   | R |");
            ps.println("  -------------------------------");
            ps.println("   a   b   c   d   e   f   g   h");
            ps.println("Turn: WHITE , enPassantSquare: b6, castlingWhiteQueenAllowed: true, castlingWhiteKingAllowed: true, castlingBlackQueenAllowed: true, castlingBlackKingAllowed: true");
            ps.flush();
        }

        //Actual
        exporter.export(FEN.of("rnbqkb1r/5ppp/p2ppn2/1p6/3NP3/2N1BP2/PPP3PP/R2QKB1R w KQkq b6 0 8"));

        String stringRepresentation = builder.getPositionRepresentation();

        assertEquals(baosExp.toString(), stringRepresentation);
    }

}
