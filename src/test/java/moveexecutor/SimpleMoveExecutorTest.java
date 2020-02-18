package moveexecutor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;
import moveexecutors.SimpleMoveExecutor;

public class SimpleMoveExecutorTest {

	@Mock
	private DummyBoard board;
	
	@Mock
	private BoardState boardState;
	
	private SimpleMoveExecutor moveExecutor;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		moveExecutor =  new SimpleMoveExecutor();
		when(board.getBoardState()).thenReturn(boardState);	
	}
	
	
	@Test
	public void testExecute() {
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(Square.e5, Pieza.TORRE_BLANCO);
		Map.Entry<Square, Pieza> destino = new SimpleImmutableEntry<Square, Pieza>(Square.e7, null);

		moveExecutor.execute(board, origen, destino);
		
		verify(board).setPieza(destino.getKey(), Pieza.TORRE_BLANCO);
		verify(board).setEmptySquare(origen.getKey());
		
		verify(boardState).setFrom(origen);
		verify(boardState).setTo(destino);				
		verify(boardState).setCaptura(null);
		verify(boardState).setPeonPasanteSquare(null);		
	}
	
	
	@Test
	public void testUndo() {
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(Square.e5, Pieza.TORRE_BLANCO);
		Map.Entry<Square, Pieza> destino = new SimpleImmutableEntry<Square, Pieza>(Square.e7, null);

		when(boardState.getFrom()).thenReturn(origen);	
		when(boardState.getTo()).thenReturn(destino);	
		
		moveExecutor.undo(board, boardState);
		
		
		verify(board).setPosicion(origen);
		verify(board).setPosicion(destino);
		
	}	

}
