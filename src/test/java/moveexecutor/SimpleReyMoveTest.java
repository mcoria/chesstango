package moveexecutor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import layers.KingCacheBoard;
import moveexecutors.SimpleReyMove;

public class SimpleReyMoveTest {
	
	private BoardState boardState;
	
	private SimpleReyMove moveExecutor;

	private KingCacheBoard kingCacheBoard;

	@Before
	public void setUp() throws Exception {
		boardState = new BoardState();
		kingCacheBoard = new KingCacheBoard();
	}
	
	
	@Test
	public void testExecuteUndoBoardState() {
		boardState.setTurnoActual(Color.BLANCO);

		PosicionPieza origen = new PosicionPieza(Square.d2, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e3, null);

		moveExecutor =  new SimpleReyMove(origen, destino);
		
		moveExecutor.executeMove(boardState);
		
		assertEquals(Color.NEGRO, boardState.getTurnoActual());
		
		moveExecutor.undoMove(boardState);

		assertEquals(Color.BLANCO, boardState.getTurnoActual());
	}
	
	@Test
	public void testExecuteUndoMoveCacheBoard() {
		kingCacheBoard.setKingSquare(Color.BLANCO, Square.d2);

		PosicionPieza origen = new PosicionPieza(Square.d2, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e3, null);

		moveExecutor =  new SimpleReyMove(origen, destino);
		
		moveExecutor.executeMove(kingCacheBoard);
		
		assertEquals(Square.e3, kingCacheBoard.getSquareKingBlancoCache());
		
		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.d2, kingCacheBoard.getSquareKingBlancoCache());
	}	

}
