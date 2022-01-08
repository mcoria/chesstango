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
	public void testSimpleReyMoveBlancoPierdeEnroque() {
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueNegroReinaPermitido(true);
		boardState.setEnroqueNegroReyPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.REINA_NEGRO);

		moveExecutor = moveFactory.createCaptureReyMoveNegro(origen, destino);
		
		moveExecutor.executeMove(boardState);

		assertEquals(Color.NEGRO, boardState.getTurnoActual());
		assertFalse(boardState.isEnroqueBlancoReyPermitido());
		assertFalse(boardState.isEnroqueBlancoReinaPermitido());
	}
	
	
	@Test
	public void testSimpleReyMoveNegroPierdeEnroque() {
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueNegroReinaPermitido(true);
		boardState.setEnroqueNegroReyPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e8, Pieza.REY_NEGRO);
		PosicionPieza destino = new PosicionPieza(Square.e7, null);

		moveExecutor = moveFactory.createSimpleReyMoveNegro(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLANCO, boardState.getTurnoActual());
		assertFalse(boardState.isEnroqueNegroReinaPermitido());
		assertFalse(boardState.isEnroqueNegroReyPermitido());
	}
	
	@Test
	public void testCapturaReyMoveNegroPierdeEnroque() {
		boardState.setTurnoActual(Color.NEGRO);
		boardState.setEnroqueNegroReinaPermitido(true);
		boardState.setEnroqueNegroReyPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e8, Pieza.REINA_NEGRO);
		PosicionPieza destino = new PosicionPieza(Square.e7, Pieza.CABALLO_BLANCO);

		moveExecutor = moveFactory.createCaptureReyMoveNegro(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.BLANCO, boardState.getTurnoActual());
		assertFalse(boardState.isEnroqueNegroReinaPermitido());
		assertFalse(boardState.isEnroqueNegroReyPermitido());
	}
	
	@Test
	public void testCapturaReyMoveBlancoPierdeEnroque() {
		boardState.setTurnoActual(Color.BLANCO);
		boardState.setEnroqueBlancoReyPermitido(true);
		boardState.setEnroqueBlancoReinaPermitido(true);

		PosicionPieza origen = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
		PosicionPieza destino = new PosicionPieza(Square.e2, Pieza.CABALLO_NEGRO);

		moveExecutor = moveFactory.createCaptureReyMoveBlanco(origen, destino);

		moveExecutor.executeMove(boardState);

		assertEquals(Color.NEGRO, boardState.getTurnoActual());
		assertFalse(boardState.isEnroqueBlancoReyPermitido());
		assertFalse(boardState.isEnroqueBlancoReinaPermitido());
	}	
	
}
