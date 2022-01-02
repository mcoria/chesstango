package movecalculators;

import static org.junit.Assert.assertTrue;

import java.util.function.BooleanSupplier;

import org.junit.Test;

import builder.ChessBuilderParts;
import chess.BoardState;
import debug.builder.DebugChessFactory;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movegenerators.MoveGeneratorStrategy;
import parsers.FENParser;
import positioncaptures.Capturer;
import positioncaptures.ImprovedCapturer;


/**
 * @author Mauricio Coria
 *
 */
public class DefaultLegalMoveCalculatorTest {

	private DefaultLegalMoveCalculator moveCalculator;
	
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
		initDependencies("k7/2Q5/K7/8/8/8/8/8 b KQkq - 0 1", () -> false);
		
		moveCalculator = new DefaultLegalMoveCalculator(dummyBoard, kingCacheBoard, colorBoard, moveCache, boardState, strategy, filter);
		
		//assertFalse(moveCalculator.existsLegalMove());
		assertTrue(moveCalculator.getLegalMoves().isEmpty());
	}


	private void initDependencies(String string, BooleanSupplier kingInCheckFn) {		
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
		strategy.setIsKingInCheck(kingInCheckFn);
		
		filter = new MoveFilter(dummyBoard, kingCacheBoard, colorBoard, boardState, capturer);
		moveCache = new MoveCacheBoard(dummyBoard, strategy);
	}
}
