package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilderParts;
import chess.BoardState;
import chess.CachePosiciones;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import debug.builder.DebugChessFactory;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import moveexecutors.EnroqueNegroReyMove;
import moveexecutors.EnroqueNegroReynaMove;
import moveexecutors.Move;
import moveexecutors.MoveFactory;
import parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class ReyNegroMoveGeneratorTest {

	private ReyNegroMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private BoardState state;

	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moves = new ArrayList<Move>();
		state = new BoardState();
		state.setTurnoActual(Color.NEGRO);
		
		moveGenerator = new ReyNegroMoveGenerator();
		moveGenerator.setBoardState(state);
		moveGenerator.setMoveFactory(moveFactory);
	}
	
	@Test
	public void testEnroqueNegroReina01() {
		PosicionPiezaBoard tablero =  getTablero("r3k3/8/8/8/8/8/8/8");
		
		state.setEnroqueNegroReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( new EnroqueNegroReynaMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testEnroqueNegroReina02() {
		PosicionPiezaBoard tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8");
		
		state.setEnroqueNegroReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
	
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.f6));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( new EnroqueNegroReynaMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testEnroqueNegroReina03() {
		PosicionPiezaBoard tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8");
		
		state.setEnroqueNegroReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.f5));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( new EnroqueNegroReynaMove() ));
		
		assertEquals(6, moves.size());
	}		
	
	@Test
	public void testEnroqueNegroRey01() {
		PosicionPiezaBoard tablero =  getTablero("4k2r/8/8/8/8/8/8/8");
		
		state.setEnroqueNegroReyPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( new EnroqueNegroReyMove() ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testEnroqueNegroRey02() {
		PosicionPiezaBoard tablero =  getTablero("4k2r/8/3B4/8/8/8/8/8");
		
		state.setEnroqueNegroReyPermitido(true);

		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.d6));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( new EnroqueNegroReyMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testEnroqueNegroRey03() {
		PosicionPiezaBoard tablero =  getTablero("4k2r/8/8/3B4/8/8/8/8");
		
		state.setEnroqueNegroReyPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.d5));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		
		assertTrue(moves.contains( new EnroqueNegroReyMove() ));
		
		assertEquals(6, moves.size());
	}		

	@Test
	public void testEnroqueNegroJaque() {
		PosicionPiezaBoard tablero =  getTablero("r3k2r/8/8/4R3/8/8/8/8");
		
		state.setEnroqueNegroReyPermitido(true);
		state.setEnroqueNegroReinaPermitido(true);
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		assertEquals(Pieza.REY_NEGRO, tablero.getPieza(CachePosiciones.REY_NEGRO.getKey()));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a8));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		
		PosicionPieza origen = new PosicionPieza(CachePosiciones.REY_NEGRO.getKey(), Pieza.REY_NEGRO);
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f8) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e7) ));
		assertTrue(moves.contains(  new EnroqueNegroReyMove() ));			// No se considera si el rey esta en jaque
		assertTrue(moves.contains(  new EnroqueNegroReynaMove() ));			// No se considera si el rey esta en jaque
		
		assertEquals(7, moves.size());
	}
	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return moveFactory.createSimpleReyMoveNegro(origen, new PosicionPieza(destinoSquare, null));
	}

	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
