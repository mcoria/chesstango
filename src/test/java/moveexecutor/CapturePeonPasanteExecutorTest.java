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
import moveexecutors.CapturePeonPasanteExecutor;

public class CapturePeonPasanteExecutorTest {

	@Mock
	private DummyBoard board;
	
	@Mock
	private BoardState boardState;
	
	private CapturePeonPasanteExecutor moveExecutor;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		moveExecutor = new CapturePeonPasanteExecutor();
		
		when(board.getBoardState()).thenReturn(boardState);
	}
	
	@Test
	public void testExecute() {
		Map.Entry<Square, Pieza> peonBlanco = new SimpleImmutableEntry<Square, Pieza>(Square.b5, Pieza.PEON_BLANCO);
		Map.Entry<Square, Pieza> peonNegro = new SimpleImmutableEntry<Square, Pieza>(Square.a5, Pieza.PEON_NEGRO);
		Map.Entry<Square, Pieza> peonPasanteSquare = new SimpleImmutableEntry<Square, Pieza>(Square.a6, null);
		
		when(board.getPosicion(Square.a5)).thenReturn(peonNegro);	

		moveExecutor.execute(board, peonBlanco, peonPasanteSquare);

		verify(board).setEmptySquare(peonBlanco.getKey());						//Dejamos el origen
		verify(board).setPieza(peonPasanteSquare.getKey(), Pieza.PEON_BLANCO);  //Vamos al destino
		verify(board).setEmptySquare(peonNegro.getKey());						//Capturamos peon		

		verify(boardState).setPeonPasanteSquare(null);
	}
	
	
	@Test
	public void testUndo() {
		Map.Entry<Square, Pieza> peonBlanco = new SimpleImmutableEntry<Square, Pieza>(Square.b5, Pieza.PEON_BLANCO);
		Map.Entry<Square, Pieza> peonNegro = new SimpleImmutableEntry<Square, Pieza>(Square.a5, Pieza.PEON_NEGRO);
		Map.Entry<Square, Pieza> peonPasanteSquare = new SimpleImmutableEntry<Square, Pieza>(Square.a6, null);


		moveExecutor.undo(board, peonBlanco, peonPasanteSquare);
		
		verify(board).setPosicion(peonBlanco);									//Volvemos al origen
		verify(board).setPosicion(peonPasanteSquare);							//Dejamos el destino
		verify(board).setPosicion(peonNegro);									//Devolvemos peon
	}

}
