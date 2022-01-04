package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilderParts;
import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import debug.builder.DebugChessFactory;
import layers.PosicionPiezaBoard;
import moveexecutors.Move;
import moveexecutors.MoveFactory;
import parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class PeonPasanteMoveGeneratorTest {
	private PeonPasanteMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 
	
	private BoardState state;

	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moves = new ArrayList<Move>();
		state = new BoardState();
		
		moveGenerator = new PeonPasanteMoveGenerator();
		moveGenerator.setBoardState(state);
	}
	



	
	@Test
	public void testPeonBlancoPasanteIzquierda() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/3pP3/8/8/8/8");
		
		state.setPeonPasanteSquare(Square.d6);
		state.setTurnoActual(Color.BLANCO);
		
		moveGenerator.setTablero(tablero);

		Square from = Square.e5;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.d5));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moves = moveGenerator.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureNegroPeonPasanteMove(origen, Square.d6) ));
	}
	
	

	
	@Test
	public void testPeonBlancoPasanteDerecha() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/3Pp3/8/8/8/8");
		
		state.setPeonPasanteSquare(Square.e6);
		state.setTurnoActual(Color.BLANCO);
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d5;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e5));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_BLANCO);
		
		moves = moveGenerator.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureNegroPeonPasanteMove(origen, Square.e6) ));
	}
	

	@Test
	public void testPeonNegroPasanteDerecha() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/3pP3/8/8/8");
		
		state.setPeonPasanteSquare(Square.e3);
		state.setTurnoActual(Color.NEGRO);
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d4;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.e4));

		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		moves = moveGenerator.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureBlancoPeonPasanteMove(origen, Square.e3) ));
	}	

	@Test
	public void testPeonNegroPasanteIzquierda() {
		PosicionPiezaBoard tablero = getTablero("8/8/8/8/3Pp3/8/8/8");
		
		state.setPeonPasanteSquare(Square.d3);
		state.setTurnoActual(Color.NEGRO);		
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e4;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d4));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.PEON_NEGRO);
		
		moves = moveGenerator.getPseudoMoves();
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createCaptureBlancoPeonPasanteMove(origen, Square.d3) ));
	}

	private Move createCaptureNegroPeonPasanteMove(PosicionPieza origen, Square destinoSquare) {
		return moveFactory.createCapturePeonPasante(origen, new PosicionPieza(destinoSquare, null), new PosicionPieza(Square.getSquare(destinoSquare.getFile(), 4), Pieza.PEON_NEGRO));
	}
	
	private Move createCaptureBlancoPeonPasanteMove(PosicionPieza origen, Square destinoSquare) {
		return moveFactory.createCapturePeonPasante(origen, new PosicionPieza(destinoSquare, null), new PosicionPieza(Square.getSquare(destinoSquare.getFile(), 3), Pieza.PEON_BLANCO));
	}	
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
