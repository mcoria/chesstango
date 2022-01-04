package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilderParts;
import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import debug.builder.DebugChessFactory;
import iterators.Cardinal;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import moveexecutors.Move;
import moveexecutors.MoveFactory;
import parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class AbstractCardinalMoveGeneratorNorteEsteTest {
	
	private AbstractCardinalMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	private MoveFactory moveFactory;
	
	@Before
	public void setUp() throws Exception {
		moveFactory = new MoveFactory();
		moveGenerator = new AbstractCardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteEste}){

			@Override
			protected Move createSimpleMove(PosicionPieza origen, PosicionPieza destino) {
				return moveFactory.createSimpleMove(origen, destino);
			}

			@Override
			protected Move createCaptureMove(PosicionPieza origen, PosicionPieza destino) {
				return moveFactory.createCaptureMove(origen, destino);
			}
			
		};
		moveGenerator.setMoveFactory(moveFactory);
		
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void testNorteEste() {
		PosicionPiezaBoard tablero =  getTablero("8/8/8/4B3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.ALFIL_BLANCO);
	
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g7) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.h8) ));
	}
	
	
	
	@Test
	public void testNorteEste01() {
		PosicionPiezaBoard tablero =  getTablero("7R/8/8/4B3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h8));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.ALFIL_BLANCO);
	
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g7) ));
	}
	
	@Test
	public void testNorteEste02() {
		PosicionPiezaBoard tablero =  getTablero("7r/8/8/4B3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		moveGenerator.setColorBoard(new ColorBoard(tablero));
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
		
		PosicionPieza origen = new PosicionPieza(from, Pieza.ALFIL_BLANCO);
	
		MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
		
		moves = generatorResult.getPseudoMoves();
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.g7) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.h8, Pieza.TORRE_NEGRO) ));
	}	
	
	private Move createSimpleMove(PosicionPieza origen, Square destinoSquare) {
		return moveFactory.createSimpleMove(origen, new PosicionPieza(destinoSquare, null));
	}
	
	private Move createCaptureMove(PosicionPieza origen, Square destinoSquare, Pieza destinoPieza) {
		return moveFactory.createCaptureMove(origen, new PosicionPieza(destinoSquare, destinoPieza));
	}	
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	

}
