package chess.moves;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import chess.moves.Move;
import chess.moves.MoveFactory;


/**
 * @author Mauricio Coria
 *
 */
public class MoveFactoryTest {
	private MoveFactory moveFactory;
	
	private Move moveExecutor;
	
	private BoardState boardState;

	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		boardState = new BoardState();
		moveExecutor = null;
	}	
	
	@Test
	public void testSimpleKingMoveBlancoPierdeEnroque() {
		boardState.setTurnoActual(Color.WHITE);
		boardState.setCastlingBlackReinaPermitido(true);
		boardState.setCastlingBlackKingPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.QUEEN_BLACK);

		moveExecutor = moveFactory.createCaptureKingMoveNegro(origen, destino);
		
		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLACK, boardState.getTurnoActual());
		assertFalse(boardState.isCastlingWhiteKingPermitido());
		assertFalse(boardState.isCastlingWhiteReinaPermitido());
	}
	
	
	@Test
	public void testSimpleKingMoveNegroPierdeEnroque() {
		boardState.setTurnoActual(Color.BLACK);
		boardState.setCastlingBlackReinaPermitido(true);
		boardState.setCastlingBlackKingPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e8, Pieza.KING_BLACK);
		PosicionPieza destino = new PosicionPieza(Square.e7, null);

		moveExecutor = moveFactory.createSimpleKingMoveNegro(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.WHITE, boardState.getTurnoActual());
		assertFalse(boardState.isCastlingBlackReinaPermitido());
		assertFalse(boardState.isCastlingBlackKingPermitido());
	}
	
	@Test
	public void testCapturaKingMoveNegroPierdeEnroque() {
		boardState.setTurnoActual(Color.BLACK);
		boardState.setCastlingBlackReinaPermitido(true);
		boardState.setCastlingBlackKingPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e8, Pieza.QUEEN_BLACK);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.CABALLO_WHITE);

		moveExecutor = moveFactory.createCaptureKingMoveNegro(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.WHITE, boardState.getTurnoActual());
		assertFalse(boardState.isCastlingBlackReinaPermitido());
		assertFalse(boardState.isCastlingBlackKingPermitido());
	}
	
	@Test
	public void testCapturaKingMoveBlancoPierdeEnroque() {
		boardState.setTurnoActual(Color.WHITE);
		boardState.setCastlingWhiteKingPermitido(true);
		boardState.setCastlingWhiteReinaPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.CABALLO_BLACK);

		moveExecutor = moveFactory.createCaptureKingMoveBlanco(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLACK, boardState.getTurnoActual());
		assertFalse(boardState.isCastlingWhiteKingPermitido());
		assertFalse(boardState.isCastlingWhiteReinaPermitido());
	}	
	
}
