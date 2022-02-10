package chess.legalmovesgenerators;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.analyzer.Capturer;
import chess.builder.ChessFactory;
import chess.builder.ChessPositionBuilderImp;
import chess.debug.builder.DebugChessFactory;
import chess.fen.FENDecoder;
import chess.position.ColorBoard;
import chess.position.KingCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;
import chess.pseudomovesgenerators.imp.MoveGeneratorImp;


/**
 * @author Mauricio Coria
 *
 */
public class NoCheckLegalMoveGeneratorTest {

	private NoCheckLegalMoveGenerator moveCalculator;
	
	private PiecePlacement dummyBoard;
	
	private PositionState positionState;
	
	private KingCacheBoard kingCacheBoard;
	
	private ColorBoard colorBoard;
	
	private MoveGeneratorImp strategy;
	
	private Capturer capturer;
	
	private MoveFilter filter;
	
	
	@Test
	public void testEquals01() {
		initDependencies("k7/2Q5/K7/8/8/8/8/8 b KQkq - 0 1");
		
		moveCalculator = new NoCheckLegalMoveGenerator(dummyBoard, kingCacheBoard, colorBoard, positionState, strategy, filter);
		
		//assertFalse(moveCalculator.existsLegalMove());
		assertTrue(moveCalculator.getLegalMoves().isEmpty());
	}


	private void initDependencies(String string) {		
		ChessFactory chessFactory = new DebugChessFactory();
		ChessPositionBuilderImp builder = new ChessPositionBuilderImp(chessFactory);
		FENDecoder parser = new FENDecoder(builder);
		parser.parseFEN(string);
		
		dummyBoard = builder.getPiecePlacement();
		positionState = builder.getPositionState();
		kingCacheBoard = builder.getKingCacheBoard();
		colorBoard = builder.getColorBoard();
		
		capturer = new Capturer(dummyBoard);
		
		strategy = new MoveGeneratorImp();
		strategy.setPiecePlacement(dummyBoard);
		strategy.setBoardState(positionState);
		strategy.setColorBoard(colorBoard);
		
		filter = new MoveFilter(dummyBoard, kingCacheBoard, colorBoard, positionState, capturer);
		
		colorBoard.init(dummyBoard);
		kingCacheBoard.init(dummyBoard);		
	}
}
