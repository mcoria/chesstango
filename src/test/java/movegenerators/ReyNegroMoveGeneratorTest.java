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

public class ReyNegroMoveGeneratorTest {
	
	
	private ReyNegroMoveGenerator moveGenerator;

	@Before
	public void setUp() throws Exception {
		moveGenerator = new ReyNegroMoveGenerator();
	}
	
	@Test
	public void testEnroqueNegroReina01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("r3k3/8/8/8/8/8/8/8");
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(DummyBoard.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueNegroReinaPermitido(true);
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(tablero, origen);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( Move.MOVE_ENROQUE_NEGRO_REYNA ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testEnroqueNegroReina02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("r3k3/8/5B2/8/8/8/8/8");		
	
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(DummyBoard.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.f6));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueNegroReinaPermitido(true);	
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(tablero, origen);
		
		assertEquals(5, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
	}
	
	@Test
	public void testEnroqueNegroReina03() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("r3k3/8/8/5B2/8/8/8/8");
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(DummyBoard.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.f5));
	
		BoardState boardState = new BoardState();
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueNegroReinaPermitido(true);	
		boardState.saveState();
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		Collection<Move> moves = moveGenerator.getLegalMoves(origen);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
	}		
	
	@Test
	public void testEnroqueNegroRey01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("4k2r/8/8/8/8/8/8/8");
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(DummyBoard.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueNegroReyPermitido(true);
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(tablero, origen);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( Move.MOVE_ENROQUE_NEGRO_REY ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testEnroqueNegroRey02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("4k2r/8/3B4/8/8/8/8/8");
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(DummyBoard.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.d6));
	
		BoardState boardState = new BoardState();
		boardState.setEnroqueNegroReyPermitido(true);
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(tablero, origen);
		
		assertEquals(5, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
	}
	
	@Test
	public void testEnroqueNegroRey03() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("4k2r/8/8/3B4/8/8/8/8");
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(DummyBoard.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.d5));
	
		BoardState boardState = new BoardState();
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueNegroReyPermitido(true);
		boardState.saveState();
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		Collection<Move> moves = moveGenerator.getLegalMoves(origen);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
	}		

	@Test
	public void testEnroqueNegroJaque() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("r3k2r/8/8/4R3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(DummyBoard.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		BoardState boardState = new BoardState();
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueBlancoReinaPermitido(true);
		boardState.saveState();
		
		tablero.setBoardState(boardState);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(DummyBoard.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		Collection<Move> moves = moveGenerator.getLegalMoves(origen);
		
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertFalse(moves.contains( createSimpleMove(origen, Square.e7) ));
		
		assertEquals(4, moves.size());
	}
	
	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.SIMPLE);
	}	
	
}
