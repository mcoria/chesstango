package moveexecutor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.CapturePeonPasante;

public class CapturePeonPasanteTest {

	@Mock
	private DummyBoard board;
	
	@Mock
	private BoardState boardState;
	
	private CapturePeonPasante moveExecutor;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testExecute() {
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);
		
		when(board.getPosicion(Square.a5)).thenReturn(peonNegro);
		
		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		moveExecutor.executeMove(board);
		moveExecutor.executeMove(boardState);

		verify(board).setEmptySquare(peonBlanco.getKey());						//Dejamos el origen
		verify(board).setPieza(peonPasanteSquare.getKey(), Pieza.PEON_BLANCO);  //Vamos al destino
		verify(board).setEmptySquare(peonNegro.getKey());						//Capturamos peon		

		verify(boardState).setPeonPasanteSquare(null);
	}
	
	
	@Test
	public void testUndo() {
		PosicionPieza peonBlanco = new PosicionPieza(Square.b5, Pieza.PEON_BLANCO);
		PosicionPieza peonNegro = new PosicionPieza(Square.a5, Pieza.PEON_NEGRO);
		PosicionPieza peonPasanteSquare = new PosicionPieza(Square.a6, null);

		moveExecutor = new CapturePeonPasante(peonBlanco, peonPasanteSquare, peonNegro);

		moveExecutor.undoMove(board);
		
		verify(board).setPosicion(peonBlanco);									//Volvemos al origen
		verify(board).setPosicion(peonPasanteSquare);							//Dejamos el destino
		verify(board).setPosicion(peonNegro);									//Devolvemos peon
	}

}
