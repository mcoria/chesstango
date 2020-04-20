package moveexecutor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import chess.Board;
import chess.BoardState;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.SimpleMove;

public class SimpleMoveExecutorTest {

	@Mock
	private Board board;
	
	@Mock
	private BoardState boardState;
	
	private SimpleMove moveExecutor;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(board.getBoardState()).thenReturn(boardState);	
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
	}
	
	
	@Test
	public void testUndo() {
		PosicionPieza origen = new PosicionPieza(Square.e5, Pieza.TORRE_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, null);

		moveExecutor =  new SimpleMove(origen, destino);
		
		moveExecutor.undoMove(board);
		
		verify(board).setPosicion(origen);
		verify(board).setPosicion(destino);
		
	}	

}
