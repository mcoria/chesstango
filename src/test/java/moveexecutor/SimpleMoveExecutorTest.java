package moveexecutor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import chess.BoardMediator;
import chess.Pieza;
import moveexecutors.SimpleMoveExecutor;

public class SimpleMoveExecutorTest {

	@Mock
	private BoardMediator mediator;

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
		
		//SimpleMoveExecutor moveExecutor =  new SimpleMoveExecutor();
		//moveExecutor.execute(mediator, new Move(Square.e5, Square.e7));
		
		
		//verify(mediator).setPieza(to, pieza);
	}

}
