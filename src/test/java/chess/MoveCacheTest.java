package chess;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import layers.MoveCacheBoard;
import moveexecutors.SimpleMove;
import movegenerators.MoveGeneratorResult;

public class MoveCacheTest {

	private MoveCacheBoard cache;
	
	@Before
	public void setUp() throws Exception {
		cache = new MoveCacheBoard();
	}
	
	@Test
	public void test01() {
		MoveGeneratorResult result = new MoveGeneratorResult();
		result.setSaveMovesInCache(true);
		result.moveContainerAdd(new SimpleMove(new PosicionPieza(Square.a2, Pieza.PEON_BLANCO), new PosicionPieza(Square.a3, null)));
		result.moveContainerAdd(new SimpleMove(new PosicionPieza(Square.a2, Pieza.PEON_BLANCO), new PosicionPieza(Square.a4, null)));
		cache.setPseudoMoves(Square.a2, result);
		
		
		assertNotNull(cache.getPseudoMoves(Square.a2));
		
	}
	

}
