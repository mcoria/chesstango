package net.chesstango.board.moves.generators.legal.imp;

import net.chesstango.board.GameState;
import net.chesstango.board.analyzer.PositionAnalyzer;
import net.chesstango.board.builders.DefaultChessPositionBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.factory.ChessInjector;
import net.chesstango.board.moves.generators.legal.imp.check.CheckLegalMoveGenerator;
import net.chesstango.board.moves.generators.pseudo.MoveGenerator;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


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
		
		DefaultChessPositionBuilder builder = new DefaultChessPositionBuilder(injector);
		FENDecoder parser = new FENDecoder(builder);
		parser.parseFEN(string);

		chessPosition = builder.getChessRepresentation();
		
		strategy = injector.getPseudoMoveGenerator();
		
		//filter = injector.getMoveFilter();
		
		gameState = injector.getGameState();
		
		PositionAnalyzer analyzer = injector.getAnalyzer();
		
		analyzer.updateGameState();
		
	}
}
