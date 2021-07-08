package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilder;
import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.Cardinal;
import layers.PosicionPiezaBoard;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;

public class CardinalMoveGeneratorEsteTest {
	
	private FENBoarBuilder<ChessBuilder> builder;
	
	private CardinalMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder<ChessBuilder>(new ChessBuilder());
		moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Este});
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void testEste() {
		PosicionPiezaBoard tablero =  builder.constructTablero("8/8/8/4R3/8/8/8/8").getBuilder().getPosicionPiezaBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.TORRE_BLANCO);	
		
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.h5) ));
	}	
	
	@Test
	public void testEste01() {
		PosicionPiezaBoard tablero =  builder.constructTablero("8/8/8/4R2B/8/8/8/8").getBuilder().getPosicionPiezaBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.h5));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.TORRE_BLANCO);
	
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g5) ));
	}	
	
	@Test
	public void testEste02() {
		PosicionPiezaBoard tablero =  builder.constructTablero("8/8/8/4R2b/8/8/8/8").getBuilder().getPosicionPiezaBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.h5));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.TORRE_BLANCO);
	
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g5) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.h5, Pieza.ALFIL_NEGRO) ));
	}	
	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return new SimpleMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}
		
}
