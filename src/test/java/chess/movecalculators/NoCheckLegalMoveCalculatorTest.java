package chess.movecalculators;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.BoardState;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PosicionPiezaBoard;
import chess.movecalculators.MoveFilter;
import chess.movecalculators.NoCheckLegalMoveCalculator;
import chess.movesgenerators.MoveGeneratorStrategy;
import chess.parsers.FENParser;
import chess.positioncaptures.Capturer;
import chess.positioncaptures.ImprovedCapturer;


/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveCalculatorTest {

	private NoCheckLegalMoveCalculator moveCalculator;
	
	private PosicionPiezaBoard dummyBoard;
	
	private BoardState boardState;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;	
	
	private MoveCacheBoard moveCache;
	
	private MoveGeneratorStrategy strategy;
	
	private Capturer capturer;
	
	private MoveFilter filter;
	
	
	@Test
	public void testEquals01() {
		initDependencies("k7/2Q5/K7/8/8/8/8/8 b KQkq - 0 1");
		
		moveCalculator = new NoCheckLegalMoveCalculator(dummyBoard, kingCacheBoard, colorBoard, moveCache, boardState, strategy, filter);
		
		//assertFalse(moveCalculator.existsLegalMove());
		assertTrue(moveCalculator.getLegalMoves().isEmpty());
	}


	private void initDependencies(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		parser.parseFEN(string);
		
		dummyBoard = builder.getPosicionPiezaBoard();
		boardState = builder.getState();
		kingCacheBoard = new KingCacheBoard(dummyBoard);
		colorBoard = new ColorBoard(dummyBoard);
		
		capturer = new ImprovedCapturer(dummyBoard);
		
		strategy = new MoveGeneratorStrategy();
		strategy.setDummyBoard(dummyBoard);
		strategy.setBoardState(boardState);
		strategy.setColorBoard(colorBoard);
		
		filter = new MoveFilter(dummyBoard, kingCacheBoard, colorBoard, boardState, capturer);
		moveCache = new MoveCacheBoard(dummyBoard, strategy);		
	}
}
