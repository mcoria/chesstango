package chess.pseudomovesgenerators;

import java.util.Iterator;

import chess.Color;
import chess.PiecePositioned;
import chess.Square;
import chess.iterators.square.SaltoSquareIterator;
import chess.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractJumpMoveGenerator extends AbstractMoveGenerator {
	
	protected abstract Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);
	
	protected abstract Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);		
	
	private final int[][] saltos;
	
	public AbstractJumpMoveGenerator(Color color, int[][] saltos) {
		super(color);
		this.saltos = saltos;
	}

	//El calculo de movimientos lo puede hacer en funcion de ColorBoard
	
	@Override
	public void generateMovesPseudoMoves(PiecePositioned origen) {
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
	public boolean puedeCapturarPosicion(PiecePositioned origen, Square square) {
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
