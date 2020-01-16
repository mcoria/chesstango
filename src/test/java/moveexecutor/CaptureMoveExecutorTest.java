package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.CaptureMoveExecutor;

public class CaptureMoveExecutorTest {
	
	@Mock
	private DummyBoard board;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testEquals01() {
		assertEquals(new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.ALFIL_NEGRO), new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.ALFIL_NEGRO));
	}
	
	@Test
	public void testExecute() {
		CaptureMoveExecutor moveExecutor = new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.PEON_NEGRO);
		moveExecutor.execute(board, new Move(Square.e5, Square.e7, null));
		
		verify(board).setPieza(Square.e7, Pieza.TORRE_BLANCO);
		verify(board).setEmptySquare(Square.e5);
		
	}

}
