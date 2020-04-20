package moveexecutor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Pieza;
import moveexecutors.EnroqueBlancoReyMove;

public class EnroqueBlancoReyMoveTest {
	
	@Mock
	private DummyBoard board;
	
	private BoardState boardState;	
	
	private EnroqueBlancoReyMove moveExecutor;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		moveExecutor = new EnroqueBlancoReyMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReinaPermitido(true);
		boardState.setEnroqueBlancoReyPermitido(true);
	}
	
	@Test
	public void testExecute() {
		moveExecutor.executeMove(board);
		moveExecutor.executeMove(boardState);

		verify(board).setEmptySquare(EnroqueBlancoReyMove.FROM.getKey());						//Dejamos el origen
		verify(board).setPieza(EnroqueBlancoReyMove.TO.getKey(), Pieza.REY_BLANCO);  			//Vamos al destino
		
		verify(board).setEmptySquare(EnroqueBlancoReyMove.TORRE_FROM.getKey());					//Dejamos el origen
		verify(board).setPieza(EnroqueBlancoReyMove.TORRE_TO.getKey(), Pieza.TORRE_BLANCO);  	//Vamos al destino		
		
		assertNull(boardState.getPeonPasanteSquare());
		assertFalse(boardState.isEnroqueBlancoReyPermitido());
		assertFalse(boardState.isEnroqueBlancoReinaPermitido());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());
	}	

}
