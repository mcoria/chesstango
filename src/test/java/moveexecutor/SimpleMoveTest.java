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
import moveexecutors.SimpleMove;

public class SimpleMoveTest {

	@Mock
	private DummyBoard board;
	
	@Mock
	private BoardState boardState;
	
	private SimpleMove moveExecutor;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Test
	public void testExecute() {
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, null);
		
		moveExecutor =  new SimpleMove(origen, destino);

		moveExecutor.executeMove(board);
		moveExecutor.executeMove(boardState);
		
		verify(board).setPieza(destino.getKey(), Pieza.TORRE_BLANCO);
		verify(board).setEmptySquare(origen.getKey());

		verify(boardState).setPeonPasanteSquare(null);
		verify(boardState).rollTurno();
	}
	
	
	@Test
	public void testUndo() {
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, null);

		moveExecutor =  new SimpleMove(origen, destino);
		
		moveExecutor.undoMove(board);
		moveExecutor.undoMove(boardState);
		
		verify(board).setPosicion(origen);
		verify(board).setPosicion(destino);
		
		verify(boardState).popState();
	}
	
	@Test
	public void testSimple() {
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, null);

		moveExecutor =  new SimpleMove(origen, destino);
		
		moveExecutor.executeMove(board);
		moveExecutor.executeMove(boardState);
		
		verify(board).setPieza(destino.getKey(), Pieza.TORRE_BLANCO);
		verify(board).setEmptySquare(origen.getKey());

		verify(boardState).setPeonPasanteSquare(null);
		verify(boardState).rollTurno();
		
		
		moveExecutor.undoMove(board);
		moveExecutor.undoMove(boardState);
		
		verify(board).setPosicion(origen);
		verify(board).setPosicion(destino);
		
		verify(boardState).popState();
	}	

}
