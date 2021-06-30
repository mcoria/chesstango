package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.CachePosiciones;
import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import layers.PosicionPiezaBoard;
import moveexecutors.CaptureReyMove;
import moveexecutors.EnroqueBlancoReyMove;
import moveexecutors.EnroqueBlancoReynaMove;
import moveexecutors.SimpleReyMove;
import parsers.FENBoarBuilder;
import positioncaptures.Capturer;
public class ReyBlancoMoveGeneratorTest {

	private FENBoarBuilder builder;
	
	private ReyBlancoMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private BoardState state;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moves = new ArrayList<Move>();
		state = new BoardState();
		
		moveGenerator = new ReyBlancoMoveGenerator();
		moveGenerator.setBoardState(state);
	}
	
	@Test
	public void test01() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/8/4K3/8/8/8/8").buildPosicionPiezaBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		
		assertEquals(8, moves.size());
	}

	@Test
	public void test02() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/4P3/4K3/4p3/8/8/8").buildPosicionPiezaBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.e6));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e4));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e4, Pieza.PEON_NEGRO) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));
		
		assertEquals(7, moves.size());
	}
	
	
	@Test
	public void testEnroqueBlancoReina01() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/8/8/8/8/8/R3K3").buildPosicionPiezaBoard();
		
		state.setEnroqueBlancoReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return false;
			}
		});
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoReynaMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testEnroqueBlancoReina02() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/8/8/8/5b2/8/R3K3").buildPosicionPiezaBoard();
		
		state.setEnroqueBlancoReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.d1, Square.e2});
		
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});

		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f3));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( new EnroqueBlancoReynaMove() ));
		
		assertEquals(5, moves.size());		
	}
	
	@Test
	public void testEnroqueBlancoReina03() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/8/8/5b2/8/8/R3K3").buildPosicionPiezaBoard();
		
		state.setEnroqueBlancoReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.c1, Square.d2});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});
	
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f4));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( new EnroqueBlancoReynaMove() ));
		
		assertEquals(5, moves.size());	
	}
	
	@Test
	public void testEnroqueBlancoReina04() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/8/8/8/8/8/RN2K3").buildPosicionPiezaBoard();
		
		state.setEnroqueBlancoReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(Square.b1));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( new EnroqueBlancoReynaMove() ));
		
		assertEquals(5, moves.size());	
	}	
	
	@Test
	public void testEnroqueBlancoRey01() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/8/8/8/8/8/4K2R").withEnroqueBlancoReyPermitido(true).buildPosicionPiezaBoard();
		
		state.setEnroqueBlancoReyPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return false;
			}
		});
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoReyMove() ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testEnroqueBlancoRey02() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/8/8/8/3b4/8/4K2R").withEnroqueBlancoReyPermitido(true).buildPosicionPiezaBoard();
		
		state.setEnroqueBlancoReyPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.e2, Square.f1});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.d3));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( new EnroqueBlancoReyMove() ));
		
		assertEquals(5, moves.size());		
	}
	
	@Test
	public void testEnroqueBlancoRey03() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/8/8/3b4/8/8/4K2R").withEnroqueBlancoReyPermitido(true).buildPosicionPiezaBoard();
		
		state.setEnroqueBlancoReyPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.f2, Square.g1});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});	
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.d4));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( new EnroqueBlancoReyMove() ));
		
		assertEquals(5, moves.size());
	}		
	
	@Test
	public void testEnroqueBlancoRey04() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/8/8/8/8/6p1/4K2R").withEnroqueBlancoReyPermitido(true).buildPosicionPiezaBoard();
		
		state.setEnroqueBlancoReyPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.f1});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});
		
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g2));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( new EnroqueBlancoReyMove() ));
		
		assertEquals(5, moves.size());
	}		

	@Test
	public void testEnroqueBlancoJaque() {
		PosicionPiezaBoard tablero = builder.withTablero("8/8/8/8/4r3/8/8/R3K2R").withEnroqueBlancoReinaPermitido(true).buildPosicionPiezaBoard();
		
		state.setEnroqueBlancoReyPermitido(true);
		state.setEnroqueBlancoReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setKingInCheck( () -> true );
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.e2, Square.e1});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.e4));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertFalse(moves.contains( new EnroqueBlancoReyMove() ));
		assertFalse(moves.contains( new EnroqueBlancoReynaMove() ));
		
		assertEquals(5, moves.size());		
	}
	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return new SimpleReyMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureReyMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}	
	
}
