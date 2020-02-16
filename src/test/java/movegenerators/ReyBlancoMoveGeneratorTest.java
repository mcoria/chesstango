package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Move.MoveType;
import chess.Pieza;
import chess.Square;
import parsers.FENParser;

public class ReyBlancoMoveGeneratorTest {

	private ReyBlancoMoveGenerator moveGenerator;

	@Before
	public void setUp() throws Exception {
		moveGenerator = new ReyBlancoMoveGenerator();
	}
	
	@Test
	public void test01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4K3/8/8/8/8");
		tablero.setBoardState(new BoardState());
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(8, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));			
	}

	@Test
	public void test02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/4P3/4K3/4p3/8/8/8");
		tablero.setBoardState(new BoardState());
		moveGenerator.setTablero(tablero);
		
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.e6));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e4));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(7, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e4, Pieza.PEON_NEGRO) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));			
	}
	
	
	@Test
	public void testEnroqueBlancoReina01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/8/8/R3K3");
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(DummyBoard.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReinaPermitido(true);
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(6, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( Move.MOVE_ENROQUE_BLANCO_REINA ));
	}
	
	@Test
	public void testEnroqueBlancoReina02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/5b2/8/R3K3");
		moveGenerator.setTablero(tablero);

		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(DummyBoard.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f3));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReinaPermitido(true);	
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(5, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
	}
	
	@Test
	public void testEnroqueBlancoReina03() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/5b2/8/8/R3K3");
		moveGenerator.setTablero(tablero);
		moveGenerator.setFilter(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(DummyBoard.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f4));
	
		BoardState boardState = new BoardState();
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReinaPermitido(true);	
		boardState.saveState();
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getLegalMoves(origen);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(4, moves.size());
	}
	
	@Test
	public void testEnroqueBlancoReina04() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/8/8/RN2K3");
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(DummyBoard.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(Square.b1));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReinaPermitido(true);
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( Move.MOVE_ENROQUE_BLANCO_REINA ));
		
		assertEquals(5, moves.size());
	}	
	
	@Test
	public void testEnroqueBlancoRey01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/8/8/4K2R");
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(DummyBoard.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReyPermitido(true);	
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( Move.MOVE_ENROQUE_BLANCO_REY ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testEnroqueBlancoRey02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/3b4/8/4K2R");
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(DummyBoard.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.d3));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueBlancoReyPermitido(true);	
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(5, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
	}
	
	@Test
	public void testEnroqueBlancoRey03() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/3b4/8/8/4K2R");
		moveGenerator.setTablero(tablero);
		moveGenerator.setFilter(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(DummyBoard.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.d4));
	
		BoardState boardState = new BoardState();
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReyPermitido(true);	
		boardState.saveState();
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getLegalMoves(origen);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(4, moves.size());
	}		
	
	@Test
	public void testEnroqueBlancoRey04() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/8/6p1/4K2R");
		moveGenerator.setTablero(tablero);
		moveGenerator.setFilter(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(DummyBoard.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g2));
	
		BoardState boardState = new BoardState();
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReyPermitido(true);	
		boardState.saveState();
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getLegalMoves(origen);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		
		assertEquals(4, moves.size());
	}		

	@Test
	public void testEnroqueBlancoJaque() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/4r3/8/8/R3K2R");
		moveGenerator.setTablero(tablero);
		moveGenerator.setFilter(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(DummyBoard.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.e4));
	
		BoardState boardState = new BoardState();
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReinaPermitido(true);	
		boardState.saveState();
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		Collection<Move> moves = moveGenerator.getLegalMoves(origen);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(4, moves.size());		
	}
	
	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.SIMPLE);
	}
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza), MoveType.CAPTURA);
	}	
	
}
