package movegenerators;

import java.util.Iterator;

import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import iterators.SaltoSquareIterator;

public abstract class SaltoMoveGenerator extends AbstractMoveGenerator {
	
	private final int[][] saltos;
	
	protected abstract Move createSimpleMove(PosicionPieza origen, PosicionPieza destino);
	
	protected abstract Move createCaptureMove(PosicionPieza origen, PosicionPieza destino);
	
	public SaltoMoveGenerator(Color color, int[][] saltos) {
		super(color);
		this.saltos = saltos;
	}

	//El calculo de movimientos lo puede hacer en funcion de ColorBoard
	
	@Override
	public void generateMovesPseudoMoves(PosicionPieza origen) {
		Square casillero = origen.getKey();
		Iterator<PosicionPieza> iterator = tablero.iterator(new SaltoSquareIterator(casillero, saltos));
		while (iterator.hasNext()) {
		    PosicionPieza destino = iterator.next();
		    this.result.affectedByContainerAdd(destino.getKey());
			Pieza pieza = destino.getValue();
			if(pieza == null){
				Move move = createSimpleMove(origen, destino);
				this.result.moveContainerAdd(move);			
			} else if(color.opositeColor().equals(pieza.getColor())){
				Move move = createCaptureMove(origen, destino);
				this.result.moveContainerAdd(move);
			}//  else if(color.equals(pieza.getColor())){
			 //	 continue;
			 //} 
		}
	}

	@Override
	public boolean puedeCapturarPosicion(PosicionPieza origen, Square kingSquare) {
		Square squareOrigen = origen.getKey();
		int fileOrigen = squareOrigen.getFile();
		int rankOrigen = squareOrigen.getRank();
		
		int fileKing = kingSquare.getFile();
		int rankKing = kingSquare.getRank();

		for (int i = 0; i < saltos.length; i++) {
			if(fileOrigen + saltos[i][0] == fileKing && rankOrigen + saltos[i][1] == rankKing) {
				return true;
			}
				
		}
		return false;		
	}

}
