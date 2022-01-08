package chess.movegenerators;

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
import chess.moveexecutors.Move;
import chess.moveexecutors.MoveFactory;
import chess.movegenerators.MoveGeneratorResult;
import chess.movegenerators.PeonBlancoMoveGenerator;
import chess.parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class PeonBlancoMoveGeneratorTest {
	private PeonBlancoMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 


	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moves = new ArrayList<Move>();
		
		moveGenerator = new PeonBlancoMoveGenerator();
		moveGenerator.setMoveFactory(moveFactory);
	}
	
	@Test
	public void testSaltoSimple() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/8/P7/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a3;		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a4) ));
	}
	
	@Test
	public void testSaltoDoble() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/8/8/P7/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a4, Square.a3) ));
	}
	
	@Test
	public void testSaltoDoble01() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/8/8/N7/P7/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(0, moves.size());
		

	}	
	
	@Test
	public void testAtaqueIzquierda() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/3p4/4P3/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.d3));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d3, Pieza.PEON_NEGRO) ));
	}
	
	
	@Test
	public void testAtaqueDerecha() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/8/5p2/4P3/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f3));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f3, Pieza.PEON_NEGRO) ));
	}
	
	@Test
	public void testPeonSimplePeonPromocion() {
		PosicionPiezaBoard tablero = getTablero("8/3P4/8/8/8/8/8/8");
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d7;
		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimplePeonPromocion(origen, Square.d8, Pieza.TORRE_BLANCO) ));
		assertTrue(moves.contains( createSimplePeonPromocion(origen, Square.d8, Pieza.CABALLO_BLANCO) ));
		assertTrue(moves.contains( createSimplePeonPromocion(origen, Square.d8, Pieza.ALFIL_BLANCO) ));
		assertTrue(moves.contains( createSimplePeonPromocion(origen, Square.d8, Pieza.REINA_BLANCO) ));
		
		assertEquals(4, moves.size());
	}

	@Test
	public void testPeonCapturaPeonPromocion() {
		PosicionPiezaBoard tablero = getTablero("2rr4/3P4/8/8/8/8/8/8");
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d7;
		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.c8));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.d8));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createCapturePeonPromocion(origen, Square.c8, Pieza.TORRE_NEGRO, Pieza.TORRE_BLANCO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(origen, Square.c8, Pieza.TORRE_NEGRO, Pieza.CABALLO_BLANCO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(origen, Square.c8, Pieza.TORRE_NEGRO, Pieza.ALFIL_BLANCO) ));
		assertTrue(moves.contains( createCapturePeonPromocion(origen, Square.c8, Pieza.TORRE_NEGRO, Pieza.REINA_BLANCO) ));
		
		assertEquals(4, moves.size());
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
	
	private Move createSimplePeonPromocion(PosicionPieza origen, Square destinoSquare, Pieza promocion) {
		return moveFactory.createSimplePeonPromocion(origen, new PosicionPieza(destinoSquare, null), promocion);
	}	
	
	private Move createCapturePeonPromocion(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza, Pieza promocion) {
		return moveFactory.createCapturePeonPromocion(origen, new PosicionPieza(destinoSquare, destinoPieza), promocion);
	}	
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
