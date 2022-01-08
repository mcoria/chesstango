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
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setCastleBlackReinaPermitido(true);
		boardState.setCastleBlackKingPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.REINA_NEGRO);

		moveExecutor = moveFactory.createCaptureKingMoveNegro(origen, destino);
		
		moveExecutor.executeMove(boardState);

		assertEquals(Color.NEGRO, boardState.getTurnoActual());
		assertFalse(boardState.isCastleWhiteKingPermitido());
		assertFalse(boardState.isCastleWhiteReinaPermitido());
	}
	
	
	@Test
	public void testSimpleKingMoveNegroPierdeEnroque() {
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setCastleBlackReinaPermitido(true);
		boardState.setCastleBlackKingPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e8, Pieza.REY_NEGRO);
		PosicionPieza destino = new PosicionPieza(Square.e7, null);

		moveExecutor = moveFactory.createSimpleKingMoveNegro(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLANCO, boardState.getTurnoActual());
		assertFalse(boardState.isCastleBlackReinaPermitido());
		assertFalse(boardState.isCastleBlackKingPermitido());
	}
	
	@Test
	public void testCapturaKingMoveNegroPierdeEnroque() {
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setCastleBlackReinaPermitido(true);
		boardState.setCastleBlackKingPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e8, Pieza.REINA_NEGRO);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.CABALLO_BLANCO);

		moveExecutor = moveFactory.createCaptureKingMoveNegro(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLANCO, boardState.getTurnoActual());
		assertFalse(boardState.isCastleBlackReinaPermitido());
		assertFalse(boardState.isCastleBlackKingPermitido());
	}
	
	@Test
	public void testCapturaKingMoveBlancoPierdeEnroque() {
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setCastleWhiteKingPermitido(true);
		boardState.setCastleWhiteReinaPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.CABALLO_NEGRO);

		moveExecutor = moveFactory.createCaptureKingMoveBlanco(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.NEGRO, boardState.getTurnoActual());
		assertFalse(boardState.isCastleWhiteKingPermitido());
		assertFalse(boardState.isCastleWhiteReinaPermitido());
	}	
	
}
