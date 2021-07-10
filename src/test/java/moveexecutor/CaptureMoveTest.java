package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilderParts;
import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import layers.PosicionPiezaBoard;
import moveexecutors.CaptureMove;
import parsers.FENParser;

public class CaptureMoveTest {

	private PosicionPiezaBoard board;
	
	private BoardState boardState;
	
	private CaptureMove moveExecutor;

	@Before
	public void setUp() throws Exception {
		boardState = new BoardState();
	}

	
	@Test
	public void testExecuteMoveBoard() {
		board =  getTablero("8/4p3/8/4R3/8/8/8/8");
		
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.PEON_NEGRO);

		moveExecutor = new CaptureMove(origen, destino);
		
		moveExecutor.executeMove(board);
		
		assertEquals(Pieza.TORRE_BLANCO, board.getPieza(Square.e7));
		assertTrue(board.isEmtpy(Square.e5));	
	}
	
	@Test
	public void testExecuteMoveState() {
		boardState.setTurnoActual(Color.BLANCO);
		
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.PEON_NEGRO);

		moveExecutor = new CaptureMove(origen, destino);
		
		moveExecutor.executeMove(boardState);		

		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());		
	}	
	
	
	@Test
	public void testUndoMoveBoard() {
		board =  getTablero("8/4R3/8/8/8/8/8/8");
		
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.PEON_NEGRO);

		moveExecutor = new CaptureMove(origen, destino);
		
		moveExecutor.undoMove(board);
		
		assertEquals(Pieza.TORRE_BLANCO, board.getPieza(Square.e5));
		assertEquals(Pieza.PEON_NEGRO, board.getPieza(Square.e7));
	}	

	@Test
	public void testUndoMoveState() {	
		boardState.setTurnoActual(Color.BLANCO);
		boardState.pushState();
		boardState.rollTurno();
		
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.PEON_NEGRO);

		moveExecutor = new CaptureMove(origen, destino);
		
		moveExecutor.undoMove(boardState);

		assertEquals(Color.BLANCO, boardState.getTurnoActual());		
	}
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts();
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	
	
}
