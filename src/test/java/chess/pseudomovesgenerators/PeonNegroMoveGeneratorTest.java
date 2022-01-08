package chess.pseudomovesgenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.PosicionPiezaBoard;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.parsers.FENParser;
import chess.pseudomovesgenerators.MoveGeneratorResult;
import chess.pseudomovesgenerators.PeonNegroMoveGenerator;

/**
 * @author Mauricio Coria
 *
 */
public class PeonNegroMoveGeneratorTest {

	private PeonNegroMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moves = new ArrayList<Move>();
		
		moveGenerator = new PeonNegroMoveGenerator();
		moveGenerator.setMoveFactory(moveFactory);
	}
	
	@Test
	public void testSaltoSimple() {
		PosicionPiezaBoard tablero =  getTablero("8/8/p7/8/8/8/8/8");

		moveGenerator.setTablero(tablero);
		
		Square from = Square.a6;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a5) ));
	}
	
	@Test
	public void testSaltoDoble() {		
		PosicionPiezaBoard tablero = getTablero("8/p7/8/8/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a5, Square.a6) ));
	}
	
	@Test
	public void testAtaqueIzquierda() {
		PosicionPiezaBoard tablero = getTablero("8/4p3/3P4/8/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d6));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e5, Square.a6) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d6, Pieza.PEON_BLANCO) ));
	}
	
	@Test
	public void testAtaqueDerecha() {
		PosicionPiezaBoard tablero = getTablero("8/4p3/5P2/8/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.f6));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e5, Square.e6) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f6, Pieza.PEON_BLANCO) ));
		
		assertEquals(3, moves.size());
	}
	
	@Test
	public void testPuedeCapturarRey() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/8/6p1/4K2R");
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(Square.e1));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g2));

		PosicionPieza origen = new PosicionPieza(Square.g2, Pieza.PEON_NEGRO);

		assertTrue( moveGenerator.puedeCapturarPosicion(origen, Square.f1) );
	}	
	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return moveFactory.createSimpleMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createSaltoDobleMove(PosicionPieza origen, Square destinoSquare, Square squarePasante) {
		return moveFactory.createSaltoDoblePeonMove(origen, new PosicionPieza(destinoSquare, null), squarePasante);
	}	
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return moveFactory.createCaptureMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}
}
