package chess;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import layers.MoveCacheBoard;
import moveexecutors.MoveFactory;
import movegenerators.MoveGeneratorResult;

public class MoveCacheTest {

	private MoveCacheBoard cache;
	
	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		cache = new MoveCacheBoard();
	}
	
	@Test
	public void test01() {
		MoveGeneratorResult result = new MoveGeneratorResult();
		result.setSaveMovesInCache(true);
		result.moveContainerAdd(moveFactory.createSimpleMove(new PosicionPieza(Square.a2, Pieza.PEON_BLANCO), new PosicionPieza(Square.a3, null)));
		result.moveContainerAdd(moveFactory.createSimpleMove(new PosicionPieza(Square.a2, Pieza.PEON_BLANCO), new PosicionPieza(Square.a4, null)));
		cache.setPseudoMoves(Square.a2, result);
		
		
		assertNotNull(cache.getPseudoMoves(Square.a2));
		
	}	

}
