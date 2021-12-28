package movegenerators;

import java.util.Iterator;

import chess.Color;
import chess.PosicionPieza;
import chess.Square;
import iterators.SaltoSquareIterator;
import moveexecutors.Move;

/**
 * @author Mauricio Coria
 *
 */
public abstract class SaltoMoveGenerator extends AbstractMoveGenerator {
	
	private final int[][] saltos;
	
	public SaltoMoveGenerator(Color color, int[][] saltos) {
		super(color);
		this.saltos = saltos;
	}

	//El calculo de movimientos lo puede hacer en funcion de ColorBoard
	
	@Override
	public void generateMovesPseudoMoves(PosicionPieza origen) {
		Square casillero = origen.getKey();
		Iterator<Square> iterator = new SaltoSquareIterator(casillero, saltos);
		while (iterator.hasNext()) {
			Square destino = iterator.next();
			this.result.affectedByContainerAdd(destino);
			Color colorDestino = colorBoard.getColor(destino);
			if (colorDestino == null) {
				Move move = createSimpleMove(origen, tablero.getPosicion(destino));
				this.result.moveContainerAdd(move);
			} else if (color.opositeColor().equals(colorDestino)) {
				Move move = createCaptureMove(origen, tablero.getPosicion(destino));
				this.result.moveContainerAdd(move);
			} // else if(color.equals(pieza.getColor())){
				// continue;
				// }
		}
	}

	@Override
	public boolean puedeCapturarPosicion(PosicionPieza origen, Square square) {
		Square squareOrigen = origen.getKey();
		int fileOrigen = squareOrigen.getFile();
		int rankOrigen = squareOrigen.getRank();
		
		int fileKing = square.getFile();
		int rankKing = square.getRank();

		for (int i = 0; i < saltos.length; i++) {
			if(fileOrigen + saltos[i][0] == fileKing && rankOrigen + saltos[i][1] == rankKing) {
				return true;
			}
				
		}
		return false;		
	}

}
