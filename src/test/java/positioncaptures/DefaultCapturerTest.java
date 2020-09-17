package positioncaptures;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.Color;
import chess.Square;
import layers.ColorBoard;
import layers.DummyBoard;
import movegenerators.MoveGeneratorStrategy;
import parsers.FENBoarBuilder;

public class DefaultCapturerTest {
	private DummyBoard dummyBoard;
	
	private BoardState boardState;
	
	private FENBoarBuilder builder;

	private MoveGeneratorStrategy strategy;

	private ColorBoard colorBoard;


	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		boardState = new BoardState();
		strategy = new MoveGeneratorStrategy();
		strategy.setBoardState(boardState);	
	}
	
	@Test
	public void testPositionCaptured() {
		dummyBoard = builder.withTablero("8/8/8/1P6/8/8/8/8").buildDummyBoard();
		colorBoard = dummyBoard.buildColorBoard();
		
		strategy.setDummyBoard(dummyBoard);
		strategy.setColorBoard(colorBoard);
		strategy.settupMoveGenerators();
		
		DefaultCapturer capturer = new DefaultCapturer(dummyBoard, colorBoard, strategy);
		
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.a6) );
		assertFalse( capturer.positionCaptured(Color.BLANCO, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.c6) );
	}

}
