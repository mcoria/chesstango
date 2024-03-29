package net.chesstango.board.representations.ascii;

import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;




/**
 * @author Mauricio Coria
 *
 */
public class ASCIIEncoderTest {

	private ASCIIEncoder builder;
	private FENDecoder parser;

	@BeforeEach
	public void setUp() throws Exception {
	    builder = new ASCIIEncoder();
	    parser = new FENDecoder(builder);  
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
	    parser.parseFEN(FENDecoder.INITIAL_FEN);
	    
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
	    parser.parseFEN(FENDecoder.INITIAL_FEN);
	    
	    final ByteArrayOutputStream baosActual = new ByteArrayOutputStream();
	    try (PrintStream ps = new PrintStream(baosActual)) {
	    	builder.getState(ps);
	    }    
	    
		assertEquals(baosExp.toString(), baosActual.toString());
	}
	
	@Test
	public void testGetResult() {	    
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
			//ps.println("Turno Actual: WHITE , pawnPasanteSquare: - , castlingWhiteQueenAllowed: true, castlingWhiteKingAllowed: true, castlingBlackQueenAllowed: true, castlingBlackKingAllowed: true");
	    	ps.flush();
	    }		
	    
		//Actual
	    parser.parseFEN(FENDecoder.INITIAL_FEN);
	    
	    String result = builder.getChessRepresentation();
	    
		assertEquals(baosExp.toString(), result);
	}		

}
