package chess.legalmovesgenerators;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.builder.imp.ChessPositionBuilderImp;
import chess.debug.builder.DebugChessFactory;
import chess.factory.ChessFactory;
import chess.factory.ChessInjector;
import chess.fen.FENDecoder;
import chess.legalmovesgenerators.strategies.NoCheckLegalMoveGenerator;
import chess.position.ChessPosition;
import chess.pseudomovesgenerators.MoveGenerator;


/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveGeneratorTest {

	private NoCheckLegalMoveGenerator moveCalculator;
	
	private ChessPosition chessPosition;	
	
	private MoveGenerator strategy;
	
	private MoveFilter filter;
	
	
	@Test
	public void testEquals01() {
		initDependencies("k7/2Q5/K7/8/8/8/8/8 b - - 0 1");
		
		moveCalculator = new NoCheckLegalMoveGenerator(chessPosition, strategy, filter);
		
		//assertFalse(moveCalculator.existsLegalMove());
		assertTrue(moveCalculator.getLegalMoves().isEmpty());
	}


	private void initDependencies(String string) {		
		ChessFactory chessFactory = new DebugChessFactory();
		ChessInjector injector = new ChessInjector(chessFactory);
		
		ChessPositionBuilderImp builder = new ChessPositionBuilderImp(injector);
		FENDecoder parser = new FENDecoder(builder);
		parser.parseFEN(string);

		chessPosition = builder.getResult();
		
		strategy = injector.getMoveGenerator();
		
		filter = injector.getMoveFilter();		
	}
}
