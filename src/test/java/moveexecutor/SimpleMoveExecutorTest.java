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
import moveexecutors.SimpleMoveExecutor;

public class SimpleMoveExecutorTest {

	@Mock
	private DummyBoard board;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testEquals01() {
		assertEquals(new SimpleMoveExecutor(Pieza.TORRE_BLANCO), new SimpleMoveExecutor(Pieza.TORRE_BLANCO));
	}
	
	
	@Test
	public void test() {
		SimpleMoveExecutor moveExecutor =  new SimpleMoveExecutor(Pieza.TORRE_BLANCO);
		moveExecutor.execute(board, new Move(Square.e5, Square.e7, null));
		
		
		verify(board).setPieza(Square.e7, Pieza.TORRE_BLANCO);
		verify(board).setEmptySquare(Square.e5);
		
	}

}
