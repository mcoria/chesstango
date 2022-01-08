package chess.pseudomovesgenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.CachePosiciones;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.ColorBoard;
import chess.layers.PosicionPiezaBoard;
import chess.moves.CastleBlackKingMove;
import chess.moves.CastleBlackQueenMove;
import chess.moves.Move;
import chess.moves.MoveFactory;
import chess.parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class KingNegroMoveGeneratorTest {

	private KingNegroMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private BoardState state;

	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moves = new ArrayList<Move>();
		state = new BoardState();
		state.setTurnoActual(Color.NEGRO);
		
		moveGenerator = new KingNegroMoveGenerator();
		moveGenerator.setBoardState(state);
		moveGenerator.setMoveFactory(moveFactory);
	}
	
	@Test
	public void testCastleBlackReina01() {
		PosicionPiezaBoard tablero =  getTablero("r3k3/8/8/8/8/8/8/8");
		
		state.setCastleBlackReinaPermitido(true);
		
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
		assertTrue(moves.contains( new CastleBlackQueenMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastleBlackReina02() {
		PosicionPiezaBoard tablero =  getTablero("r3k3/8/5B2/8/8/8/8/8");
		
		state.setCastleBlackReinaPermitido(true);
		
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
		
		assertTrue(moves.contains( new CastleBlackQueenMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastleBlackReina03() {
		PosicionPiezaBoard tablero =  getTablero("r3k3/8/8/5B2/8/8/8/8");
		
		state.setCastleBlackReinaPermitido(true);
		
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
		
		assertTrue(moves.contains( new CastleBlackQueenMove() ));
		
		assertEquals(6, moves.size());
	}		
	
	@Test
	public void testCastleBlackKing01() {
		PosicionPiezaBoard tablero =  getTablero("4k2r/8/8/8/8/8/8/8");
		
		state.setCastleBlackKingPermitido(true);
		
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
		assertTrue(moves.contains( new CastleBlackKingMove() ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testCastleBlackKing02() {
		PosicionPiezaBoard tablero =  getTablero("4k2r/8/3B4/8/8/8/8/8");
		
		state.setCastleBlackKingPermitido(true);

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
		
		assertTrue(moves.contains( new CastleBlackKingMove() ));
		
		assertEquals(6, moves.size());
	}
	
	@Test
	public void testCastleBlackKing03() {
		PosicionPiezaBoard tablero =  getTablero("4k2r/8/8/3B4/8/8/8/8");
		
		state.setCastleBlackKingPermitido(true);
		
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
		
		assertTrue(moves.contains( new CastleBlackKingMove() ));
		
		assertEquals(6, moves.size());
	}		

	@Test
	public void testCastleBlackJaque() {
		PosicionPiezaBoard tablero =  getTablero("r3k2r/8/8/4R3/8/8/8/8");
		
		state.setCastleBlackKingPermitido(true);
		state.setCastleBlackReinaPermitido(true);
		
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
		assertTrue(moves.contains(  new CastleBlackKingMove() ));			// No se considera si el rey esta en jaque
		assertTrue(moves.contains(  new CastleBlackQueenMove() ));			// No se considera si el rey esta en jaque
		
		assertEquals(7, moves.size());
	}
	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return moveFactory.createSimpleKingMoveNegro(origen, new PosicionPieza(destinoSquare, null));
	}

	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
