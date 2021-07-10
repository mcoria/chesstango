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

import builder.ChessBuilderParts;
import chess.BoardState;
import chess.CachePosiciones;
import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import layers.PosicionPiezaBoard;
import moveexecutors.EnroqueNegroReyMove;
import moveexecutors.EnroqueNegroReynaMove;
import moveexecutors.SimpleReyMove;
import parsers.FENParser;
import positioncaptures.Capturer;
public class ReyNegroMoveGeneratorTest {

	private ReyNegroMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private BoardState state;

	@Before
	public void setUp() throws Exception {
		moves = new ArrayList<Move>();
		state = new BoardState();
		state.setTurnoActual(Color.NEGRO);
		
		moveGenerator = new ReyNegroMoveGenerator();
		moveGenerator.setBoardState(state);
	}
	
	@Test
	public void testEnroqueNegroReina01() {
		PosicionPiezaBoard tablero =  getTablero("r3k3/8/8/8/8/8/8/8");
		
		state.setEnroqueNegroReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return false;
			}
		});
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( simpleReyMove(origen, Square.d8) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.d7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.e7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f8) ));
		assertTrue(moves.contains( new EnroqueNegroReynaMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testEnroqueNegroReina02() {
		PosicionPiezaBoard tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8");
		
		state.setEnroqueNegroReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.d8, Square.e7});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});
	
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.f6));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( simpleReyMove(origen, Square.d8) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.d7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.e7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f8) ));
		
		assertFalse(moves.contains( new EnroqueNegroReynaMove() ));
		
		assertEquals(5, moves.size());
	}
	
	@Test
	public void testEnroqueNegroReina03() {
		PosicionPiezaBoard tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8");
		
		state.setEnroqueNegroReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.c8, Square.d7});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.f5));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( simpleReyMove(origen, Square.d8) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.d7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.e7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f8) ));
		
		assertFalse(moves.contains( new EnroqueNegroReynaMove() ));
		
		assertEquals(5, moves.size());
	}		
	
	@Test
	public void testEnroqueNegroRey01() {
		PosicionPiezaBoard tablero =  getTablero("4k2r/8/8/8/8/8/8/8");
		
		state.setEnroqueNegroReyPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return false;
			}
		});
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( simpleReyMove(origen, Square.d8) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.d7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.e7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f8) ));
		assertTrue(moves.contains( new EnroqueNegroReyMove() ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testEnroqueNegroRey02() {
		PosicionPiezaBoard tablero =  getTablero("4k2r/8/3B4/8/8/8/8/8");
		
		state.setEnroqueNegroReyPermitido(true);

		moveGenerator.setTablero(tablero);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.e7, Square.f8});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.d6));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( simpleReyMove(origen, Square.d8) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.d7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.e7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f8) ));
		
		assertFalse(moves.contains( new EnroqueNegroReyMove() ));
		
		assertEquals(5, moves.size());
	}
	
	@Test
	public void testEnroqueNegroRey03() {
		PosicionPiezaBoard tablero =  getTablero("4k2r/8/8/3B4/8/8/8/8");
		
		state.setEnroqueNegroReyPermitido(true);
		
		moveGenerator.setTablero(tablero);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.f7, Square.g8});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.d5));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( simpleReyMove(origen, Square.d8) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.d7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.e7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f8) ));
		
		assertFalse(moves.contains( new EnroqueNegroReyMove() ));
		
		assertEquals(5, moves.size());
	}		

	@Test
	public void testEnroqueNegroJaque() {
		PosicionPiezaBoard tablero =  getTablero("r3k2r/8/8/4R3/8/8/8/8");
		
		state.setEnroqueNegroReyPermitido(true);
		state.setEnroqueNegroReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setKingInCheck(() -> true);
		
		List<Square> positionCaptured = Arrays.asList(new Square[] {Square.e6, Square.e7, Square.e8});
		
		moveGenerator.setCapturer(new Capturer(){
			@Override
			public boolean positionCaptured(Color color, Square square) {
				return positionCaptured.contains(square);
			}
		});
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( simpleReyMove(origen, Square.d8) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.d7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f7) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.f8) ));
		assertTrue(moves.contains( simpleReyMove(origen, Square.e7) ));
		assertFalse(moves.contains(  new EnroqueNegroReyMove() ));
		assertFalse(moves.contains(  new EnroqueNegroReynaMove() ));
		
		assertEquals(5, moves.size());
	}
	
	private Move simpleReyMove(PosicionPieza origen, Square destinoSquare) {
		return new SimpleReyMove(origen, new PosicionPieza(destinoSquare, null));
	}	
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts();
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
