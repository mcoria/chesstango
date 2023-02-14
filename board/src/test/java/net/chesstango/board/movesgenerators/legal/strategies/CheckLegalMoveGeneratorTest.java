package net.chesstango.board.movesgenerators.legal.strategies;

import static org.junit.Assert.assertTrue;

import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.GameState;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.builders.ChessPositionBuilder;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.factory.ChessInjector;
import net.chesstango.board.movesgenerators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Test;


/**
 * @author Mauricio Coria
 *
 */
public class CheckLegalMoveGeneratorTest {
	
	private CheckLegalMoveGenerator moveGeneretor;
	
	private ChessPosition chessPosition;
	
	private MoveGenerator strategy;
	
	//private MoveFilter filter;

	private GameState gameState;
	
	
	@Test
	//TODO: no tiene sentido este test
	public void testEquals01() {
		initDependencies("k7/2Q5/K7/8/8/8/8/8 b - - 0 1");
		
		//moveGeneretor = new CheckLegalMoveGenerator(chessPosition, strategy, filter);

		assertTrue(gameState.getLegalMoves().isEmpty());
	}


	private void initDependencies(String string) {
		ChessFactory chessFactory = new ChessFactoryDebug();
		ChessInjector injector = new ChessInjector(chessFactory);
		
		ChessPositionBuilder builder = new ChessPositionBuilder(injector);
		FENDecoder parser = new FENDecoder(builder);
		parser.parseFEN(string);

		chessPosition = builder.getResult();
		
		strategy = injector.getMoveGenerator();
		
		//filter = injector.getMoveFilter();
		
		gameState = injector.getGameState();
		
		PositionAnalyzer analyzer = injector.getAnalyzer();
		
		analyzer.updateGameStatus();
		
	}
}
