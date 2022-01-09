package chess.pseudomovesfilters;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.ChessPositionState;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PiecePlacement;
import chess.parsers.FENParser;
import chess.positioncaptures.Capturer;
import chess.positioncaptures.ImprovedCapturer;
import chess.pseudomovesfilters.MoveFilter;
import chess.pseudomovesfilters.NoCheckLegalMoveCalculator;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;


/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveCalculatorTest {

	private NoCheckLegalMoveCalculator moveCalculator;
	
	private PiecePlacement dummyBoard;
	
	private ChessPositionState chessPositionState;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;	
	
	private MoveCacheBoard moveCache;
	
	private MoveGeneratorStrategy strategy;
	
	private Capturer capturer;
	
	private MoveFilter filter;
	
	
	@Test
	public void testEquals01() {
		initDependencies("k7/2Q5/K7/8/8/8/8/8 b KQkq - 0 1");
		
		moveCalculator = new NoCheckLegalMoveCalculator(dummyBoard, kingCacheBoard, colorBoard, moveCache, chessPositionState, strategy, filter);
		
		//assertFalse(moveCalculator.existsLegalMove());
		assertTrue(moveCalculator.getLegalMoves().isEmpty());
	}


	private void initDependencies(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		parser.parseFEN(string);
		
		dummyBoard = builder.getPosicionPiezaBoard();
		chessPositionState = builder.getState();
		kingCacheBoard = new KingCacheBoard(dummyBoard);
		colorBoard = new ColorBoard(dummyBoard);
		
		capturer = new ImprovedCapturer(dummyBoard);
		
		strategy = new MoveGeneratorStrategy();
		strategy.setDummyBoard(dummyBoard);
		strategy.setBoardState(chessPositionState);
		strategy.setColorBoard(colorBoard);
		
		filter = new MoveFilter(dummyBoard, kingCacheBoard, colorBoard, chessPositionState, capturer);
		moveCache = new MoveCacheBoard(dummyBoard, strategy);		
	}
}
