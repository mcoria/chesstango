package net.chesstango.board.iterators.bysquare;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.bypiece.KingJumpSquareIterator;
import net.chesstango.board.iterators.bysquare.bypiece.KnightJumpSquareIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Mauricio Coria
 *
 */
public class JumpSquareIteratorTest {

	//@Test
	public void printSaltosKnightPosicionesLong() {
		long[] arraySaltos = new long[64];
		for(int idx = 0; idx < 64; idx++){
			Square square = Square.getSquareByIdx(idx);
			JumpSquareIterator iterator = new JumpSquareIterator(square, KnightJumpSquareIterator.KNIGHT_JUMPS);
			long posicionesSalto = 0;
			while (iterator.hasNext()) {
				Square salto = iterator.next();

				posicionesSalto |= salto.getBitPosition();
			}
			arraySaltos[idx] = posicionesSalto;
		}
		for(int idx = 0; idx < 64; idx++){
			System.out.println(arraySaltos[idx] + "L,");
		}
	}

	//@Test
	public void printSaltosKingPosicionesLong() {
		long[] arraySaltos = new long[64];
		for(int idx = 0; idx < 64; idx++){
			Square square = Square.getSquareByIdx(idx);
			JumpSquareIterator iterator = new JumpSquareIterator(square, KingJumpSquareIterator.SALTOS_KING);
			long posicionesSalto = 0;
			while (iterator.hasNext()) {
				Square salto = iterator.next();
				posicionesSalto |= salto.getBitPosition();
			}
			arraySaltos[idx] = posicionesSalto;
		}

		for(int idx = 0; idx < 64; idx++){
			System.out.println(arraySaltos[idx] + "L,");
		}
	}



	private final int[][] casillerosPawnWhite = {
			{ -1, -1 }, 
			{ 1, -1 }
		};
	
	//@Test
	public void printSaltosPawnWhitePosicionesLong() {
		long[] arraySaltos = new long[64];
		for(int idx = 0; idx < 64; idx++){
			Square square = Square.getSquareByIdx(idx);
			JumpSquareIterator iterator = new JumpSquareIterator(square, casillerosPawnWhite);
			long posicionesSalto = 0;
			while (iterator.hasNext()) {
				Square salto = iterator.next();
				
				posicionesSalto |= salto.getBitPosition();
			}			
			arraySaltos[idx] = posicionesSalto;
		}
		
		for(int idx = 0; idx < 64; idx++){
			System.out.println(arraySaltos[idx] + "L,");
		}

	}
	
	private final int[][] casillerosPawnBlack = {
			{ -1, 1 }, 
			{ 1, 1 }
		};	
	
	//@Test
	public void printSaltosPawnBlackPosicionesLong() {
		long[] arraySaltos = new long[64];
		for(int idx = 0; idx < 64; idx++){
			Square square = Square.getSquareByIdx(idx);
			JumpSquareIterator iterator = new JumpSquareIterator(square, casillerosPawnBlack);
			long posicionesSalto = 0;
			while (iterator.hasNext()) {
				Square salto = iterator.next();
				
				posicionesSalto |= salto.getBitPosition();
			}			
			arraySaltos[idx] = posicionesSalto;
		}
		
		for(int idx = 0; idx < 64; idx++){
			System.out.println(arraySaltos[idx] + "L,");
		}

	}	
	
}
