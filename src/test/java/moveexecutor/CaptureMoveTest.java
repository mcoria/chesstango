package moveexecutor;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.CaptureMove;

public class CaptureMoveTest {
	
	@Mock
	private DummyBoard board;
	
	@Mock
	private BoardState boardState;
	
	private CaptureMove moveExecutor;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	
	@Test
	public void testExecute() {
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.PEON_NEGRO);

		moveExecutor = new CaptureMove(origen, destino);
		
		moveExecutor.executeMove(board);
		moveExecutor.executeMove(boardState);
		
		verify(board).setPieza(Square.e7, Pieza.TORRE_BLANCO);
		verify(board).setEmptySquare(Square.e5);

		verify(boardState).setPeonPasanteSquare(null);
		
	}
	
	
	@Test
	public void testUndo() {
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.PEON_NEGRO);

		moveExecutor = new CaptureMove(origen, destino);
		
		moveExecutor.undoMove(board);
		moveExecutor.undoMove(boardState);
		
		verify(board).setPosicion(origen);
		verify(board).setPosicion(destino);
		
		verify(boardState).popState();
	}	

	@Test
	public void testCapture() {		
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.PEON_NEGRO);

		
		moveExecutor = new CaptureMove(origen, destino);
		
		moveExecutor.executeMove(board);
		moveExecutor.executeMove(boardState);
		
		verify(board).setPieza(Square.e7, Pieza.TORRE_BLANCO);
		verify(board).setEmptySquare(Square.e5);

		verify(boardState).setPeonPasanteSquare(null);
		
		moveExecutor.undoMove(board);
		moveExecutor.undoMove(boardState);
		
		verify(board).setPosicion(origen);
		verify(board).setPosicion(destino);
		
		verify(boardState).popState();
		
	}
	
}
