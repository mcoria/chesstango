package chess.board.legalmovesgenerators.strategies;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.board.GameState;
import chess.board.analyzer.PositionAnalyzer;
import chess.board.builder.imp.ChessPositionBuilderImp;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.factory.ChessFactory;
import chess.board.factory.ChessInjector;
import chess.board.fen.FENDecoder;
import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.position.ChessPosition;
import chess.board.pseudomovesgenerators.MoveGenerator;


/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveGeneratorTest {

	private NoCheckLegalMoveGenerator moveCalculator;
	
	private ChessPosition chessPosition;	
	
	private MoveGenerator strategy;
	
	private MoveFilter filter;

	private GameState gameState;	
	
	
	@Test
	//TODO: no tiene sentido este test	
	public void testEquals01() {
		initDependencies("k7/2Q5/K7/8/8/8/8/8 b - - 0 1");
		
		moveCalculator = new NoCheckLegalMoveGenerator(chessPosition, strategy, filter);

		assertTrue(gameState.getLegalMoves().isEmpty());
	}


	private void initDependencies(String string) {		
		ChessFactory chessFactory = new ChessFactoryDebug();
		ChessInjector injector = new ChessInjector(chessFactory);
		
		ChessPositionBuilderImp builder = new ChessPositionBuilderImp(injector);
		FENDecoder parser = new FENDecoder(builder);
		parser.parseFEN(string);

		chessPosition = builder.getResult();
		
		strategy = injector.getMoveGenerator();
		
		filter = injector.getMoveFilter();	
		
		gameState = injector.getGameState();
		
		PositionAnalyzer analyzer = injector.getAnalyzer();
		
		analyzer.updateGameStatus();		
	}
}
