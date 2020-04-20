package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Pieza;
import moveexecutors.EnroqueNegroReyMove;

public class EnroqueNegroReyMoveTest {
	
	@Mock
	private DummyBoard board;
	
	private BoardState boardState;	
	
	private EnroqueNegroReyMove moveExecutor;


	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		moveExecutor = new EnroqueNegroReyMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueNegroReinaPermitido(true);
		boardState.setEnroqueNegroReyPermitido(true);
	}
	
	@Test
	public void testExecute() {
		moveExecutor.executeMove(board);
		moveExecutor.executeMove(boardState);

		verify(board).setEmptySquare(EnroqueNegroReyMove.FROM.getKey());						//Dejamos el origen
		verify(board).setPieza(EnroqueNegroReyMove.TO.getKey(), Pieza.REY_NEGRO);  				//Vamos al destino
		
		verify(board).setEmptySquare(EnroqueNegroReyMove.TORRE_FROM.getKey());					//Dejamos el origen
		verify(board).setPieza(EnroqueNegroReyMove.TORRE_TO.getKey(), Pieza.TORRE_NEGRO);  		//Vamos al destino		
		
		assertNull(boardState.getPeonPasanteSquare());
		assertFalse(boardState.isEnroqueBlancoReyPermitido());
		assertFalse(boardState.isEnroqueBlancoReinaPermitido());
		assertEquals(Color.BLANCO, boardState.getTurnoActual());
	}	

}
