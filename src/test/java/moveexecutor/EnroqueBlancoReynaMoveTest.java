package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilderConcrete;
import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.Square;
import layers.KingCacheBoard;
import layers.PosicionPiezaBoard;
import moveexecutors.EnroqueBlancoReynaMove;
import parsers.FENParser;

public class EnroqueBlancoReynaMoveTest {	
	
	private PosicionPiezaBoard board;
	
	private BoardState boardState;
	
	private KingCacheBoard kingCacheBoard;
	
	private EnroqueBlancoReynaMove moveExecutor;

	@Before
	public void setUp() throws Exception {
		moveExecutor = new EnroqueBlancoReynaMove();
		
		boardState = new BoardState();		
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueBlancoReinaPermitido(true);
		boardState.setEnroqueBlancoReyPermitido(true);
		
		kingCacheBoard = new KingCacheBoard();
	}
	
	@Test
	public void testExecuteMoveBoard() {
		board =  getTablero("8/8/8/8/8/8/8/R3K3");
		
		moveExecutor.executeMove(board);
		
		assertEquals(Pieza.REY_BLANCO, board.getPieza(Square.c1));
		assertTrue(board.isEmtpy(Square.e1));
		
		assertEquals(Pieza.TORRE_BLANCO, board.getPieza(Square.d1));
		assertTrue(board.isEmtpy(Square.a1));	
	}

	@Test
	public void testExecuteMoveState() {
		boardState.setTurnoActual(Color.BLANCO);
		
		moveExecutor.executeMove(boardState);		

		assertNull(boardState.getPeonPasanteSquare());
		assertEquals(Color.NEGRO, boardState.getTurnoActual());		
		assertFalse(boardState.isEnroqueBlancoReinaPermitido());
		assertFalse(boardState.isEnroqueBlancoReyPermitido());
	}	
	
	@Test
	public void testKingCacheBoard() {
		
		kingCacheBoard.setKingSquare(Color.BLANCO, Square.e1);
		
		moveExecutor.executeMove(kingCacheBoard);
		
		assertEquals(Square.c1, kingCacheBoard.getSquareKingBlancoCache());

	}	
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderConcrete builder = new ChessBuilderConcrete();
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	

}
