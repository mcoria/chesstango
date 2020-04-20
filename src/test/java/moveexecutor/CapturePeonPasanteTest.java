package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.CapturePeonPasante;
import parsers.FENBoarBuilder;

public class CapturePeonPasanteTest {

	private DummyBoard board;
	
	private BoardState boardState;
	
	private FENBoarBuilder builder;
	
	private CapturePeonPasante moveExecutor;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		boardState = new BoardState();
	}
	
	@Test
	public void testExecuteMoveBoard() {
		board = builder.withTablero("8/8/8/pP6/8/8/8/8").buildDummyBoard();
		boardState.setPeonPasanteSquare(Square.a6);
		
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);
		
		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		moveExecutor.executeMove(board);
		
		assertTrue(board.isEmtpy(Square.a5));
		assertTrue(board.isEmtpy(Square.b5));
		assertEquals(Pieza.PEON_BLANCO, board.getPieza(Square.a6));

	}
	
	@Test
	public void testExecuteMoveState() {
		boardState.setPeonPasanteSquare(Square.a6);
		boardState.setTurnoActual(Color.BLANCO);
		
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);
		
		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		moveExecutor.executeMove(boardState);		
		
		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());	
	}	
	
	
	/*
	@Test
	public void testUndo() {
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);

		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		moveExecutor.undoMove(board);
		
		verify(board).setPosicion(peonBlanco);									//Volvemos al origen
		verify(board).setPosicion(peonPasanteSquare);							//Dejamos el destino
		verify(board).setPosicion(peonNegro);									//Devolvemos peon
	}*/
}
