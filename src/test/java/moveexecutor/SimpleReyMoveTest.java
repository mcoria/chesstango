package moveexecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilderParts;
import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import layers.KingCacheBoard;
import layers.PosicionPiezaBoard;
import moveexecutors.SimpleReyMove;
import parsers.FENParser;

public class SimpleReyMoveTest {

	private BoardState boardState;

	private SimpleReyMove moveExecutor;

	private KingCacheBoard kingCacheBoard;

	private PosicionPiezaBoard board = null;

	@Before
	public void setUp() throws Exception {
		boardState = new BoardState();
		kingCacheBoard = new KingCacheBoard();
		moveExecutor = null;
		board = null;
	}

	@Test
	public void testExecuteUndoBoardState() {
		boardState.setTurnoActual(Color.BLANCO);

		PosicionPieza origen = new PosicionPieza(Square.d2, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e3, null);

		moveExecutor = new SimpleReyMove(origen, destino);

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

		moveExecutor = new SimpleReyMove(origen, destino);

		moveExecutor.executeMove(kingCacheBoard);

		assertEquals(Square.e3, kingCacheBoard.getSquareKingBlancoCache());

		moveExecutor.undoMove(kingCacheBoard);

		assertEquals(Square.d2, kingCacheBoard.getSquareKingBlancoCache());
	}

	@Test
	public void test01() {
		board = getTablero("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR");

		PosicionPieza origen = new PosicionPieza(Square.e8, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e7, null);

		moveExecutor = new SimpleReyMove(origen, destino);

		moveExecutor.executeMove(board);

		assertEquals(Pieza.REY_BLANCO, board.getPieza(Square.e7));

		moveExecutor.undoMove(board);

		assertEquals(Pieza.REY_BLANCO, board.getPieza(Square.e8));
		assertTrue(board.isEmtpy(Square.e7));
	}
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts();
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	

}
